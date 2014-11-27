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

import com.enseirb.telecom.s9.Relation;

//import com.enseirb.telecom.s9.Relation;

// The Java class will be hosted at the URI path "/app/friends"
@Path("app/{userID}/friends")
public class RelationEndPoints {

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
	public Relation getRelation() {
		// TODO: get info of username relation
		// NHE: easy way to return an error for a rest api: throw an
		// WebApplicationException
		throw new WebApplicationException(Status.CONFLICT);
	}

	@POST
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response postFriend(Relation relation) {
		// TODO: ajout un ami
		// add a friend
		return Response.status(Status.SERVICE_UNAVAILABLE).build();

	}

	@PUT
	@Path("{username}")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response putFriend(Relation relation) {
		// TODO: change de groupe et confirme une demande d'ajout
		// Pour confirme un ami, il faut : regarder la valeur qui existe dans la
		// data base si on a decide quelle serait sous la forme zero demande
		// emise, une demande recue et deux demande accepter alors si c'est
		// l'user local qui fait la demande pour passer a deux la valeur et
		// quelle etait a un OK sinon refus
		// need to verify the friend
		// and after this modifies the friend
		return Response.status(Status.SERVICE_UNAVAILABLE).build();

	}

	@DELETE
	@Path("{username}")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response deleteFriend(Relation relation) {
		// TODO: delete this friends thinks to send a message to the over box
		// and after this delete the user
		return Response.status(Status.SERVICE_UNAVAILABLE).build();

	}

}
