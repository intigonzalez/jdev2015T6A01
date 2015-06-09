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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.enseirb.telecom.dngroup.dvd2c.exception.NoSuchUserException;
import com.enseirb.telecom.dngroup.dvd2c.exception.SuchUserException;
import com.enseirb.telecom.dngroup.dvd2c.model.User;
import com.enseirb.telecom.dngroup.dvd2c.service.AccountService;
import com.enseirb.telecom.dngroup.dvd2c.service.RelationService;

// The Java class will be hosted at the URI path "/app/account"

@Component
@Path("app")
public class UserEndPoints extends HttpServlet {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(UserEndPoints.class);

	@Inject
	protected AccountService uManager;

	@Inject
	protected RelationService rManager;

	@Path("/account/user")
	@GET()
	public Response getverification() {
		String uuid = SecurityContextHolder.getContext().getAuthentication().getName();

		return Response
				.ok("authenticated successfully!")
				.cookie(new NewCookie("authentication", uuid, "/", null, 1,
						"no comment", 1073741823, false)).build();
	}

	/**
	 * Create the cookie on user side when the user is connected
	 * 
	 * @param userAgent
	 * @param username
	 * @param password
	 * @return
	 */
	@POST()
	@Path("/account/Connect")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	// resultat en JSON
	public Response getConnect(User user) { // FormParam ce sont les parametres
											// d'un formulaire.
		String userID = user.getUserID().toLowerCase();

		com.enseirb.telecom.dngroup.dvd2c.modeldb.User userAuth;
		try {
			userAuth = uManager.findUserByEmail(userID);
		} catch (NoSuchUserException e) {
			throw new WebApplicationException(Status.NOT_FOUND);
		}
		if (user.getPassword().equals(userAuth.getEncryptedPassword())) {
			// maxAge max int value/2
			return Response
					.ok()
					.cookie(new NewCookie("authentication", userAuth.getId()
							.toString(), "/", null, 1, "no comment",
							1073741823, false)).build();
		}
		return Response.status(403).build();

	}

	/**
	 * Get a current user
	 * 
	 * @return a user
	 */
	@GET
	@Path("/account")
	// @PreAuthorize("hasAnyRole('USER','User')")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public User getUser() {
		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		String uuid = auth.getName();
		try {
			return getUserByUUID(UUID.fromString(uuid));
		} catch (java.lang.IllegalArgumentException e) {
			LOGGER.error("No Email !!!!", uuid);
			return getUserByEmail(uuid);
		}

	}

	/**
	 * Get a user by userID
	 * 
	 * @param userUUID
	 *            the user to get
	 * @return a user
	 */
	@GET
	@Path("/account/{userUUID}")
	// @PreAuthorize("hasRole('USER')")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public User getUserByUUID(@PathParam("userUUID") UUID userUUID) {
		try {
			return uManager.findUserByUUID(userUUID).toXSDUser();
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
	@Path("/account/email/{email}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public User getUserByEmail(@PathParam("email") String email) {
		try {
			return uManager.findUserByEmail(email).toXSDUser();
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
	@Path("/account/firstname/{firstname}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<User> getUserByName(@PathParam("firstname") String firstname) {
		return uManager.findUserByNameOnServer(firstname);
	}

	/**
	 * Create a user on server and local db
	 * 
	 * @param user
	 *            the user to save
	 * @return Status web
	 * @throws URISyntaxException
	 */
	@Path("/account")
	@POST
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response createUser(User user) throws URISyntaxException {

		User u;
		try {
			u = uManager.createUserOnServer(user).toXSDUser();
			rManager.creatDefaultUserRoles(UUID.fromString(u.getUuid()));
			// maxAge max int value/2
			return Response
					.created(new URI("app/account/" + u.getUuid()))
					.cookie(new NewCookie("authentication", u.getUuid(), "/",
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
	 * Create a user on server and local db
	 * 
	 * @param user
	 *            the user to save
	 * @return Status web
	 * @throws URISyntaxException
	 */
	@Path("/register")
	@POST
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response createUser2(User user) throws URISyntaxException {
		return createUser(user);
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
	@Path("/account/{userID}")
	@RolesAllowed("account")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response updateUser(User user,
			@PathParam("userID") UUID userIDFromPath) {
		// TODO: need to check the authentication of the user
		// modify the user, check if the user has changed his email address, and
		// check the ability of the new email address
		if (user.getUuid().equals(userIDFromPath.toString())
				&& uManager.userExistOnLocal(UUID.fromString(user.getUuid())) == true) {
			try {
				uManager.saveUserOnServer(user);
				return Response.status(200).build();
			} catch (NoSuchUserException e) {
				return Response.status(Status.NOT_FOUND).build();
			}
		} else {
			return Response.status(Status.NOT_FOUND).build();
		}

	}

	/**
	 * Update the current User
	 * 
	 * @param user
	 *            the user information to update
	 * @return Response
	 */
	@PUT
	@Path("/account")
	@RolesAllowed("account")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response updateUser(User user) {
		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		String uuid = auth.getName();
		return updateUser(user, UUID.fromString(uuid));
	}

	/**
	 * Delete the user with userID
	 * 
	 * @param userID
	 *            the userID to delete
	 * @return web status
	 */
	@DELETE
	@Path("/account/{userID}")
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
