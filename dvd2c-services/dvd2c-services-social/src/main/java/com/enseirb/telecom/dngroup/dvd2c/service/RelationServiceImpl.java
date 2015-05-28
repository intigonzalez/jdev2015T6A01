package com.enseirb.telecom.dngroup.dvd2c.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

//import com.enseirb.telecom.dngroup.dvd2c.endpoints.RelationEndPoints;
import com.enseirb.telecom.dngroup.dvd2c.exception.NoRelationException;
import com.enseirb.telecom.dngroup.dvd2c.exception.NoRoleException;
import com.enseirb.telecom.dngroup.dvd2c.exception.NoSuchBoxException;
import com.enseirb.telecom.dngroup.dvd2c.exception.NoSuchContactException;
import com.enseirb.telecom.dngroup.dvd2c.exception.NoSuchUserException;
import com.enseirb.telecom.dngroup.dvd2c.model.ContactXSD;
import com.enseirb.telecom.dngroup.dvd2c.model.Content;
import com.enseirb.telecom.dngroup.dvd2c.model.User;
import com.enseirb.telecom.dngroup.dvd2c.modeldb.Contact;
import com.enseirb.telecom.dngroup.dvd2c.modeldb.Permission;
import com.enseirb.telecom.dngroup.dvd2c.modeldb.ReceiverActor;
import com.enseirb.telecom.dngroup.dvd2c.modeldb.Role;
import com.enseirb.telecom.dngroup.dvd2c.repository.ContactRepository;
import com.enseirb.telecom.dngroup.dvd2c.repository.ReceiverActorRepository;
import com.enseirb.telecom.dngroup.dvd2c.repository.RoleRepository;
import com.enseirb.telecom.dngroup.dvd2c.request.RequestRelationService;
import com.enseirb.telecom.dngroup.dvd2c.request.RequestRelationServiceImpl;
import com.enseirb.telecom.dngroup.dvd2c.service.request.RequestContentService;
import com.enseirb.telecom.dngroup.dvd2c.service.request.RequestUserService;

@Service
public class RelationServiceImpl implements RelationService {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(RelationServiceImpl.class);

	// @Inject
	// RelationRepository relationRepository;

	@Inject
	ContactRepository contactRepository;

	@Inject
	AccountService accountService;

	@Inject
	RoleRepository roleRepository;

	@Inject
	RequestRelationService rrs;

	@Inject
	RequestUserService rus;

	@Inject
	RequestContentService requestContentService;

	@Inject
	private ReceiverActorRepository receiverActorRepository;


	@Override
	public boolean RelationExist(UUID UserUUID, UUID actorUUID) {

		Contact c = contactRepository.findContact(UserUUID, actorUUID);
		return (c != null);
	}

	@Override
	public void updateRelation(UUID UserUUID) throws IOException,
			NoSuchUserException {

		User user = accountService.getUserFromUUID(UserUUID);
		for (Contact rro : contactRepository.findByOwner(UserUUID)) {

			try {
				User relationUpdate = rrs.get(UUID.fromString(user.getUuid()),
						rro.getReceiverActor().getId());
				ReceiverActor relationIntoDb = receiverActorRepository
						.findByEmail(rro.getReceiverActor().getEmail());
				relationIntoDb.setFirstname(relationUpdate.getFirstname());
				relationIntoDb.setSurname(relationUpdate.getSurname());
				receiverActorRepository.save(relationIntoDb);
			} catch (NoSuchBoxException e) {
				LOGGER.warn("All users should have a box, ignoring");

			}
		}

	}

	@Override
	public ContactXSD getRelation(UUID ownerID, UUID relationID)
			throws NoSuchContactException {

		Contact contact;
		if ((contact = contactRepository.findContact(ownerID, relationID)) == null)
			throw new NoSuchContactException();
		ContactXSD relation = toContactXSD(contact);

		return relation;

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
		contactXSD.setUuid(contact.getReceiverActor().getId().toString());

		contactXSD.getRole().addAll(getRolesNames(contact.getRole()));

		return contactXSD;
	}

	@Override
	public List<ContactXSD> getListContact(UUID userID) {
		List<ContactXSD> listContactXSD = new ArrayList<ContactXSD>();
		List<Contact> contacts = contactRepository.findByOwner(userID);
		for (Contact contact : contacts) {
			listContactXSD.add(toContactXSD(contact));
		}
		return listContactXSD;
	}

