package com.enseirb.telecom.dngroup.dvd2c.endpoints;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;

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

import com.enseirb.telecom.dngroup.dvd2c.exception.NoSuchUserException;
import com.enseirb.telecom.dngroup.dvd2c.exception.SuchUserException;
import com.enseirb.telecom.dngroup.dvd2c.model.Box;
import com.enseirb.telecom.dngroup.dvd2c.model.User;
import com.enseirb.telecom.dngroup.dvd2c.service.CentralService;

// The Java class will be hosted at the URI path "/app/account"
@Path("app")
public class UserEndPoints {

	@Inject
	CentralService uManager;

	@GET
	@Path("account/{userID}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public User getUser(@PathParam("userID") UUID userID) {
		try {
			return uManager.getUser(userID);
		} catch (NoSuchUserException e) {

			throw new WebApplicationException(Status.NOT_FOUND);
		}
	}

	/**
	 * Find a list of users from their firstname
	 * 
	 * @param firstname
	 * @return a list of user
	 */
	@GET
	@Path("account/firstname/{firstname}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<User> getUserFromName(@PathParam("firstname") String firstname) {

		List<User> users = uManager.getUserFromName(firstname);

		return users;
	}

	@GET
	@Path("account/boxID/{boxID}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<User> getUserFromBoxID(@PathParam("boxID") String boxID) {

		return uManager.getUserFromBoxID(boxID);

	}

	@GET
	@Path("account/{userID}/box")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Box getBoxOfUser(@PathParam("userID") UUID userID) {
		try {
			return uManager.getBox(userID);
		} catch (NoSuchUserException e) {
			throw new WebApplicationException(Status.NOT_FOUND);
		}
	}

	@POST
	@Path("account/")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response creatUser(User user) throws URISyntaxException {
		try {
			uManager.createUserOnLocal(user);
			return Response.created(new URI("app/account/" + user.getUuid()))
					.build();
		} catch (SuchUserException e) {
			throw new WebApplicationException(Status.CONFLICT);
		}
	}

	/**
	 * Update a user's profile
	 * 
	 * @param user
	 * @return
	 */
	@PUT
	@Path("account/{userID}")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response updateUser(User user, @PathParam("userID") UUID userID) {
		// TODO: need to check the authentication of the user
		if (!user.getUuid().equals(userID.toString())) {
			return Response.status(Status.NOT_ACCEPTABLE).build();
		}
		// modify the user
		if (uManager.userExistOnLocal(UUID.fromString(user.getUuid()))) {
			uManager.saveUserOnLocal(user);
			return Response.status(200).build();
		} else {
			return Response.status(Status.NOT_FOUND).build();
		}

	}

	@DELETE
	@Path("account/{userID}")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response deleteUser(@PathParam("userID") UUID userID) {

		if (uManager.userExistOnLocal(userID)) {
			uManager.deleteUserOnLocal(userID);
			return Response.status(200).build();
		} else {
			return Response.status(Status.NOT_FOUND).build();
		}

	}

}
