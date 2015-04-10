package com.enseirb.telecom.dngroup.dvd2c.endpoints;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

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
import com.enseirb.telecom.dngroup.dvd2c.model.Box;
import com.enseirb.telecom.dngroup.dvd2c.model.User;
import com.enseirb.telecom.dngroup.dvd2c.service.AccountService;

// The Java class will be hosted at the URI path "/app/account"
@Path("app/account")
public class UserEndPoints {

	@Inject
	AccountService uManager;

	@GET
	@Path("{userID}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public User getUser(@PathParam("userID") String userID) {
		try {
			return uManager.getUserOnLocal(userID);
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
	@Path("firstname/{firstname}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<User> getUserFromName(@PathParam("firstname") String firstname) {

		List<User> users = uManager.getUserFromName(firstname);

		return users;
	}

	@GET
	@Path("boxIX/{boxID}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<User> getUserFromBoxID(@PathParam("boxID") String boxID) {

		return uManager.getUserFromBoxID(boxID);

	}

	@GET
	@Path("{userID}/box")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Box getBoxOfUser(@PathParam("userID") String userID) {
		try {
			return uManager.getBox(userID);
		} catch (NoSuchUserException e) {
			throw new WebApplicationException(Status.NOT_FOUND);
		}
	}

	@POST
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response creatUser(User user) throws URISyntaxException {
		if (uManager.userExistOnLocal(user.getUserID()) == false) {
			uManager.createUserOnLocal(user);
			// NHE that the answer we expect from a post (see location header)
			return Response.created(new URI(user.getUserID())).build();
		} else {
			return Response.status(javax.ws.rs.core.Response.Status.CONFLICT)
					.build();
		}

	}

	/**
	 * Update a user's profile
	 * 
	 * @param user
	 * @return
	 */
	@PUT
	@Path("{userID}")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response updateUser(User user,@PathParam("userID") String userID) {
		// TODO: need to check the authentication of the user
		if (!user.getUserID().equals(userID)){
			return Response.status(Status.NOT_ACCEPTABLE).build();
		}
		// modify the user
		if (uManager.userExistOnLocal(user.getUserID())) {
			uManager.saveUserOnLocal(user);
			return Response.status(200).build();
		} else {
			return Response.status(Status.NOT_FOUND).build();
		}

	}

	@DELETE
	@Path("{userID}")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response deleteUser(@PathParam("userID") String userID) {

		if (uManager.userExistOnLocal(userID)) {
			uManager.deleteUserOnLocal(userID);
			return Response.status(200).build();
		} else {
			return Response.status(Status.NOT_FOUND).build();
		}

	}

}
