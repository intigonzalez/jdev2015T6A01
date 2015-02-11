package com.enseirb.telecom.dngroup.dvd2c.endpoints;

import java.net.URI;
import java.net.URISyntaxException;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.enseirb.telecom.dngroup.dvd2c.db.UserRepositoryMongo;
import com.enseirb.telecom.dngroup.dvd2c.service.AccountServiceCentral;
import com.enseirb.telecom.dngroup.dvd2c.service.AccountServiceCentralImpl;
import com.enseirb.telecom.dngroup.dvd2c.model.Box;
import com.enseirb.telecom.dngroup.dvd2c.model.ListUser;
import com.enseirb.telecom.dngroup.dvd2c.model.User;

// The Java class will be hosted at the URI path "/app/account"
@Path("app/account")
public class UserEndPoints {

	AccountServiceCentral uManager = new AccountServiceCentralImpl(new UserRepositoryMongo("CentralMediaHome"));

	@GET
	@Path("{userID}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public User getIt(@PathParam("userID") String userID) {
		return uManager.getUser(userID);
	}

	/**
	 * Find a list of users from their name
	 * 
	 * @param name
	 * @return a list of user
	 */
	@GET
	@Path("name/{name}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public ListUser getUserFromName(@PathParam("name") String name) {

		return uManager.getUserFromName(name);

	}

	@GET
	@Path("boxIX/{boxID}")
	public ListUser getUserFromBoxID(@PathParam("boxID") String boxID) {

		return uManager.getUserFromBoxID(boxID);

	}

	@GET
	@Path("{userID}/box")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Box getBoxIt(@PathParam("userID") String userID) {
		return uManager.getBox(userID);
	}

	@POST
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response postUser(User user) throws URISyntaxException {
		if (uManager.userExist(user.getUserID()) == false) {
			uManager.createUser(user);
			// NHE that the answer we expect from a post (see location header)
			return Response.created(new URI(user.getUserID())).build();
		} else {
			return Response.status(409).build();
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
	public Response putUser(User user) {
		// TODO: need to check the authentication of the user

		// modify the user
		if (uManager.userExist(user.getUserID())) {
			uManager.saveUser(user);
			return Response.status(200).build();
		} else {
			return Response.status(409).build();
		}

	}

	@DELETE
	@Path("{userID}")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response deleteUser(@PathParam("userID") String userID) {
		// TODO: need to check the authentication of the user

		// delete the user
		uManager.deleteUser(userID);
		return Response.status(200).build();

	}

}
