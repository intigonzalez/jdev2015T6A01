package com.enseirb.telecom.s9.request;

import java.io.IOException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.enseirb.telecom.s9.Box;
import com.enseirb.telecom.s9.exception.NoSuchUserException;
import com.enseirb.telecom.s9.exception.SuchUserException;

public class RequestBoxServiceImpl implements RequestBoxService{
	
	private String url;
	private Client client;
	
	public RequestBoxServiceImpl(String url) {
		this.url = url;
		client = ClientBuilder.newClient();
	}

	@Override
	public Box get(Box box) throws IOException, NoSuchUserException {
		// TODO Auto-generated method stub
		
		Box boxGet = new Box();
		// Client client = ClientBuilder.newClient();
		WebTarget target = client.target(url + box.getBoxID());
		boxGet = target.request(MediaType.APPLICATION_XML_TYPE)
				.get(Box.class);

		return boxGet;
		
	}

	@Override
	public void post(Box box) throws IOException, SuchUserException {
		// TODO Auto-generated method stub
		
		box.setPrivateKey(null);//??????
		WebTarget target = client.target(url);
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
			throw new SuchUserException();
		default:
			throw new IOException("Can not conect to the server :"
					+ response.getStatus());
		}
		
		
	}

	@Override
	public void put(Box box) throws IOException, NoSuchUserException {
		// TODO Auto-generated method stub
		
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
			throw new NoSuchUserException();
		default:
			throw new IOException("Can not conect to the server :"
					+ response.getStatus());
		}
		
	}

}
