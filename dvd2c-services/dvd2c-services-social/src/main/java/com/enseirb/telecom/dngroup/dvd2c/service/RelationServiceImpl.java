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
import com.enseirb.telecom.dngroup.dvd2c.db.RelationshipRepositoryOld;
import com.enseirb.telecom.dngroup.dvd2c.db.RelationshipRepositoryOldObject;
import com.enseirb.telecom.dngroup.dvd2c.db.UserRepositoryOld;
import com.enseirb.telecom.dngroup.dvd2c.db.UserRepositoryOldObject;
//import com.enseirb.telecom.dngroup.dvd2c.endpoints.RelationEndPoints;
import com.enseirb.telecom.dngroup.dvd2c.exception.NoRelationException;
import com.enseirb.telecom.dngroup.dvd2c.exception.NoSuchBoxException;
import com.enseirb.telecom.dngroup.dvd2c.exception.NoSuchContactException;
import com.enseirb.telecom.dngroup.dvd2c.exception.NoSuchUserException;
import com.enseirb.telecom.dngroup.dvd2c.model.ContactXSD;
import com.enseirb.telecom.dngroup.dvd2c.model.Content;
import com.enseirb.telecom.dngroup.dvd2c.model.User;
import com.enseirb.telecom.dngroup.dvd2c.modeldb.Actor;
import com.enseirb.telecom.dngroup.dvd2c.modeldb.Contact;
import com.enseirb.telecom.dngroup.dvd2c.modeldb.ReceiverActor;
import com.enseirb.telecom.dngroup.dvd2c.modeldb.Relation;
import com.enseirb.telecom.dngroup.dvd2c.repository.ContactRepository;
import com.enseirb.telecom.dngroup.dvd2c.repository.ReceiverActorRepository;
import com.enseirb.telecom.dngroup.dvd2c.repository.RelationRepository;
import com.enseirb.telecom.dngroup.dvd2c.repository.UserRepository;
import com.enseirb.telecom.dngroup.dvd2c.request.RequestRelationService;
import com.enseirb.telecom.dngroup.dvd2c.request.RequestRelationServiceImpl;
import com.enseirb.telecom.dngroup.dvd2c.service.request.RequestContentService;
import com.enseirb.telecom.dngroup.dvd2c.service.request.RequestContentServiceImpl;
import com.enseirb.telecom.dngroup.dvd2c.service.request.RequestUserService;
import com.enseirb.telecom.dngroup.dvd2c.service.request.RequestUserServiceImpl;

@Service
public class RelationServiceImpl implements RelationService {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(RelationServiceImpl.class);

	// @Inject
	// RelationRepository relationRepository;

	@Inject
	ContactRepository contactRepository;

	@Inject
	ReceiverActorRepository rar;

	@Inject
	AccountService accountService;

	@Inject
	RelationRepository relationRepository;

	@Inject
	RequestRelationService rrs;

	@Inject
	RequestUserService rus;

	@Inject
	RequestContentService requestContentService;
	
	@Inject
	private ReceiverActorRepository receiverActorRepository;

	public boolean RelationExist(String UserID, String actorID) {
//		ReceiverActor a = receiverActorRepository.findByEmail(actorID);
		Contact c = contactRepository.findContact(UserID, actorID);
		return ( c!= null);
	}

	@Override
	public void updateRelation(String userID) throws IOException,
			NoSuchUserException {

		RequestRelationService rrs = new RequestRelationServiceImpl();

		for (Contact rro : contactRepository.findByOwner(userID)) {

			try {
				User relationUpdate = rrs.get(userID, rro.getReceiverActor().getEmail());
				ReceiverActor relationIntoDb = receiverActorRepository.findByEmail(rro.getReceiverActor().getEmail());
				relationIntoDb.setFirstname(relationUpdate.getFirstname());
				relationIntoDb.setSurname(relationUpdate.getSurname());
				receiverActorRepository.save(relationIntoDb);
			} catch (NoSuchBoxException e) {
				LOGGER.warn("All users should have a box, ignoring");

			}
		}
		rrs.close();
	}

	// //RBAC: CHANGE TO USER SERVICE
	// @Override
	// public User getContactInformation(String userID) {
	// UserRepositoryOldObject user = userRepo.findOne(userID);
	//
	// if (user == null) {
	// return null;
	// } else {
	// User userComplet = user.toUser();
	// User userReturn = new User();
	// userReturn.setFirstname(userComplet.getFirstname());
	// userReturn.setSurname(userComplet.getSurname());
	// userReturn.setUserID(userComplet.getUserID());
	// return userReturn;
	// }
	//
	// }

