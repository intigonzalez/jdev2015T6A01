package com.enseirb.telecom.dngroup.dvd2c.service.request;

import java.io.IOException;

import javax.inject.Inject;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.enseirb.telecom.dngroup.dvd2c.CliConfSingleton;
import com.enseirb.telecom.dngroup.dvd2c.exception.NoSuchBoxException;
import com.enseirb.telecom.dngroup.dvd2c.model.Box;

@Service
public class RequestBoxServiceImpl implements RequestBoxService {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(RequestBoxServiceImpl.class);

	private String url = CliConfSingleton.centralURL + "/api/app/box/";
	
	@Inject
	private Client client;

	public RequestBoxServiceImpl() {

	}

	@Override
	public Box get(String boxID) throws IOException, NoSuchBoxException {
		Box boxGet = new Box();
		WebTarget target = client.target(url + "id/" + boxID);
		LOGGER.debug("URL To send the request {}", target.getUri());
		try {
			boxGet = target.request(MediaType.APPLICATION_XML_TYPE).get(
					Box.class);
		} catch (WebApplicationException e) {
			if (e.getResponse().getStatus() == 404) {
				throw new NoSuchBoxException();
			}
		}

		return boxGet;
	}

	@Override
	public void createBoxORH(Box box) throws IOException,ProcessingException {

		WebTarget target = client.target(url);
		LOGGER.debug("Send request to server {}", target.getUri());
		Response response = target.request(MediaType.APPLICATION_XML_TYPE)
				.post(Entity.entity(box, MediaType.APPLICATION_XML),
						Response.class);

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
			throw new IOException(
					"Can not conect to the server : POST on this link"
							+ target.getUri() + +response.getStatus());
		}
	}

	@Override
	public void updateBoxORH(Box box) throws IOException {

		WebTarget target = client.target(url + box.getBoxID());
		// try {
		Response response = target.request(MediaType.APPLICATION_XML_TYPE).put(
				Entity.entity(box, MediaType.APPLICATION_XML), Response.class);

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
		case NOT_FOUND:
			// throw new NoSuchUserException();
		default:
			throw new IOException("Can not conect to the server :"
					+ response.getStatus());
		}

	}

	@Override
	public void deleteBoxORH(String boxID) throws IOException,
			NoSuchBoxException {

		WebTarget target = client.target(this.url + boxID);
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
			throw new NoSuchBoxException();
		default:
			throw new IOException("Can not conect to the server :"
					+ response.getStatus());
		}

	}

	@Override
	public void sendOauthORH(String actorID, Box box, String code)
			throws IOException {

		WebTarget target = client.target(box.getIp() + "/api/oauth/" + actorID);
		LOGGER.debug("Send request to server {}", target.getUri());

		Response response = target.request().post(
				Entity.entity(code, "text/plain"), Response.class);
		LOGGER.info(response.toString());
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
			LOGGER.error("can't send Oauth to {} {}", target.getUri(),
					response.getStatus());
			throw new IOException("can't send Oauth to" + target.getUri()
					+ +response.getStatus());
		}
	}

}
