package com.enseirb.telecom.s9.request;

import java.io.IOException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import com.enseirb.telecom.s9.User;
import com.enseirb.telecom.s9.exception.NoSuchUserException;

public class RequestRelationServiceImpl implements RequestRelationService {


	private Client client;

	public RequestRelationServiceImpl() {

		client = ClientBuilder.newClient();
	}
	
	@Override
	public User get(String url) throws IOException, NoSuchUserException {
		User userGet = new User();
		WebTarget target = client.target(url);
		userGet = target.request(MediaType.APPLICATION_XML_TYPE).get(User.class);

		return userGet;
	}

	@Override
	public void close() {
		client.close();
		
	}
}
