package com.enseirb.telecom.dngroup.dvd2c.service;

import java.io.IOException;
import java.util.List;

import com.enseirb.telecom.dngroup.dvd2c.exception.NoRelationException;
import com.enseirb.telecom.dngroup.dvd2c.exception.NoSuchBoxException;
import com.enseirb.telecom.dngroup.dvd2c.exception.NoSuchContactException;
import com.enseirb.telecom.dngroup.dvd2c.exception.NoSuchUserException;
import com.enseirb.telecom.dngroup.dvd2c.model.ContactXSD;
import com.enseirb.telecom.dngroup.dvd2c.model.Content;
import com.enseirb.telecom.dngroup.dvd2c.model.User;

public interface RelationService {

	/**
	 * verify if the relation exist
	 * @param userID the main relation
	 * @param relationID the id of relation
	 * @return true if is exist
	 */
	public abstract boolean RelationExist(String userID, String relationID);

	/**
	 * get relation (and the status)
	 * @param userID the main relation
	 * @param relationID the id of relation
	 * @return the relation
	 * @throws NoSuchContactException 
	 */
	public abstract ContactXSD getRelation(String userID, String relationID) throws NoSuchContactException;

	/**
	 * get the list of relation from a user
	 * @param userID to get relation
	 * @return the list of relation
	 */
	public abstract List<ContactXSD> getListContact(String userID);

//	/**
//	 * get the list of relation of a group from a user
//	 * @param userID to get relation
//	 * @param roleID the group to get
//	 * @return the list of relation
//	 */
//	public abstract List<ContactXSD> getListRelation(String userID, Role roleID);

	/**
	 * get all content of the first user to the seconde user
	 * @param userIDToGet the first user to get content
	 * @param userIDForm the second user
	 * @return a list of content
	 */
	public abstract List<Content> getAllContent(String userIDToGet, String userIDForm);

	/**
	 *  create a relation between userID and the relation
	 *  and send a request to create the relation on the other box
	 * @param userID of the box
	 * @param relationID the second user
	 * @param fromBox if the request is from the box
	 * @return the relation
	 * @throws NoSuchUserException
	 * @throws IOException 
	 * @throws NoSuchBoxException 
	 */
	public abstract ContactXSD createRelation(String userID, String relationID,
			Boolean fromBox) throws NoSuchUserException, IOException, NoSuchBoxException;

	

	/**
	 * update a relation between the user 
	 * this service verify the status
	 * @param userID of the box
	 * @param relation the other
	 * @throws NoRelationException 
	 */
	public abstract void saveRelation(String userID, ContactXSD relation) throws NoRelationException;

	/**
	 * delete a relation 
	 * @param userID the user of the box
	 * @param relationID the other user
	 * @throws NoSuchUserException 
	 * @throws NoSuchBoxException 
	 * @throws NoRelationException 
	 */
	public abstract void deleteRelation(String userID, String relationID) throws NoSuchUserException, NoSuchBoxException, NoRelationException;

	/**
	 *  create a relation between userID and the relation
	 *  and send a request to create the relation on the other box
	 * @param userID of the box
	 * @param relationID the second user
	 * @param fromBox if the request is from the box
	 * @return the relation
	 * @throws NoSuchUserException
	 */
	public abstract void createDefaultRelation(String userIDFromPath, String relationID,
			Boolean fromBox) throws NoSuchUserException;

	//RBAC: GO TO USER SERVICE
//	/**
//	 * get the more information on this box for metaData of a other box
//	 * OTHER BOX SERVICE
//	 * @param userID
//	 * @return
//	 */
//	public abstract User getContactInformation(String userID);

	/**
	 * Update relation list for metaData send getMe to all relation
	 * @param userIDFromPath the relation to update metaData
	 * @throws IOException if the request have a problem
	 * @throws NoSuchUserException if the user is not found
	 */
	public abstract void updateRelation(String userIDFromPath) throws IOException,
			NoSuchUserException;

	/**
	 *  Set approve relation to pass to not arpouve to arpouve or ask to aprouve
	 * @param userId the local user
	 * @param relationId
	 */
	public abstract void setAprouveBox(String userId, String relationId);

	/**
	 * delete a relation on the other box
	 * @param userId
	 * @param relationId
	 * @throws NoRelationException 
	 */
	public abstract void deleteRelationBox(String userId, String relationId) throws NoRelationException;
}
