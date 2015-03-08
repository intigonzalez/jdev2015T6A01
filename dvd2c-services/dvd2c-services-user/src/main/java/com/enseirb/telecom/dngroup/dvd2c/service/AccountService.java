package com.enseirb.telecom.dngroup.dvd2c.service;

import java.util.Collection;
import java.util.List;

import com.enseirb.telecom.dngroup.dvd2c.model.Box;
import com.enseirb.telecom.dngroup.dvd2c.model.Property;
import com.enseirb.telecom.dngroup.dvd2c.model.User;

public interface AccountService {

	/** 
	 * Verify if the user exist on central server and in local
	 * @param userID to verify
	 * @return return true if the user exist
	 */
	public abstract boolean userExistOnServer(String userID);
	
	/** 
	 * Verify if the user exist on local
	 * @param userID to verify
	 * @return return true if the user exist
	 */
	public abstract boolean userExistOnLocal(String userID);

	/**
	 * get a user from local
	 * @param userID the user to get
	 * @return the user
	 */
	public abstract User getUserOnLocal(String userID);

	/**
	 * get user by firstname form server
	 * @param firstname to get list
	 * @return
	 */
	public abstract List<User> getUserFromNameOnServer(String firstname);

	/**
	 *  create user on server and after in local
	 * @param user to create
	 * @return the user
	 */
	public abstract User createUserOnServer(User user);
	
	/**
	 *  create user on local
	 * @param user to create
	 * @return the user
	 */
	public abstract User createUserOnLocal(User user);

	/**
	 * modify user on server and on local
	 * @param user to modify
	 */
	public abstract void saveUserOnServer(User user);
	
	/**
	 * modify user on local
	 * @param user to modify
	 */
	public abstract void saveUserOnLocal(User user);
	
	/**
	 * delete a user on server and on local
	 * @param userID the user to delete
	 */
	public abstract void deleteUserOnServer(String userID);
	
	/**
	 * delete a user on local
	 * @param userID the user to delete
	 */
	public abstract void deleteUserOnLocal(String userID);
	
	/**
	 * Get the list of users who have the same firstname on local
	 * SERVER SERVICES
	 * @param firstname the firstname to search
	 * @return a list of user
	 */
	public abstract List<User> getUserFromName(String firstname);

	/**
	 * Get the list of users who have the same box on local
	 * SERVER SERVICES
	 * @param boxID the firstname to search
	 * @return a list of user
	 */
	public abstract List<User> getUserFromBoxID(String boxID);
	
	/**
	 * Get the box of a user on local
	 * SERVER SERVICES
	 * @param userID to get a box
	 * @return a box with lot information
	 */
	public abstract Box getBox(String userID);

	/**
	 * get all user of boxes
	 * @param listBox the all box to get users
	 * @return a list of user
	 */
	public abstract List<User> getUsersFromBoxes(List<Box> listBox);

	/**
	 * get the list of user from a list of box
	 * @param listBox the list of box to extract users
	 * @return the list of user
	 */
	public abstract List<User> getUsersFromListBoxes(List<Box> listBox);
	
	/**
	 * get the collection of all properties type of a user
	 * @param actorId - the user from whom we want the properties, type - the type of properties
	 * @return the collection of properties
	 */
	public abstract <T extends Property> Collection<T> getUserProperty(String actorId, final Class<T> type);

	/**
	 * Save the type properties of a user
	 * @param actorId - the user from whom we want to save the properties, type - the type of properties
	 */
	public abstract <T extends Property> void saveUserProperty(String actorId, T property);
	
}