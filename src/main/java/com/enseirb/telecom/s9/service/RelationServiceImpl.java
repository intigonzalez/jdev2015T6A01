package com.enseirb.telecom.s9.service;

import java.io.IOException;
import java.util.Iterator;

import com.enseirb.telecom.s9.Box;
import com.enseirb.telecom.s9.ListContent;
import com.enseirb.telecom.s9.ListRelation;
import com.enseirb.telecom.s9.Relation;
import com.enseirb.telecom.s9.User;
import com.enseirb.telecom.s9.db.CrudRepository;
import com.enseirb.telecom.s9.db.RelationshipRepositoryInterface;
import com.enseirb.telecom.s9.db.RelationshipRepositoryObject;
import com.enseirb.telecom.s9.db.UserRepositoryObject;
import com.enseirb.telecom.s9.exception.NoRelationException;
import com.enseirb.telecom.s9.exception.NoSuchBoxException;
import com.enseirb.telecom.s9.exception.NoSuchUserException;
import com.enseirb.telecom.s9.request.RequestBoxServiceImpl;
import com.enseirb.telecom.s9.request.RequestContentService;
import com.enseirb.telecom.s9.request.RequestContentServiceImpl;
import com.enseirb.telecom.s9.request.RequestRelationService;
import com.enseirb.telecom.s9.request.RequestRelationServiceImpl;
import com.enseirb.telecom.s9.request.RequestUserService;
import com.enseirb.telecom.s9.request.RequestUserServiceImpl;
import com.sun.research.ws.wadl.Request;

public class RelationServiceImpl implements RelationService {

	RelationshipRepositoryInterface relationshipDatabase;
	CrudRepository<UserRepositoryObject, String> userDatabase;
	RequestContentService requestContentService = new RequestContentServiceImpl(
			"http://localhost:9998/api/app/");

	public RelationServiceImpl(
			RelationshipRepositoryInterface RelationshipDatabase,
			CrudRepository<UserRepositoryObject, String> userDatabase) {
		this.relationshipDatabase = RelationshipDatabase;
		this.userDatabase = userDatabase;
	}

	public boolean RelationExist(String UserID, String email) {
		return relationshipDatabase.exists(UserID, email);
	}

	@Override
	public void updateRelation(String userID) throws IOException,
			NoSuchUserException {

		RequestRelationService rrs = new RequestRelationServiceImpl();
		RequestUserService requestServ = new RequestUserServiceImpl(
				"http://localhost:9999/api/app/");

		for (RelationshipRepositoryObject rro : relationshipDatabase.findAll()) {

			if (rro.getUserId().equals(userID)) {

				System.out.println(rro.getEmail());

				try {
					Box boxRelation = requestServ.getBox(rro.getEmail());

					User relationUpdate = rrs.get("http://"
							+ boxRelation.getIp() + "/api/app/"
							+ rro.getEmail() + "/relation/from/" + userID);
					Relation relationIntoDb = relationshipDatabase.findOne(
							userID, relationUpdate.getUserID()).toRelation();
					relationIntoDb.setName(relationUpdate.getName());
					relationIntoDb.setSurname(relationUpdate.getSurname());
					relationshipDatabase.save(new RelationshipRepositoryObject(
							userID, relationIntoDb));
				} catch (NoSuchBoxException e) {
					System.out.println("All users should have a box, ignoring");
				}

			}
		}
		rrs.close();
	}

	@Override
	public Relation getRelation(String userID, String email) {
		RelationshipRepositoryObject relation = relationshipDatabase.findOne(
				userID, email);
		if (relation == null) {
			return null;
		} else {
			return relation.toRelation();
		}
	}

	@Override
	public User getMe(String userID) {
		UserRepositoryObject user = userDatabase.findOne(userID);

		if (user == null) {
			return null;
		} else {
			User userComplet = user.toUser();
			User userReturn = new User();
			userReturn.setName(userComplet.getName());
			userReturn.setSurname(userComplet.getSurname());
			userReturn.setUserID(userComplet.getUserID());
			return userReturn;
		}

	}

