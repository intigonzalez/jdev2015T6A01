package com.enseirb.telecom.dvd2c.service;

import com.enseirb.telecom.s9.ListUser;
import com.enseirb.telecom.s9.User;

public interface AccountService {

	/**
	 * 
	 * @param id
	 * @return
	 */
	public abstract boolean userExist(User user);

	public abstract User getUser(String email);

	/**
	 * get user by name form server
	 * @param name to get list
	 * @return
	 */
	public abstract ListUser getUserFromNameOnServer(String name);

	/**
	 *  create user like XSD
	 * @param user
	 * @return
	 */
	public abstract User createUser(User user);

	/**
	 * modify user for save in database
	 * @param user
	 */
	public abstract void saveUser(User user);
	
	public abstract void deleteUser(String userID);

}