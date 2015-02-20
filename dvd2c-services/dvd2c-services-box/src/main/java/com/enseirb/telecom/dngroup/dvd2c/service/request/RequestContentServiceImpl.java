package com.enseirb.telecom.dngroup.dvd2c.service.request;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import com.enseirb.telecom.dngroup.dvd2c.endpoints.ContentEndPoints;
import com.enseirb.telecom.dngroup.dvd2c.exception.NoRelationException;
import com.enseirb.telecom.dngroup.dvd2c.exception.NoSuchBoxException;
import com.enseirb.telecom.dngroup.dvd2c.exception.NoSuchUserException;
import com.enseirb.telecom.dngroup.dvd2c.model.Box;
import com.enseirb.telecom.dngroup.dvd2c.model.Content;
import com.enseirb.telecom.dngroup.dvd2c.model.ListContent;

public class RequestContentServiceImpl implements RequestContentService {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(RequestContentServiceImpl.class);
	private Client client;

	public RequestContentServiceImpl() {

	}

	@Override
	public List<Content> get(String userID, String relationID)
			throws IOException, NoSuchUserException, NoRelationException,
			NoSuchBoxException {
		RequestUserServiceImpl requestServ = new RequestUserServiceImpl();
		Box boxRelation = requestServ.getBoxByUserIDORH(relationID);

		client = ClientBuilder.newClient();

		LOGGER.debug("Launch the request to the box");
		WebTarget target = client.target(boxRelation.getIp() + "/api/box/"
				+ relationID + "/content/" + userID);

		List<Content> listContent = target.request(
				MediaType.APPLICATION_XML_TYPE).get(List.class);
		client.close();
		LOGGER.debug("listContent is {}", listContent.toString());
		return listContent;
	}

}
