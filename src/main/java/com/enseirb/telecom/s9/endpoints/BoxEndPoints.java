package com.enseirb.telecom.s9.endpoints;

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
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enseirb.telecom.s9.Box;

// The Java class will be hosted at the URI path "/myresource"
@Path("box")
public class BoxEndPoints {
	private static final Logger LOGGER = LoggerFactory.getLogger(BoxEndPoints.class);


	// TODO: update the class to suit your needs

	// The Java method will process HTTP GET requests
	// The Java method will produce content identified by the MIME Media
	// type "text/plain"
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public Box getBox() {
		// NHE: easy way to return an error for a rest api: throw an WebApplicationException
		throw new WebApplicationException(Status.CONFLICT);
	}

	@POST
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response postBox(Box box) {
		// add a comment
		return Response.status(Status.SERVICE_UNAVAILABLE).build();

	}

	@PUT
	@Path("{boxId}")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response putBox(Box box) {
		// need to verify user
		// and after this modifies the comment
		return Response.status(Status.SERVICE_UNAVAILABLE).build();

	}

	@DELETE
	@Path("{boxId}")
	public Response deleteBox(@PathParam("boxId") String boxId) {
		// need to verify user
		// and after this delete the comment
		return Response.status(Status.SERVICE_UNAVAILABLE).build();

	}

}
