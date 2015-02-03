package com.enseirb.telecom.s9.request;


import java.io.IOException;

import com.enseirb.telecom.s9.ListContent;
import com.enseirb.telecom.s9.User;
import com.enseirb.telecom.s9.exception.NoRelationException;
import com.enseirb.telecom.s9.exception.NoSuchBoxException;
import com.enseirb.telecom.s9.exception.NoSuchUserException;

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