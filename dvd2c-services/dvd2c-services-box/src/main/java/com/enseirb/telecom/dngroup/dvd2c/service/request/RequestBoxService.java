package com.enseirb.telecom.dngroup.dvd2c.service.request;

import java.io.IOException;

import javax.ws.rs.ProcessingException;

import com.enseirb.telecom.dngroup.dvd2c.exception.NoSuchBoxException;
import com.enseirb.telecom.dngroup.dvd2c.exception.SuchBoxException;
import com.enseirb.telecom.dngroup.dvd2c.model.Box;


public interface RequestBoxService {

	
	/**
	 * get a box on remote host
	 * @param UserID the user to get
	 * @return user of userID
	 * @throws IOException host is not reachable
	 * @throws NoSuchBoxException user doesn't exist on remote host 
	 */
	public abstract Box get(String boxID) throws IOException, NoSuchBoxException;

	/**
	 * post a box on remote host
	 * @param user the user to post
	 * @throws IOException host is not reachable
	 */
	public abstract void createBoxORH(Box box) throws IOException,ProcessingException;

	/**
	 * update a box on remote host
	 * @param user the user to update
	 * @throws IOException host is not reachable
	 * @throws NoSuchBoxException user doesn't exist on remote host 
	 */
	public abstract void updateBoxORH(Box box) throws IOException, NoSuchBoxException;
	
	
	/**
	 * delete a box on remote host
	 * @param boxID the box to delete
	 * @throws IOException host is not reachable
	 * @throws NoSuchBoxException box not found on remote host
	 */
	public abstract void deleteBoxORH(String boxID) throws IOException, NoSuchBoxException;

	public abstract void sendOauthORH(String actorID, Box box, String code) throws IOException;

	
}
