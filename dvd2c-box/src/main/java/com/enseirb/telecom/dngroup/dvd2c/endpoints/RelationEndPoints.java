package com.enseirb.telecom.dngroup.dvd2c.endpoints;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
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

import com.enseirb.telecom.dngroup.dvd2c.exception.NoRelationException;
import com.enseirb.telecom.dngroup.dvd2c.exception.NoRoleException;
import com.enseirb.telecom.dngroup.dvd2c.exception.NoSuchBoxException;
import com.enseirb.telecom.dngroup.dvd2c.exception.NoSuchContactException;
import com.enseirb.telecom.dngroup.dvd2c.exception.NoSuchUserException;
import com.enseirb.telecom.dngroup.dvd2c.model.ContactXSD;
import com.enseirb.telecom.dngroup.dvd2c.model.Content;
import com.enseirb.telecom.dngroup.dvd2c.model.User;
import com.enseirb.telecom.dngroup.dvd2c.service.AccountService;
import com.enseirb.telecom.dngroup.dvd2c.service.RelationService;

//import com.enseirb.telecom.s9.Relation;

// The Java class will be hosted at the URI path "/app/friends"
@Path("app/{userUUID}/relation")
// @RolesAllowed("other") //The roles must be adapted depending on the function
// !
public class RelationEndPoints {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(RelationEndPoints.class);

	@Inject
	protected RelationService rManager;

	@Inject
	protected AccountService aService;

	/**
	 * get user for a remote host
	 * 
	 * @param userIDFromPath
	 *            the userID to get information
	 * @param relationUUID
	 *            the userID of request
	 * @return a user with information
	 */
	@GET
	@Path("from/{relationUUID}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public User getMeFRH(@PathParam("userUUID") UUID userIDFromPath,
			@PathParam("relationUUID") UUID relationUUID) {
		if (rManager.RelationExist(userIDFromPath, relationUUID) == true) {
			return aService.getContactInformation(userIDFromPath);
		} else {
			throw new WebApplicationException(Status.NOT_FOUND);
		}
	}

