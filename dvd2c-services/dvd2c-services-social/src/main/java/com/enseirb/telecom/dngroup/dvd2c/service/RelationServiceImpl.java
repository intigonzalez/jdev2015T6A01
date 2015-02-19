package com.enseirb.telecom.dngroup.dvd2c.service;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enseirb.telecom.dngroup.dvd2c.db.CrudRepository;
import com.enseirb.telecom.dngroup.dvd2c.db.RelationshipRepository;
import com.enseirb.telecom.dngroup.dvd2c.db.RelationshipRepositoryObject;
import com.enseirb.telecom.dngroup.dvd2c.db.UserRepositoryObject;
//import com.enseirb.telecom.dngroup.dvd2c.endpoints.RelationEndPoints;
import com.enseirb.telecom.dngroup.dvd2c.exception.NoRelationException;
import com.enseirb.telecom.dngroup.dvd2c.exception.NoSuchBoxException;
import com.enseirb.telecom.dngroup.dvd2c.exception.NoSuchUserException;
import com.enseirb.telecom.dngroup.dvd2c.model.ListContent;
import com.enseirb.telecom.dngroup.dvd2c.model.ListRelation;
import com.enseirb.telecom.dngroup.dvd2c.model.Relation;
import com.enseirb.telecom.dngroup.dvd2c.model.User;
import com.enseirb.telecom.dngroup.dvd2c.service.request.RequestContentService;
import com.enseirb.telecom.dngroup.dvd2c.service.request.RequestContentServiceImpl;
import com.enseirb.telecom.dngroup.dvd2c.service.request.RequestRelationService;
import com.enseirb.telecom.dngroup.dvd2c.service.request.RequestRelationServiceImpl;
import com.enseirb.telecom.dngroup.dvd2c.service.request.RequestUserService;
import com.enseirb.telecom.dngroup.dvd2c.service.request.RequestUserServiceImpl;

public class RelationServiceImpl implements RelationService {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(RelationServiceImpl.class);

	RelationshipRepository relationshipDatabase;
	CrudRepository<UserRepositoryObject, String> userDatabase;

