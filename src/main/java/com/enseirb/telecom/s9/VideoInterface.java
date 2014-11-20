package com.enseirb.telecom.s9;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

// The Java class will be hosted at the URI path "/app/video"
@Path("/app/video")
public class VideoInterface {

	// TODO: update the class to suit your needs

	// The Java method will process HTTP GET requests
	// The Java method will produce content identified by the MIME Media
	// type "text/plain"
	@GET
	@Path("{videoID}")
	@Produces(MediaType.APPLICATION_XML)
	public Response getIt() {
		// need to create
		return Response.status(Status.SERVICE_UNAVAILABLE).build();
	}

	@POST
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response postVideo(Video video) {
		return Response.status(Status.SERVICE_UNAVAILABLE).build();
	}

	@PUT
	@Path("{videoID}")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response putVideo(Video video) {
		// need to verify the user
		// and after this modifies the user
		return Response.status(Status.SERVICE_UNAVAILABLE).build();

	}
	
	@DELETE
	@Path("{videoID}")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response deleteVideo(Video video) {
		// need to verify the user
		// and after this delete the user
		return Response.status(Status.SERVICE_UNAVAILABLE).build();

	}

}
