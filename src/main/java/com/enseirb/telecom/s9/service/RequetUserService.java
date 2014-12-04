package com.enseirb.telecom.s9.service;


import java.io.IOException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.enseirb.telecom.s9.User;
import com.enseirb.telecom.s9.exception.NoSuchUserException;

public interface RequetUserService {

	
	
	public abstract Response put(String url, User user);

	public abstract Response post(String url, User user);

	public abstract Response get(String url);

	/**
	 * delete a user on remote host
	 * @param user the user to delete
	 * @throws IOException host is not reachable
	 * @throws NoSuchUserException user doesn't exist on remote host 
	 */
	public abstract void delete(User user) throws IOException, NoSuchUserException;

}