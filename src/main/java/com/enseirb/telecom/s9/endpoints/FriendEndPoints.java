package com.enseirb.telecom.s9.endpoints;

import java.util.ListResourceBundle;

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

import com.enseirb.telecom.s9.Friend;

// The Java class will be hosted at the URI path "/app/friends"
@Path("/app/friends")
public class FriendEndPoints {

	// move in groupe
	// @GET
	// @Produces(MediaType.APPLICATION_XML)
	// public ListRelation getFriendliste() {
	// // TODO: get the list of relation
	// // NHE: easy way to return an error for a rest api: throw an
	// WebApplicationException
	// throw new WebApplicationException(Status.CONFLICT);
	// }
	//
	@GET
	@Path("{username}")
	@Produces(MediaType.APPLICATION_XML)
	public Friend getFriend() {
		// TODO: get info of username relation
		// NHE: easy way to return an error for a rest api: throw an
		// WebApplicationException
		throw new WebApplicationException(Status.CONFLICT);
	}

	@POST
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response postFriend(Friend friend) {
		// TODO: ajout un ami
		// add a friend
		return Response.status(Status.SERVICE_UNAVAILABLE).build();

	}

	@PUT
	@Path("{username}")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response putFriend(Friend friend) {
		// TODO: change de groupe et confirmé une demande d'ajout
		// Pour confirmé un ami, il faut : regarder la valeur qui existe dans la
		// data base si on a décidé quelle serait sous la forme zéro demande
		// émise, une demande reçue et deux demande accepter alors si c'est
		// l'user local qui fait la demande pour passer à deux la valeur et
		// quelle était à un OK sinon refus
		// need to verify the friend
		// and after this modifies the friend
		return Response.status(Status.SERVICE_UNAVAILABLE).build();

	}

	@DELETE
	@Path("{username}")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response deleteFriend(Friend friend) {
		// TODO: delete this friends thinks to send a message to the over box
		// and after this delete the user
		return Response.status(Status.SERVICE_UNAVAILABLE).build();

	}

}
