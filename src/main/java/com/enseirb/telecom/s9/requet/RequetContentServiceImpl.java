package com.enseirb.telecom.s9.requet;

import java.io.IOException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import com.enseirb.telecom.s9.ListContent;
import com.enseirb.telecom.s9.User;
import com.enseirb.telecom.s9.exception.NoRelationException;
import com.enseirb.telecom.s9.exception.NoSuchUserException;

public class RequetContentServiceImpl implements RequetContentService {

	private String url;
	private Client client;

	public RequetContentServiceImpl(String url) {
		this.url = url;
		client = ClientBuilder.newClient();
	}

	@Override
	public ListContent get(User user) throws IOException, NoSuchUserException,
			NoRelationException {
		ListContent listContent = new ListContent();

		WebTarget target = client.target(url  + user.getUserID()+ "video/");
		try {
			listContent = target.request(MediaType.APPLICATION_XML_TYPE).get(
					ListContent.class);
		} catch (Exception e) {
			System.out.println(target.getUri());
			e.printStackTrace();
		}
		return listContent;
	}

	// @Override
	// public User get(User user) throws IOException, NoSuchUserException {
	// User userGet =new User();
	// Client client = ClientBuilder.newClient();
	// WebTarget target = client.target(url+"userID/"+user.getUserID());
	// try {
	// userGet = target.request(MediaType.APPLICATION_XML_TYPE).get(User.class);
	// }catch (Exception e){
	// System.out.println(target.getUri());
	// e.printStackTrace();
	// }
	//
	// return userGet;
	// }

}
