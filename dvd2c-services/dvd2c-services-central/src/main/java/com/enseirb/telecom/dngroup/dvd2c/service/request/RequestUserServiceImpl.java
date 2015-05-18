package com.enseirb.telecom.dngroup.dvd2c.service.request;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.enseirb.telecom.dngroup.dvd2c.ApplicationContext;
import com.enseirb.telecom.dngroup.dvd2c.CliConfSingleton;
//import com.enseirb.telecom.dngroup.dvd2c.endpoints.RelationEndPoints;
import com.enseirb.telecom.dngroup.dvd2c.exception.NoSuchBoxException;
import com.enseirb.telecom.dngroup.dvd2c.exception.NoSuchUserException;
import com.enseirb.telecom.dngroup.dvd2c.exception.SuchUserException;
import com.enseirb.telecom.dngroup.dvd2c.model.Box;
import com.enseirb.telecom.dngroup.dvd2c.model.User;

@Service
public class RequestUserServiceImpl implements RequestUserService {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(RequestUserService.class);
	@Inject
	private Client client;
	private String server = CliConfSingleton.centralURL;;
	private String url= server + "/api/app/account/";;
	public RequestUserServiceImpl() {
//
//		client = ClientBuilder.newClient();
	}

	@Override
	public User get(String user) throws IOException, NoSuchUserException {
		User userGet = new User();
		// Client client = ClientBuilder.newClient();
		WebTarget target = client.target(url + user);
		try {
			userGet = target.request(MediaType.APPLICATION_XML_TYPE).get(
					User.class);
		} catch (WebApplicationException e) {
			if (e.getResponse().getStatus() == 404) {
				throw new NoSuchUserException();
			} else {
				throw e;
			}
		}

		return userGet;
	}

	@Override
	public List<User> getUserFromName(String firstname) throws IOException {
		List<User> listUser = new ArrayList<User>();
		// Client client = ClientBuilder.newClient();
		WebTarget target = client.target(url + "firstname/" + firstname);
		try {
			listUser = target.request(MediaType.APPLICATION_XML_TYPE).get(new GenericType<List<User>>(){});
		} catch (WebApplicationException e) {
			if (e.getResponse().getStatus() == 500) {
				LOGGER.error("Error on remote Host : get {}",target.getUri(), e);
			} else {
				LOGGER.error("Error for get {}",target.getUri(), e);
			}

		}

		return listUser;
	}

	@Override
	public void createUserORH(User user) throws IOException, SuchUserException {
		WebTarget target = client.target(url);
		Response response = target.request(MediaType.APPLICATION_XML_TYPE)
				.post(Entity.entity(user, MediaType.APPLICATION_XML),
						Response.class);

		switch (Status.fromStatusCode(response.getStatus())) {
		case CREATED:
			// normal statement
			break;
		case CONFLICT:
			throw new SuchUserException();
		case NOT_FOUND:
			//DB: actual return 
			throw new SuchUserException();
		default:
			throw new IOException("Can not conect to the server :"
					+ response.getStatus());
		}

	}

	@Override
	public void updateUserORH(User user) throws IOException, NoSuchUserException {

		// Client client = ClientBuilder.newClient();
		WebTarget target = client.target(url + user.getUserID());
		// try {
		Response response = target.request(MediaType.APPLICATION_XML_TYPE).put(
				Entity.entity(user, MediaType.APPLICATION_XML), Response.class);
		switch (Status.fromStatusCode(response.getStatus())) {
		case ACCEPTED:
			// normal statement but don't is normally not that
			break;
		case CREATED:
			// normal statement
			break;
		case OK:
			// DB : current solution
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
	public void deleteUserORH(String userID) throws IOException,
			NoSuchUserException {
		// Client client = ClientBuilder.newClient();
		WebTarget target = client.target(this.url + userID);
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

	public Box getBoxByUserIDORH(String email) throws IOException,
			NoSuchBoxException {

		Box boxGet = new Box();
		// Client client = ClientBuilder.newClient();
		WebTarget target = client.target(url + email + "/box");
		try {
			boxGet = target.request(MediaType.APPLICATION_XML_TYPE).get(
					Box.class);
		} catch (WebApplicationException e) {
			if (e.getResponse().getStatus() == 404)
				throw new NoSuchBoxException();
		}
		return boxGet;

	}

}
