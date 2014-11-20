package com.enseirb.telecom.s9;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import sun.security.provider.certpath.OCSPResponse.ResponseStatus;

import com.enseirb.telecom.s9.db.UserDB;
import com.enseirb.telecom.s9.service.userManager;

// The Java class will be hosted at the URI path "/myresource"
@Path("/app/account")
public class UserInterface {

	// TODO: update the class to suit your needs

	// The Java method will process HTTP GET requests
	// The Java method will produce content identified by the MIME Media
	// type "text/plain"
	@GET
	@Path("{username}")
	@Produces(MediaType.APPLICATION_XML)
	public String getIt() {
		// need to create
		return "Got it!";
	}

	@POST
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response postUser(User user) {
		if (userManager.userExist(user.email) == false) {
			UserDB database = new UserDB();
			database.save(user);
			return Response.ok().build();
		} else {
			return Response.status(409).build();
		}

	}

	@PUT
	@Path("{username}")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response putUser(User user) {
		// need to verfie the user
		// and after this modifie the user
		return Response.status(Status.SERVICE_UNAVAILABLE).build();

	}
	
	@DELETE
	@Path("{username}")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response deleteUser(User user) {
		// need to verfie the user
		// and after this delete the user
		return Response.status(Status.SERVICE_UNAVAILABLE).build();

	}

}
