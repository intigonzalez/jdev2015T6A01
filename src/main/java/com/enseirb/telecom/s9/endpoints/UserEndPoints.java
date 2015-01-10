package com.enseirb.telecom.s9.endpoints;

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

import com.enseirb.telecom.s9.ListUser;
import com.enseirb.telecom.s9.User;
import com.enseirb.telecom.s9.db.UserRepositoryMongo;
import com.enseirb.telecom.s9.service.AccountService;
import com.enseirb.telecom.s9.service.AccountServiceImpl;

// The Java class will be hosted at the URI path "/app/account"
@Path("app/account")
public class UserEndPoints {

	AccountService uManager = new AccountServiceImpl(new UserRepositoryMongo());

	// TODO: update the class to suit your needs

	// The Java method will process HTTP GET requests
	// The Java method will produce content identified by the MIME Media
	// type "text/plain"
	@GET
	@Path("{userID}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public User getIt(@PathParam("userID") String userID) {
		return uManager.getUser(userID);
	}
	
	/**
	 * Find a list of users from their name on server
	 * @param name
	 * @return a list of user
	 */
	@GET
	@Path("name/{name}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public ListUser getUserFromName(@PathParam("name") String name){
		
		return uManager.getUserFromNameOnServer(name);
		
	}

	@POST
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response postUser(User user) throws URISyntaxException {
		if (uManager.userExist(user) == false) {
			User u=uManager.createUser(user);
			// NHE that the answer we expect from a post (see location header)
			return Response.created(new URI(u.getUserID())).build();
		} else {
			return Response.status(409).build();
		}
	}

	@PUT
	@Path("{userID}")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response putUser(User user, @PathParam("userID") String userIDFromPath) {
		// TODO: need to check the authentication of the user
		// modify the user, check if the user has changed his email address, and check the ability of the new email address
		if ( user.getUserID().equals(userIDFromPath ) || uManager.userExist(user) == false ) {
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