	public RelationServiceImpl(RelationshipRepository RelationshipDatabase,
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

		for (RelationshipRepositoryObject rro : relationshipDatabase.findAll()) {

			if (rro.getUserId().equals(userID)) {

				LOGGER.debug("{}", rro.getEmail());

				try {
					// Box boxRelation = requestServ.getBox(rro.getEmail());

					User relationUpdate = rrs.get(userID, rro.getEmail());
					Relation relationIntoDb = relationshipDatabase.findOne(
							userID, relationUpdate.getUserID()).toRelation();
					relationIntoDb.setName(relationUpdate.getName());
					relationIntoDb.setSurname(relationUpdate.getSurname());
					relationshipDatabase.save(new RelationshipRepositoryObject(
							userID, relationIntoDb));
				} catch (NoSuchBoxException e) {
					LOGGER.warn("All users should have a box, ignoring");
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
	public ListRelation getListRelation(final String userID) {
		ListRelation listRelation = new ListRelation();
		Iterable<RelationshipRepositoryObject> relation = relationshipDatabase
				.findAll();
		Iterator<RelationshipRepositoryObject> itr = relation.iterator();
		while (itr.hasNext()) {
			RelationshipRepositoryObject relationshipRepositoryObject = itr
					.next();
			if (relationshipRepositoryObject.getUserId().equals(userID)) {
				// XXX: REWORK#1 won't compile

				listRelation.getRelation().add(
						relationshipRepositoryObject.toRelation());
			}
		}

		return listRelation;
	}

	@Override
	public ListRelation getListRelation(String userID, int groupID) {
		// XXX: won't compile since the following line doesn't compile
		// List<Integer> groupeIDs =
		// listRelation.getRelation().get(i).getGroupID();
		ListRelation listRelation = getListRelation(userID);
		ListRelation listRelation2 = new ListRelation();
		for (int i = 0; i < listRelation.getRelation().size(); i++) {
			List<Integer> groupeIDs = listRelation.getRelation().get(i)
					.getGroupID();
			for (Integer groupeID : groupeIDs) {
				if (groupeID == groupID) {
					listRelation2.getRelation().add(
							listRelation.getRelation().get(i));
				}
			}

		}

		return listRelation2;

	}

	@Override
	public void createDefaultRelation(String userIDFromPath,
			String relationIDString, Boolean fromBox)
					throws NoSuchUserException {
		Relation relation = new Relation();
		relation.setEmail(relationIDString);
		relation.setAprouve(1);
		relation.getGroupID().add(0);
		createRelation(userIDFromPath, relation, fromBox);

	}

	@Override
	public Relation createRelation(String userID, Relation relation,
			Boolean fromBox) throws NoSuchUserException {
		/*
		 * TODO(0) : Check the content, add a state 1 to the approuve value
		 * TODO(1) : Check the user really exists from the central server
		 * TODO(1) : Send a request to the right box to say it add as a friend
		 */
		User user = new User();
		RequestUserService rus = new RequestUserServiceImpl();
		try {
			user = rus.get(relation.getEmail());
			if (user == null) {
				throw new NoSuchUserException();
			}
		} catch (IOException e) {
			LOGGER.error("get user fail",e);
		}
		relation.setName(user.getName());
		relation.setSurname(user.getSurname());
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
			relation2.getGroupID().add(0);
			if (!fromBox) {
				if (userDatabase.exists(relation.getEmail())) {
					relationshipDatabase.save(new RelationshipRepositoryObject(
							relation.getEmail(), relation2));
				} else {

					RequestRelationService rss = new RequestRelationServiceImpl();
					try {
						rss.updateRelationORH(relation2, relation);
					} catch (IOException e) {
						LOGGER.error("Error during create the relation  betewen {} and {}",relation2,relation,e);
						e.printStackTrace();
					} catch (NoSuchBoxException e) {
						LOGGER.error("Error during create the relation  betewen {} and {} box not found",relation2,relation,e);
					}
					// Send a request to the right box with the profile of userID
					rss.close();
				}
			} else {
				relation.setAprouve(2);
			}
			return relationshipDatabase.save(
					new RelationshipRepositoryObject(userID, relation))
					.toRelation();
		} else {
			throw new NoSuchUserException();
		}

	}

	@Override
	public void saveRelation(String userID, Relation relation) {
		// Here, the user is only allowed to edit the approve value if the
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
				RequestRelationService rss = new RequestRelationServiceImpl();
				try {
					rss.setAprouveRelationORH(userID, relation.getEmail());
				} catch (IOException e) {
					LOGGER.error("Can not set aprouve {} for {} Error IO",userID,relation.getEmail(),e);
					e.printStackTrace();
				} catch (NoSuchBoxException e) {
					LOGGER.error("Can not set aprouve {} for {} no box found",userID,relation.getEmail(),e);
				} catch (NoSuchUserException e) {
					LOGGER.error("Can not set aprouve {} for {} user not found",userID,relation.getEmail(),e);
				}
				rss.close();
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
	public void setAprouveBox(String userId, String relationId) {
		Relation relationIntoDb = relationshipDatabase.findOne(userId,
				relationId).toRelation();
		if (relationIntoDb.getAprouve() == 1) {
			relationIntoDb.setAprouve(3);
			relationshipDatabase.save(new RelationshipRepositoryObject(userId,
					relationIntoDb));
		} else {
			LOGGER.error("Something has been changed...");
		}

	}

	@Override
	public ListContent getAllContent(String userID, String relationID) {
		try {

			RequestContentService requestContentService = new RequestContentServiceImpl();

			ListContent listContent = requestContentService.get(userID,
					relationID);
			LOGGER.debug("Content from {} fetched ! ", relationID);
			return listContent;
		} catch (IOException e) {
			LOGGER.error("Error for get all content of {}",userID,e);
		} catch (NoSuchUserException e) {
			LOGGER.error("Error for get all content of {} user not found",userID,e);
		} catch (NoSuchBoxException e) {
			LOGGER.error("Error for get all content of {} box not found",userID,e);
		} catch (NoRelationException e) {
			LOGGER.debug("Error for get all content of {} relation not found with {}",userID,relationID,e);
		}
		return null;

	}

	@Override
	public void deleteRelationBox(String userId, String relationId) {
		if (userDatabase.exists(userId)) {
			relationshipDatabase.delete(userId, relationId);
		}

	}

	@Override
	public void deleteRelation(String userID, String email) {
		if (userDatabase.exists(email)) {
			relationshipDatabase.delete(email, userID);

		} else {
			RequestRelationService rss = new RequestRelationServiceImpl();
			try {
				rss.deleteRelationORH(userID, email);
			} catch (IOException e) {
				LOGGER.error("Can not delete a relation betewen {} and {} Error IO",userID,email,e);
			} catch (NoSuchUserException e) {
				LOGGER.debug("Can not delete a relation betewen {} and {} Error user not found (already delete ???)",userID,email,e);
			} catch (NoSuchBoxException e) {
				LOGGER.error("Can not delete a relation betewen {} and {} box of the first not found",userID,email,e);
			}
			rss.close();
		}
		relationshipDatabase.delete(userID, email);

	}

}
