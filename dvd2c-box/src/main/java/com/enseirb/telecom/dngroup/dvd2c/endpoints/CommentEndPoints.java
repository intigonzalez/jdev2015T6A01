/**
 * NOT USE CURENTLY
 */
package com.enseirb.telecom.dngroup.dvd2c.endpoints;

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

import com.enseirb.telecom.dngroup.dvd2c.model.Comment;

// The Java class will be hosted at the URI path "/myresource"
@Path("app/{userID}/contents/{contentID}")
public class CommentEndPoints {

	// This part is not used

	
	@GET
	@Path("{commentID}")
	@Produces(MediaType.APPLICATION_XML)
	public Comment getIt() {		
		throw new WebApplicationException(Status.NOT_IMPLEMENTED);
	}

	@POST
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response postComment(@PathParam("userID") String userID, Comment comment) {
		return Response.status(Status.NOT_IMPLEMENTED).build();

	}

	@PUT
	@Path("{commentID}")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response putComment(Comment comment) {
		return Response.status(Status.NOT_IMPLEMENTED).build();

	}

	@DELETE
	@Path("{commentID}")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response deleteComment(Comment comment) {
		return Response.status(Status.NOT_IMPLEMENTED).build();

	}

}
