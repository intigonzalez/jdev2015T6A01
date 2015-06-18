package com.enseirb.telecom.dngroup.dvd2c.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.inject.Inject;
import javax.persistence.NonUniqueResultException;
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
import com.enseirb.telecom.dngroup.dvd2c.model.Contact;
import com.enseirb.telecom.dngroup.dvd2c.model.Content;
import com.enseirb.telecom.dngroup.dvd2c.modeldb.ActivityObjectExtand;
import com.enseirb.telecom.dngroup.dvd2c.modeldb.ContactDB;
import com.enseirb.telecom.dngroup.dvd2c.modeldb.Permission;
import com.enseirb.telecom.dngroup.dvd2c.modeldb.ReceiverActor;
import com.enseirb.telecom.dngroup.dvd2c.modeldb.Role;
import com.enseirb.telecom.dngroup.dvd2c.modeldb.User;
import com.enseirb.telecom.dngroup.dvd2c.repository.ActivityObjectAudienceRepository;
import com.enseirb.telecom.dngroup.dvd2c.repository.ContactRepository;
import com.enseirb.telecom.dngroup.dvd2c.repository.PermissionRepository;
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
	public static final String TYPE = "Social";
	public static final String READ = "Read";
	public static final String COMMENT = "Comment";
	public static final String POST = "Post";
	public static final String DELETE = "Delete";
	public static final String CONTACT = "Contact";

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
	RequestContentService rcs;

	@Inject
	ActivityObjectAudienceRepository activityObjectExtandRepo;

	@Inject
	private ReceiverActorRepository receiverActorRepository;

	@Inject
	private PermissionRepository permissionRepository;

	@Override
	public boolean RelationExist(UUID UserUUID, UUID actorUUID) {

		ContactDB c = contactRepository.findContact(UserUUID, actorUUID);
		return (c != null);
	}

	@Override
	public void updateRelation(UUID UserUUID) throws IOException,
			NoSuchUserException {

		for (ContactDB rro : contactRepository.findByOwner(UserUUID)) {

			try {
				User relationUpdate = new User(rrs.get(UserUUID, rro
						.getReceiverActor().getId()));
				ReceiverActor relationIntoDb = receiverActorRepository
						.findByEmail(rro.getReceiverActor().getEmail());
				relationIntoDb.setFirstname(relationUpdate.getFirstname());
				relationIntoDb.setSurname(relationUpdate.getSurname());
				receiverActorRepository.save(relationIntoDb);
			} catch (NoSuchBoxException e) {
				LOGGER.warn("All users should have a box, ignoring");

			} catch (NullPointerException e) {
				LOGGER.error("user of not found");

			}
		}

	}

	@Override
	public ContactDB getContact(UUID ownerID, UUID contactID)
			throws NoSuchContactException {

		ContactDB contactdb;
		if ((contactdb = contactRepository.findContact(ownerID, contactID)) == null)
			throw new NoSuchContactException();

		return contactdb;

	}

	@Override
	public List<ContactDB> getListContact(UUID userID) {

		List<ContactDB> contactdbs = contactRepository.findByOwner(userID);

		return contactdbs;
	}

	@Override
	public Contact createRelation(UUID userUUID, UUID relationUUID,
			Boolean fromBox) throws  IOException,
			NoSuchBoxException, NoSuchUserException {
		/*
		 * Check the content, add a state 1 to the approuve value Check the user
		 * really exists from the central server Send a request to the right box
		 * to say it add as a friend
		 */

		if (RelationExist(userUUID, relationUUID)) {
			LOGGER.debug("relation allredy existing between {} and {}",
					userUUID, relationUUID);
			throw new WebApplicationException(Status.CONFLICT);
		}

		User user;
		if ((user = accountService.findUserByUUID(userUUID)) == null) {
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
			if ((receiverUser = accountService.findUserByUUID(relationUUID)) == null) {
				// is not on the box

				try {
					receiverUser = new User(rus.get(relationUUID));
				} catch (NoSuchUserException e) {
					LOGGER.error("user not found :", relationUUID);
				}

			}
			receiverActor.fromUser(receiverUser);
			receiverActorRepository.save(receiverActor);
		}

		ContactDB contactdb2 = new ContactDB();
		contactdb2.setOwnerId(user.getId());
		contactdb2.setReceiverActor(receiverActor);
		if (fromBox)
			contactdb2.setStatus(2);
		else
			contactdb2.setStatus(1);

		// set public relation
		Role role;

		if ((role = roleRepository.findByName("Public", user.getId(), TYPE)) == null) {
			LOGGER.error("this user {} a not role Public", user.getId());
		}

		contactdb2.setRole(Arrays.asList(role));

		if (!fromBox) {
			// User receiverActor1;
			if ((accountService.findUserByUUID(relationUUID)) != null) {
				createRelation(receiverActor.getId(), (user.getId()), true);
			} else {
				// receiverActor1 = rus.get(relationIDString);
				Contact contact = new Contact();
				contact.setActorID(user.getId().toString());
				contact.setAprouve(2);
				contact.setFirstname(user.getFirstname());
				contact.setSurname(user.getSurname());
				rrs.updateRelationORH(contact, relationUUID);
			}

		}
		contactdb2 = contactRepository.save(contactdb2);
		// role.addContact(contact2);
		//

		Contact contact = new Contact();
		contact.setActorID(contactdb2.getReceiverActor().getEmail());
		contact.setAprouve(contactdb2.getStatus());
		contact.setFirstname(contactdb2.getReceiverActor().getFirstname());
		contact.setSurname(contactdb2.getReceiverActor().getSurname());
		contact.setUuid(contactdb2.getReceiverActor().getId().toString());
		return contact;
		// User user;
		// try {
		// user = rus.get(contact.getActorID());
		// if (user == null) {
		// throw new NoSuchUserException();
		// }
		// } catch (IOException e) {
		// LOGGER.error("get user fail", e);
		// }
		// contact.setFirstname(user.getFirstname());
		// contact.setSurname(user.getSurname());
		// contact.setAprouve(1);
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
		// relation2.setUnixTime(contact.getUnixTime());
		// Role role = new Role();
		// role.setRoleId("public");
		// relation2.getRole().add(role.getRoleId());
		// if (!fromBox) {
		// if (userRepo.exists(contact.getActorID())) {
		// relationRepository
		// .save(new RelationshipRepositoryOldObject(
		// contact.getActorID(), relation2));
		// } else {
		//
		// RequestRelationService rss = new RequestRelationServiceImpl();
		// try {
		// rss.updateRelationORH(relation2, contact.get);
		// } catch (IOException e) {
		// LOGGER.error(
		// "Error during create the relation  betewen {} and {}",
		// relation2, contact, e);
		// e.printStackTrace();
		// } catch (NoSuchBoxException e) {
		// LOGGER.error(
		// "Error during create the relation  betewen {} and {} box not found",
		// relation2, contact, e);
		// }
		// // Send a request to the right box with the profile of
		// // userID
		// rss.close();
		// }
		// } else {
		// contact.setAprouve(2);
		// }
		// return relationRepository.save(
		// new RelationshipRepositoryOldObject(userID, contact))
		// .toRelation();
		// }
		//
	}

	/**
	 * init public, Family, Friends and Pro relation for the user (if not
	 * already exist)
	 * 
	 * @param uuid
	 *            to add the role
	 * @return the public role
	 */
	private Role initRolesAndGetPublic(UUID uuid) {
		Permission permissionRead;
		Permission permissionComment;
		Permission permissionPost;
		Permission permissionDelete;
		Permission permissionContact;

		if ((permissionRead = permissionRepository.findByName(READ)) == null) {
			permissionRead = new Permission(READ, "Read content");
			permissionRepository.save(permissionRead);
		}
		if ((permissionComment = permissionRepository.findByName(COMMENT)) == null) {
			permissionComment = new Permission(COMMENT, "Comment content");
			permissionRepository.save(permissionComment);
		}
		if ((permissionPost = permissionRepository.findByName(POST)) == null) {
			permissionPost = new Permission(POST, "Post content");
			permissionRepository.save(permissionPost);
		}
		if ((permissionDelete = permissionRepository.findByName(DELETE)) == null) {
			permissionDelete = new Permission(DELETE, "Delete content");
			permissionRepository.save(permissionDelete);
		}
		if ((permissionContact = permissionRepository.findByName(CONTACT)) == null) {
			permissionContact = new Permission(CONTACT, "Watch contact list");
			permissionRepository.save(permissionContact);
		}

		Role roleFamily;
		if ((roleFamily = roleRepository.findByName("Family", uuid, TYPE)) == null) {

			roleFamily = new Role();
			roleFamily.setActorId(uuid);
			roleFamily.setName("Family");
			roleFamily.setType(TYPE);

			roleFamily.setPermissions(Arrays.asList(permissionRead,
					permissionComment, permissionContact));

			roleRepository.save(roleFamily);
		}
		Role roleFriends;
		if ((roleFriends = roleRepository.findByName("Friends", uuid, TYPE)) == null) {

			roleFriends = new Role();
			roleFriends.setActorId(uuid);
			roleFriends.setName("Friends");
			roleFriends.setType(TYPE);
			// init permission
			roleFriends.setPermissions(Arrays.asList(permissionRead,
					permissionComment, permissionContact));
			roleRepository.save(roleFriends);
		}
		Role rolePro;
		if ((rolePro = roleRepository.findByName("Public", uuid, TYPE)) == null) {

			rolePro = new Role();
			rolePro.setActorId(uuid);
			rolePro.setName("Public");
			rolePro.setType(TYPE);
			// init permission
			rolePro.setPermissions(Arrays.asList(permissionRead,
					permissionComment, permissionContact));

			roleRepository.save(rolePro);
		}
		Role rolePublic;
		if ((rolePublic = roleRepository.findByName("Public", uuid, TYPE)) == null) {

			rolePublic = new Role();
			rolePublic.setActorId(uuid);
			rolePublic.setName("Public");
			rolePublic.setType(TYPE);
			// init permission
			rolePublic.setPermissions(Arrays.asList(permissionRead));

			roleRepository.save(rolePublic);
		}

		return rolePublic;
	}

	@Override
	public void saveRelation(UUID userUUID, Contact contact)
			throws NoSuchUserException, NoRoleException, NoSuchContactException {
		// Here, the user is only allowed to edit the approve value if the
		// current value is = 2
		// User user = accountService.getUserFromUUID(userUUID);
		ContactDB contactdb;
		if ((contactdb = contactRepository.findContact(userUUID,
				UUID.fromString(contact.getUuid()))) == null) {
			throw new NoSuchContactException();
		}

		if (contactdb.getStatus() != contact.getAprouve()
				&& contactdb.getStatus() == 2 && contact.getAprouve() == 3) {
			contactdb.setStatus(3);

			ContactDB contactdb2;
			if ((contactdb2 = contactRepository.findContact(
					UUID.fromString(contact.getUuid()), userUUID)) != null) {
				contactdb2.setStatus(3);
				contactRepository.save(contactdb2);
			} else {
				
				try {
					rrs.setAprouveRelationORH(userUUID,
							UUID.fromString(contact.getUuid()));
				} catch (IOException e) {
					LOGGER.error("Can not set aprouve {} for {} Error IO",
							userUUID, contact.getActorID(), e);
					e.printStackTrace();
				} catch (NoSuchBoxException e) {
					LOGGER.error("Can not set aprouve {} for {} no box found",
							userUUID, contact.getActorID(), e);
				} catch (NoSuchUserException e) {
					LOGGER.error(
							"Can not set aprouve {} for {} user not found",
							userUUID, contact.getActorID(), e);
				}

				// Send a request to the box to tell it the user accepts the
				// relationship
			}

		}
		// Or, the user can edit the group his/her relationship is in.

		List<String> roleName = ContactDB.getRolesNames(contactdb.getRole());
		if (!roleName.equals(contact.getRole())) {
			contactdb.getRole().clear();
			for (String roleStr : contact.getRole()) {
				Role role;
				if ((role = roleRepository.findByName(roleStr,
						contactdb.getOwnerId(), TYPE)) != null)

					contactdb.getRole().add(role);
				else {
					role = new Role();
					role.setActorId(contactdb.getOwnerId());
					role.setName(roleStr);
					role.setType(TYPE);
					roleRepository.save(role);
				}
				// throw new NoRoleException();
			}

		}
		contactRepository.save(contactdb);
		return;
	}

	@Override
	public void setAprouveBox(UUID userUUID, UUID contactUUID) {
		ContactDB contactdb = contactRepository.findContact(userUUID,
				contactUUID);
		if (contactdb.getStatus() == 1) {
			contactdb.setStatus(3);
			contactRepository.save(contactdb);
		} else {
			LOGGER.error("Something has been changed...");
			throw new WebApplicationException(Status.FORBIDDEN);
		}

	}

	@Override
	public List<Content> getAllContent(UUID userUUID, UUID relationUUID) {
		try {
			List<Content> listContent = rcs.get(userUUID, relationUUID);
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
		ContactDB contactdb;
		try {
			if ((contactdb = contactRepository.findContact(userUUId,
					relationUUId)) != null) {
				contactRepository.delete(contactdb);
			} else {
				throw new NoRelationException();
			}
		} catch (NonUniqueResultException e) {
			LOGGER.error("No unique relation");
			throw e;
		}

	}

	@Override
	public void deleteRelation(UUID actorUUID, UUID contactUUID)
			throws NoSuchUserException, NoSuchBoxException, NoRelationException {
		ContactDB contactdb;
		deleteRelationBox(actorUUID, contactUUID);
		if ((contactdb = contactRepository.findContact(contactUUID, actorUUID)) != null) {
			contactRepository.delete(contactdb);

		} else {
			
			try {
				rrs.deleteRelationORH(actorUUID, contactUUID);
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
	}

	@Override
	public void creatDefaultUserRoles(UUID uuid) {
		initRolesAndGetPublic(uuid);
		Role roleUser;
		if ((roleUser = roleRepository.findByName("USER", uuid, "Box")) == null) {

			roleUser = new Role();
			roleUser.setActorId(uuid);
			roleUser.setName("USER");
			roleUser.setType("Box");
			// init permission
			Permission permission;
			if ((permission = permissionRepository.findByName("%BOXRead")) == null) {
				permission = new Permission("%BOXRead",
						"read stored information on the box");
				permissionRepository.save(permission);
			}
			Permission permission2;
			if ((permission2 = permissionRepository.findByName("%BOXText")) == null) {
				permission2 = new Permission("%BOXText",
						"store text(comment,link,... ) on the box");
				permissionRepository.save(permission2);
			}
			Permission permission3;
			if ((permission3 = permissionRepository.findByName("%BOXContent")) == null) {
				permission3 = new Permission("%BOXContent",
						"store content(video, picture,...) on the box");
				permissionRepository.save(permission3);
			}
			Permission permission4;
			if ((permission4 = permissionRepository.findByName("%BOXContact")) == null) {
				permission4 = new Permission("%BOXContact", "Can add relation");
				permissionRepository.save(permission4);
			}
			roleUser.setPermissions(Arrays.asList(permission, permission2,
					permission3, permission4));

			roleRepository.save(roleUser);
		}

	}

	@Override
	public void setContentRole(Content content) {
		List<String> roles = content.getMetadata();
		UUID uuid = UUID.fromString(content.getActorID());
		ActivityObjectExtand aOA;
		if ((aOA = activityObjectExtandRepo.findOne(content.getContentsID())) == null) {
			aOA = new ActivityObjectExtand();
			aOA.setId(content.getContentsID());
		}
		List<Role> roleToSave = new ArrayList<Role>();
		for (String roleStr : roles) {
			Role role;
			if ((role = roleRepository.findByName(roleStr, uuid, TYPE)) == null) {
				// if not exist create role with no permission
				role = new Role();
				role.setActorId(uuid);
				role.setName(roleStr);
				role.setType(TYPE);
				// ADD Permission to read
				Permission permission;
				if ((permission = permissionRepository.findByName(READ)) == null) {
					permission = new Permission("%BOXContact",
							"Can add relation");
					permissionRepository.save(permission);
				}
				role.setPermissions(Arrays.asList(permission));
				roleRepository.save(role);
			}
			roleToSave.add(role);

		}

		aOA.setRoles(roleToSave);
		activityObjectExtandRepo.save(aOA);

	}

	@Override
	public void getContentRole(Content content) {
		// List<String> roles = content.getMetadata();
		UUID uuid = UUID.fromString(content.getActorID());
		ActivityObjectExtand aOA;

		if ((aOA = activityObjectExtandRepo.findOne(content.getContentsID())) != null) {
			List<Role> roles = aOA.getRoles();
			for (Role role : roles) {
				content.getMetadata().add(role.getName());
			}
		}

	}

	@Override
	public List<ActivityObjectExtand> getActivityForContact(UUID actorUUID,
			UUID contactUUID) {
		// get list of roles of this contact
		List<Role> roles;
		try {
			ContactDB c = getContact(actorUUID, contactUUID);
			roles = c.getRole();

			if (roles.size() == 0) {
				Role role;
				if ((role = roleRepository
						.findByName("Public", actorUUID, TYPE)) == null) {
					LOGGER.error("this user {} a not role Public", actorUUID);
				}
				roles.add(role);
				LOGGER.info("this contact {} a not role Public", actorUUID);
			}

			Role roleOnly;
			if ((roleOnly = roleRepository.findByName("%" + contactUUID,
					actorUUID, TYPE)) == null) {
				LOGGER.error("this user {} a not role only for this user ",
						actorUUID, contactUUID);
			}
			roles.add(roleOnly);

			for (Role role : roles) {
				LOGGER.debug("role = {}", role);
			}
		} catch (NoSuchContactException e) {
			Role role;
			if ((role = roleRepository.findByName("Public", actorUUID, TYPE)) == null) {
				LOGGER.error("this user {} a not role Public", actorUUID);
			}
			roles = Arrays.asList(role);
		}

		List<ActivityObjectExtand> activityObjectExtand = new ArrayList<ActivityObjectExtand>();
		activityObjectExtand.addAll(activityObjectExtandRepo
				.findByRolesIn(roles));

		return activityObjectExtand;
	}

	@Override
	public void deleteActivityObject(Integer contentsID) {
		activityObjectExtandRepo.delete(contentsID);
		ActivityObjectExtand a;
		if((a = activityObjectExtandRepo.findOne(contentsID))!=null){
			LOGGER.error("can not deleting");
			
			List<Role> roles = a.getRoles();
			for (Role role : roles) {
				LOGGER.error("this role {} is linked", role.getName());
				
			}
		}
	}
}
