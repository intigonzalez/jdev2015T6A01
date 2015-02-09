package com.enseirb.telecom.dngroup.dvd2c.service;

import com.enseirb.telecom.dngroup.dvd2c.model.Box;
import com.enseirb.telecom.dngroup.dvd2c.model.ListUser;
import com.enseirb.telecom.dngroup.dvd2c.model.User;

public interface AccountServiceCentral {

	/**
	 * Check if a user already exists in the database from his userID
	 * 
	 * @param email
	 * @return
	 */

	public abstract boolean userExist(String userID);

	/**
	 * Retrieve a user from the database
	 * 
	 * @param email
	 * @return
	 */

	public abstract User getUser(String userID);

	public abstract Box getBox(String userID);

	public abstract User createUser(User user);

	public abstract void saveUser(User user);

	/**
	 * Delete a user from the database
	 * 
	 * @param email
	 */

	public abstract void deleteUser(String email);

	/**
	 * Get the list of users who have the same name
	 */
	public abstract ListUser getUserFromName(String name);

	public ListUser getUserFromBoxID(String boxID);

}