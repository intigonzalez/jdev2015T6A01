package com.enseirb.telecom.dngroup.dvd2c.request;

import java.io.IOException;
import java.util.UUID;

import com.enseirb.telecom.dngroup.dvd2c.exception.NoSuchBoxException;
import com.enseirb.telecom.dngroup.dvd2c.exception.NoSuchUserException;
import com.enseirb.telecom.dngroup.dvd2c.model.Contact;
import com.enseirb.telecom.dngroup.dvd2c.model.User;

public interface RequestRelationService {

	/**
	 * get a Relation on remote host
	 * 
	 * @param UserID
	 *            the user taht performs the request
	 * @param UserToGet
	 *            the user for which we want to have the details
	 * @return user that has a relation with provided UserId
	 * @throws IOException
	 *             host is not reachable
	 * @throws NoSuchUserException
	 *             user doesn't exist on remote host
	 * @throws NoSuchBoxException
	 *             if the user doesn't have a box on the central server
	 */
	public abstract User get(UUID UserID, UUID UserToGet) throws IOException,
			NoSuchUserException, NoSuchBoxException;

	/**
	 * Create a relation on the remote host
	 * 
	 * @param relationOfRequest
	 *            the user of the origin
	 * @param relationToRequest
	 *            the user to send the request
	 * @throws IOException
	 *             host is not reachable
	 * @throws NoSuchBoxException
	 *             no box found
	 */
	public abstract void updateRelationORH(Contact relationOfRequest,
			UUID relationID) throws IOException, NoSuchBoxException;

	/**
	 * delete a Relation on remote host
	 * 
	 * @param relationOfRequest
	 *            the relation to delete
	 * @param relationToRequest
	 *            the user to send the request
	 * @throws IOException
	 *             host is not reachable
	 * @throws NoSuchUserException
	 *             user doesn't exist on remote host
	 */
	public abstract void deleteRelationORH(UUID relationOfRequest,
			UUID relationToRequest) throws IOException, NoSuchUserException,
			NoSuchBoxException;

	/**
	 * Set approve a relation on remote host
	 * 
	 * @param userID
	 *            this user has accept relation
	 * @param actorIDOfRelation
	 *            the user to request
	 * @throws NoSuchBoxException
	 * @throws IOException
	 * @throws NoSuchUserException
	 */
	public abstract void setAprouveRelationORH(UUID userID,
			UUID actorIDOfRelation) throws IOException, NoSuchBoxException,
			NoSuchUserException;

	// /**
	// * get user information from userservice
	// * @param relationIDString
	// * @return
	// */
	// public abstract User getUserLocal(String relationIDString);
}
