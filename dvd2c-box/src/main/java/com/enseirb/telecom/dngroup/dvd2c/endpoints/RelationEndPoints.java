package com.enseirb.telecom.dngroup.dvd2c.endpoints;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

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
import org.springframework.beans.factory.annotation.Autowired;

import com.enseirb.telecom.dngroup.dvd2c.exception.NoSuchRelationException;
import com.enseirb.telecom.dngroup.dvd2c.exception.NoSuchUserException;
import com.enseirb.telecom.dngroup.dvd2c.model.Content;
import com.enseirb.telecom.dngroup.dvd2c.model.Relation;
import com.enseirb.telecom.dngroup.dvd2c.model.User;
import com.enseirb.telecom.dngroup.dvd2c.service.RelationService;

//import com.enseirb.telecom.s9.Relation;

// The Java class will be hosted at the URI path "/app/friends"
@Path("app/{userID}/relation")
// @RolesAllowed("other") //The roles must be adapted depending on the function
// !
public class RelationEndPoints {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(RelationEndPoints.class);

	@Autowired
	protected RelationService rManager = null;

	/**
	 * get user for remote host
	 * 
	 * @param userIDFromPath
	 *            the userID to get information
	 * @param relationIDFromPath
	 *            the userID of request
	 * @return a user with information
	 */
	@GET
	@Path("from/{username}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public User getMeFRH(@PathParam("userID") String userIDFromPath,
			@PathParam("username") String relationIDFromPath) {
		if (rManager.RelationExist(userIDFromPath, relationIDFromPath) == true) {
			return rManager.getMe(userIDFromPath);
		} else {
			throw new WebApplicationException(Status.NOT_FOUND);
		}
	}

	/**
	 * Get relation information
	 * 
	 * @param userIDFromPath
	 *            the userID of the request
	 * @param relationIDFromPath
	 *            the relation to get information
	 * @return
	 */
	@RolesAllowed("other")
	@GET
	@Path("{relationID}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Relation getRelation(@PathParam("userID") String userIDFromPath,
			@PathParam("relationID") String relationIDFromPath) {
		try {
			return rManager.getRelation(userIDFromPath, relationIDFromPath);
		} catch (NoSuchRelationException e) {
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
	public List<Content> getRelationContents(
			@PathParam("relationID") String relationID,
			@PathParam("userID") String userID) {
		// TODO: need to change beacause the content of one user doit etre
		// recupre directement aupré de la ressource utilisateur

		Relation relation;
		try {
			relation = rManager.getRelation(userID, relationID);

			if (relation.getAprouve() == 3)
				return rManager.getAllContent(userID, relationID);
			else {
				throw new WebApplicationException(Status.FORBIDDEN);
			}
		} catch (NoSuchRelationException e) {
			throw new WebApplicationException(Status.NOT_FOUND);
		}

	}

	/**
	 * Get the list of relation of the userID
	 * 
	 * @param userIDFromPath
	 *            The userID to get list of relation
	 * @return the list of relation
	 */
	@RolesAllowed("other")
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<Relation> getListRelation(
			@PathParam("userID") String userIDFromPath) {

		return rManager.getListRelation(userIDFromPath);

	}

	/**
	 * add relation on database of userID
	 * 
	 * @Deprecated
	 * @param userIDFromPath
	 *            the userID root
	 * @param relationID
	 *            the relation to add at userID
	 * @return WebStatus
	 * @throws URISyntaxException
	 */
	@Deprecated
	@POST
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response oldpostFriend(@PathParam("userID") String userIDFromPath,
			Relation relationID) throws URISyntaxException {
		return postFriend2(userIDFromPath, relationID.getActorID());
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
	@RolesAllowed("other")
	@POST
	@Path("{relationID}")
	public Response postFriend2(@PathParam("userID") String userIDFromPath,
			@PathParam("relationID") String relationIDString)
			throws URISyntaxException {
		LOGGER.debug("add a relation between {} and {} ", userIDFromPath,
				relationIDString);
		if (rManager.RelationExist(userIDFromPath, relationIDString) == false) {
			try {
				rManager.createDefaultRelation(userIDFromPath,
						relationIDString, false);
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
	public Response postFriendFromBox(
			@PathParam("userID") String userIDFromPath, Relation relation)
			throws URISyntaxException {
		LOGGER.debug("add a relation from box between {} and {} ",
				userIDFromPath, relation.getActorID());

		if (rManager.RelationExist(userIDFromPath, relation.getActorID()) == false) {
			try {
				rManager.createRelation(userIDFromPath, relation, true);
			} catch (NoSuchUserException e) {
				throw new WebApplicationException(Status.NOT_FOUND);
			}
			// NHE that the answer we expect from a post (see location header)
			return Response.created(new URI(relation.getActorID())).build();
		} else {

			return Response.status(Status.CONFLICT).build();
		}
	}

	@PUT
	@RolesAllowed("other")
	@Path("{username}")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response putFriend(@PathParam("userID") String userIDFromPath,
			@PathParam("username") String friendActorID, Relation relation) {
		// need to verify the friend and after this modifies the friend
		if (relation.getActorID() == null) {
			relation.setActorID(friendActorID);
		}
		if (relation.getActorID().equals(friendActorID)) {
			if (rManager.RelationExist(userIDFromPath, relation.getActorID())) {
				rManager.saveRelation(userIDFromPath, relation);
				return Response.status(200).build();
			} else {
				return Response.status(404).build();
			}

		} else {
			return Response.status(Status.NOT_ACCEPTABLE).build();
		}

	}

	/**
	 * Update the list of friend of UserID Update information of each user
	 * 
	 * @param userIDFromPath
	 *            the userID to update relation
	 * @return webstatus
	 */
	@PUT
	@RolesAllowed("other")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response updateListFriend(@PathParam("userID") String userIDFromPath) {
		try {
			rManager.updateRelation(userIDFromPath);
			return Response.status(200).build();
		} catch (IOException e) {
			LOGGER.error("Updating fail can not connect", e);
			return Response.status(403).build();
		} catch (NoSuchUserException e) {
			LOGGER.error("Updating fail no user {}", userIDFromPath, e);
			return Response.status(403).build();
		} catch (Exception e) {
			LOGGER.error("Updating fail", e);
			return Response.status(403).build();
		}
	}

	/**
	 * delete a relation on this box and in the over box
	 * 
	 * @param userIDFromPath
	 * @param relationIDFromPath
	 * @return
	 */
	@DELETE
	@Path("{username}")
	@RolesAllowed("other")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response deleteFriend(@PathParam("userID") String userIDFromPath,
			@PathParam("username") String relationIDFromPath) {
		rManager.deleteRelation(userIDFromPath, relationIDFromPath);
		return Response.status(200).build();
	}
}
