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
import javax.ws.rs.core.Response.Status;

import com.enseirb.telecom.s9.db.UserRepositoryMock;
import com.enseirb.telecom.s9.service.AccountService;
import com.enseirb.telecom.s9.service.AccountServiceImpl;
import com.sun.jersey.spi.resource.Singleton;

// The Java class will be hosted at the URI path "/app/account"
@Path("/app/account")
@Singleton //NHE: lifecycle for demo purpose, make only 1 endpoint per application
public class UserInterface {

	AccountService uManager = new AccountServiceImpl(new UserRepositoryMock());

	// TODO: update the class to suit your needs

	// The Java method will process HTTP GET requests
	// The Java method will produce content identified by the MIME Media
	// type "text/plain"
	@GET
	@Path("{email}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public User getIt(@PathParam("email") String email) {
		return uManager.getUser(email);
	}

	@POST
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response postUser(User user) throws URISyntaxException {
		if (uManager.userExist(user.email) == false) {
			uManager.saveUser(user);
			//NHE that the answer we expect from a post (see location header)
			return Response.created(new URI(user.email)).build();
		} else {
			return Response.status(409).build();
		}

	}

	@PUT
	@Path("{username}")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response putUser(User user) {
		// need to verify the user
		// and after this modifies the user
		return Response.status(Status.SERVICE_UNAVAILABLE).build();

	}

	@DELETE
	@Path("{username}")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response deleteUser(User user) {
		// need to verify the user
		// and after this delete the user
		return Response.status(Status.SERVICE_UNAVAILABLE).build();

	}

}
