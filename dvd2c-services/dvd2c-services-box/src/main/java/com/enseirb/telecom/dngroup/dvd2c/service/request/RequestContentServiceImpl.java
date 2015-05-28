package com.enseirb.telecom.dngroup.dvd2c.service.request;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

//import com.enseirb.telecom.dngroup.dvd2c.endpoints.ContentEndPoints;
import com.enseirb.telecom.dngroup.dvd2c.exception.NoRelationException;
import com.enseirb.telecom.dngroup.dvd2c.exception.NoSuchBoxException;
import com.enseirb.telecom.dngroup.dvd2c.exception.NoSuchUserException;
import com.enseirb.telecom.dngroup.dvd2c.model.Box;
import com.enseirb.telecom.dngroup.dvd2c.model.Content;

@Service
public class RequestContentServiceImpl implements RequestContentService {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(RequestContentServiceImpl.class);
	@Inject
	private Client client;
	// TODO client should be created only once
	@Inject
	RequestUserServiceImpl requestServ;

	public RequestContentServiceImpl() {

	}

	@Override
	public List<Content> get(UUID userID, UUID relationID)
			throws IOException, NoSuchUserException, NoRelationException,
			NoSuchBoxException {

		Box boxRelation = requestServ.getBoxByUserUuidORH(relationID);

		client = ClientBuilder.newClient();

		WebTarget target = client.target(boxRelation.getIp() + "/api/box/"
				+ relationID + "/content/" + userID);
		LOGGER.debug("Launch the request to the box : {}", target.getUri());

		List<Content> listContent = target.request(
				MediaType.APPLICATION_XML_TYPE).get(
				new GenericType<List<Content>>() {
				});

		LOGGER.debug("size of list content is {}", listContent.size());
		return listContent;
	}

}
