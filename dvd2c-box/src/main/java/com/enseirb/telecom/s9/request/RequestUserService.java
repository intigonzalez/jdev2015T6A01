package com.enseirb.telecom.s9.request;


import java.io.IOException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.enseirb.telecom.s9.Box;
import com.enseirb.telecom.s9.ListUser;
import com.enseirb.telecom.s9.User;
import com.enseirb.telecom.s9.exception.NoSuchBoxException;
import com.enseirb.telecom.s9.exception.NoSuchUserException;
import com.enseirb.telecom.s9.exception.SuchUserException;

public interface RequestUserService {

	
	
	/**
	 * get a user on remote host
	 * @param UserID the user to get
	 * @return user of userID
	 * @throws IOException host is not reachable
	 * @throws NoSuchUserException user doesn't exist on remote host 
	 */
	public abstract User get(String string) throws IOException, NoSuchUserException;

	public abstract ListUser getUserFromName(String name) throws IOException;

	/**
	 * post a user on remote host
	 * @param user the user to post
	 * @throws IOException host is not reachable
	 * @throws SuchUserException user doesn't exist on remote host 
	 */
	public abstract void post(User user) throws IOException, SuchUserException;

	/**
	 * update a user on remote host
	 * @param user the user to update
	 * @throws IOException host is not reachable
	 * @throws NoSuchUserException user doesn't exist on remote host 
	 */
	public abstract void put(User user) throws IOException, NoSuchUserException;

	/**
	 * delete a user on remote host
	 * @param user the user to delete
	 * @throws IOException host is not reachable
	 * @throws NoSuchUserException user doesn't exist on remote host 
	 */
	public abstract void delete(String userID) throws IOException, NoSuchUserException;

	public abstract Box getBox(String email) throws IOException, NoSuchBoxException;

}