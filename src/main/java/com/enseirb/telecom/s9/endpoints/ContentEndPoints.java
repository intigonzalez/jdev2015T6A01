package com.enseirb.telecom.s9.endpoints;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;

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

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import com.enseirb.telecom.s9.Content;
import com.enseirb.telecom.s9.db.ContentRepositoryObject;
import com.enseirb.telecom.s9.db.mock.CrudRepositoryMock;
import com.enseirb.telecom.s9.service.ContentService;
import com.enseirb.telecom.s9.service.ContentServiceImpl;
import com.google.common.io.Files;

// The Java class will be hosted at the URI path "/app/video"
@Path("app/{userID}/video")
public class ContentEndPoints {

	ContentService uManager = new ContentServiceImpl(
			new CrudRepositoryMock<ContentRepositoryObject>() {

				@Override
				protected String getID(ContentRepositoryObject t) {
					return t.getUserId();
				}
			});

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

//	@POST
//	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
//	public Response postVideo(Content content) {
//		return Response.status(Status.SERVICE_UNAVAILABLE).build();
//	}
	

	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response postcontent(@PathParam("userID") String email,
			@FormDataParam("file") InputStream uploadedInputStream)
			throws URISyntaxException, IOException {
		// Le uploadedFileLocation doit être changé suivant le besoin
		

		
		File upload = File.createTempFile("nicolas", "enseirb",
				Files.createTempDir());

		//NHE: all the rest should be in the Service Layer
		// save it
		uManager.writeToFile(uploadedInputStream, upload);

		String output = "File uploaded to : " + upload.getAbsolutePath();
		Content content = new Content();
		content.setName(upload.getName());
		content.setLogin(email);
		content.setStatus("In progress");
		content.setLink("/content/" + upload.getName());
		long unixTime = System.currentTimeMillis() / 1000L;
		content.setUnixTime(unixTime);
		
		content = uManager.createContent(content); 
		return Response.created(new URI(content.getContentsID())).build();

	}


	@PUT
	@Path("{contentsID}")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response putContent(Content content) {
		// TODO: need to check the authentication of the user
		
		// modify the content
		if (uManager.contentExist(content.getContentsID()) == false) {
			uManager.saveContent(content);;
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
