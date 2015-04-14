package com.enseirb.telecom.dngroup.dvd2c.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.enseirb.telecom.dngroup.dvd2c.db.CrudRepository;
import com.enseirb.telecom.dngroup.dvd2c.db.RelationshipRepository;
import com.enseirb.telecom.dngroup.dvd2c.db.RelationshipRepositoryObject;
import com.enseirb.telecom.dngroup.dvd2c.db.UserRepository;
import com.enseirb.telecom.dngroup.dvd2c.db.UserRepositoryObject;
//import com.enseirb.telecom.dngroup.dvd2c.endpoints.RelationEndPoints;
import com.enseirb.telecom.dngroup.dvd2c.exception.NoRelationException;
import com.enseirb.telecom.dngroup.dvd2c.exception.NoSuchBoxException;
import com.enseirb.telecom.dngroup.dvd2c.exception.NoSuchRelationException;
import com.enseirb.telecom.dngroup.dvd2c.exception.NoSuchUserException;
import com.enseirb.telecom.dngroup.dvd2c.model.Content;
import com.enseirb.telecom.dngroup.dvd2c.model.Relation;
import com.enseirb.telecom.dngroup.dvd2c.model.User;
import com.enseirb.telecom.dngroup.dvd2c.service.request.RequestContentService;
import com.enseirb.telecom.dngroup.dvd2c.service.request.RequestContentServiceImpl;
import com.enseirb.telecom.dngroup.dvd2c.service.request.RequestRelationService;
import com.enseirb.telecom.dngroup.dvd2c.service.request.RequestRelationServiceImpl;
import com.enseirb.telecom.dngroup.dvd2c.service.request.RequestUserService;
import com.enseirb.telecom.dngroup.dvd2c.service.request.RequestUserServiceImpl;

@Service
public class RelationServiceImpl implements RelationService {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(RelationServiceImpl.class);

	@Inject
	RelationshipRepository relationshipDatabase;
	
	@Inject
	UserRepository userRepo;

	public boolean RelationExist(String UserID, String actorID) {
		return relationshipDatabase.exists(UserID+actorID);
	}

