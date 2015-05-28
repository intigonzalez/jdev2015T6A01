package com.enseirb.telecom.dngroup.dvd2c.endpoints;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;

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

import com.enseirb.telecom.dngroup.dvd2c.CliConfSingleton;
import com.enseirb.telecom.dngroup.dvd2c.exception.NoRelationException;
import com.enseirb.telecom.dngroup.dvd2c.exception.NoSuchBoxException;
import com.enseirb.telecom.dngroup.dvd2c.exception.NoSuchContactException;
import com.enseirb.telecom.dngroup.dvd2c.exception.SuchBoxException;
import com.enseirb.telecom.dngroup.dvd2c.model.Box;
import com.enseirb.telecom.dngroup.dvd2c.model.ContactXSD;
import com.enseirb.telecom.dngroup.dvd2c.model.Content;
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
	protected ContentService uManager;

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
			rManager.getRelation(userId, relationId);

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
		} catch (NoRelationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Response.status(200).build();
	}

	/**
	 * This endpoint is used by a box, to get the content of one of its
	 * relations.
	 * 
	 * @param relationID
	 *            : remote user (the relation)
	 * @param userID
	 *            : local user (the one who stores content)
	 * @return
	 */
	@GET
	@Path("{userID}/content/{relationID}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<Content> getLocalContentForRelation(
			@PathParam("relationID") UUID relationID,
			@PathParam("userID") UUID userID) {

		LOGGER.debug("Receive Request to get list content from {}", relationID);
		try {

			ContactXSD relation;

			relation = rManager.getRelation(userID, relationID);

			LOGGER.debug("Check the relation : {}", relation);
			// //RBAC:need to fix it after merge
			// List<Content> listContent = uManager
			// .getAllContent(userID, relation);
			// return listContent;
			return null;
		} catch (NoSuchContactException e) {
			throw new WebApplicationException(Status.NOT_ACCEPTABLE);
		}

	}

}
