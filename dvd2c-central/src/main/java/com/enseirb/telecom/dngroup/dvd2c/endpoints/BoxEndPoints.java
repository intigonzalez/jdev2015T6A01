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

import com.enseirb.telecom.dngroup.dvd2c.exception.NoSuchBoxException;
import com.enseirb.telecom.dngroup.dvd2c.model.Box;
import com.enseirb.telecom.dngroup.dvd2c.model.User;
import com.enseirb.telecom.dngroup.dvd2c.service.CentralService;

// The Java class will be hosted at the URI path "/myresource"
@Path("app/box")
public class BoxEndPoints {

	@Inject
	CentralService boxManager;


	/**
	 * Get List of box
	 * 
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public List<Box> getListBox() {
		return boxManager.getAllBox();
	}

	/**
	 * get a box by BoxID (unique)
	 * 
	 * @param boxId
	 * @return
	 */
	@GET
	@Path("id/{boxId}")
	@Produces(MediaType.APPLICATION_XML)
	public Box getBox(@PathParam("boxId") String boxId) {
		try {
			return boxManager.getBoxOnLocal(boxId);
		} catch (NoSuchBoxException e) {
			throw new WebApplicationException(Status.NOT_FOUND);
		}
	}

	/**
	 * get list User by IP
	 * 
	 * @param ip
	 * @return
	 */
	@GET
	@Path("ip/{boxIp}")
	@Produces(MediaType.APPLICATION_XML)
	public List<User> getUserFromIP(@PathParam("boxIp") String ip) {
		List<Box> listBox = boxManager.getBoxListFromIP(ip);
		return boxManager.getUsersFromListBoxes(listBox);

	}

	/**
	 * get a new box
	 * 
	 * @param box
	 * @return
	 * @throws URISyntaxException
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response postBox(Box box) throws URISyntaxException {
		if (boxManager.boxExistOnLocal(box.getBoxID()) == false) {
			boxManager.createBoxOnLocal(box);
			return Response.created(new URI(box.getBoxID())).build();
		} else {
			boxManager.saveBoxOnLocal(box);
			return Response.created(new URI(box.getBoxID())).build();
		}
	}

	/**
	 * Modify BoxID
	 * 
	 * @param box
	 * @return
	 * @throws URISyntaxException
	 */
	@PUT
	@Path("{boxId}")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response putBox(Box box) throws URISyntaxException {
		if (boxManager.boxExistOnLocal(box.getBoxID()) == true) {
			boxManager.saveBoxOnLocal(box);
			// NHE that the answer we expect from a post (see location header)
			return Response.created(new URI(box.getBoxID())).build();
		} else {
			return Response.status(404).build();
		}
	}

	/**
	 * Delete boxID
	 * 
	 * @param boxId
	 * @return
	 */
	@DELETE
	@Path("{boxId}")
	public Response deleteBox(@PathParam("boxId") String boxId) {
		if (boxManager.boxExistOnLocal(boxId) == true) {
			boxManager.deleteBoxOnLocal(boxId);
			// NHE that the answer we expect from a post (see location header)
			return Response.accepted().build();
		} else {
			return Response.status(404).build();
		}

	}

}
