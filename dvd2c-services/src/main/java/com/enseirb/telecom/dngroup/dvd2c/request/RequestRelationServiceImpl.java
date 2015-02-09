package com.enseirb.telecom.dngroup.dvd2c.request;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enseirb.telecom.dngroup.dvd2c.ApplicationContext;
import com.enseirb.telecom.dngroup.dvd2c.exception.NoSuchBoxException;
import com.enseirb.telecom.dngroup.dvd2c.exception.NoSuchUserException;
import com.enseirb.telecom.dngroup.dvd2c.service.RelationServiceImpl;
import com.enseirb.telecom.dngroup.dvd2c.model.Box;
import com.enseirb.telecom.dngroup.dvd2c.model.Relation;
import com.enseirb.telecom.dngroup.dvd2c.model.User;

public class RequestRelationServiceImpl implements RequestRelationService {
	private static final Logger LOGGER = LoggerFactory.getLogger(RequestRelationServiceImpl.class);
	private Client client;
	private String centralServer;
	private RequestUserService requestServ;

	public RequestRelationServiceImpl() {

		client = ClientBuilder.newClient();
		centralServer = ApplicationContext.getProperties().getProperty("CentralURL");
		requestServ = new RequestUserServiceImpl();
	}

	@Override
	public User get(String UserID, String UserToGet) throws IOException, NoSuchUserException, NoSuchBoxException {
		Box boxRelation = requestServ.getBox(UserToGet);
		User userGet = new User();
		WebTarget target = client.target(boxRelation.getIp() + "/api/app/" + UserToGet + "/relation/from/" + UserID);
		userGet = target.request(MediaType.APPLICATION_XML_TYPE).get(User.class);

		return userGet;
	}

	@Override
	public void postRelation(Relation relationOfRequest, Relation relationToRequest) throws IOException, NoSuchBoxException {
		Box boxRelation;
		try {
			boxRelation = requestServ.getBox(relationToRequest.getEmail());
		} catch (Exception e) {
			LOGGER.error("Error while fetching Box information for relation {}", relationToRequest.getEmail());
			throw new IOException("Can not conect to the server : Box information not fetched from CentralServer");
		}
		String requestUrl = boxRelation.getIp() + "/api/app/" + relationToRequest.getEmail() + "/relation/frombox";
		LOGGER.debug("Request : {}", requestUrl);
		WebTarget target = client.target(requestUrl);
		
		Response response = target.request(MediaType.APPLICATION_XML_TYPE).post(Entity.entity(relationOfRequest, MediaType.APPLICATION_XML), Response.class);
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
			throw new IOException("Can not conect to the server :" + response.getStatus());
		}
	}

	@Override
	public void close() {
		client.close();

	}

	@Override
	public void delete(String relationOfRequest, String relationToRequest) throws IOException, NoSuchUserException, NoSuchBoxException {
		// Client client = ClientBuilder.newClient();
		Box boxRelation = requestServ.getBox(relationToRequest);
		WebTarget target = client.target(boxRelation.getIp() + "/api/box/relation/" + relationToRequest + "/" + relationOfRequest);
		Response response = target.request(MediaType.APPLICATION_XML_TYPE).delete();
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
			throw new IOException("Can not conect to the server :" + response.getStatus());
		}
	}

	@Override
	public void setAprouve(String userID, String emailOfRelation) throws IOException, NoSuchBoxException, NoSuchUserException {
		Box boxRelation = requestServ.getBox(emailOfRelation);
		WebTarget target = client.target(boxRelation.getIp() + "/api/box/relation/" + emailOfRelation + "/" + userID);
		Relation relation=new Relation();
		try{
		Response response = target.request(MediaType.APPLICATION_XML_TYPE).put(Entity.entity(relation,MediaType.APPLICATION_XML));
		
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
			throw new IOException("Can not conect to the server :" + response.getStatus());
		}
		}catch (RuntimeException  e){
		    e.printStackTrace();
		}
	}
}
