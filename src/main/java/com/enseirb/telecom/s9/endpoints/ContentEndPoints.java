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

import com.enseirb.telecom.s9.Content;
import com.enseirb.telecom.s9.db.ContentRepositoryMongo;
import com.enseirb.telecom.s9.service.ContentService;
import com.enseirb.telecom.s9.service.ContentServiceImpl;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

// The Java class will be hosted at the URI path "/app/video"
@Path("/app/{userID}/video")
public class ContentEndPoints {

	ContentService uManager = new ContentServiceImpl(new ContentRepositoryMongo());

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
	@Path("{email}")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response postcontent(
			Content content,
			@FormDataParam("file") InputStream uploadedInputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetail) throws URISyntaxException {
		
		// Le uploadedFileLocation doit être changé suivant le besoin
		String uploadedFileLocation = "Desktop/"
				+ fileDetail.getFileName();

		// save it
		writeToFile(uploadedInputStream, uploadedFileLocation);

		String output = "File uploaded to : " + uploadedFileLocation;
		

		uManager.saveContent(content); // save ou create ???
		// return Response.status(200).entity(output).build();
		return Response.created(new URI(content.getContentsID())).build();

	}

	// save uploaded file to new location
	private void writeToFile(InputStream uploadedInputStream,
			String uploadedFileLocation) {

		try {
			OutputStream out = new FileOutputStream(new File(
					uploadedFileLocation));
			int read = 0;
			byte[] bytes = new byte[1024];

			out = new FileOutputStream(new File(uploadedFileLocation));
			while ((read = uploadedInputStream.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
			out.flush();
			out.close();
		} catch (IOException e) {

			e.printStackTrace();
		}

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
