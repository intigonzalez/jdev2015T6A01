package com.enseirb.telecom.s9.endpoints;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

import javax.management.relation.RelationService;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import com.enseirb.telecom.s9.Content;
import com.enseirb.telecom.s9.ListContent;
import com.enseirb.telecom.s9.Relation;
import com.enseirb.telecom.s9.db.ContentRepositoryMongo;
import com.enseirb.telecom.s9.db.RelationshipRepositoryMongo;
import com.enseirb.telecom.s9.db.UserRepositoryMongo;
import com.enseirb.telecom.s9.service.ContentService;
import com.enseirb.telecom.s9.service.ContentServiceImpl;
import com.enseirb.telecom.s9.service.RabbitMQServer;
import com.enseirb.telecom.s9.service.RelationServiceImpl;
import com.google.common.io.Files;

// The Java class will be hosted at the URI path "/app/content"
@Path("app/{userID}/content")
public class ContentEndPoints {

	ContentService uManager = new ContentServiceImpl(new ContentRepositoryMongo(), new RabbitMQServer());

	// TODO: update the class to suit your needs

	// The Java method will process HTTP GET requests
	// The Java method will produce content identified by the MIME Media
	// type "text/plain"
	@GET
	@Path("{contentsID}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Content getIt(@PathParam("contentsID") String contentsID) {
		return uManager.getContent(contentsID);
	}

	/**
	 * get the local video of userID (local) for RelationID
	 * @param contentsID
	 * @param relationID
	 * @param userID
	 * @return
	 */
	@GET
	@Path("relation/{relationID}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public ListContent getLocalFromRelation(@PathParam("contentsID") String contentsID,@PathParam("relationID") String relationID,@PathParam("userID") String userID) {
		RelationServiceImpl relationService = new RelationServiceImpl(new RelationshipRepositoryMongo(), new UserRepositoryMongo());
		 if (relationService.RelationExist(userID, relationID)){
			Relation relation = relationService.getRelation(userID, relationID);
			return uManager.getAllContent(relation.getGroupID());
		 }
		return null;
	}
	

	
//	@POST
//	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
//	public Response postVideo(Content content) {
//		return Response.status(Status.SERVICE_UNAVAILABLE).build();
//	}
	

	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response postcontent(@PathParam("userID") String email,
			@FormDataParam("file") InputStream uploadedInputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetail,
			@FormDataParam("file") FormDataBodyPart body)
			throws URISyntaxException, IOException {
		String fileName = fileDetail.getFileName();
		String extension = Files.getFileExtension(fileName);			
		MediaType fileMediaType = body.getMediaType();
		String fileTypeTemp = fileMediaType.toString();
		String [] fileType = fileTypeTemp.split("/");
		
		File upload = File.createTempFile(email, "."+extension,Files.createTempDir());

		//NHE: all the rest should be in the Service Layer
		// save it
		uManager.writeToFile(uploadedInputStream, upload);

		//System.out.println("File uploaded to : " + upload.getAbsolutePath());
		System.out.println("File type : " + fileType[0]);
		
		Content content = new Content();
		content.setName(upload.getName());
		content.setLogin(email);
		content.setStatus("In progress");
		content.setType(fileType[0]);
		UUID uuid = UUID.randomUUID();
		content.setContentsID(uuid.toString().replace("-", ""));
		String link = "/videos/"+email+"/"+uuid.toString();
		content.setLink(link);
		long unixTime = System.currentTimeMillis() / 1000L;
		content.setUnixTime(unixTime);

		content = uManager.createContent(content,upload.getAbsolutePath(), content.getContentsID());
		return Response.created(new URI("app/"+email+"/content/"+content.getContentsID())).build();

	}


	@PUT
	@Path("{contentsID}")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response putContent(Content content) {
		// TODO: need to check the authentication of the user
		
		// modify the content
		if (uManager.contentExist(content.getContentsID()) == true) {
			uManager.saveContent(content);
			return Response.status(200).build();
		} else {
			return Response.status(409).build();
		}


	}
	
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
