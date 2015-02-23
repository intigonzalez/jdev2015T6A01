package com.enseirb.telecom.dngroup.dvd2c.endpoints;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enseirb.telecom.dngroup.dvd2c.db.ContentRepositoryMongo;
import com.enseirb.telecom.dngroup.dvd2c.model.Content;
import com.enseirb.telecom.dngroup.dvd2c.service.ContentService;
import com.enseirb.telecom.dngroup.dvd2c.service.ContentServiceImpl;
import com.enseirb.telecom.dngroup.dvd2c.service.RabbitMQServer;
import com.google.common.io.Files;

// The Java class will be hosted at the URI path "/app/content"
@Path("app/{userID}/content")
@RolesAllowed("other")
public class ContentEndPoints {

	private static final Logger LOGGER = LoggerFactory.getLogger(ContentEndPoints.class);

	ContentService uManager = new ContentServiceImpl(new ContentRepositoryMongo(), new RabbitMQServer());

	/**
	 * Get all contents for a user. This request only called by videos owners
	 * @param userID
	 * @return Content list
	 */
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<Content> getAllContentsFromUser(@PathParam("userID") String userID) {
		return uManager.getAllContentsFromUser(userID);
	}

	/**
	 * Get a specific content from the owner
	 * @param userID
	 * @return Content list
	 */
	@GET
	@Path("{contentsID}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Content getSpecificContentInformations(@PathParam("userID") String userID, @PathParam("contentsID") String contentsID) {
		Content content = uManager.getContent(contentsID);
		if ( content.getLogin().equals(userID) ) {
			return content;
		}
		else {
			// No URL parameter idLanguage was sent
			ResponseBuilder builder = Response.status(Response.Status.FORBIDDEN);
			builder.entity("This content doesn't belong to you ! ");
			Response response = builder.build();
			throw new WebApplicationException(response);	
		}

	}



	//	@POST
	//	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	//	public Response postVideo(Content content) {
	//		return Response.status(Status.SERVICE_UNAVAILABLE).build();
	//	}

	/**
	 * post a file on the box for the userID
	 * @param userID the sender of the request
	 * @param uploadedInputStream
	 * @param fileDetail
	 * @param body
	 * @return
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response postcontent(@PathParam("userID") String userID,
			@FormDataParam("file") InputStream uploadedInputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetail,
			@FormDataParam("file") FormDataBodyPart body)
					throws URISyntaxException, IOException {
		String fileName = fileDetail.getFileName();
		String extension = Files.getFileExtension(fileName);			
		MediaType fileMediaType = body.getMediaType();
		String fileTypeTemp = fileMediaType.toString();
		String [] fileType = fileTypeTemp.split("/");

		File upload = File.createTempFile(userID, "."+extension,Files.createTempDir());

		
		Content content = uManager.createContent(userID, uploadedInputStream, fileType, upload);
		return Response.created(new URI("app/"+userID+"/content/"+content.getContentsID())).build();

	}

	

	/**
	 * Update information for the video
	 * @param content the content
	 * @param contentsID the id of the content
	 * @return
	 */
	@PUT
	@Path("{contentsID}")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response putContent(Content content,@PathParam("contentsID") String contentsID) {
		// TODO: need to check the authentication of the user
		content.setContentsID(contentsID);
		// modify the content
		if (uManager.contentExist(content.getContentsID()) == true) {
			uManager.saveContent(content);
			return Response.status(200).build();
		} else {
			return Response.status(409).build();
		}
	}

	/**
	 * delete the contents with contentsID 
	 * @param contentsID the contentsID to delete
	 * @return
	 */
	@DELETE
	@Path("{contentsID}")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response deleteContent(@PathParam("contentsID") String contentsID) {
		// TODO: need to check the authentication of the user

		// delete the content
		uManager.deleteContent(contentsID);
		return Response.status(200).build();

	}

}
