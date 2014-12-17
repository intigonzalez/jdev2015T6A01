package com.enseirb.telecom.s9.request;

import java.io.IOException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.enseirb.telecom.s9.Box;
import com.enseirb.telecom.s9.User;
import com.enseirb.telecom.s9.exception.NoSuchUserException;
import com.enseirb.telecom.s9.exception.SuchUserException;


public interface RequestBoxService {

	
	/**
	 * get a box on remote host
	 * @param UserID the user to get
	 * @return user of userID
	 * @throws IOException host is not reachable
	 * @throws NoSuchUserException user doesn't exist on remote host 
	 */
	public abstract Box get(Box box) throws IOException, NoSuchUserException;

	/**
	 * post a box on remote host
	 * @param user the user to post
	 * @throws IOException host is not reachable
	 * @throws SuchUserException user doesn't exist on remote host 
	 */
	public abstract void post(Box box) throws IOException, SuchUserException;

	/**
	 * update a box on remote host
	 * @param user the user to update
	 * @throws IOException host is not reachable
	 * @throws NoSuchUserException user doesn't exist on remote host 
	 */
	public abstract void put(Box box) throws IOException, NoSuchUserException;
	
	
}
