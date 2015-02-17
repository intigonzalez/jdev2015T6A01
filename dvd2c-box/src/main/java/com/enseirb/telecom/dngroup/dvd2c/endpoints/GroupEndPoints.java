package com.enseirb.telecom.dngroup.dvd2c.endpoints;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enseirb.telecom.dngroup.dvd2c.db.RelationshipRepositoryMongo;
import com.enseirb.telecom.dngroup.dvd2c.db.UserRepositoryMongo;
import com.enseirb.telecom.dngroup.dvd2c.model.ListRelation;
import com.enseirb.telecom.dngroup.dvd2c.service.RelationService;
import com.enseirb.telecom.dngroup.dvd2c.service.RelationServiceImpl;

// The Java class will be hosted at the URI path "/myresource"
@Path("app/{userID}/group")
public class GroupEndPoints {
	private static final Logger LOGGER = LoggerFactory.getLogger(GroupEndPoints.class);


	RelationService rManager = new RelationServiceImpl(new RelationshipRepositoryMongo(),
			new UserRepositoryMongo("mediahome"));
	@GET
	@Path("{groupID}")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public ListRelation getGroupe(@PathParam("userID") String userID,
			@PathParam("groupID") int groupID) {
		// TODO: get the list of relation if null return all relation
		// Return the list of mender group
		// NHE: easy way to return an error for a rest api: throw an
		// WebApplicationException
		
		return rManager.getListRelation(userID,groupID);

	}

}
