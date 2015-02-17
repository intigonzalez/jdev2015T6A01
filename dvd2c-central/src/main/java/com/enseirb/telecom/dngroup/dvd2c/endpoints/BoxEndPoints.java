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

import com.enseirb.telecom.dngroup.dvd2c.db.BoxRepositoryMongo;
import com.enseirb.telecom.dngroup.dvd2c.db.UserRepositoryMongo;
import com.enseirb.telecom.dngroup.dvd2c.service.AccountService;
import com.enseirb.telecom.dngroup.dvd2c.service.AccountServiceImpl;
import com.enseirb.telecom.dngroup.dvd2c.service.BoxService;
import com.enseirb.telecom.dngroup.dvd2c.service.BoxServiceImpl;
import com.enseirb.telecom.dngroup.dvd2c.model.Box;
import com.enseirb.telecom.dngroup.dvd2c.model.ListBox;
import com.enseirb.telecom.dngroup.dvd2c.model.ListUser;

// The Java class will be hosted at the URI path "/myresource"
@Path("app/box")
public class BoxEndPoints {

	BoxService boxManager = new BoxServiceImpl(new BoxRepositoryMongo("CentralMediaHome"));
	AccountService uManager = new AccountServiceImpl(new UserRepositoryMongo("CentralMediaHome"));

	/**
	 * Get List of box
	 * 
	 * @return
	 */
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public ListBox getListBox() {
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
		return boxManager.getBoxOnLocal(boxId);
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
	public ListUser getUserFromIP(@PathParam("boxIp") String ip) {
		ListBox listBox = boxManager.getBoxListFromIP(ip);
		return uManager.getUsersFromListBoxes(listBox);
		
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
			boxManager.saveBoxOnLocal(box);
			return Response.created(new URI(box.getBoxID())).build();
		} else {
			return Response.status(409).build();
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
			return Response.status(409).build();
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
			return Response.status(409).build();
		}

	}

}
