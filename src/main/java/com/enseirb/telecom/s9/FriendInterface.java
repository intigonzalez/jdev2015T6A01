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

// The Java class will be hosted at the URI path "/myresource"
@Path("/app/friends")
public class FriendInterface {

	// TODO: update the class to suit your needs

	// The Java method will process HTTP GET requests
	// The Java method will produce content identified by the MIME Media
	// type "text/plain"
	@GET
	@Path("{username}")
	@Produces(MediaType.APPLICATION_XML)
	public Response getFriend() {
		// need to create
		return Response.status(Status.SERVICE_UNAVAILABLE).build();
	}

	@POST
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response postFriend(Friend friend) {
		// add a friend
		return Response.status(Status.SERVICE_UNAVAILABLE).build();
		

	}

	@PUT
	@Path("{username}")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response putFriend(Friend friend) {
		// need to verify the friend
		// and after this modifies the friend
		return Response.status(Status.SERVICE_UNAVAILABLE).build();

	}
	
	@DELETE
	@Path("{username}")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response deleteFriend(Friend friend) {
		// need to verify the user
		// and after this delete the user
		return Response.status(Status.SERVICE_UNAVAILABLE).build();

	}

}
