package com.enseirb.telecom.dngroup.dvd2c.endpoints;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NoContentException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import com.enseirb.telecom.dngroup.dvd2c.CliConfSingleton;
import com.enseirb.telecom.dngroup.dvd2c.exception.NoRelationException;
import com.enseirb.telecom.dngroup.dvd2c.exception.NoSuchBoxException;
import com.enseirb.telecom.dngroup.dvd2c.exception.NoSuchContactException;
import com.enseirb.telecom.dngroup.dvd2c.exception.NoSuchUserException;
import com.enseirb.telecom.dngroup.dvd2c.exception.SuchBoxException;
import com.enseirb.telecom.dngroup.dvd2c.model.Box;
import com.enseirb.telecom.dngroup.dvd2c.model.Contact;
import com.enseirb.telecom.dngroup.dvd2c.model.Content;
import com.enseirb.telecom.dngroup.dvd2c.model.Property;
import com.enseirb.telecom.dngroup.dvd2c.model.PropertyGroups;
import com.enseirb.telecom.dngroup.dvd2c.modeldb.User;
import com.enseirb.telecom.dngroup.dvd2c.modeldb.ActivityObjectExtand;
import com.enseirb.telecom.dngroup.dvd2c.service.AccountService;
import com.enseirb.telecom.dngroup.dvd2c.service.BoxService;
import com.enseirb.telecom.dngroup.dvd2c.service.ContentService;
import com.enseirb.telecom.dngroup.dvd2c.service.RelationService;

@Path("box")
public class BoxEndPoints {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(BoxEndPoints.class);
	@Autowired
	protected BoxService boxManager;

	@Autowired
	protected RelationService rManager;

	@Autowired
	protected ContentService cManager;


	/**
	 * get box with boxID
	 * 
	 * @param boxId
	 *            the box to get
	 * @return
	 */
	@GET
	@Path("id/{boxId}")
	@Produces(MediaType.APPLICATION_XML)
	public Box getBox(@PathParam("boxId") String boxId) {
		// try {
		throw new WebApplicationException(Status.NOT_IMPLEMENTED);
		// return boxManager.getBoxOnServer(boxId);
		// } catch (NoSuchBoxException e) {
		//
		// throw new WebApplicationException(Status.NOT_FOUND);
		// }
	}

	/**
	 * if box don't exist create this (local and on the central serveur)
	 * 
	 * @return
	 * @throws URISyntaxException
	 * @throws SuchBoxException
	 */
	@POST
	// @Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response postBox() throws SuchBoxException, URISyntaxException {

		Box box = new Box();
		box.setBoxID(CliConfSingleton.boxID);
		box.setIp(CliConfSingleton.publicAddr);
		try {
			boxManager.createBoxOnServer(box);
			return Response.created(new URI(box.getBoxID())).build();
		} catch (IOException e) {
			return Response.status(Status.NOT_FOUND).build();
		}

	}

	@PUT
	@Path("{boxId}")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response putBox(Box box) throws URISyntaxException {
		try {
			boxManager.saveBoxOnServer(box);
		} catch (NoSuchBoxException e) {
			return Response.status(409).build();
		}
		return Response.created(new URI(box.getBoxID())).build();
	}

	@PUT
	@Path("relation/{userUUID}/{relationId}")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response setAprouve(@PathParam("userUUID") UUID userId,
			@PathParam("relationId") UUID relationId) throws URISyntaxException {

		LOGGER.debug("try to setAprouve a relation from a other box {} and {}",
				userId, relationId);
		try {
			rManager.getContact(userId, relationId);

			rManager.setAprouveBox(userId, relationId);
			return Response.status(Status.ACCEPTED).build();
		} catch (NoSuchContactException e) {
			return Response.status(Status.NOT_FOUND).build();
		}
	}

	/**
	 * delete box
	 * 
	 * @param boxID
	 * @return
	 */
	@DELETE
	@Path("{boxId}")
	public Response deleteBox(@PathParam("boxId") String boxID) {
		boxManager.deleteBoxOnServer(boxID);
		return Response.accepted().build();
	}

	/**
	 * delete a relation on this box and in the over box
	 * 
	 * @param userIDFromPath
	 * @param relationIDFromPath
	 * @return
	 */
	@DELETE
	@Path("relation/{userId}/{relationId}")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response deleteFriend(@PathParam("userId") UUID userId,
			@PathParam("relationId") UUID relationId) {
		LOGGER.debug("try to delete a relation from a other box {} and {}",
				userId, relationId);

		try {
			rManager.deleteRelationBox(userId, relationId);
			return Response.status(200).build();
		} catch (NoRelationException e) {
			return Response.status(404).build();
		}

	}

	/**
	 * This endpoint is used by a box, to get the content of one of its
	 * relations.
	 * 
	 * @param contactUUID
	 *            : remote user (the relation)
	 * @param actorUUID
	 *            : local user (the one who stores content)
	 * @return
	 * @throws NoContentException
	 */
	@GET
	@Path("{actorUUID}/content/{contactUUID}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<Content> getLocalContentForRelation(
			@PathParam("contactUUID") UUID contactUUID,
			@PathParam("actorUUID") UUID actorUUID) throws NoContentException {

		LOGGER.debug("Receive Request to get list content from {}", contactUUID);
		// try {

		// Contact relation;
		// relation = rManager.getRelation(actorUUID, contactUUID);

		LOGGER.debug("Check contact {} and get content of {}", contactUUID,
				actorUUID);
		List<Content> contents = new ArrayList<Content>();
		List<ActivityObjectExtand> a = rManager.getActivityForContact(
				actorUUID, contactUUID);
		for (ActivityObjectExtand activityObjectExtand : a) {
			try {
				Content content = cManager.getContent(activityObjectExtand.getId());
				content.setLink(CliConfSingleton.publicAddr + content.getLink());
				contents.add(content);
			} catch (NoContentException e) {
				LOGGER.error("some content is null "
						+ "Deleting {}",activityObjectExtand.getId());
				
				rManager.deleteActivityObject(activityObjectExtand.getId());
			}
			
			
		}

		// //RBAC:need to fix it after merge
		// List<Content> listContent = uManager
		// .getAllContent(userID, relation);
		// return listContent;
		return contents;
		// } catch (NoSuchContactException e) {
		// throw new WebApplicationException(Status.NOT_ACCEPTABLE);
		// }

	}

	/**
	 * add relation on database of userID from this relation
	 * 
	 * @param uuid
	 * @param relation
	 * @return
	 * @throws URISyntaxException
	 */
	@POST
	@Path("{actorUUID}/relationfrombox")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response postFriendFromBox(@PathParam("actorUUID") UUID uuid,
			Contact relation) throws URISyntaxException {
		// String uuid = SecurityContextHolder.getContext().getAuthentication()
		// .getName();
		LOGGER.debug("add a relation from box between {} and {} ", uuid,
				relation.getActorID());

		if (rManager
				.RelationExist(uuid, UUID.fromString(relation.getActorID())) == false) {
			try {
				rManager.createRelation(uuid,
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
	

}
