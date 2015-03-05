package com.enseirb.telecom.dngroup.dvd2c.endpoints;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.core.status.Status;

import com.enseirb.telecom.dngroup.dvd2c.db.RelationshipRepositoryMongo;
import com.enseirb.telecom.dngroup.dvd2c.db.UserRepositoryMongo;
import com.enseirb.telecom.dngroup.dvd2c.model.Relation;
import com.enseirb.telecom.dngroup.dvd2c.service.RelationService;
import com.enseirb.telecom.dngroup.dvd2c.service.RelationServiceImpl;

// The Java class will be hosted at the URI path "/myresource"
@Path("app/{userID}/role")
public class GroupEndPoints {
	private static final Logger LOGGER = LoggerFactory.getLogger(GroupEndPoints.class);


	RelationService rManager = new RelationServiceImpl(new RelationshipRepositoryMongo(),
			new UserRepositoryMongo("mediahome"));
	/**
	 * get all user of on groupe
	 * @param userID
	 * @param roleID
	 * @return the list of mender group
	 */
	@GET
	@Path("{roleID}")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<Relation> getGroupe(@PathParam("userID") String userID,
			@PathParam("roleID") int roleID) {

		try {
			return rManager.getListRelation(userID,roleID);
		} catch (Exception e) {
			throw new WebApplicationException(Status.ERROR);
		}

	}

}
