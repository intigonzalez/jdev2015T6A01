package com.enseirb.telecom.dngroup.dvd2c.service.request;


import java.io.IOException;

import com.enseirb.telecom.dngroup.dvd2c.exception.NoSuchBoxException;
import com.enseirb.telecom.dngroup.dvd2c.exception.NoSuchUserException;
import com.enseirb.telecom.dngroup.dvd2c.exception.SuchUserException;
import com.enseirb.telecom.dngroup.dvd2c.model.Box;
import com.enseirb.telecom.dngroup.dvd2c.model.ListUser;
import com.enseirb.telecom.dngroup.dvd2c.model.User;

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