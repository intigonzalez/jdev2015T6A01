package com.enseirb.telecom.dngroup.dvd2c.endpoints;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enseirb.telecom.dngroup.dvd2c.db.BoxRepositoryMongo;
import com.enseirb.telecom.dngroup.dvd2c.db.UserRepositoryMongo;
import com.enseirb.telecom.dngroup.dvd2c.exception.NoSuchBoxException;
import com.enseirb.telecom.dngroup.dvd2c.service.AccountService;
import com.enseirb.telecom.dngroup.dvd2c.service.AccountServiceImpl;
import com.enseirb.telecom.dngroup.dvd2c.service.BoxService;
import com.enseirb.telecom.dngroup.dvd2c.service.BoxServiceImpl;
import com.enseirb.telecom.dngroup.dvd2c.model.Box;
import com.enseirb.telecom.dngroup.dvd2c.model.User;

// The Java class will be hosted at the URI path "/myresource"
@Path("oauth")
public class SnapmailEndPoints {
	private static final Logger LOGGER = LoggerFactory.getLogger(SnapmailEndPoints.class);
	BoxService boxManager = new BoxServiceImpl(new BoxRepositoryMongo(
			"CentralMediaHome"));
	AccountService uManager = new AccountServiceImpl(new UserRepositoryMongo(
			"CentralMediaHome"));

	/**
	 * state=actorID
	 * 
	 * 
	 * @return
	 */
	@GET
	public Response googlecode(@QueryParam("state")String actorID,@QueryParam("code") String code) {
		User actor = uManager.getUserOnLocal(actorID);
		try {
			Box box = boxManager.getBoxOnLocal(actor.getBoxID());
			boxManager.sendGoogleCode(actorID,box,code);
			
		} catch (NoSuchBoxException e) {
			LOGGER.error("box of {} not found",actorID);
			throw new WebApplicationException("box of user not found",Status.NOT_FOUND);
		}catch (IOException e) {
			throw new WebApplicationException(Status.NOT_FOUND);
		}
		return null;
		
	}


}
