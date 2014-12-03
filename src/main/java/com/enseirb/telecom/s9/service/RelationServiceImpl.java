package com.enseirb.telecom.s9.service;

import com.enseirb.telecom.s9.Relation;
import com.enseirb.telecom.s9.db.CrudRepository;
import com.enseirb.telecom.s9.db.RelationshipRepositoryMongo;
import com.enseirb.telecom.s9.db.RelationshipRepositoryObject;

public class RelationServiceImpl implements RelationService {

	CrudRepository<RelationshipRepositoryObject, String> relationshipDatabase;
	
	public boolean RelationExist(String UserID,String email) {
		return ((RelationshipRepositoryMongo)relationshipDatabase).exists(UserID, email);
	}
	
	@Override
	public Relation getRelation(String userID, String email) {
		RelationshipRepositoryObject relation = ((RelationshipRepositoryMongo)relationshipDatabase).findOne(userID, email);
		if (relation == null ) {
			return null;
		}
		else {
			return relation.toRelation();
		}
	}
	@Override
	public Relation createRelation(String userID, Relation relation) {
		return relationshipDatabase.save(new RelationshipRepositoryObject(relation)).toRelation(); 

	}
	@Override
	public void saveRelation(String userID,Relation relation) {
		 relationshipDatabase.save(new RelationshipRepositoryObject(userID,relation));
		return;
	}
	public RelationServiceImpl(CrudRepository<RelationshipRepositoryObject, String> RelationshipDatabase) {
	//	this.userDatabase = userDatabase;
		this.relationshipDatabase = RelationshipDatabase;
	}
		/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enseirb.telecom.s9.service.AccountService#userExist(java.lang.String)
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enseirb.telecom.s9.service.AccountService#getUser(java.lang.String)
	 */

	@Override
	public void deleteRelation(String userID, String email) {
		((RelationshipRepositoryMongo)relationshipDatabase).delete(userID, email);
		
	}

	}