	/**
	 * Get relation information
	 * 
	 * @param userIDFromPath
	 *            the userID of the request
	 * @param relationUUID
	 *            the relation to get information
	 * @return
	 */
	@RolesAllowed("other")
	@GET
	@Path("{relationUUID}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public ContactXSD getRelation(@PathParam("userUUID") UUID userIDFromPath,
			@PathParam("relationUUID") UUID relationUUID) {
		try {
			return rManager.getRelation(userIDFromPath, relationUUID);
		} catch (NoSuchContactException e) {
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
	@Path("{relationUUID}/content")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<Content> getRelationContents(
			@PathParam("relationUUID") UUID relationID,
			@PathParam("userUUID") UUID userID) {
		// TODO: need to change beacause the content of one user doit etre
		// recupre directement aupré de la ressource utilisateur

		ContactXSD relation;
		try {
			relation = rManager.getRelation(userID, relationID);

			if (relation.getAprouve() == 3)
				return rManager.getAllContent(userID, relationID);
			else {
				throw new WebApplicationException(Status.FORBIDDEN);
			}
		} catch (NoSuchContactException e) {
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
	public List<ContactXSD> getListRelation(
			@PathParam("userUUID") UUID userIDFromPath) {

		return rManager.getListContact(userIDFromPath);

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
	public Response oldpostFriend(@PathParam("userUUID") UUID userIDFromPath,
			ContactXSD relationID) throws URISyntaxException {
		return postFriend2(userIDFromPath,
				UUID.fromString(relationID.getUuid()));
	}

	/**
	 * add relationID on database of userID
	 * 
	 * @param userUUID
	 *            user id of dataBase
	 * @param relationUUID
	 *            relation to add
	 * @return
	 * @throws URISyntaxException
	 */
	@RolesAllowed("other")
	@POST
	@Path("{relationUUID}")
	public Response postFriend2(@PathParam("userUUID") UUID userUUID,
			@PathParam("relationUUID") UUID relationUUID)
			throws URISyntaxException {
		LOGGER.debug("add a relation between {} and {} ", userUUID,
				relationUUID);
		if (rManager.RelationExist(userUUID, relationUUID) == false) {
			try {
				ContactXSD r = rManager.createRelation(userUUID, relationUUID,
						false);
				// RABC: FIXE it
				return Response
						.created(
								new URI("app/" + userUUID + "/relation/"
										+ r.getUuid())).build();
			} catch (NoSuchUserException e) {
				throw new WebApplicationException("no such user", 404);
			} catch (IOException e) {
				throw new WebApplicationException(e, 500);
			} catch (NoSuchBoxException e) {
				throw new WebApplicationException("no such box", 404);
			}
			// NHE that the answer we expect from a post (see location header)

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
	public Response postFriendFromBox(@PathParam("userID") UUID userIDFromPath,
			ContactXSD relation) throws URISyntaxException {
		LOGGER.debug("add a relation from box between {} and {} ",
				userIDFromPath, relation.getActorID());

		if (rManager.RelationExist(userIDFromPath,
				UUID.fromString(relation.getActorID())) == false) {
			try {
				rManager.createRelation(userIDFromPath,
						UUID.fromString(relation.getActorID()), true);
			} catch (NoSuchUserException e) {
				throw new WebApplicationException(Status.NOT_FOUND);
			} catch (IOException e) {
				throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR);
			} catch (NoSuchBoxException e) {
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
	@Path("{relationUUID}")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response putFriend(@PathParam("userUUID") UUID userUUID,
			@PathParam("relationUUID") UUID relationUUID, ContactXSD contact) {
		// need to verify the friend and after this modifies the friend
		try {
			ContactXSD c = rManager.getRelation(userUUID, relationUUID);
			contact.setUuid(c.getUuid());
			if (contact.getUuid().equals(relationUUID.toString())) {
				rManager.saveRelation(userUUID, contact);
				return Response.status(200).build();
			} else {
				return Response.status(Status.NOT_ACCEPTABLE).build();
			}
		} catch (NoSuchContactException e1) {
			LOGGER.error("No Such Contact Exception");
			throw new WebApplicationException("No Such Contact Exception",Status.NOT_FOUND);
		} catch (NoRelationException e) {
			LOGGER.error("No Such Relation Exception");
			throw new WebApplicationException("No Such Relation Exception", Status.NOT_FOUND);
		} catch (NoSuchUserException e) {
			LOGGER.error("No Such User Exception");
			throw new WebApplicationException("No Such User Exception",e, Status.NOT_FOUND);
		} catch (NoRoleException e) {
			LOGGER.error("No Such Role Exception");
//			throw new WebApplicationException(Response.status(404).build());
			throw new WebApplicationException("No Such Role Exception",e, Status.NOT_FOUND);
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
	public Response updateListFriend(@PathParam("userUUID") UUID userIDFromPath) {
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
	 * @param relationUUID
	 * @return
	 */
	@DELETE
	@Path("{relationUUID}")
	@RolesAllowed("other")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response deleteFriend(@PathParam("userUUID") UUID userIDFromPath,
			@PathParam("relationUUID") UUID relationUUID) {
		try {
			rManager.deleteRelation(userIDFromPath, relationUUID);
		} catch (NoSuchUserException e) {
			LOGGER.info("no user {} or no contact {} found", userIDFromPath,
					relationUUID);
			return Response.status(404).build();
		} catch (NoSuchBoxException e) {
			LOGGER.info("no box found for your contact {}", relationUUID);
			return Response.status(404).build();
		} catch (NoRelationException e) {
			LOGGER.info("no relation found between{} {}", userIDFromPath,
					relationUUID);
			return Response.status(404).build();
		}
		return Response.status(200).build();
	}
}
