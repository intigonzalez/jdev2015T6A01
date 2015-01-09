package com.enseirb.telecom.s9.request;

import java.io.IOException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import com.enseirb.telecom.s9.Box;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.enseirb.telecom.s9.ListContent; 
import com.enseirb.telecom.s9.endpoints.ContentEndPoints;
import com.enseirb.telecom.s9.exception.NoRelationException;
import com.enseirb.telecom.s9.exception.NoSuchBoxException;
import com.enseirb.telecom.s9.exception.NoSuchUserException;

public class RequestContentServiceImpl implements RequestContentService {
	private static final Logger LOGGER = LoggerFactory.getLogger(ContentEndPoints.class);
    private Client client;

    public RequestContentServiceImpl() {

    }

    @Override
    public ListContent get(String userID, String relationID)
	    throws IOException, NoSuchUserException, NoRelationException,
	    NoSuchBoxException {
	RequestUserServiceImpl requestServ = new RequestUserServiceImpl();
	Box boxRelation = requestServ.getBox(relationID);

	client = ClientBuilder.newClient();
	ListContent listContent = new ListContent();

	WebTarget target = client.target(boxRelation.getIp() + "/api/app/"
		+ relationID + "/content/relation/" + userID);

	listContent = target.request(MediaType.APPLICATION_XML_TYPE).get(
		ListContent.class);
	client.close();
	LOGGER.debug("listContent is {}", listContent);
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
