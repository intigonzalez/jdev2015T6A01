package com.enseirb.telecom.s9.endpoints;

import javax.ws.rs.Path;



// The Java class will be hosted at the URI path "/myresource"
@Path("app/task")
public class TaskEndPoints {

//	@GET
//	@Path("{taskID}")
//	@Produces(MediaType.APPLICATION_XML)
//	public tas getIt() {
//		// need to create
//		// NHE: easy way to return an error for a rest api: throw an
//		// WebApplicationException
//		throw new WebApplicationException(Status.CONFLICT);
//	}
//
//	@POST
//	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
//	public Response postComment(Task task) {
//		// add a comment
//		return Response.status(Status.SERVICE_UNAVAILABLE).build();
//
//	}
//
//	//not obligatory
//	@PUT
//	@Path("{taskID}")
//	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
//	public Response putComment(Task task) {
//		// need to verify user
//		// and after this modifies the comment
//		return Response.status(Status.SERVICE_UNAVAILABLE).build();
//
//	}
//
//	@DELETE
//	@Path("{taskID}")
//	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
//	public Response deleteComment(Task task) {
//		// need to verify user
//		// and after this delete the comment
//		return Response.status(Status.SERVICE_UNAVAILABLE).build();
//
//	}

}
