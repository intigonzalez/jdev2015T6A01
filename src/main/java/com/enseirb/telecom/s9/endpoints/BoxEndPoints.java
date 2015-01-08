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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enseirb.telecom.s9.ApplicationContext;
import com.enseirb.telecom.s9.Box;
import com.enseirb.telecom.s9.db.BoxRepositoryMongo;
import com.enseirb.telecom.s9.service.BoxService;
import com.enseirb.telecom.s9.service.BoxServiceImpl;

// The Java class will be hosted at the URI path "/myresource"
@Path("box")
public class BoxEndPoints {
	private static final Logger LOGGER = LoggerFactory.getLogger(BoxEndPoints.class);


	
	BoxService boxManager = new BoxServiceImpl(new BoxRepositoryMongo());
	
	// TODO: update the class to suit your needs

	// The Java method will process HTTP GET requests
	// The Java method will produce content identified by the MIME Media
	// type "text/plain"
	@GET
	@Path("id/{boxId}")
	@Produces(MediaType.APPLICATION_XML)
	public Box getBox(@PathParam("boxId") String boxId) {
		
		return boxManager.getBox(boxId);
		// NHE: easy way to return an error for a rest api: throw an WebApplicationException
		//throw new WebApplicationException(Status.CONFLICT);
	}

	/**
	 * if box don't exist create this (local and on the central serveur)
	 * @return
	 * @throws URISyntaxException
	 */
	@POST
	@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response postBox2() throws URISyntaxException {
		// add a comment
		Box box = new Box();
		box.setBoxID(ApplicationContext.getProperties().getProperty("BoxID"));
		box.setIp(ApplicationContext.getProperties().getProperty("PublicAddr"));
		if (boxManager.boxExist(box) == false) {
			boxManager.createBox(box);
			return Response.created(new URI(box.getBoxID())).build();
		} else {
			return Response.status(409).build();
		}
		
		//return Response.status(Status.SERVICE_UNAVAILABLE).build();
	}
	
//	@POST
//	@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
//	public Response postBox(Box box) throws URISyntaxException {
//		// add a comment
//		
//		if (boxManager.boxExist(box) == false) {
//			boxManager.createBox(box);
//			return Response.created(new URI(box.getBoxID())).build();
//		} else {
//			return Response.status(409).build();
//		}
//		
//		//return Response.status(Status.SERVICE_UNAVAILABLE).build();
//	}

	@PUT
	@Path("{boxId}")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response putBox(Box box) throws URISyntaxException {
		// need to verify user
		// and after this modifies the comment
		
		if (boxManager.boxExist(box) == true) {
			boxManager.saveBox(box);
			// NHE that the answer we expect from a post (see location header)
			return Response.created(new URI(box.getBoxID())).build();
		} else {
			return Response.status(409).build();
		}
		
		//return Response.status(Status.SERVICE_UNAVAILABLE).build();

	}

	@DELETE
	@Path("{boxId}")
	public Response deleteBox(@PathParam("boxId") String boxID) {
		// need to verify user
		// and after this delete the comment
		
		//if (boxManager.boxExist(box) == true) {
			boxManager.deleteBox(boxID);
			// NHE that the answer we expect from a post (see location header)
			return Response.accepted().build();
		//} else {
			//return Response.status(409).build();
		//}		
	}

}
