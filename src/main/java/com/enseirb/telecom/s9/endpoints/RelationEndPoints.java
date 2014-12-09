package com.enseirb.telecom.s9.endpoints;

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
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.enseirb.telecom.s9.ListRelation;
import com.enseirb.telecom.s9.Relation;
import com.enseirb.telecom.s9.User;
import com.enseirb.telecom.s9.db.CrudRepository;
import com.enseirb.telecom.s9.db.RelationshipRepositoryMongo;
import com.enseirb.telecom.s9.db.UserRepositoryMongo;
import com.enseirb.telecom.s9.db.UserRepositoryObject;
import com.enseirb.telecom.s9.service.AccountService;
import com.enseirb.telecom.s9.service.AccountServiceImpl;
import com.enseirb.telecom.s9.service.RelationService;
import com.enseirb.telecom.s9.service.RelationServiceImpl;

//import com.enseirb.telecom.s9.Relation;

// The Java class will be hosted at the URI path "/app/friends"
@Path("app/{userID}/friends")
public class RelationEndPoints {

	RelationService rManager = new RelationServiceImpl(new RelationshipRepositoryMongo(), new UserRepositoryMongo());

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
	public Relation getRelation(@PathParam("userID") String userIDFromPath, @PathParam("username") String relationIDFromPath) {
		if (rManager.RelationExist(userIDFromPath, relationIDFromPath) == true) {
			return rManager.getRelation(userIDFromPath, relationIDFromPath);
		} else {
			throw new WebApplicationException(Status.NOT_FOUND);
		}
	}
	
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public ListRelation getListRelation(@PathParam("userID") String userIDFromPath) {
		
			return rManager.getListRelation(userIDFromPath);
		
	}

	@POST
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response postFriend(@PathParam("userID") String userIDFromPath, Relation relation) throws URISyntaxException {
		// TODO: ajout un ami
		// add a friend
		if (rManager.RelationExist(userIDFromPath, relation.getEmail()) == false) {
			rManager.createRelation(userIDFromPath, relation);
			// NHE that the answer we expect from a post (see location header)
			return Response.created(new URI(relation.getEmail())).build();
		} else {

			return Response.status(409).build();
		}
	}

	@PUT
	@Path("{username}")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response putFriend(@PathParam("userID") String userIDFromPath, @PathParam("username") String friendEmail, Relation relation) {
		// TODO: change de groupe et confirme une demande d'ajout
		// Pour confirme un ami, il faut : regarder la valeur qui existe dans la
		// data base si on a decide quelle serait sous la forme zero demande
		// emise, une demande recue et deux demande accepter alors si c'est
		// l'user local qui fait la demande pour passer a deux la valeur et
		// quelle etait a un OK sinon refus
		// need to verify the friend and after this modifies the friend
		if (relation.getEmail().equals(friendEmail)) {
			if (rManager.RelationExist(userIDFromPath, relation.getEmail())) {
				rManager.saveRelation(userIDFromPath, relation);
				return Response.status(200).build();
			} else {
				return Response.status(404).build();
			}
		} else {
			return Response.status(403).build();
		}
	}

	@DELETE
	@Path("{username}")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response deleteFriend(@PathParam("userID") String userIDFromPath, @PathParam("username") String relationIDFromPath) {
		// TODO: delete this friends thinks to send a message to the over box
		// and after this delete the user
		rManager.deleteRelation(userIDFromPath, relationIDFromPath);
		return Response.status(200).build();

	}

}
