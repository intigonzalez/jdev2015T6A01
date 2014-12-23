package com.enseirb.telecom.s9.request;

import java.io.IOException;

import com.enseirb.telecom.s9.Relation;
import com.enseirb.telecom.s9.User;
import com.enseirb.telecom.s9.exception.NoSuchBoxException;
import com.enseirb.telecom.s9.exception.NoSuchUserException;

public interface RequestRelationService {

	/**
	 * get a user on remote host
	 * @param UserID the userID
	 * @param UserToGet  
	 * @return user of userID
	 * @throws IOException host is not reachable
	 * @throws NoSuchUserException user doesn't exist on remote host 
	 * @throws NoSuchBoxException 
	 */
	public abstract User get(String UserID, String UserToGet ) throws IOException, NoSuchUserException, NoSuchBoxException;

	public abstract void close();

	public abstract void postRelation(Relation relation, Relation relation2) throws IOException, NoSuchBoxException;

	
}
