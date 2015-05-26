package com.enseirb.telecom.dngroup.dvd2c.service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import com.enseirb.telecom.dngroup.dvd2c.exception.NoRelationException;
import com.enseirb.telecom.dngroup.dvd2c.exception.NoRoleException;
import com.enseirb.telecom.dngroup.dvd2c.exception.NoSuchBoxException;
import com.enseirb.telecom.dngroup.dvd2c.exception.NoSuchContactException;
import com.enseirb.telecom.dngroup.dvd2c.exception.NoSuchUserException;
import com.enseirb.telecom.dngroup.dvd2c.model.ContactXSD;
import com.enseirb.telecom.dngroup.dvd2c.model.Content;

public interface RelationService {

	/**
	 * verify if the relation exist
	 * @param userUUID the main relation
	 * @param relationUUID the id of relation
	 * @return true if is exist
	 */
	public abstract boolean RelationExist(UUID userUUID, UUID relationUUID);

	/**
	 * get relation (and the status)
	 * @param userUUID the main relation
	 * @param relationUUID the id of relation
	 * @return the relation
	 * @throws NoSuchContactException 
	 */
	public abstract ContactXSD getRelation(UUID userUUID, UUID relationUUID) throws NoSuchContactException;

	/**
	 * get the list of relation from a user
	 * @param userUUID to get relation
	 * @return the list of relation
	 */
	public abstract List<ContactXSD> getListContact(UUID userUUID);

//	/**
//	 * get the list of relation of a group from a user
//	 * @param userUUID to get relation
//	 * @param roleID the group to get
//	 * @return the list of relation
//	 */
//	public abstract List<ContactXSD> getListRelation(String userUUID, Role roleID);

	/**
	 * get all content of the first user to the seconde user
	 * @param userUUIDToGet the first user to get content
	 * @param userUUIDForm the second user
	 * @return a list of content
	 */
	public abstract List<Content> getAllContent(UUID userUUIDToGet, UUID userUUIDForm);

	/**
	 *  create a relation between userUUID and the relation
	 *  and send a request to create the relation on the other box
	 * @param userUUID of the box
	 * @param relationUUID the second user
	 * @param fromBox if the request is from the box
	 * @return the relation
	 * @throws NoSuchUserException
	 * @throws IOException 
	 * @throws NoSuchBoxException 
	 */
	public abstract ContactXSD createRelation(UUID userUUID, UUID relationUUID,
			Boolean fromBox) throws NoSuchUserException, IOException, NoSuchBoxException;

	

	/**
	 * update a relation between the user 
	 * this service verify the status
	 * @param userUUID of the box
	 * @param relation the other
	 * @throws NoRelationException 
	 * @throws NoSuchUserException 
	 * @throws NoRoleException 
	 */
	public abstract void saveRelation(UUID userUUID, ContactXSD relation) throws NoRelationException, NoSuchUserException, NoRoleException;

	/**
	 * delete a relation 
	 * @param userUUID the user of the box
	 * @param relationUUID the other user
	 * @throws NoSuchUserException 
	 * @throws NoSuchBoxException 
	 * @throws NoRelationException 
	 */
	public abstract void deleteRelation(UUID userUUID, UUID relationUUID) throws NoSuchUserException, NoSuchBoxException, NoRelationException;

	

	/**
	 * Update relation list for metaData send getMe to all relation
	 * @param userUUID the relation to update metaData
	 * @throws IOException if the request have a problem
	 * @throws NoSuchUserException if the user is not found
	 */
	public abstract void updateRelation(UUID userUUID) throws IOException,
			NoSuchUserException;

	/**
	 *  Set approve relation to pass to not arpouve to arpouve or ask to aprouve
	 * @param userUUID the local user
	 * @param relationUUID
	 */
	public abstract void setAprouveBox(UUID userUUID, UUID relationUUID);

	/**
	 * delete a relation on the other box
	 * @param userUUID
	 * @param relationUUID
	 * @throws NoRelationException 
	 */
	public abstract void deleteRelationBox(UUID userUUID, UUID relationUUID) throws NoRelationException;

	
}
