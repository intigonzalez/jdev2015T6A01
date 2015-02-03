package com.enseirb.telecom.dngroup.dvd2c.request;


import java.io.IOException;

import com.enseirb.telecom.dngroup.dvd2c.exception.NoRelationException;
import com.enseirb.telecom.dngroup.dvd2c.exception.NoSuchBoxException;
import com.enseirb.telecom.dngroup.dvd2c.exception.NoSuchUserException;
import com.enseirb.telecom.dngroup.dvd2c.model.ListContent;
import com.enseirb.telecom.dngroup.dvd2c.model.User;

public interface RequestContentService {

	
	
	/**
	 * get a list of contents from a relation in other box
	 * @param userIDToGet the user to get list
	 * @param userIDForm the user of request 
	 * @return ListContent of userID
	 * @throws IOException host is not reachable
	 * @throws NoRelationException the relation doesn't exist
	 * @throws NoSuchUserException user doesn't exist on remote host 
	 * @throws NoSuchBoxException 
	 */
	public abstract ListContent get(String userIDToGet,String userIDForm) throws IOException, NoSuchUserException, NoRelationException, NoSuchBoxException;

	

}