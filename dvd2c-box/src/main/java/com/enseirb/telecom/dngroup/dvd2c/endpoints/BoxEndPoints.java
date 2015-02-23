package com.enseirb.telecom.dngroup.dvd2c.endpoints;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

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

import com.enseirb.telecom.dngroup.dvd2c.ApplicationContext;
import com.enseirb.telecom.dngroup.dvd2c.db.BoxRepositoryMongo;
import com.enseirb.telecom.dngroup.dvd2c.db.ContentRepositoryMongo;
import com.enseirb.telecom.dngroup.dvd2c.db.RelationshipRepositoryMongo;
import com.enseirb.telecom.dngroup.dvd2c.db.UserRepositoryMongo;
import com.enseirb.telecom.dngroup.dvd2c.exception.NoSuchBoxException;
import com.enseirb.telecom.dngroup.dvd2c.model.Box;
import com.enseirb.telecom.dngroup.dvd2c.model.Content;
import com.enseirb.telecom.dngroup.dvd2c.model.Relation;
import com.enseirb.telecom.dngroup.dvd2c.service.BoxService;
import com.enseirb.telecom.dngroup.dvd2c.service.BoxServiceImpl;
import com.enseirb.telecom.dngroup.dvd2c.service.ContentService;
import com.enseirb.telecom.dngroup.dvd2c.service.ContentServiceImpl;
import com.enseirb.telecom.dngroup.dvd2c.service.RabbitMQServer;
import com.enseirb.telecom.dngroup.dvd2c.service.RelationService;
import com.enseirb.telecom.dngroup.dvd2c.service.RelationServiceImpl;

@Path("box")
public class BoxEndPoints {
	private static final Logger LOGGER = LoggerFactory.getLogger(BoxEndPoints.class);
	BoxService boxManager = new BoxServiceImpl(new BoxRepositoryMongo("mediahome"));
	UserRepositoryMongo uRM = new UserRepositoryMongo("mediahome");

	/**
	 *  get box with boxID
	 * @param boxId the box to get
	 * @return
	 */
	@GET
	@Path("id/{boxId}")
	@Produces(MediaType.APPLICATION_XML)
	public Box getBox(@PathParam("boxId") String boxId) {
		try {
			return boxManager.getBoxOnLocal(boxId);
		} catch (NoSuchBoxException e) {
			
			throw new WebApplicationException(Status.NOT_FOUND);
		}
	}

	/**
	 * if box don't exist create this (local and on the central serveur)
	 * 
	 * @return
	 * @throws URISyntaxException
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response postBox() throws URISyntaxException {
		// add a comment
		Box box = new Box();
		box.setBoxID(ApplicationContext.getProperties().getProperty("BoxID"));
		box.setIp(ApplicationContext.getProperties().getProperty("PublicAddr"));
		if (boxManager.boxExistOnServer(box.getBoxID()) == false) {
			boxManager.createBoxOnServer(box);
			return Response.created(new URI(box.getBoxID())).build();
		} 
		else {
			return Response.status(409).build();
		}

		// return Response.status(Status.SERVICE_UNAVAILABLE).build();
	}

	// @POST
	// @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	// public Response postBox(Box box) throws URISyntaxException {
	// // add a comment
	//
	// if (boxManager.boxExist(box) == false) {
	// boxManager.createBox(box);
	// return Response.created(new URI(box.getBoxID())).build();
	// } else {
	// return Response.status(409).build();
	// }
	//
	// //return Response.status(Status.SERVICE_UNAVAILABLE).build();
	// }

	@PUT
	@Path("{boxId}")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response putBox(Box box) throws URISyntaxException {
		// need to verify user
		// and after this modifies the comment

		if (boxManager.boxExistOnLocal(box.getBoxID()) == true) {
			boxManager.saveBoxOnLocal(box);
			// NHE that the answer we expect from a post (see location header)
			return Response.created(new URI(box.getBoxID())).build();
		} else {
			return Response.status(409).build();
		}

		// return Response.status(Status.SERVICE_UNAVAILABLE).build();

	}

	@PUT
	@Path("relation/{userId}/{relationId}")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response setAprouve(@PathParam("userId") String userId, @PathParam("relationId") String relationId) throws URISyntaxException {
		RelationService rManager = new RelationServiceImpl(new RelationshipRepositoryMongo(), uRM);
		LOGGER.debug("try to setAprouve a relation from a other box {} and {}", userId, relationId);
		rManager.setAprouveBox(userId, relationId);
		return Response.status(Status.ACCEPTED).build();
	}

	/** delete box
	 * 
	 * @param boxID
	 * @return
	 */
	@DELETE
	@Path("{boxId}")
	public Response deleteBox(@PathParam("boxId") String boxID) {
		// need to verify user
		// and after this delete the comment

		// if (boxManager.boxExist(box) == true) {
		boxManager.deleteBoxOnServer(boxID);
		// NHE that the answer we expect from a post (see location header)
		return Response.accepted().build();
		// } else {
		// return Response.status(409).build();
		// }
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
	public Response deleteFriend(@PathParam("userId") String userId, @PathParam("relationId") String relationId) {
		LOGGER.debug("try to delete a relation from a other box {} and {}", userId, relationId);
		RelationService rManager = new RelationServiceImpl(new RelationshipRepositoryMongo(), uRM);
		rManager.deleteRelationBox(userId, relationId);
		return Response.status(200).build();
	}

	/**
	 * This endpoint is used by a box, to get the content of one of its relations.
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
	public List<Content> getLocalContentForRelation(@PathParam("relationID") String relationID, @PathParam("userID") String userID) {
		RelationServiceImpl relationService = new RelationServiceImpl(new RelationshipRepositoryMongo(), uRM);
		ContentService uManager = new ContentServiceImpl(new ContentRepositoryMongo(), new RabbitMQServer());

		LOGGER.debug("Receive Request to get list content from {}", relationID);
		if (relationService.RelationExist(userID, relationID)) {
			Relation relation = relationService.getRelation(userID, relationID);

			LOGGER.debug("Check the relation : {}", relation);
			List<Content> listContent = uManager.getAllContent(userID, relation);
			return listContent;
		}
		return null;
	}

}
