package com.enseirb.telecom.s9.service;

import java.io.IOException;

import com.enseirb.telecom.s9.ListContent;
import com.enseirb.telecom.s9.ListRelation;
import com.enseirb.telecom.s9.Relation;
import com.enseirb.telecom.s9.User;
import com.enseirb.telecom.s9.exception.NoSuchUserException;

public interface RelationService {

	public abstract boolean RelationExist(String UserID, String email);

	public abstract Relation getRelation(String userID, String email);

	public abstract ListRelation getListRelation(String userID);

	public abstract ListRelation getListRelation(String userID, int groupID);

	public abstract ListContent getAllContent(String userIDToGet, String userIDForm);

	public abstract Relation createRelation(String userID, Relation relation, Boolean fromBox)
			throws NoSuchUserException;

	public abstract void saveRelation(String userID, Relation relation);

	public abstract void deleteRelation(String userID, String email);

	public abstract void createDefaultRelation(String userIDFromPath, String relationIDString,
			Boolean fromBox) throws NoSuchUserException;

	public abstract User getMe(String userIDFromPath);

	public abstract void updateRelation(String userIDFromPath) throws IOException,
			NoSuchUserException;

	public abstract void setAprouveBox(String userId, String relationId);

	public abstract void deleteRelationBox(String userId, String relationId);
}
