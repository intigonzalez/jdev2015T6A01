package com.enseirb.telecom.s9.service;

import com.enseirb.telecom.s9.Relation;
import com.enseirb.telecom.s9.User;
import com.enseirb.telecom.s9.db.CrudRepository;
import com.enseirb.telecom.s9.db.RelationshipRepositoryInterface;
import com.enseirb.telecom.s9.db.RelationshipRepositoryObject;
import com.enseirb.telecom.s9.db.UserRepositoryObject;

public class RelationServiceImpl implements RelationService {

	RelationshipRepositoryInterface relationshipDatabase;
	CrudRepository<UserRepositoryObject, String> userDatabase;
	
	public boolean RelationExist(String UserID,String email) {
		return relationshipDatabase.exists(UserID, email);
	}
	
	@Override
	public Relation getRelation(String userID, String email) {
		RelationshipRepositoryObject relation = relationshipDatabase.findOne(userID, email);
		if (relation == null ) {
			return null;
		}
		else {
			return relation.toRelation();
		}
	}
	@Override
	public Relation createRelation(String userID, Relation relation) {
		/*
		 * TODO(0) : Check the content, add a state  1 to the approuve value
		 * TODO(1) : Check the user really exists from the central server
		 * TODO(1) : Send a request to the right box to say it add as a friend 
		 */
		relation.setAprouve(1);
		
		// Prepare the relation for the "UserAsked" 
		User userWhoAsked = userDatabase.findOne(userID).toUser();
		Relation relation2 = new Relation();
		relation2.setEmail(userWhoAsked.getUserID());
		relation2.setName(userWhoAsked.getName());
		relation2.setSurname(userWhoAsked.getSurname());
		relation2.setPubKey(userWhoAsked.getPubKey());
		relation2.setAprouve(2);
		relation2.setUnixTime(relation.getUnixTime());
		
		if ( userDatabase.exists(relation.getEmail()) ) {
			relationshipDatabase.save(new RelationshipRepositoryObject(relation.getEmail(),relation2));
		}
		else {
			//Send a request to the right box with the profile of userID
		}
		return relationshipDatabase.save(new RelationshipRepositoryObject(userID, relation)).toRelation(); 

	}
	@Override
	public void saveRelation(String userID,Relation relation) {
		// Here, the user is only allowed to edit the approuve value if the current value is = 2
		Relation relationIntoDb = relationshipDatabase.findOne(userID, relation.getEmail()).toRelation();
		if ( relationIntoDb.getAprouve() != relation.getAprouve() && relationIntoDb.getAprouve() == 2 && relation.getAprouve() == 3) {
			relationIntoDb.setAprouve(3);
			
			if ( userDatabase.exists(relation.getEmail()) ) {
				Relation relation2 = relationshipDatabase.findOne(relation.getEmail(), userID).toRelation();
				relation2.setAprouve(3);
				relationshipDatabase.save(new RelationshipRepositoryObject(relation.getEmail(), relation2));
			}
			else {
				//Send a request to the box to tell it the user accepts the relationship
			}
			
		}
		// Or, the user can edit the group his/her relationshis is in. 
		if ( !relationIntoDb.getGroupID().equals(relation.getGroupID())) {
			relationIntoDb.getGroupID().clear();
			relationIntoDb.getGroupID().addAll(relation.getGroupID());
		}
		 relationshipDatabase.save(new RelationshipRepositoryObject(userID,relationIntoDb));
		return;
	}
	public RelationServiceImpl(RelationshipRepositoryInterface RelationshipDatabase, CrudRepository<UserRepositoryObject, String> userDatabase ) {
		this.relationshipDatabase = RelationshipDatabase;
		this.userDatabase = userDatabase;
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
		if ( userDatabase.exists(email) ) {
			relationshipDatabase.delete(email, userID);
		}
		else {
			//Send a request to the right box
		}
		relationshipDatabase.delete(userID, email);
		
	}

	}
