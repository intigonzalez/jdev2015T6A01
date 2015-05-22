package com.enseirb.telecom.dngroup.dvd2c.endpoints;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.servlet.http.HttpServlet;
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
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enseirb.telecom.dngroup.dvd2c.exception.NoSuchUserException;
import com.enseirb.telecom.dngroup.dvd2c.exception.SuchUserException;
import com.enseirb.telecom.dngroup.dvd2c.model.User;
import com.enseirb.telecom.dngroup.dvd2c.service.AccountService;

// The Java class will be hosted at the URI path "/app/account"

@Path("app/account")
public class UserEndPoints extends HttpServlet {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(UserEndPoints.class);

	@Inject
	protected AccountService uManager;

	// // Only for tests
	// @GET
	// @Path("get")
	// //@RolesAllowed("account")
	// public Response addUser(@Context HttpHeaders headers,@Context
	// SecurityContext context) {//@HeaderParam("cookie") String userAgent) {
	//
	// String userAgent = headers.getRequestHeader("cookie").get(0);
	// LOGGER.debug("userAgent : {}",userAgent);
	// return Response.status(200)
	// .entity("addUser is called, userAgent : " + userAgent)
	// .build();
	//
	// }
	//
	/**
	 * Create the cookie on user side when the user is connected
	 * 
	 * @param userAgent
	 * @param username
	 * @param password
	 * @return
	 */
	@POST()
	@Path("Connect")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	// resultat en JSON
	public Response getConnect(User user) { // FormParam ce sont les parametres
											// d'un formulaire.
		String userID = user.getUserID().toLowerCase();

		User userAuth;
		try {
			userAuth = uManager.getUserFromEmail(userID);
		} catch (NoSuchUserException e) {
			throw new WebApplicationException(Status.NOT_FOUND);
		}
		if (user.getPassword().equals(userAuth.getPassword())) {
			// maxAge max int value/2
			return Response
					.ok()
					.cookie(new NewCookie("authentication", userID, "/", null,
							1, "no comment", 1073741823, false)).build();
		}
		return Response.status(403).build();

	}

	/**
	 * Get a user by userID
	 * 
	 * @param userUUID
	 *            the user to get
	 * @return a user
	 */
	@GET
	@Path("{userUUID}")
	@RolesAllowed({ "account", "authenticated" })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public User getUserByUUID(@PathParam("userUUID") UUID userUUID) {
		try {
			return uManager.getUserFromUUID(userUUID);
		} catch (NoSuchUserException e) {
			throw new WebApplicationException(Status.NOT_FOUND);
		}
	}

	/**
	 * Find a list of users from their firstname on server
	 * 
	 * @param firstname
	 *            the firstname to search
	 * @return a list of user
	 */
	@GET
	@Path("email/{email}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public User getUserByEmail(@PathParam("email") String email) {
		try {
			return uManager.getUserFromEmail(email);
		} catch (NoSuchUserException e) {
			throw new WebApplicationException(Status.NOT_FOUND);
		}
	}

	/**
	 * Find a list of users from their firstname on server
	 * 
	 * @param firstname
	 *            the firstname to search
	 * @return a list of user
	 */
	@GET
	@Path("firstname/{firstname}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<User> getUserByName(@PathParam("firstname") String firstname) {
		return uManager.getUserFromNameOnServer(firstname);
	}

	/**
	 * Create a user on server and local db
	 * 
	 * @param user
	 *            the user to save
	 * @return Status web
	 * @throws URISyntaxException
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response createUser(User user) throws URISyntaxException {

		User u;
		try {
			u = uManager.createUserOnServer(user);
			// maxAge max int value/2
			return Response
					.created(new URI("app/account/" + u.getUuid()))
					.cookie(new NewCookie("authentication", u.getUserID(), "/",
							null, 1, "no comment", 1073741823, false)).build();
			// return Response.created(new URI(u.getUserID())).build();
		} catch (SuchUserException e) {
			throw new WebApplicationException("user" + user.getUserID()
					+ "already exists on central", Status.CONFLICT);
		} catch (IOException e) {
			throw new WebApplicationException("error for creating user",
					Status.INTERNAL_SERVER_ERROR);
		}

	}

	/**
	 * Update User by userID
	 * 
	 * @param user
	 *            the user information
	 * @param userIDFromPath
	 *            the userID to update
	 * @return Response
	 */
	@PUT
	@Path("{userID}")
	@RolesAllowed("account")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response updateUser(User user,
			@PathParam("userID") UUID userIDFromPath) {
		// TODO: need to check the authentication of the user
		// modify the user, check if the user has changed his email address, and
		// check the ability of the new email address
		if (user.getUserID().equals(userIDFromPath)
				&& uManager.userExistOnLocal(UUID.fromString(user.getUuid())) == true) {
			uManager.saveUserOnServer(user);
			return Response.status(200).build();
		} else {
			return Response.status(Status.NOT_FOUND).build();
		}

	}

	/**
	 * Delete the user with userID
	 * 
	 * @param userID
	 *            the userID to delete
	 * @return web status
	 */
	@DELETE
	@Path("{userID}")
	@RolesAllowed("account")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response deleteUser(@PathParam("userID") UUID userID) {
		// TODO: need to check the authentication of the user

		// delete the user

		try {
			uManager.deleteUserOnServer(userID);
			return Response.status(200).build();
		} catch (IOException e) {
			LOGGER.error("Error for delete this user on server : {}", userID, e);
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		} catch (NoSuchUserException e) {
			LOGGER.error("Error for delete this user (no found user) : {}",
					userID, e);
			return Response.status(Status.NOT_FOUND).build();
		}

	}
}
