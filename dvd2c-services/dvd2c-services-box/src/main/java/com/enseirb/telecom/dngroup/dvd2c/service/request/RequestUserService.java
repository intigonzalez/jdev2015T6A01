package com.enseirb.telecom.dngroup.dvd2c.service.request;


import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.UUID;

import com.enseirb.telecom.dngroup.dvd2c.exception.NoSuchBoxException;
import com.enseirb.telecom.dngroup.dvd2c.exception.NoSuchUserException;
import com.enseirb.telecom.dngroup.dvd2c.exception.SuchUserException;
import com.enseirb.telecom.dngroup.dvd2c.model.Box;
import com.enseirb.telecom.dngroup.dvd2c.model.User;

public interface RequestUserService {

	
	
	/**
	 * get a user on remote host
	 * @param UserID the user to get
	 * @return user of userID
	 * @throws IOException host is not reachable
	 * @throws NoSuchUserException user doesn't exist on remote host 
	 */
	public abstract User get(UUID string) throws IOException, NoSuchUserException;

	/**
	 * Get a list of user by firstname on remote host (server normally)
	 * @param firstname the firstname to request
	 * @return the list of user with this name
	 * @throws IOException the host is not reachable
	 */
	public abstract List<User> getUserFromName(String firstname) throws IOException;

	/**
	 * post a user on remote host
	 * @param user the user to post
	 * @return 
	 * @throws IOException host is not reachable
	 * @throws SuchUserException user doesn't exist on remote host 
	 */
	public abstract URI createUserORH(User user) throws IOException, SuchUserException;

	/**
	 * update a user on remote host
	 * @param user the user to update
	 * @throws IOException host is not reachable
	 * @throws NoSuchUserException user doesn't exist on remote host 
	 */
	public abstract void updateUserORH(User user) throws IOException, NoSuchUserException;

	/**
	 * delete a user on remote host
	 * @param user the user to delete
	 * @throws IOException host is not reachable
	 * @throws NoSuchUserException user doesn't exist on remote host 
	 */
	public abstract void deleteUserORH(UUID userID) throws IOException, NoSuchUserException;

	/**
	 * Get a box with a userID on Remote host (normally server)
	 * @param userID the userID to found the box
	 * @return the box with addr of this
	 * @throws IOException the host is not reachable
	 * @throws NoSuchUserException 
	 */
	public abstract Box getBoxByUserUuidORH(UUID userID) throws IOException, NoSuchUserException;

	

}