package com.enseirb.telecom.dngroup.dvd2c.service;

import com.enseirb.telecom.dngroup.dvd2c.model.Box;
import com.enseirb.telecom.dngroup.dvd2c.model.ListUser;
import com.enseirb.telecom.dngroup.dvd2c.model.User;

public interface AccountService {

	
	public abstract boolean userExistOnServer(String userID);
	public abstract boolean userExistOnLocal(String userID);

	public abstract User getUserOnLocal(String userID);

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
	public abstract User createUserOnServer(User user);
	
	public abstract User createUserOnLocal(User user);

	/**
	 * modify user for save in database
	 * @param user
	 */
	public abstract void saveUserOnServer(User user);
	public abstract void saveUserOnLocal(User user);
	
	public abstract void deleteUserOnServer(String userID);
	public abstract void deleteUserOnLocal(String userID);
	

	


	
	/**
	 * Get the list of users who have the same name
	 */
	public abstract ListUser getUserFromName(String name);

	public abstract ListUser getUserFromBoxID(String boxID);
	public abstract Box getBox(String userID);

}