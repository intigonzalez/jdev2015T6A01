package com.enseirb.telecom.s9.request;

import java.io.IOException;

import com.enseirb.telecom.s9.Relation;
import com.enseirb.telecom.s9.User;
import com.enseirb.telecom.s9.exception.NoSuchBoxException;
import com.enseirb.telecom.s9.exception.NoSuchUserException;

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

    public abstract void close();

    public abstract void postRelation(Relation relation, Relation relation2) throws IOException, NoSuchBoxException;

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
    public abstract void delete(String relationOfRequest, String relationToRequest) throws IOException, NoSuchUserException,
	    NoSuchBoxException;

    /**
     * 
     * @param userID this user has accept relation
     * @param emailOfRelation the user to request
     * @throws NoSuchBoxException 
     * @throws IOException 
     * @throws NoSuchUserException 
     */
    public abstract void setAprouve(String userID, String emailOfRelation) throws IOException, NoSuchBoxException, NoSuchUserException;
}
