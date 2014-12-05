package com.enseirb.telecom.s9.endpoints;


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

import com.enseirb.telecom.s9.Task;



// The Java class will be hosted at the URI path "/myresource"
@Path("app/task")
public class TaskEndPoints {

	@GET
	@Path("{taskID}")
	@Produces(MediaType.APPLICATION_XML)
	public Task getIt() {
		// need to create
		// NHE: easy way to return an error for a rest api: throw an
		// WebApplicationException
		throw new WebApplicationException(Status.CONFLICT);
	}

	@POST
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response postComment(Task task) {
		// add a comment
		return Response.status(Status.SERVICE_UNAVAILABLE).build();

	}

	//not obligatory
	@PUT
	@Path("{taskID}")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response putComment(Task task) {
		// need to verify user
		// and after this modifies the comment
		return Response.status(Status.SERVICE_UNAVAILABLE).build();

	}

	@DELETE
	@Path("{taskID}")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response deleteComment(Task task) {
		// need to verify user
		// and after this delete the comment
		return Response.status(Status.SERVICE_UNAVAILABLE).build();

	}

}
