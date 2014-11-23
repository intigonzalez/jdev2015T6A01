package com.enseirb.telecom.s9;

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

import com.enseirb.telecom.s9.db.UserRepositoryMongo;
import com.enseirb.telecom.s9.service.AccountService;
import com.enseirb.telecom.s9.service.AccountServiceImpl;
import com.sun.jersey.spi.resource.Singleton;

// The Java class will be hosted at the URI path "/app/account"
@Path("/app/account")
@Singleton //NHE: lifecycle for demo purpose, make only 1 endpoint per application
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

	@POST
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response postUser(User user) throws URISyntaxException {
		if (uManager.userExist(user.userID) == false) {
			uManager.saveUser(user);
			//NHE that the answer we expect from a post (see location header)
			return Response.created(new URI(user.userID)).build();
		} else {
			return Response.status(409).build();
		}

	}

	@PUT
	@Path("{email}")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response putUser(User user) {
		// TODO: need to check the authentication of the user
		
		// modify the user
		if (uManager.userExist(user.userID) == false) {
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
