package com.enseirb.telecom.s9;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

// The Java class will be hosted at the URI path "/myresource"
@Path("/app/account") 
public class createUser {

	// TODO: update the class to suit your needs

	// The Java method will process HTTP GET requests
//	@GET
//	// The Java method will produce content identified by the MIME Media
//	// type "text/plain"
//	@Produces("text/plain")
//	public String getIt() {
//		return "Got it!";
//	}

	@POST
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response postUser(User user) {

		
		return Response.ok().build();

	}

}
