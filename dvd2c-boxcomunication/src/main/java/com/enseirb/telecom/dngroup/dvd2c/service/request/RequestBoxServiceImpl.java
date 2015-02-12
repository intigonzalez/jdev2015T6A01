package com.enseirb.telecom.dngroup.dvd2c.service.request;

import java.io.IOException;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.enseirb.telecom.dngroup.dvd2c.exception.NoSuchBoxException;
import com.enseirb.telecom.dngroup.dvd2c.model.Box;

public class RequestBoxServiceImpl implements RequestBoxService{
	
	private String url;
	private Client client;
	
	public RequestBoxServiceImpl(String url) {
		this.url = url;
		client = ClientBuilder.newClient();
	}

	@Override
	public Box get(String boxID) throws IOException,NoSuchBoxException {
		// TODO Auto-generated method stub
		
		Box boxGet = new Box();
		// Client client = ClientBuilder.newClient();
		WebTarget target = client.target(url +"id/"+ boxID);
		
		try{
		boxGet = target.request(MediaType.APPLICATION_XML_TYPE).get(Box.class);
		}catch(WebApplicationException e){
			if (e.getResponse().getStatus()==404){
				throw new NoSuchBoxException();
			}
		}

		return boxGet;		
	}

	@Override
	public void post(Box box) throws IOException {
		// TODO Auto-generated method stub
		
		WebTarget target = client.target(url);
		Response response = target.request(MediaType.APPLICATION_XML_TYPE)
				.post(Entity.entity(box, MediaType.APPLICATION_XML),Response.class);

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
			//throw new SuchUserException();
		default:
			throw new IOException("Can not conect to the server : POST on this link" + target.getUri() +
					+ response.getStatus());
		}			
	}

	@Override
	public void put(Box box) throws IOException {
		// TODO Auto-generated method stub
		
		WebTarget target = client.target(url +box.getBoxID());
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
			//throw new NoSuchUserException();
		default:
			throw new IOException("Can not conect to the server :"
					+ response.getStatus());
		}
		
	}

	@Override
	public void delete(String boxID) throws IOException, NoSuchBoxException {
		// TODO Auto-generated method stub
	
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
	

}
