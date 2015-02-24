package com.enseirb.telecom.dngroup.dvd2c.service.request;

import java.io.IOException;

import com.enseirb.telecom.dngroup.dvd2c.exception.NoSuchBoxException;
import com.enseirb.telecom.dngroup.dvd2c.exception.NoSuchUserException;
import com.enseirb.telecom.dngroup.dvd2c.model.Relation;
import com.enseirb.telecom.dngroup.dvd2c.model.User;

public interface RequestRelationService {

    /**
     * get a Relation on remote host
     * 
     * @param UserID
     *            the userID
     * @param UserToGet
     * @return user of userID
     * @throws IOException
     *             host is not reachable
     * @throws NoSuchUserException
     *             user doesn't exist on remote host
     * @throws NoSuchBoxException
     */
    public abstract User get(String UserID, String UserToGet) throws IOException, NoSuchUserException, NoSuchBoxException;

    /**
     * close the connection
     */
    public abstract void close();

    /**
     * Create a relation on the remote host
     * @param relationOfRequest the user of the origin 
     * @param relationToRequest the user to send the request
     * @throws IOException host is not reachable
     * @throws NoSuchBoxException no box found
     */
    public abstract void updateRelationORH(Relation relationOfRequest, Relation relationToRequest) throws IOException, NoSuchBoxException;

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
    public abstract void deleteRelationORH(String relationOfRequest, String relationToRequest) throws IOException, NoSuchUserException,
	    NoSuchBoxException;

    /**
     * Set approve a relation on remote host
     * @param userID this user has accept relation
     * @param userIDOfRelationOfRelation the user to request
     * @throws NoSuchBoxException 
     * @throws IOException 
     * @throws NoSuchUserException 
     */
    public abstract void setAprouveRelationORH(String userID, String userIDOfRelationOfRelation) throws IOException, NoSuchBoxException, NoSuchUserException;
}
