package com.enseirb.telecom.dngroup.dvd2c.service;

import com.enseirb.telecom.dngroup.dvd2c.model.Box;
import com.enseirb.telecom.dngroup.dvd2c.model.ListBox;

public interface BoxService {

	/**
	 * Verify if box exist on server
	 * @param boxID the box to verify
	 * @return true if exist
	 */
	public abstract boolean boxExistOnServer(String boxID);

	/**
	 * Verify if box exist on local
	 * @param boxID the box to verify
	 * @return true if exist
	 */
	public abstract boolean boxExistOnLocal(String boxID);

	//	public abstract Box getBoxOnServer(String boxID);

	/**
	 * Get a box by a boxID
	 * @param boxID the box to get
	 * @return the box (null if don't exist)
	 */
	public abstract Box getBoxOnLocal(String boxID);

	/**
	 * create a new box on server
	 * @param box the box to create
	 * @return the box
	 */
	public abstract Box createBoxOnServer(Box box);

	//	public abstract Box createBoxOnLocal(Box box);
	/**
	 * update the box on server and on local
	 * @param box the box to update the boxID need to be the same)
	 */
	public abstract void saveBoxOnServer(Box box);
	
	/**
	 * update the box on local
	 * @param box the box to update the boxID need to be the same)
	 */
	public abstract void saveBoxOnLocal(Box box);

	/**
	 * delete a box on server and on local
	 * @param boxID the boxid of the box to delete
	 */
	public abstract void deleteBoxOnServer(String boxID);

	/**
	 * delete a box on local
	 * @param boxID the boxid of the box to delete
	 */
	public abstract void deleteBoxOnLocal(String boxID);

	/**
	 * get list of box with ip
	 * SERVER SERVICE
	 * @param ip the ip to get user
	 * @return a user list
	 */
	public abstract ListBox getBoxListFromIP(String ip);

	/**
	 * get all box on local db
	 * @return all box
	 */
	public abstract ListBox getAllBox();

	/**
	 * Get all box with a ip of @param
	 * @param ip the ip to get the list of box
	 * @return
	 */
	public abstract ListBox getBoxesFromIP(String ip);

	

	//	public abstract Box getBox(String userID);
}