	@Override
	public ContactXSD getRelation(String ownerID, String relationID)
			throws NoSuchContactException {
		try {
			Contact contact = contactRepository
					.findContact(ownerID, relationID);

			ContactXSD relation = toContactXSD(contact);

			return relation;
		} catch (Exception e) {
			throw new NoSuchContactException();
		}
	}

	/**
	 * @param contact
	 * @return
	 */
	private ContactXSD toContactXSD(Contact contact) {
		ReceiverActor receiverActor = contact.getReceiverActor();
		ContactXSD contactXSD = new ContactXSD();
		contactXSD.setActorID(receiverActor.getEmail());
		contactXSD.setAprouve(contact.getStatus());
		contactXSD.setFirstname(receiverActor.getFirstname());
		contactXSD.setSurname(receiverActor.getSurname());
		return contactXSD;
	}

	@Override
	public List<ContactXSD> getListContact(String userID) {
		List<ContactXSD> listContactXSD = new ArrayList<ContactXSD>();
		List<Contact> contacts = contactRepository.findByOwner(userID);
		for (Contact contact : contacts) {
			listContactXSD.add(toContactXSD(contact));
		}
		return listContactXSD;
	}

	// //RBAC: Never use
	// @Override
	// public List<Relation> getListRelation(String userID, Role roleIDfromUser)
	// {
	//
	// List<Relation> listRelation = getListRelation(userID);
	// List<Relation> listRelation2 = new ArrayList<Relation>();
	// for (int i = 0; i < listRelation.size(); i++) {
	// List<String> roles = listRelation.get(i).getRole();
	//
	// for (String role : roles) {
	// if (role.equals(roleIDfromUser.getRoleId())) {
	// listRelation2.add(listRelation.get(i));
	// }
	// }
	//
	// }
	//
	// return listRelation2;
	//
	// }

	@Override
	public void createDefaultRelation(String userIDFromPath,
			String relationIDString, Boolean fromBox)
			throws NoSuchUserException {
		// Relation relation = new Relation();
		// relation.setName("public");
		//
		// Contact contact =new Contact();
		// contact.setOwnerId(ownerMail);
		// ReceiverActor receiverActor = new ReceiverActor();
		// receiverActor.setEmail(contactMail);
		// contact.setReceiveractor(receiverActor);
		// RoleXSD role = new RoleXSD();
		// role.setRole("public");

	}

	@Override
	public ContactXSD createRelation(String userIDFromPath,
			String relationIDString, Boolean fromBox)
			throws NoSuchUserException, IOException, NoSuchBoxException {
		/*
		 * Check the content, add a state 1 to the approuve value Check the user
		 * really exists from the central server Send a request to the right box
		 * to say it add as a friend
		 */

		User user;
		if ((user = accountService.getUserOnLocal(userIDFromPath)) == null) {
			throw new NoSuchUserException();
		}

		ReceiverActor receiverActor;
		if ((receiverActor = rar.findByEmail(relationIDString)) == null) {
			// verify if is local relation
			User receiverUser;
			if ((receiverUser = accountService.getUserOnLocal(relationIDString)) == null) {
				receiverUser = rus.get(relationIDString);
			}
			receiverActor.setEmail(receiverUser.getUserID());
			receiverActor.setFirstname(receiverUser.getFirstname());
			receiverActor.setSurname(receiverUser.getSurname());
		}

		Contact c = new Contact();
		c.setOwnerId(userIDFromPath);
		c.setReceiverActor(receiverActor);
		if (fromBox)
			c.setStatus(2);
		else
			c.setStatus(1);

		if (!fromBox) {
			User receiverActor1;
			if ((receiverActor1 = accountService
					.getUserOnLocal(relationIDString)) != null) {
				createRelation(relationIDString, relationIDString, true);
			} else {
				receiverActor1 = rus.get(relationIDString);
				ContactXSD contact = new ContactXSD();
				contact.setActorID(user.getUserID());
				contact.setAprouve(2);
				contact.setFirstname(user.getFirstname());
				contact.setSurname(user.getSurname());
				rrs.updateRelationORH(contact, userIDFromPath);
			}
		}
		contactRepository.save(c);
		ContactXSD contactXSD = new ContactXSD();
		contactXSD.setActorID(c.getReceiverActor().getEmail());
		contactXSD.setAprouve(c.getStatus());
		contactXSD.setFirstname(c.getReceiverActor().getFirstname());
		contactXSD.setSurname(c.getReceiverActor().getSurname());
		return contactXSD;
		// User user;
		// try {
		// user = rus.get(contactXSD.getActorID());
		// if (user == null) {
		// throw new NoSuchUserException();
		// }
		// } catch (IOException e) {
		// LOGGER.error("get user fail", e);
		// }
		// contactXSD.setFirstname(user.getFirstname());
		// contactXSD.setSurname(user.getSurname());
		// contactXSD.setAprouve(1);
		//
		// // Prepare the relation for the "UserAsked"
		// com.enseirb.telecom.dngroup.dvd2c.modeldb.User uro = userRepo
		// .findByEmail(userID);
		// if (uro == null) {
		// throw new NoSuchUserException();
		// } else {
		// User userWhoAsked = userRepo.findOne(userID).toUser();
		// Relation relation2 = new Relation();
		// relation2.setActorID(userWhoAsked.getUserID());
		// relation2.setFirstname(userWhoAsked.getFirstname());
		// relation2.setSurname(userWhoAsked.getSurname());
		// relation2.setPubKey(userWhoAsked.getPubKey());
		// relation2.setAprouve(2);
		// relation2.setUnixTime(contactXSD.getUnixTime());
		// Role role = new Role();
		// role.setRoleId("public");
		// relation2.getRole().add(role.getRoleId());
		// if (!fromBox) {
		// if (userRepo.exists(contactXSD.getActorID())) {
		// relationRepository
		// .save(new RelationshipRepositoryOldObject(
		// contactXSD.getActorID(), relation2));
		// } else {
		//
		// RequestRelationService rss = new RequestRelationServiceImpl();
		// try {
		// rss.updateRelationORH(relation2, contactXSD.get);
		// } catch (IOException e) {
		// LOGGER.error(
		// "Error during create the relation  betewen {} and {}",
		// relation2, contactXSD, e);
		// e.printStackTrace();
		// } catch (NoSuchBoxException e) {
		// LOGGER.error(
		// "Error during create the relation  betewen {} and {} box not found",
		// relation2, contactXSD, e);
		// }
		// // Send a request to the right box with the profile of
		// // userID
		// rss.close();
		// }
		// } else {
		// contactXSD.setAprouve(2);
		// }
		// return relationRepository.save(
		// new RelationshipRepositoryOldObject(userID, contactXSD))
		// .toRelation();
		// }
		//
	}