	@Override
	public ContactXSD createRelation(UUID userUUID, UUID relationUUID,
			Boolean fromBox) throws NoSuchUserException, IOException,
			NoSuchBoxException {
		/*
		 * Check the content, add a state 1 to the approuve value Check the user
		 * really exists from the central server Send a request to the right box
		 * to say it add as a friend
		 */

		User user;
		if ((user = accountService.getUserFromUUID(userUUID)) == null) {
			throw new NoSuchUserException();
		}

		ReceiverActor receiverActor;
		// verify where is the relation
		// is it already registered on box ?
		if ((receiverActor = receiverActorRepository.findOne(relationUUID)) == null) {
			receiverActor = new ReceiverActor();
			// is not already registered
			User receiverUser;
			// is it a user on the box ?
			if ((receiverUser = accountService.getUserFromUUID(relationUUID)) == null) {
				// is not on the box
				receiverUser = rus.get(relationUUID);
			}
			receiverActor.setEmail(receiverUser.getUserID());
			receiverActor.setFirstname(receiverUser.getFirstname());
			receiverActor.setSurname(receiverUser.getSurname());
			receiverActor.setId(UUID.fromString(receiverUser.getUuid()));
			receiverActorRepository.save(receiverActor);
		}

		Contact contact2 = new Contact();
		contact2.setOwnerId(UUID.fromString(user.getUuid()));
		contact2.setReceiverActor(receiverActor);
		if (fromBox)
			contact2.setStatus(2);
		else
			contact2.setStatus(1);

		// set public relation
		Role role;

		role = initRolesAndGetPublic(user);

		contact2.setRole(Arrays.asList(role));

		if (!fromBox) {
			// User receiverActor1;
			if ((accountService.getUserFromUUID(relationUUID)) != null) {
				createRelation(receiverActor.getId(),
						UUID.fromString((user.getUuid())), true);
			} else {
				// receiverActor1 = rus.get(relationIDString);
				ContactXSD contact = new ContactXSD();
				contact.setActorID(user.getUserID());
				contact.setAprouve(2);
				contact.setFirstname(user.getFirstname());
				contact.setSurname(user.getSurname());
				rrs.updateRelationORH(contact, relationUUID);
			}

		}
		contactRepository.save(contact2);
		ContactXSD contactXSD = new ContactXSD();
		contactXSD.setActorID(contact2.getReceiverActor().getEmail());
		contactXSD.setAprouve(contact2.getStatus());
		contactXSD.setFirstname(contact2.getReceiverActor().getFirstname());
		contactXSD.setSurname(contact2.getReceiverActor().getSurname());
		contactXSD.setUuid(contact2.getReceiverActor().getId().toString());
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

	/**
	 * init public, Family, Friends and Pro relation for the user (if not
	 * already exist)
	 * 
	 * @param user to add the role
	 * @return the public role
	 */
	private Role initRolesAndGetPublic(User user) {
		//
		Role roleFamily;
		if ((roleFamily = roleRepository.findByName("Public",
				UUID.fromString(user.getUuid()))) == null) {

			roleFamily = new Role();
			roleFamily.setActorId(UUID.fromString(user.getUuid()));
			roleFamily.setName("Public");

			// init permission
			Permission permission = new Permission("Read", "Family",
					Arrays.asList(roleFamily));
			Permission permission2 = new Permission("Comment", "Friends",
					Arrays.asList(roleFamily));
			Permission permission3 = new Permission("Post", "Friends",
					Arrays.asList(roleFamily));
			roleFamily.setPermissions(Arrays.asList(permission,permission2,permission3));
			roleRepository.save(roleFamily);

			roleRepository.save(roleFamily);
		}
		Role roleFriends;
		if ((roleFriends = roleRepository.findByName("Public",
				UUID.fromString(user.getUuid()))) == null) {

			roleFriends = new Role();
			roleFriends.setActorId(UUID.fromString(user.getUuid()));
			roleFriends.setName("Public");

			// init permission
			Permission permission = new Permission("Read", "Friends",
					Arrays.asList(roleFriends));
			Permission permission2 = new Permission("Comment", "Friends",
					Arrays.asList(roleFriends));
			Permission permission3 = new Permission("Post", "Friends",
					Arrays.asList(roleFriends));
			roleFriends.setPermissions(Arrays.asList(permission,permission2,permission3));
			roleRepository.save(roleFriends);
		}Role rolePro;
		if ((rolePro = roleRepository.findByName("Public",
				UUID.fromString(user.getUuid()))) == null) {

			rolePro = new Role();
			rolePro.setActorId(UUID.fromString(user.getUuid()));
			rolePro.setName("Public");

			// init permission
			Permission permission = new Permission("Read", "Pro",
					Arrays.asList(rolePro));
			Permission permission2 = new Permission("Comment", "Pro",
					Arrays.asList(rolePro));
			Permission permission3 = new Permission("Post", "Pro",
					Arrays.asList(rolePro));
			rolePro.setPermissions(Arrays.asList(permission,permission2,permission3));

			roleRepository.save(rolePro);
		}
		Role rolePublic;
		if ((rolePublic = roleRepository.findByName("Public",
				UUID.fromString(user.getUuid()))) == null) {

			rolePublic = new Role();
			rolePublic.setActorId(UUID.fromString(user.getUuid()));
			rolePublic.setName("Public");

			// init permission
			Permission permission = new Permission("Read", "Public",
					Arrays.asList(rolePublic));
			rolePublic.setPermissions(Arrays.asList(permission));

			roleRepository.save(rolePublic);
		}

		return rolePublic;
	}

	@Override
	public void saveRelation(UUID userUUID, ContactXSD contactXSD)
			throws NoRelationException, NoSuchUserException, NoRoleException {
		// Here, the user is only allowed to edit the approve value if the
		// current value is = 2
//		User user = accountService.getUserFromUUID(userUUID);
		Contact contact;
		if ((contact = contactRepository.findContact(userUUID,
				UUID.fromString(contactXSD.getUuid()))) == null) {
			throw new NoRelationException();
		}

		if (contact.getStatus() != contactXSD.getAprouve()
				&& contact.getStatus() == 2 && contactXSD.getAprouve() == 3) {
			contact.setStatus(3);

			Contact contact2;
			if ((contact2 = contactRepository.findContact(
					UUID.fromString(contactXSD.getUuid()), userUUID)) != null) {
				contact2.setStatus(3);
				contactRepository.save(contact2);
			} else {
				RequestRelationService rss = new RequestRelationServiceImpl();
				try {
					rss.setAprouveRelationORH(userUUID,
							UUID.fromString(contactXSD.getUuid()));
				} catch (IOException e) {
					LOGGER.error("Can not set aprouve {} for {} Error IO",
							userUUID, contactXSD.getActorID(), e);
					e.printStackTrace();
				} catch (NoSuchBoxException e) {
					LOGGER.error("Can not set aprouve {} for {} no box found",
							userUUID, contactXSD.getActorID(), e);
				} catch (NoSuchUserException e) {
					LOGGER.error(
							"Can not set aprouve {} for {} user not found",
							userUUID, contactXSD.getActorID(), e);
				}

				// Send a request to the box to tell it the user accepts the
				// relationship
			}

		}
		// Or, the user can edit the group his/her relationship is in.

		List<String> roleName = getRolesNames(contact.getRole());
		if (!roleName.equals(contactXSD.getRole())) {
			contact.getRole().clear();
			for (String role : contactXSD.getRole()) {
				Role relation2;
				if ((relation2 = roleRepository.findByName(role,
						contact.getOwnerId())) != null)

					contact.getRole().add(relation2);
				else
					throw new NoRoleException();
			}

		}
		contactRepository.save(contact);
		return;
	}

	/**
	 * @param roles
	 * @return
	 */
	private List<String> getRolesNames(List<Role> roles) {
		List<String> roleName = new ArrayList<String>();
		// try {
		for (Role role : roles) {
			roleName.add(role.getName());
		}
		// } catch (LazyInitializationException e) {
		// LOGGER.error("no Role for this contact");
		// }
		return roleName;
	}

	@Override
	public void setAprouveBox(UUID userUUID, UUID contactUUID) {
		Contact contact = contactRepository.findContact(userUUID, contactUUID);
		if (contact.getStatus() == 1) {
			contact.setStatus(3);
			contactRepository.save(contact);
		} else {
			LOGGER.error("Something has been changed...");
			throw new WebApplicationException(Status.FORBIDDEN);
		}

	}

	@Override
	public List<Content> getAllContent(UUID userUUID, UUID relationUUID) {
		try {
			List<Content> listContent = requestContentService.get(userUUID,
					relationUUID);
			LOGGER.debug("Content from {} fetched ! ", relationUUID);
			return listContent;
		} catch (IOException e) {
			LOGGER.error("Error for get all content of {}", userUUID, e);
		} catch (NoSuchUserException e) {
			LOGGER.error("Error for get all content of {} user not found",
					userUUID, e);
		} catch (NoSuchBoxException e) {
			LOGGER.error("Error for get all content of {} box not found",
					userUUID, e);
		} catch (NoRelationException e) {
			LOGGER.debug(
					"Error for get all content of {} relation not found with {}",
					userUUID, relationUUID, e);
		}
		return null;

	}

	@Override
	public void deleteRelationBox(UUID userUUId, UUID relationUUId)
			throws NoRelationException {
		Contact contact;
		if ((contact = contactRepository.findContact(userUUId, relationUUId)) != null) {
			contactRepository.delete(contact);
		} else {
			throw new NoRelationException();
		}

	}

	@Override
	public void deleteRelation(UUID actorUUID, UUID contactUUID)
			throws NoSuchUserException, NoSuchBoxException, NoRelationException {
		Contact contact;

		if ((contact = contactRepository.findContact(contactUUID, actorUUID)) != null) {
			contactRepository.delete(contact);

		} else {
			RequestRelationService rss = new RequestRelationServiceImpl();
			try {
				rss.deleteRelationORH(actorUUID, contactUUID);
			} catch (IOException e) {
				LOGGER.error(
						"Can not delete a relation betewen {} and {} Error IO",
						actorUUID, contactUUID, e);
			} catch (NoSuchUserException e) {
				LOGGER.debug(
						"Can not delete a relation betewen {} and {} Error user not found (already delete ???)",
						actorUUID, contactUUID, e);
				throw e;
			} catch (NoSuchBoxException e) {
				LOGGER.error(
						"Can not delete a relation betewen {} and {} box of the first not found",
						actorUUID, contactUUID, e);
				throw e;
			}

		}

		deleteRelationBox(actorUUID, contactUUID);

	}

}
