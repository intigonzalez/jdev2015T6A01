package com.enseirb.telecom.s9.endpoints;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.annotation.security.RolesAllowed;
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

import com.enseirb.telecom.s9.ListContent;
import com.enseirb.telecom.s9.ListRelation;
import com.enseirb.telecom.s9.Relation;
import com.enseirb.telecom.s9.User;
import com.enseirb.telecom.s9.db.RelationshipRepositoryMongo;
import com.enseirb.telecom.s9.db.UserRepositoryMongo;
import com.enseirb.telecom.s9.exception.NoSuchUserException;
import com.enseirb.telecom.s9.service.RelationService;
import com.enseirb.telecom.s9.service.RelationServiceImpl;

//import com.enseirb.telecom.s9.Relation;

// The Java class will be hosted at the URI path "/app/friends"
@Path("app/{userID}/relation")
@RolesAllowed("relation")
public class RelationEndPoints {
	private static final Logger LOGGER = LoggerFactory.getLogger(RelationEndPoints.class);

	RelationService rManager = new RelationServiceImpl(new RelationshipRepositoryMongo(),
			new UserRepositoryMongo());

	@GET
	@Path("from/{username}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public User getMe(@PathParam("userID") String userIDFromPath,
			@PathParam("username") String relationIDFromPath) {
		if (rManager.RelationExist(userIDFromPath, relationIDFromPath) == true) {
			return rManager.getMe(userIDFromPath);
		} else {
			throw new WebApplicationException(Status.NOT_FOUND);
		}
	}

	@GET
	@Path("{username}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Relation getRelation(@PathParam("userID") String userIDFromPath,
			@PathParam("username") String relationIDFromPath) {

		if (rManager.RelationExist(userIDFromPath, relationIDFromPath) == true) {
			return rManager.getRelation(userIDFromPath, relationIDFromPath);
		} else {
			throw new WebApplicationException(Status.NOT_FOUND);
		}
	}

	/**
	 * get the video of RelationID (local) for userID
	 * 
	 * @param contentsID
	 * @param relationID
	 * @param userID
	 * @return
	 */
	@GET
	@Path("{relationID}/content")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public ListContent getRelationContents(@PathParam("userID") String contentsID,
			@PathParam("relationID") String relationID, @PathParam("userID") String userID) {

		if (rManager.RelationExist(userID, relationID)) {
			Relation relation = rManager.getRelation(userID, relationID);
			return rManager.getAllContent(relationID, userID);
		}
		return null;
	}

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public ListRelation getListRelation(@PathParam("userID") String userIDFromPath) {

		return rManager.getListRelation(userIDFromPath);

	}

	/**
	 * add relation on database of userID
	 * 
	 * @param userIDFromPath
	 * @param relation
	 * @return
	 * @throws URISyntaxException
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response postFriend(@PathParam("userID") String userIDFromPath, Relation relation)
			throws URISyntaxException {
		// TODO: ajout un ami
		// add a friend
		if (rManager.RelationExist(userIDFromPath, relation.getEmail()) == false) {
			try {
				rManager.createRelation(userIDFromPath, relation, false);
			} catch (NoSuchUserException e) {
				throw new WebApplicationException(404);
			}
			// NHE that the answer we expect from a post (see location header)
			return Response.created(new URI(relation.getEmail())).build();
		} else {

			return Response.status(409).build();
		}
	}

	/**
	 * add relationID on database of userID
	 * 
	 * @param userIDFromPath
	 *            user id of dataBase
	 * @param relationIDString
	 *            relation to add
	 * @return
	 * @throws URISyntaxException
	 */
	@POST
	@Path("{relationID}")
	public Response postFriend2(@PathParam("userID") String userIDFromPath,
			@PathParam("relationID") String relationIDString) throws URISyntaxException {
		// TODO: ajout un ami
		// add a friend
		if (rManager.RelationExist(userIDFromPath, relationIDString) == false) {
			try {
				rManager.createDefaultRelation(userIDFromPath, relationIDString, false);
			} catch (NoSuchUserException e) {
				throw new WebApplicationException(404);
			}
			// NHE that the answer we expect from a post (see location header)
			return Response.created(new URI(relationIDString)).build();
		} else {

			return Response.status(409).build();
		}
	}

	/**
	 * add relation on database of userID from this relation
	 * 
	 * @param userIDFromPath
	 * @param relation
	 * @return
	 * @throws URISyntaxException
	 */
	@POST
	@Path("frombox")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response postFriendFromBox(@PathParam("userID") String userIDFromPath, Relation relation)
			throws URISyntaxException {
		// TODO: ajout un ami
		// add a friend
		if (rManager.RelationExist(userIDFromPath, relation.getEmail()) == false) {
			try {
				rManager.createRelation(userIDFromPath, relation, true);
			} catch (NoSuchUserException e) {
				throw new WebApplicationException(Status.NOT_FOUND);
			}
			// NHE that the answer we expect from a post (see location header)
			return Response.created(new URI(relation.getEmail())).build();
		} else {

			return Response.status(Status.CONFLICT).build();
		}
	}

	@PUT
	@Path("{username}")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response putFriend(@PathParam("userID") String userIDFromPath,

	@PathParam("username") String friendEmail, Relation relation) {

		// TODO: change de groupe et confirme une demande d'ajout
		// Pour confirme un ami, il faut : regarder la valeur qui existe dans la
		// data base si on a decide quelle serait sous la forme zero demande
		// emise, une demande recue et deux demande accepter alors si c'est
		// l'user local qui fait la demande pour passer a deux la valeur et
		// quelle etait a un OK sinon refus
		// need to verify the friend and after this modifies the friend
		if (relation.getEmail()==null){
			relation.setEmail(friendEmail);
		}
		if (relation.getEmail().equals(friendEmail)) {
			if (rManager.RelationExist(userIDFromPath, relation.getEmail())) {
				rManager.saveRelation(userIDFromPath, relation);
				return Response.status(200).build();
			} else {
				return Response.status(404).build();
			}

		} else {
			return Response.status(Status.NOT_ACCEPTABLE).build();
		}

	}

	@PUT
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response updateListFriend(@PathParam("userID") String userIDFromPath) {

		try {
			rManager.updateRelation(userIDFromPath);
			return Response.status(200).build();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Response.status(403).build();
		} catch (NoSuchUserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Response.status(403).build();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Response.status(403).build();

		}

	}

	/**
	 * delete a relation on this box and in the over box
	 * @param userIDFromPath
	 * @param relationIDFromPath
	 * @return
	 */
	@DELETE
	@Path("{username}")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response deleteFriend(@PathParam("userID") String userIDFromPath,
			@PathParam("username") String relationIDFromPath) {

		// TODO: delete this friends thinks to send a message to the over box
		// and after this delete the user
		rManager.deleteRelation(userIDFromPath, relationIDFromPath);
		return Response.status(200).build();
	}
}
