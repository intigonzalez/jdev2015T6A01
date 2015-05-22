package com.enseirb.telecom.dngroup.dvd2c.service.request;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.enseirb.telecom.dngroup.dvd2c.exception.NoRelationException;
import com.enseirb.telecom.dngroup.dvd2c.exception.NoSuchBoxException;
import com.enseirb.telecom.dngroup.dvd2c.exception.NoSuchUserException;
import com.enseirb.telecom.dngroup.dvd2c.model.Content;

public interface RequestContentService {

	
	
	/**
	 * get a list of contents from a relation in other box
	 * @param userIDToGet the user to get list
	 * @param userIDForm the user of request 
	 * @return List<Content>  of userID
	 * @throws IOException host is not reachable
	 * @throws NoRelationException the relation doesn't exist
	 * @throws NoSuchUserException user doesn't exist on remote host 
	 * @throws NoSuchBoxException 
	 */
	public abstract List<Content> get(UUID userIDToGet,UUID userIDForm) throws IOException, NoSuchUserException, NoRelationException, NoSuchBoxException;

	

}