	@Override
	public void saveRelation(String userID, ContactXSD relation)
			throws NoRelationException {
		// Here, the user is only allowed to edit the approve value if the
		// current value is = 2
		Contact contact;
		if ((contact = contactRepository.findContact(userID,
				relation.getActorID())) == null) {
			throw new NoRelationException();
		}

		if (contact.getStatus() != relation.getAprouve()
				&& contact.getStatus() == 2 && relation.getAprouve() == 3) {
			contact.setStatus(3);

			Contact contact2;
			if ((contact2 = contactRepository.findContact(
					relation.getActorID(), userID)) != null) {
				contact2.setStatus(3);
				contactRepository.save(contact2);
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
		// Or, the user can edit the group his/her relationship is in.
		if (!contact.getRelations().equals(relation.getRole())) {
			contact.getRelations().clear();
			for (String role : relation.getRole()) {
				// RBAC: NEED TO FIX THIS

				Relation relation2;
				if ((relation2 = relationRepository.findByName(role, userID)) != null)

					contact.getRelations().add(relation2);
			}

		}
		contactRepository.save(contact);
		return;
	}

	@Override
	public void setAprouveBox(String userId, String relationId) {
		Contact contact = contactRepository.findContact(userId, relationId);
		if (contact.getStatus() == 1) {
			contact.setStatus(3);
			contactRepository.save(contact);
		} else {
			LOGGER.error("Something has been changed...");
			throw new WebApplicationException(Status.FORBIDDEN);
		}

	}

	@Override
	public List<Content> getAllContent(String userID, String relationID) {
		try {

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
	public void deleteRelationBox(String userId, String relationId)
			throws NoRelationException {
		Contact contact;
		if ((contact = contactRepository.findContact(userId, relationId)) != null) {
			contactRepository.delete(contact);
		} else {
			throw new NoRelationException();
		}

	}

	@Override
	public void deleteRelation(String actorID, String contactId)
			throws NoSuchUserException, NoSuchBoxException, NoRelationException {
		Contact contact;

		if ((contact = contactRepository.findContact(contactId, actorID)) != null) {
			contactRepository.delete(contact);

		} else {
			RequestRelationService rss = new RequestRelationServiceImpl();
			try {
				rss.deleteRelationORH(actorID, contactId);
			} catch (IOException e) {
				LOGGER.error(
						"Can not delete a relation betewen {} and {} Error IO",
						actorID, contactId, e);
			} catch (NoSuchUserException e) {
				LOGGER.debug(
						"Can not delete a relation betewen {} and {} Error user not found (already delete ???)",
						actorID, contactId, e);
				throw e;
			} catch (NoSuchBoxException e) {
				LOGGER.error(
						"Can not delete a relation betewen {} and {} box of the first not found",
						actorID, contactId, e);
				throw e;
			}
			rss.close();
		}

		deleteRelationBox(actorID, contactId);

	}



}
