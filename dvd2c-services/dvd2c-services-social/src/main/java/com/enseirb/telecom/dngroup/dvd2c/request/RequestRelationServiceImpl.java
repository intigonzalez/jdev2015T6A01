package com.enseirb.telecom.dngroup.dvd2c.request;

import java.io.IOException;
import java.util.UUID;

import javax.inject.Inject;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.ResponseProcessingException;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.enseirb.telecom.dngroup.dvd2c.ApplicationContext;
import com.enseirb.telecom.dngroup.dvd2c.CliConfSingleton;
import com.enseirb.telecom.dngroup.dvd2c.exception.NoSuchBoxException;
import com.enseirb.telecom.dngroup.dvd2c.exception.NoSuchUserException;
import com.enseirb.telecom.dngroup.dvd2c.model.Box;
import com.enseirb.telecom.dngroup.dvd2c.model.ContactXSD;
import com.enseirb.telecom.dngroup.dvd2c.model.User;
import com.enseirb.telecom.dngroup.dvd2c.service.request.RequestUserService;
import com.sun.research.ws.wadl.Link;

@Service
public class RequestRelationServiceImpl implements RequestRelationService {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(RequestRelationServiceImpl.class);
	@Inject
	protected Client client;
	@Inject
	protected RequestUserService requestServ;

	public RequestRelationServiceImpl() {

	}

	// @Override
	// public User getUserLocal(String relationIDString) {
	// UriBuilder builder = UriBuilder
	// .fromPath(CliConfSingleton.appHostName)
	// .scheme("http")
	// .path("/api/app/"+ relationIDString).port(CliConfSingleton.appPort);
	// WebTarget target = client.target(builder);
	// }

	@Override
	public User get(UUID UserID, UUID UserToGet) throws IOException,
			NoSuchUserException, NoSuchBoxException {
		Box boxRelation = requestServ.getBoxByUserUuidORH(UserToGet);

		WebTarget target = client.target(UriBuilder
				.fromPath(boxRelation.getIp()).path("api").path("app")
				.path(UserToGet.toString()).path("relation").path("from")
				.path(UserID.toString()).build()

		);
		User userGet = target.request(MediaType.APPLICATION_XML_TYPE).get(
				User.class);

		return userGet;
	}

	@Override
	public void updateRelationORH(ContactXSD relationOfRequest,
			UUID relationUUID) throws IOException, NoSuchBoxException {
		Box boxRelation;
		try {
			boxRelation = requestServ.getBoxByUserUuidORH(relationUUID);
		} catch (Exception e) {
			LOGGER.error(
					"Error while fetching Box information for relation {}",
					relationUUID);
			throw new IOException(
					"Can not conect to the server : Box information not fetched from CentralServer");
		}
		String requestUrl = boxRelation.getIp() + "/api/app/" + relationUUID
				+ "/relation/frombox";
		LOGGER.debug("Request : {}", requestUrl);
		WebTarget target = client.target(requestUrl);

		Response response = target.request(MediaType.APPLICATION_XML_TYPE)
				.post(Entity.entity(relationOfRequest,
						MediaType.APPLICATION_XML), Response.class);
		switch (Status.fromStatusCode(response.getStatus())) {
		case ACCEPTED:
			// normal statement but don't is normally not that
			break;
		case CREATED:
			// normal statement
			break;
		case OK:
			// normal statement but don't use this because normally we need
			// return a object
			break;
		case CONFLICT:
			// throw new SuchUserException();
		default:
			throw new IOException("Can not conect to the server :"
					+ response.getStatus());
		}
	}

	@Override
	public void deleteRelationORH(UUID relationOfRequest, UUID relationToRequest)
			throws IOException, NoSuchUserException, NoSuchBoxException {
		// Client client = ClientBuilder.newClient();
		Box boxRelation = requestServ.getBoxByUserUuidORH(relationToRequest);
		WebTarget target = client.target(boxRelation.getIp()
				+ "/api/box/relation/" + relationToRequest + "/"
				+ relationOfRequest);
		Response response = target.request(MediaType.APPLICATION_XML_TYPE)
				.delete();
		switch (Status.fromStatusCode(response.getStatus())) {
		case ACCEPTED:
			// normal statement
			break;
		case CREATED:
			// normal statement but don't is normally not that
			break;
		case OK:
			// normal statement but don't use this because normally we need
			// return a object
			break;
		case NOT_FOUND:
			throw new NoSuchUserException();
		default:
			throw new IOException("Can not conect to the server :"
					+ response.getStatus());
		}
	}

	@Override
	public void setAprouveRelationORH(UUID userID, UUID actorIDOfRelation)
			throws IOException, NoSuchBoxException, NoSuchUserException {
		Box boxRelation = requestServ.getBoxByUserUuidORH(actorIDOfRelation);
		WebTarget target = client.target(boxRelation.getIp()
				+ "/api/box/relation/" + actorIDOfRelation + "/" + userID);
		ContactXSD relation = new ContactXSD();
		Response response = null;
		try {
			response = target.request(MediaType.APPLICATION_XML_TYPE).put(
					Entity.entity(relation, MediaType.APPLICATION_XML));

		} catch (ResponseProcessingException e) {
			response = e.getResponse();
			LOGGER.error("", e);
		} catch (ProcessingException e) {

			LOGGER.error("", e);
		}
		switch (Status.fromStatusCode(response.getStatus())) {
		case ACCEPTED:
			// normal statement
			break;
		case NOT_FOUND:
			throw new NoSuchUserException();
		default:
			throw new IOException("Can not conect to the server :"
					+ response.getStatus());
		}

	}
}
