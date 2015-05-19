package com.enseirb.telecom.dngroup.dvd2c.service;

import java.io.IOException;
import java.util.List;

import com.enseirb.telecom.dngroup.dvd2c.exception.NoSuchBoxException;
import com.enseirb.telecom.dngroup.dvd2c.exception.NoSuchUserException;
import com.enseirb.telecom.dngroup.dvd2c.exception.SuchBoxException;
import com.enseirb.telecom.dngroup.dvd2c.exception.SuchUserException;
import com.enseirb.telecom.dngroup.dvd2c.model.Box;
import com.enseirb.telecom.dngroup.dvd2c.model.User;

public interface CentralService {

	/**
	 * Verify if box exist on server
	 * 
	 * @param boxID
	 *            the box to verify
	 * @return true if exist
	 */
	public abstract boolean boxExistOnServer(String boxID);

	/**
	 * Verify if box exist on local
	 * 
	 * @param boxID
	 *            the box to verify
	 * @return true if exist
	 */
	public abstract boolean boxExistOnLocal(String boxID);


	/**
	 * Get a box by a boxID
	 * 
	 * @param boxID
	 *            the box to get
	 * @return the box (null if don't exist)
	 * @throws NoSuchBoxException
	 */
	public abstract Box getBoxOnLocal(String boxID) throws NoSuchBoxException;

	/**
	 * create a new box on server
	 * 
	 * @param box
	 *            the box to create
	 * @return the box
	 * @throws SuchBoxException
	 */
	public abstract Box createBoxOnServer(Box box) throws SuchBoxException;

	/**
	 * create a new box on local
	 * 
	 * @param box
	 *            the box to create
	 * @return the box
	 */
	public abstract Box createBoxOnLocal(Box box);

	// public abstract Box createBoxOnLocal(Box box);
	/**
	 * update the box on server and on local
	 * 
	 * @param box
	 *            the box to update the boxID need to be the same)
	 */
	public abstract void saveBoxOnServer(Box box);

	/**
	 * update the box on local
	 * 
	 * @param box
	 *            the box to update the boxID need to be the same)
	 */
	public abstract void saveBoxOnLocal(Box box);

	/**
	 * delete a box on server and on local
	 * 
	 * @param boxID
	 *            the boxid of the box to delete
	 */
	public abstract void deleteBoxOnServer(String boxID);

	/**
	 * delete a box on local
	 * 
	 * @param boxID
	 *            the boxid of the box to delete
	 */
	public abstract void deleteBoxOnLocal(String boxID);

	/**
	 * get list of box with ip SERVER SERVICE
	 * 
	 * @param ip
	 *            the ip to get user
	 * @return a user list
	 */
	public abstract List<Box> getBoxListFromIP(String ip);

	/**
	 * get all box on local db
	 * 
	 * @return all box
	 */
	public abstract List<Box> getAllBox();

	/**
	 * Get all box with a ip of @param
	 * 
	 * @param ip
	 *            the ip to get the list of box
	 * @return
	 */
	public abstract List<Box> getBoxesFromIP(String ip);

	/**
	 * update box with value on parameter
	 */
	public abstract void updateBox();

	/**
	 * Get the list of users who have the same firstname on local SERVER
	 * SERVICES
	 * 
	 * @param firstname
	 *            the firstname to search
	 * @return a list of user
	 */
	public abstract List<User> getUserFromName(String firstname);

	/**
	 * Get the list of users who have the same box on local SERVER SERVICES
	 * 
	 * @param boxID
	 *            the firstname to search
	 * @return a list of user
	 */
	public abstract List<User> getUserFromBoxID(String boxID);

	/**
	 * Get the box of a user on local SERVER SERVICES
	 * 
	 * @param userID
	 *            to get a box
	 * @return a box with lot information
	 * @throws NoSuchUserException
	 */
	public abstract Box getBox(String userID) throws NoSuchUserException;

	/**
	 * get all user of boxes
	 * 
	 * @param listBox
	 *            the all box to get users
	 * @return a list of user
	 */
	public abstract List<User> getUsersFromBoxes(List<Box> listBox);

	/**
	 * get the list of user from a list of box
	 * 
	 * @param listBox
	 *            the list of box to extract users
	 * @return the list of user
	 */
	public abstract List<User> getUsersFromListBoxes(List<Box> listBox);

	/**
	 * Verify if the user exist on local
	 * 
	 * @param userID
	 *            to verify
	 * @return return true if the user exist
	 */
	public abstract boolean userExistOnLocal(String userID);

	/**
	 * get a user from local
	 * 
	 * @param userID
	 *            the user to get
	 * @return the user
	 * @throws NoSuchUserException
	 */
	public abstract User getUserOnLocal(String userID)
			throws NoSuchUserException;

	/**
	 * create user on local
	 * 
	 * @param user
	 *            to create
	 * @return the user
	 */
	public abstract User createUserOnLocal(User user);

	/**
	 * modify user on local
	 * 
	 * @param user
	 *            to modify
	 */
	public abstract void saveUserOnLocal(User user);

	/**
	 * delete a user on local
	 * 
	 * @param userID
	 *            the user to delete
	 */
	public abstract void deleteUserOnLocal(String userID);

	public abstract void sendGoogleCode(String actorID, Box box, String code)
			throws IOException;

}
