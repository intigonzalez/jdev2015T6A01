package com.enseirb.telecom.s9;

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

// The Java class will be hosted at the URI path "/myresource"
@Path("/app/group")
public class GroupEndPoints {

	// TODO: update the class to suit your needs

	// The Java method will process HTTP GET requests
	// The Java method will produce content identified by the MIME Media
	// type "text/plain"
	@GET
	@Path("{groupID}")
	@Produces(MediaType.APPLICATION_XML)
	public Group getGroupe() {
		// TODO: get the list of relation if null return all relation
		//Return the list of mender group
		// NHE: easy way to return an error for a rest api: throw an WebApplicationException
		throw new WebApplicationException(Status.CONFLICT);
	}

	@POST
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response postBox(Group group) {
		// TODO: crée un groupe 
		return Response.status(Status.SERVICE_UNAVAILABLE).build();

	}

	@PUT
	@Path("{groupID}")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response putBox(Group group) {
		// TODO: modifie le nom d'un groupe 
		return Response.status(Status.SERVICE_UNAVAILABLE).build();

	}

	@DELETE
	@Path("{groupID}")
	public Response deleteBox(@PathParam("groupId") String boxId) {
		// need to verify user
		// del the group
		return Response.status(Status.SERVICE_UNAVAILABLE).build();

	}

}
