package com.enseirb.telecom.s9.request;

import java.io.IOException;

import com.enseirb.telecom.s9.User;
import com.enseirb.telecom.s9.exception.NoSuchUserException;

public interface RequestRelationService {

	/**
	 * get a user on remote host
	 * @param UserID the user to get
	 * @return user of userID
	 * @throws IOException host is not reachable
	 * @throws NoSuchUserException user doesn't exist on remote host 
	 */
	public abstract User get(String url) throws IOException, NoSuchUserException;

	public abstract void close();

	
}
