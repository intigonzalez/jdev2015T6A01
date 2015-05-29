package com.enseirb.telecom.dngroup.dvd2c.service;

import java.io.IOException;

import com.enseirb.telecom.dngroup.dvd2c.exception.NoSuchBoxException;
import com.enseirb.telecom.dngroup.dvd2c.exception.SuchBoxException;
import com.enseirb.telecom.dngroup.dvd2c.model.Box;

public interface BoxService {

	/**
	 * Verify if box exist on server
	 * @param boxID the box to verify
	 * @return true if exist
	 */
	public abstract boolean boxExistOnServer(String boxID);

	

	//	public abstract Box getBoxOnServer(String boxID);

	/**
	 * create a new box on server
	 * @param box the box to create
	 * @return the box
	 * @throws SuchBoxException 
	 * @throws IOException 
	 */
	public abstract Box createBoxOnServer(Box box) throws SuchBoxException, IOException;

	//	public abstract Box createBoxOnLocal(Box box);
	/**
	 * update the box on server and on local
	 * @param box the box to update the boxID need to be the same)
	 */
	public abstract void saveBoxOnServer(Box box) throws NoSuchBoxException;
	
	/**
	 * delete a box on server and on local
	 * @param boxID the boxid of the box to delete
	 */
	public abstract void deleteBoxOnServer(String boxID);

	/**
	 * update box with value on parameter
	 * @throws IOException 
	 */
	public abstract void updateBox() throws IOException;

	

	//	public abstract Box getBox(String userID);
}
