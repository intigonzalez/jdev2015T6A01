package com.enseirb.telecom.s9.service;


import java.util.List;

import com.enseirb.telecom.s9.ListContent;
import com.enseirb.telecom.s9.ListRelation;
import com.enseirb.telecom.s9.Relation;
import com.enseirb.telecom.s9.exception.NoSuchUserException;

public interface RelationService {
	
	public abstract boolean RelationExist(String UserID,String email);
	
	public abstract Relation getRelation(String userID, String email);

	public abstract Relation createRelation(String userID, Relation relation) throws NoSuchUserException;

	public abstract void saveRelation(String userID,Relation relation);
	
	public abstract void deleteRelation(String userID, String email);

	public abstract ListRelation getListRelation(String userID);

	public abstract ListContent getAllContent(String userIDToGet,String userIDForm);

	public abstract void createDefaultRelation(String userIDFromPath,
			String relationIDString) throws NoSuchUserException;
}
