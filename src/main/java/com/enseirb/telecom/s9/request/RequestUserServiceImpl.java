package com.enseirb.telecom.s9.request;

import java.io.IOException;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.enseirb.telecom.s9.Box;
import com.enseirb.telecom.s9.User;
import com.enseirb.telecom.s9.exception.NoSuchBoxException;
import com.enseirb.telecom.s9.exception.NoSuchUserException;
import com.enseirb.telecom.s9.exception.SuchUserException;

public class RequestUserServiceImpl implements RequestUserService {

	private String url;
	private Client client;

	public RequestUserServiceImpl(String url) {
		this.url = url;
		client = ClientBuilder.newClient();
	}

	@Override
	public User get(User user) throws IOException, NoSuchUserException {
		User userGet = new User();
		// Client client = ClientBuilder.newClient();
		WebTarget target = client.target(url + user.getUserID());
		userGet = target.request(MediaType.APPLICATION_XML_TYPE)
				.get(User.class);

		return userGet;
	}

	@Override
	public void post(User user) throws IOException, SuchUserException {
		WebTarget target = client.target(url);
		Response response = target.request(MediaType.APPLICATION_XML_TYPE)
				.post(Entity.entity(user, MediaType.APPLICATION_XML),
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
			throw new SuchUserException();
		default:
			throw new IOException("Can not conect to the server :"
					+ response.getStatus());
		}

	}

	@Override
	public void put(User user) throws IOException, NoSuchUserException {

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
			// normal statement but don't use this because normally we need
			// return a object
			break;
		case NOT_FOUND:
			throw new NoSuchUserException();
		default:
			throw new IOException("Can not conect to the server :"
					+ response.getStatus());
		}
		// }catch (Exception e){
		// System.out.println(target.getUri());
		// e.printStackTrace();
		// }
	}

	@Override
	public void delete(String userID) throws IOException, NoSuchUserException {
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

	public Box getBox(String email) throws IOException, NoSuchBoxException {

		Box boxGet = new Box();
		// Client client = ClientBuilder.newClient();
		WebTarget target = client.target(url +"account/"+ email + "/box");
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
