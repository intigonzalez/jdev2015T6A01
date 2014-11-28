package com.enseirb.telecom.s9.service;

import com.enseirb.telecom.s9.Relation;
import com.enseirb.telecom.s9.db.CrudRepository;
import com.enseirb.telecom.s9.db.RelationshipRepositoryObject;
import com.enseirb.telecom.s9.db.UserRepositoryObject;

public class RelationServiceImpl implements RelationService {

	CrudRepository<RelationshipRepositoryObject, String> relationshipDatabase;
	
	public boolean RelationExist(String email) {
		return relationshipDatabase.exists(email);
	}
	
	@Override
	public Relation getRelation(String email) {
		RelationshipRepositoryObject relation = relationshipDatabase.findOne(email);
		if (relation == null ) {
			return null;
		}
		else {
			return relation.toRelation();
		}
	}
	@Override
	public Relation createRelation(Relation relation) {
		return relationshipDatabase.save(new RelationshipRepositoryObject(relation)).toRelation(); 

	}
	@Override
	public void saveRelation(Relation relation,String userID) {
		 relationshipDatabase.save(new RelationshipRepositoryObject(userID,relation));
		return;
	}
	@Override
	public void deleteRelation(String email) {
		relationshipDatabase.delete(email);	
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
	}
