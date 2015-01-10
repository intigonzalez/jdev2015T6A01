package com.enseirb.telecom.s9.request;

import java.io.IOException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.enseirb.telecom.s9.ApplicationContext;
import com.enseirb.telecom.s9.Box;
import com.enseirb.telecom.s9.Relation;
import com.enseirb.telecom.s9.User;
import com.enseirb.telecom.s9.exception.NoSuchBoxException;
import com.enseirb.telecom.s9.exception.NoSuchUserException;

public class RequestRelationServiceImpl implements RequestRelationService {

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
	Box boxRelation = requestServ.getBox(relationToRequest.getEmail());
	WebTarget target = client.target(boxRelation.getIp() + "/api/app/" + relationToRequest.getEmail() + "/relation/frombox");
	Response response = target.request(MediaType.APPLICATION_XML_TYPE).post(
		Entity.entity(relationOfRequest, MediaType.APPLICATION_XML), Response.class);
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
	WebTarget target = client.target(boxRelation.getIp() + "/api/app/" + relationToRequest + "/relation/" + relationOfRequest);
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
    public void setAprouve(String userID, String emailOfRelation) throws IOException, NoSuchBoxException {
	Box boxRelation = requestServ.getBox(emailOfRelation);
	WebTarget target = client.target(boxRelation.getIp() + "/api/app/box/relation/" + emailOfRelation+"/"+userID);
	Response response = target.request(MediaType.APPLICATION_XML_TYPE).put(null);

    }
}