	@Override
	public ListRelation getListRelation(String userID) {
		ListRelation listRelation = new ListRelation();
		Iterable<RelationshipRepositoryObject> relation = relationshipDatabase
				.findAll();
		Iterator<RelationshipRepositoryObject> itr = relation.iterator();
		while (itr.hasNext()) {
			RelationshipRepositoryObject relationshipRepositoryObject = itr
					.next();
			if (relationshipRepositoryObject.getUserId().equals(userID)) {
				listRelation.getRelation().add(
						relationshipRepositoryObject.toRelation());
			}
		}
		return listRelation;
	}

	@Override
	public Relation createRelation(String userID, Relation relation)
			throws NoSuchUserException {
		/*
		 * TODO(0) : Check the content, add a state 1 to the approuve value
		 * TODO(1) : Check the user really exists from the central server
		 * TODO(1) : Send a request to the right box to say it add as a friend
		 */
		relation.setAprouve(1);

		// Prepare the relation for the "UserAsked"
		UserRepositoryObject uro = userDatabase.findOne(userID);
		if (uro != null) {
			User userWhoAsked = userDatabase.findOne(userID).toUser();
			Relation relation2 = new Relation();
			relation2.setEmail(userWhoAsked.getUserID());
			relation2.setName(userWhoAsked.getName());
			relation2.setSurname(userWhoAsked.getSurname());
			relation2.setPubKey(userWhoAsked.getPubKey());
			relation2.setAprouve(2);
			relation2.setUnixTime(relation.getUnixTime());

			if (userDatabase.exists(relation.getEmail())) {
				relationshipDatabase.save(new RelationshipRepositoryObject(
						relation.getEmail(), relation2));
			} else {
				// Send a request to the right box with the profile of userID
			}
			return relationshipDatabase.save(
					new RelationshipRepositoryObject(userID, relation))
					.toRelation();
		} else {
			throw new NoSuchUserException();
		}

	}

	@Override
	public void createDefaultRelation(String userIDFromPath,
			String relationIDString) throws NoSuchUserException {
		Relation relation = new Relation();
		relation.setEmail(relationIDString);
		relation.setAprouve(1);
		relation.getGroupID().add(0);
		createRelation(userIDFromPath, relation);

	}

	@Override
	public void saveRelation(String userID, Relation relation) {
		// Here, the user is only allowed to edit the approuve value if the
		// current value is = 2
		Relation relationIntoDb = relationshipDatabase.findOne(userID,
				relation.getEmail()).toRelation();
		if (relationIntoDb.getAprouve() != relation.getAprouve()
				&& relationIntoDb.getAprouve() == 2
				&& relation.getAprouve() == 3) {
			relationIntoDb.setAprouve(3);

			if (userDatabase.exists(relation.getEmail())) {
				Relation relation2 = relationshipDatabase.findOne(
						relation.getEmail(), userID).toRelation();
				relation2.setAprouve(3);
				relationshipDatabase.save(new RelationshipRepositoryObject(
						relation.getEmail(), relation2));
			} else {
				// Send a request to the box to tell it the user accepts the
				// relationship
			}

		}
		// Or, the user can edit the group his/her relationshis is in.
		if (!relationIntoDb.getGroupID().equals(relation.getGroupID())) {
			relationIntoDb.getGroupID().clear();
			relationIntoDb.getGroupID().addAll(relation.getGroupID());
		}
		relationshipDatabase.save(new RelationshipRepositoryObject(userID,
				relationIntoDb));
		return;
	}

	@Override
	public void deleteRelation(String userID, String email) {
		if (userDatabase.exists(email)) {
			relationshipDatabase.delete(email, userID);
		} else {
			// Send a request to the right box
		}
		relationshipDatabase.delete(userID, email);

	}

	@Override
	public ListContent getAllContent(String userID, String relationID) {
		try {
			ListContent listContent = requestContentService.get(userID,
					relationID);
			return listContent;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchUserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoRelationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

}
