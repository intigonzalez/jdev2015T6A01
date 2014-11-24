package com.enseirb.telecom.s9.endpoints;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.enseirb.telecom.s9.Box;
import com.enseirb.telecom.s9.Content;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

// The Java class will be hosted at the URI path "/app/video"
@Path("/app/video")
public class ContentEndPoints {

	// TODO: update the class to suit your needs

	// The Java method will process HTTP GET requests
	// The Java method will produce content identified by the MIME Media
	// type "text/plain"
	@GET
	@Path("{videoID}")
	@Produces(MediaType.APPLICATION_XML)
	public Box getIt() {
		// need to create
		// NHE: easy way to return an error for a rest api: throw an WebApplicationException
		throw new WebApplicationException(Status.CONFLICT);
	}

//	@POST
//	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
//	public Response postVideo(Content content) {
//		return Response.status(Status.SERVICE_UNAVAILABLE).build();
//	}
	

	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response uploadFile(
			@FormDataParam("file") InputStream uploadedInputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetail) {
		// Le uploadedFileLocation doit être changé suivant le besoin
		String uploadedFileLocation = "Desktop/"
				+ fileDetail.getFileName();

		// save it
		writeToFile(uploadedInputStream, uploadedFileLocation);

		String output = "File uploaded to : " + uploadedFileLocation;

		return Response.status(200).entity(output).build();

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
	
//
//	@PUT
//	@Path("{videoID}")
//	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
//	public Response putVideo(Contents contents) {
//		// need to verify the user
//		// and after this modifies the user
//		return Response.status(Status.SERVICE_UNAVAILABLE).build();
//
//	}
//	
//	@DELETE
//	@Path("{videoID}")
//	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
//	public Response deleteVideo(Contents contents) {
//		// need to verify the user
//		// and after this delete the user
//		return Response.status(Status.SERVICE_UNAVAILABLE).build();
//
//	}

}
