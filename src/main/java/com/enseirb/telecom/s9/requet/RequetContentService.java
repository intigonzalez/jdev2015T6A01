package com.enseirb.telecom.s9.requet;


import java.io.IOException;

import com.enseirb.telecom.s9.ListContent;
import com.enseirb.telecom.s9.User;
import com.enseirb.telecom.s9.exception.NoRelationException;
import com.enseirb.telecom.s9.exception.NoSuchUserException;

public interface RequetContentService {

	
	
	/**
	 * get a list of contents from a relation in other box
	 * @param user the user to get list
	 * @return ListContent of userID
	 * @throws IOException host is not reachable
	 * @throws NoRelationException the relation doesn't exist
	 * @throws NoSuchUserException user doesn't exist on remote host 
	 */
	public abstract ListContent get(User user) throws IOException, NoSuchUserException, NoRelationException;


}