	@Override
	public void updateRelation(String userID) throws IOException,
			NoSuchUserException {

		RequestRelationService rrs = new RequestRelationServiceImpl();

		for (RelationshipRepositoryObject rro : relationshipDatabase.findAll()) {

			if (rro.getUserId().equals(userID)) {

				LOGGER.debug("{}", rro.getActorID());

				try {
					// Box boxRelation = requestServ.getBox(rro.getActorID());

					User relationUpdate = rrs.get(userID, rro.getActorID());
					Relation relationIntoDb = relationshipDatabase.findOne(
							userID + relationUpdate.getUserID()).toRelation();
					relationIntoDb.setFirstname(relationUpdate.getFirstname());
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
	public Relation getRelation(String userID, String actorID) throws NoSuchRelationException {
		RelationshipRepositoryObject relation = relationshipDatabase.findOne(
				userID+ actorID);
		if (relation == null) {
			throw new NoSuchRelationException();
		} else {
			return relation.toRelation();
		}
	}

	@Override
	public User getMe(String userID) {
		UserRepositoryObject user = userRepo.findOne(userID);

		if (user == null) {
			return null;
		} else {
			User userComplet = user.toUser();
			User userReturn = new User();
			userReturn.setFirstname(userComplet.getFirstname());
			userReturn.setSurname(userComplet.getSurname());
			userReturn.setUserID(userComplet.getUserID());
			return userReturn;
		}

	}

	@Override
	public List<Relation> getListRelation(final String userID) {
		List<Relation> listRelation = new ArrayList<Relation>();
		Iterable<RelationshipRepositoryObject> relation = relationshipDatabase
				.findAll();
		Iterator<RelationshipRepositoryObject> itr = relation.iterator();
		while (itr.hasNext()) {
			RelationshipRepositoryObject relationshipRepositoryObject = itr
					.next();
			if (relationshipRepositoryObject.getUserId().equals(userID)) {

				listRelation.add(relationshipRepositoryObject.toRelation());
			}
		}

		return listRelation;
	}

	@Override
	public List<Relation> getListRelation(String userID, int roleIDfromUser) {

		List<Relation> listRelation = getListRelation(userID);
		List<Relation> listRelation2 = new ArrayList<Relation>();
		for (int i = 0; i < listRelation.size(); i++) {
			List<Integer> roles = listRelation.get(i).getRoleID();
			for (Integer roleID : roles) {
				if (roleID == roleIDfromUser) {
					listRelation2.add(listRelation.get(i));
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
		relation.setActorID(relationIDString);
		relation.setAprouve(1);
		relation.getRoleID().add(0);
		createRelation(userIDFromPath, relation, fromBox);

	}

	@Override
	public Relation createRelation(String userID, Relation relation,
			Boolean fromBox) throws NoSuchUserException {
		/*
		 * Check the content, add a state 1 to the approuve value Check the user
		 * really exists from the central server Send a request to the right box
		 * to say it add as a friend
		 */
		User user = new User();
		RequestUserService rus = new RequestUserServiceImpl();
		try {
			user = rus.get(relation.getActorID());
			if (user == null) {
				throw new NoSuchUserException();
			}
		} catch (IOException e) {
			LOGGER.error("get user fail", e);
		}
		relation.setFirstname(user.getFirstname());
		relation.setSurname(user.getSurname());
		relation.setAprouve(1);

		// Prepare the relation for the "UserAsked"
		UserRepositoryObject uro = userRepo.findOne(userID);
		if (uro != null) {
			User userWhoAsked = userRepo.findOne(userID).toUser();
			Relation relation2 = new Relation();
			relation2.setActorID(userWhoAsked.getUserID());
			relation2.setFirstname(userWhoAsked.getFirstname());
			relation2.setSurname(userWhoAsked.getSurname());
			relation2.setPubKey(userWhoAsked.getPubKey());
			relation2.setAprouve(2);
			relation2.setUnixTime(relation.getUnixTime());
			relation2.getRoleID().add(0);
			if (!fromBox) {
				if (userRepo.exists(relation.getActorID())) {
					relationshipDatabase.save(new RelationshipRepositoryObject(
							relation.getActorID(), relation2));
				} else {

					RequestRelationService rss = new RequestRelationServiceImpl();
					try {
						rss.updateRelationORH(relation2, relation);
					} catch (IOException e) {
						LOGGER.error(
								"Error during create the relation  betewen {} and {}",
								relation2, relation, e);
						e.printStackTrace();
					} catch (NoSuchBoxException e) {
						LOGGER.error(
								"Error during create the relation  betewen {} and {} box not found",
								relation2, relation, e);
					}
					// Send a request to the right box with the profile of
					// userID
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
		Relation relationIntoDb = relationshipDatabase.findOne(userID+
				relation.getActorID()).toRelation();
		if (relationIntoDb.getAprouve() != relation.getAprouve()
				&& relationIntoDb.getAprouve() == 2
				&& relation.getAprouve() == 3) {
			relationIntoDb.setAprouve(3);

			if (userRepo.exists(relation.getActorID())) {
				Relation relation2 = relationshipDatabase.findOne(
						relation.getActorID()+ userID).toRelation();
				relation2.setAprouve(3);
				relationshipDatabase.save(new RelationshipRepositoryObject(
						relation.getActorID(), relation2));
			} else {
				RequestRelationService rss = new RequestRelationServiceImpl();
				try {
					rss.setAprouveRelationORH(userID, relation.getActorID());
				} catch (IOException e) {
					LOGGER.error("Can not set aprouve {} for {} Error IO",
							userID, relation.getActorID(), e);
					e.printStackTrace();
				} catch (NoSuchBoxException e) {
					LOGGER.error("Can not set aprouve {} for {} no box found",
							userID, relation.getActorID(), e);
				} catch (NoSuchUserException e) {
					LOGGER.error(
							"Can not set aprouve {} for {} user not found",
							userID, relation.getActorID(), e);
				}
				rss.close();
				// Send a request to the box to tell it the user accepts the
				// relationship
			}

		}
		// Or, the user can edit the group his/her relationshis is in.
		if (!relationIntoDb.getRoleID().equals(relation.getRoleID())) {
			relationIntoDb.getRoleID().clear();
			relationIntoDb.getRoleID().addAll(relation.getRoleID());
		}
		relationshipDatabase.save(new RelationshipRepositoryObject(userID,
				relationIntoDb));
		return;
	}

	@Override
	public void setAprouveBox(String userId, String relationId) {
		Relation relationIntoDb = relationshipDatabase.findOne(userId+
				relationId).toRelation();
		if (relationIntoDb.getAprouve() == 1) {
			relationIntoDb.setAprouve(3);
			relationshipDatabase.save(new RelationshipRepositoryObject(userId,
					relationIntoDb));
		} else {
			LOGGER.error("Something has been changed...");
			throw new WebApplicationException(Status.FORBIDDEN);
		}

	}

	@Override
	public List<Content> getAllContent(String userID, String relationID) {
		try {

			RequestContentService requestContentService = new RequestContentServiceImpl();

			List<Content> listContent = requestContentService.get(userID,
					relationID);
			LOGGER.debug("Content from {} fetched ! ", relationID);
			return listContent;
		} catch (IOException e) {
			LOGGER.error("Error for get all content of {}", userID, e);
		} catch (NoSuchUserException e) {
			LOGGER.error("Error for get all content of {} user not found",
					userID, e);
		} catch (NoSuchBoxException e) {
			LOGGER.error("Error for get all content of {} box not found",
					userID, e);
		} catch (NoRelationException e) {
			LOGGER.debug(
					"Error for get all content of {} relation not found with {}",
					userID, relationID, e);
		}
		return null;

	}

	@Override
	public void deleteRelationBox(String userId, String relationId) {
		if (userRepo.exists(userId)) {
			relationshipDatabase.delete(userId+ relationId);
		}

	}

	@Override
	public void deleteRelation(String userID, String actorID) {
		if (userRepo.exists(actorID)) {
			relationshipDatabase.delete(actorID+ userID);

		} else {
			RequestRelationService rss = new RequestRelationServiceImpl();
			try {
				rss.deleteRelationORH(userID, actorID);
			} catch (IOException e) {
				LOGGER.error(
						"Can not delete a relation betewen {} and {} Error IO",
						userID, actorID, e);
			} catch (NoSuchUserException e) {
				LOGGER.debug(
						"Can not delete a relation betewen {} and {} Error user not found (already delete ???)",
						userID, actorID, e);
			} catch (NoSuchBoxException e) {
				LOGGER.error(
						"Can not delete a relation betewen {} and {} box of the first not found",
						userID, actorID, e);
			}
			rss.close();
		}
		relationshipDatabase.delete(userID+ actorID);

	}

}
