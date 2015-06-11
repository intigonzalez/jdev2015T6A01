package com.enseirb.telecom.dngroup.dvd2c.service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import com.enseirb.telecom.dngroup.dvd2c.exception.NoSuchUserException;
import com.enseirb.telecom.dngroup.dvd2c.exception.SuchUserException;
import com.enseirb.telecom.dngroup.dvd2c.modeldb.User;
import com.enseirb.telecom.dngroup.dvd2c.model.Property;
import com.enseirb.telecom.dngroup.dvd2c.model.PropertyGroups;


public interface AccountService {

	

	/** 
	 * Verify if the user exist on central server and in local
	 * @param userUUID to verify
	 * @return return true if the user exist
	 */
	public abstract boolean userExistOnServer(UUID userUUID);
	
	/** 
	 * Verify if the user exist on local
	 * @param userUUID to verify
	 * @return return true if the user exist
	 */
	public abstract boolean userExistOnLocal(UUID userUUID);

	/**
	 * get a user from local
	 * @param userUUID the user id to get
	 * @return the user
	 * @throws NoSuchUserException 
	 */
	public abstract User findUserByUUID(UUID userUUID) throws NoSuchUserException;


	/**
	 * get a user from local
	 * @param userID the user mail to get
	 * @return the user
	 * @throws NoSuchUserException 
	 */
	public abstract User findUserByEmail(String userID) throws NoSuchUserException;

	/**
	 * get user by firstname form server
	 * @param firstname to get list
	 * @return
	 */
	public abstract List<com.enseirb.telecom.dngroup.dvd2c.model.User> findUserByNameOnServer(String firstname);

	/**
	 *  create user on server and after in local
	 * @param user to create
	 * @return the user
	 * @throws SuchUserException 
	 * @throws IOException 
	 */
	public abstract User createUserOnServer(com.enseirb.telecom.dngroup.dvd2c.model.User user) throws SuchUserException, IOException;
	
	/**
	 *  create user on local
	 * @param user to create
	 * @return the user
	 */
	public abstract User createUserOnLocal(User user);

	/**
	 * modify user on server and on local
	 * @param user to modify
	 * @throws NoSuchUserException 
	 */
	public abstract void saveUserOnServer(com.enseirb.telecom.dngroup.dvd2c.model.User user) throws NoSuchUserException;
	
	/**
	 * modify user on local
	 * @param user to modify
	 */
	public abstract void saveUserOnLocal(User user);
	
	/**
	 * delete a user on server and on local
	 * @param userID the user to delete
	 * @throws NoSuchUserException 
	 * @throws IOException 
	 */
	public abstract void deleteUserOnServer(UUID userID) throws IOException, NoSuchUserException;
	
	/**
	 * delete a user on local
	 * @param userID the user to delete
	 */
	public abstract void deleteUserOnLocal(UUID userID);
	
//	/**
//	 * Get the list of users who have the same firstname on local
//	 * SERVER SERVICES
//	 * @param firstname the firstname to search
//	 * @return a list of user
//	 */
//	public abstract List<User> getUserFromName(String firstname);
//
//	/**
//	 * Get the list of users who have the same box on local
//	 * SERVER SERVICES
//	 * @param boxID the firstname to search
//	 * @return a list of user
//	 */
//	public abstract List<User> getUserFromBoxID(String boxID);
//	
//	/**
//	 * Get the box of a user on local
//	 * SERVER SERVICES
//	 * @param userID to get a box
//	 * @return a box with lot information
//	 * @throws NoSuchUserException 
//	 */
//	public abstract Box getBox(String userID) throws NoSuchUserException;
//
//	/**
//	 * get all user of boxes
//	 * @param listBox the all box to get users
//	 * @return a list of user
//	 */
//	public abstract List<User> getUsersFromBoxes(List<Box> listBox);
//
//	/**
//	 * get the list of user from a list of box
//	 * @param listBox the list of box to extract users
//	 * @return the list of user
//	 */
//	public abstract List<User> getUsersFromListBoxes(List<Box> listBox);


	/**
	 * Verification if the password is equal 
	 * 
	 * @param userName to get a true password
	 * @param password the password to test
	 * @return true if is equal
	 * @throws NoSuchUserException 
	 */
	public abstract boolean getUserVerification(String userName, String password) throws NoSuchUserException;

	public abstract User getContactInformation(UUID userID) throws NoSuchUserException;
	

	public abstract List<Property> getPropertiesForUser(final UUID userId,final String propertyGroupName);
	public abstract void setPropertiesForUser(final UUID userId,final String propertyGroupName, PropertyGroups propertyGroups);


}