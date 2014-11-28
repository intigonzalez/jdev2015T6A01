package com.enseirb.telecom.s9.service;


import com.enseirb.telecom.s9.Relation;

public interface RelationService {
	
	public abstract boolean RelationExist(String email);
	
	public abstract Relation getRelation(String email);

	public abstract Relation createRelation(Relation relation);

	public abstract void saveRelation(Relation relation, String userID);
	
	public abstract void deleteRelation(String email);
}
