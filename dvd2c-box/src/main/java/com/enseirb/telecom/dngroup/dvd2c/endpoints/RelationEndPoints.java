package com.enseirb.telecom.dngroup.dvd2c.endpoints;

import java.io.IOException;
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
import org.springframework.security.core.context.SecurityContextHolder;

import com.enseirb.telecom.dngroup.dvd2c.exception.NoRelationException;
import com.enseirb.telecom.dngroup.dvd2c.exception.NoRoleException;
import com.enseirb.telecom.dngroup.dvd2c.exception.NoSuchBoxException;
import com.enseirb.telecom.dngroup.dvd2c.exception.NoSuchContactException;
import com.enseirb.telecom.dngroup.dvd2c.exception.NoSuchUserException;
import com.enseirb.telecom.dngroup.dvd2c.model.Contact;
import com.enseirb.telecom.dngroup.dvd2c.model.Content;
import com.enseirb.telecom.dngroup.dvd2c.model.User;
import com.enseirb.telecom.dngroup.dvd2c.modeldb.ContactDB;
import com.enseirb.telecom.dngroup.dvd2c.service.AccountService;
import com.enseirb.telecom.dngroup.dvd2c.service.RelationService;

//import com.enseirb.telecom.s9.Relation;

// The Java class will be hosted at the URI path "/app/friends"
@Path("app/relation")
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
	 * @param uuid
	 *            the userID to get information
	 * @param relationUUID
	 *            the userID of request
	 * @return a user with information
	 */
	@GET
	@Path("from/{relationUUID}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public User getMeFRH(@PathParam("relationUUID") UUID relationUUID) {
		String uuid = SecurityContextHolder.getContext().getAuthentication()
				.getName();
		try {
			if (rManager.RelationExist(UUID.fromString(uuid), relationUUID) == true) {

				return aService.getContactInformation(UUID.fromString(uuid))
						.toXSDUser();
			} else {
				throw new WebApplicationException(Status.NOT_FOUND);
			}
		} catch (NoSuchUserException e) {
			throw new WebApplicationException(Status.NOT_FOUND);
		}
	}

	/**
	 * Get relation information
	 * 
	 * @param uuid
	 *            the userID of the request
	 * @param relationUUID
	 *            the relation to get information
	 * @return
	 */
	@RolesAllowed("other")
	@GET
	@Path("{relationUUID}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Contact getRelation(@PathParam("relationUUID") UUID relationUUID) {
		String uuid = SecurityContextHolder.getContext().getAuthentication()
				.getName();
		try {
			return rManager.getContact(UUID.fromString(uuid), relationUUID)
					.toContact();
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
			@PathParam("relationUUID") UUID relationID) {
		String uuid = SecurityContextHolder.getContext().getAuthentication()
				.getName();
		// TODO: need to change beacause the content of one user doit etre
		// recupre directement aupré de la ressource utilisateur

//		Contact relation;
//		try {
//			relation = rManager.getContact(UUID.fromString(uuid), relationID)
//					.toContact();
//
//			if (relation.getAprouve() == 3)
				return rManager
						.getAllContent(UUID.fromString(uuid), relationID);
//			else {
//				throw new WebApplicationException(Status.FORBIDDEN);
//			}
//		} catch (NoSuchContactException e) {
//			throw new WebApplicationException(Status.NOT_FOUND);
//		}

	}

	/**
	 * Get the list of relation of the userID
	 * 
	 * @param uuid
	 *            The userID to get list of relation
	 * @return the list of relation
	 */
	@RolesAllowed("other")
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<Contact> getListRelation() {
		String uuid = SecurityContextHolder.getContext().getAuthentication()
				.getName();
		List<Contact> listContact = new ArrayList<Contact>();
		List<ContactDB> contactDBs = rManager.getListContact(UUID.fromString(uuid));
		for (ContactDB contactdb : contactDBs) {

			listContact.add(contactdb.toContact());
		}
		return listContact;

	}

	/**
	 * add relation on database of userID
	 * 
	 * @Deprecated
	 * @param uuid
	 *            the userID root
	 * @param relationID
	 *            the relation to add at userID
	 * @return WebStatus
	 * @throws URISyntaxException
	 */
	@Deprecated
	@POST
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response oldpostFriend(Contact relationID)
			throws URISyntaxException {

		LOGGER.warn("maybe this fonction don't work");
		return postFriend2(UUID.fromString(relationID.getUuid()));
		// return postFriend2(UUID.fromString(uuid),
		// UUID.fromString(relationID.getUuid()));
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
	public Response postFriend2(@PathParam("relationUUID") UUID relationUUID)
			throws URISyntaxException {
		String uuid = SecurityContextHolder.getContext().getAuthentication()
				.getName();
		LOGGER.debug("add a relation between {} and {} ", uuid, relationUUID);
		if (rManager.RelationExist(UUID.fromString(uuid), relationUUID) == false) {
			try {
				Contact r = rManager.createRelation(UUID.fromString(uuid),
						relationUUID, false);
				// RABC: FIXE it
				return Response.created(
						new URI("app/" + uuid + "/relation/" + r.getUuid()))
						.build();
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



	@PUT
	@RolesAllowed("other")
	@Path("{relationUUID}")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response putFriend(@PathParam("relationUUID") UUID relationUUID,
			Contact contact) {
		String uuid = SecurityContextHolder.getContext().getAuthentication()
				.getName();
		// need to verify the friend and after this modifies the friend
		try {
			// Contact c = rManager.getContact(UUID.fromString(uuid),
			// relationUUID).toContact();
			// contact.setUuid(c.getUuid());
			if (contact.getUuid().equals(relationUUID.toString())) {
				rManager.saveRelation(UUID.fromString(uuid), contact);
				return Response.status(200).build();
			} else {
				return Response.status(Status.NOT_ACCEPTABLE).build();
			}
		} catch (NoSuchContactException e1) {
			LOGGER.error("No Such Contact Exception");
			throw new WebApplicationException("No Such Contact Exception",
					Status.NOT_FOUND);
		} catch (NoRelationException e) {
			LOGGER.error("No Such Relation Exception");
			throw new WebApplicationException("No Such Relation Exception",
					Status.NOT_FOUND);
		} catch (NoSuchUserException e) {
			LOGGER.error("No Such User Exception");
			throw new WebApplicationException("No Such User Exception", e,
					Status.NOT_FOUND);
		} catch (NoRoleException e) {
			LOGGER.error("No Such Role Exception");
			// throw new WebApplicationException(Response.status(404).build());
			throw new WebApplicationException("No Such Role Exception", e,
					Status.NOT_FOUND);
		}

	}

	/**
	 * Update the list of friend of UserID Update information of each user
	 * 
	 * @param uuid
	 *            the userID to update relation
	 * @return webstatus
	 */
	@PUT
	@RolesAllowed("other")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response updateListFriend() {
		String uuid = SecurityContextHolder.getContext().getAuthentication()
				.getName();
		try {
			rManager.updateRelation(UUID.fromString(uuid));
			return Response.status(200).build();
		} catch (IOException e) {
			LOGGER.error("Updating fail can not connect", e);
			return Response.status(403).build();
		} catch (NoSuchUserException e) {
			LOGGER.error("Updating fail no user {}", uuid, e);
			return Response.status(403).build();
		} catch (Exception e) {
			LOGGER.error("Updating fail", e);
			return Response.status(403).build();
		}
	}

	/**
	 * delete a relation on this box and in the over box
	 * 
	 * @param uuid
	 * @param relationUUID
	 * @return
	 */
	@DELETE
	@Path("{relationUUID}")
	@RolesAllowed("other")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response deleteFriend(@PathParam("relationUUID") UUID relationUUID) {
		String uuid = SecurityContextHolder.getContext().getAuthentication()
				.getName();
		try {
			rManager.deleteRelation(UUID.fromString(uuid), relationUUID);
		} catch (NoSuchUserException e) {
			LOGGER.info("no user {} or no contact {} found", uuid, relationUUID);
			return Response.status(404).build();
		} catch (NoSuchBoxException e) {
			LOGGER.info("no box found for your contact {}", relationUUID);
			return Response.status(404).build();
		} catch (NoRelationException e) {
			LOGGER.info("no relation found between{} {}", uuid, relationUUID);
			return Response.status(404).build();
		}
		return Response.status(200).build();
	}
}
