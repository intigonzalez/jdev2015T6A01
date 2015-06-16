package com.enseirb.telecom.dngroup.dvd2c.service;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import jersey.repackaged.com.google.common.base.Predicate;
import jersey.repackaged.com.google.common.collect.Collections2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.enseirb.telecom.dngroup.dvd2c.CliConfSingleton;
import com.enseirb.telecom.dngroup.dvd2c.exception.NoSuchUserException;
import com.enseirb.telecom.dngroup.dvd2c.exception.SuchUserException;
import com.enseirb.telecom.dngroup.dvd2c.model.Property;
import com.enseirb.telecom.dngroup.dvd2c.model.PropertyGroups;
import com.enseirb.telecom.dngroup.dvd2c.modeldb.PropertyDB;
import com.enseirb.telecom.dngroup.dvd2c.modeldb.PropertyGroupsDB;
import com.enseirb.telecom.dngroup.dvd2c.modeldb.User;
import com.enseirb.telecom.dngroup.dvd2c.repository.PropertyGroupsRepository;
import com.enseirb.telecom.dngroup.dvd2c.repository.PropertyRepository;
import com.enseirb.telecom.dngroup.dvd2c.repository.UserRepository;
import com.enseirb.telecom.dngroup.dvd2c.service.request.RequestUserService;
import com.google.common.base.Strings;

@Service
public class AccountServiceImpl implements AccountService {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(AccountServiceImpl.class);

	@Inject
	protected UserRepository userRepository;

	@Inject
	protected PropertyGroupsRepository propertyGroupsRepository;

	@Inject
	protected RequestUserService requetUserService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enseirb.telecom.s9.service.AccountService#userExist(java.lang.String)
	 */
	@Override
	public boolean userExistOnLocal(UUID userUUID) {
		boolean exist = (userRepository.findOne(userUUID) != null);

		return exist;
	}

	@Override
	public boolean userExistOnServer(UUID userID) {
		boolean exist = userExistOnLocal(userID);
		if (!exist) {
			try {
				User userGet = new User(requetUserService.get(userID));
				if (userGet.getId().equals(userID))
					exist = true;
			} catch (IOException e) {
				LOGGER.error("Can not connect on the server : {}", userID, e);
			} catch (NoSuchUserException e) {
				LOGGER.debug("User not found : {}", userID);
			}
		}
		return exist;
	}

	@Override
	public User getContactInformation(UUID userID) throws NoSuchUserException {
		com.enseirb.telecom.dngroup.dvd2c.modeldb.User user = userRepository
				.findOne(userID);

		if (user == null) {
			throw new NoSuchUserException();
		} else {

			return user;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enseirb.telecom.s9.service.AccountService#getUserOnLocal(java.lang
	 * .String)
	 */
	@Override
	public User findUserByUUID(UUID userUUID) throws NoSuchUserException {
		User user = userRepository.findOne(userUUID);

		if (user == null) {
			throw new NoSuchUserException();
		} else {
			// User userXsd = new User();
			// userXsd.setUserID(user.getEmail());
			// userXsd.setFirstname(user.getFirstname());
			// userXsd.setPassword(user.getEncryptedPassword());
			// userXsd.setSurname(user.getSurname());
			// userXsd.setBoxID(CliConfSingleton.boxID);
			// userXsd.setUuid(user.getId().toString());
			return user;
		}
	}

	@Override
	public User findUserByEmail(String userID) throws NoSuchUserException {
		com.enseirb.telecom.dngroup.dvd2c.modeldb.User user;
		if ((user = userRepository.findByEmail(userID)) == null) {
			throw new NoSuchUserException();
		}
		return user;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enseirb.telecom.s9.service.AccountService#createUser(com.enseirb.
	 * telecom.s9.User)
	 */
	@Override
	public List<com.enseirb.telecom.dngroup.dvd2c.model.User> findUserByNameOnServer(
			String firstname) {
		try {
			return requetUserService.getUserFromName(firstname);
		} catch (IOException e) {
			LOGGER.error("error during geting users on server : {} ",
					firstname, e);
			return null;
		}
	}

	// RBAC: This class is only for central we don't want this here
	// @Override
	// public List<User> getUserFromName(String firstname) {
	// // DB: need to change
	// Iterable<UserRepositoryOldObject> userIterable =
	// userRepository.findAll();
	// UserRepositoryOldObject userRepo = null;
	// List<User> listUser = new ArrayList<User>();
	//
	// if (userIterable == null)
	// return listUser;
	// else {
	// Iterator<UserRepositoryOldObject> iterator = userIterable.iterator();
	//
	// while (iterator.hasNext()) {
	// userRepo = iterator.next();
	//
	// try {
	// if (userRepo.getFirstname().equalsIgnoreCase(firstname))
	// listUser.add(userRepo.toUser());
	// } catch (NullPointerException e) {
	// LOGGER.error("this user have not firstname {}",
	// userRepo.getUserID());
	// }
	// }
	// return listUser;
	// }
	// }
	//
	// public List<User> getUserFromBoxID(String boxID) {
	// // DB: need to change
	// Iterable<UserRepositoryOldObject> userIterable =
	// userRepository.findAll();
	// UserRepositoryOldObject userRepo = null;
	// List<User> listUser = new ArrayList<User>();
	//
	// if (userIterable == null)
	// return listUser;
	// else {
	// Iterator<UserRepositoryOldObject> iterator = userIterable.iterator();
	//
	// while (iterator.hasNext()) {
	// userRepo = iterator.next();
	// if (userRepo.getBoxID().equals(boxID))
	// listUser.add(userRepo.toUser());
	// }
	// return listUser;
	// }
	//
	// }

	@Override
	public User createUserOnServer(
			com.enseirb.telecom.dngroup.dvd2c.model.User user)
			throws SuchUserException, IOException {

		// Spring: fixme
		try {

			com.enseirb.telecom.dngroup.dvd2c.model.User userRestric = new com.enseirb.telecom.dngroup.dvd2c.model.User();
			userRestric.setBoxID(CliConfSingleton.boxID);
			userRestric.setFirstname(user.getFirstname());
			userRestric.setSurname(user.getSurname());
			userRestric.setUserID(user.getUserID());

			URI uri = requetUserService.createUserORH(userRestric);
			String path = uri.getPath();
			String uuidStr = path.substring(path.lastIndexOf('/') + 1);
			user.setUuid(uuidStr);
			return (createUserOnLocal(new User(user)));

		} catch (IOException e) {
			LOGGER.debug("error during creating user on server : {} ",
					user.getUserID(), e);
			throw e;
		} catch (SuchUserException e) {
			LOGGER.debug("User already existing {}", user.getUserID());
			throw e;
		}

	}

	@Override
	public User createUserOnLocal(User user) {

		User user2 = userRepository.save(user);
		return user2;
	}

	@Override
	public void saveUserOnServer(
			com.enseirb.telecom.dngroup.dvd2c.model.User user)
			throws NoSuchUserException {
		try {
			User userdb = new User(user);
			user.setPassword(null);
			user.setBoxID(CliConfSingleton.boxID);
			requetUserService.updateUserORH(user);

			saveUserOnLocal(userdb);

		} catch (IOException e) {
			LOGGER.error("Error for Update this user on server : {}",
					user.getBoxID(), e);
		} catch (NoSuchUserException e) {
			LOGGER.error("Error for Update this user (no found user) : {}",
					user.getBoxID(), e);
			throw e;
		}
	}

	@Override
	public void saveUserOnLocal(User user) {
		User u = userRepository.findOne(user.getId());
		if (!Strings.isNullOrEmpty(user.getEmail()))
			u.setEmail(user.getEmail());
		if (!Strings.isNullOrEmpty(user.getFirstname()))
			u.setFirstname(user.getFirstname());
		if (!Strings.isNullOrEmpty(user.getSurname()))
			u.setSurname(user.getSurname());

		userRepository.save(u);
	}

	@Override
	public void deleteUserOnServer(UUID userUUID) throws IOException,
			NoSuchUserException {

		requetUserService.deleteUserORH(userUUID);
		deleteUserOnLocal(userUUID);

	}

	@Override
	public void deleteUserOnLocal(UUID userUUID) {
		com.enseirb.telecom.dngroup.dvd2c.modeldb.User u = userRepository
				.findOne(userUUID);
		userRepository.delete(u);
	}

	@Override
	public boolean getUserVerification(String userUUID, String password)
			throws NoSuchUserException {
		if (password.equals(findUserByEmail(userUUID).getEncryptedPassword())) {
			return true;
		}
		return false;
	}

	// TODO : verify this fonction

	@Override
	public List<Property> getPropertiesForUser(UUID userId,
			final String propertyGroupName) {
		User user = userRepository.findOne(userId);
		PropertyGroupsDB groups=propertyGroupsRepository.findByNameAndUser(
				propertyGroupName, user);
		ArrayList<Property> property = new ArrayList<Property>();
		for (PropertyDB propertydb : groups.getProperty()) {
			Property p= new Property();
			p.setKey(propertydb.getKey());
			p.setValue(propertydb.getValue());
			property.add(p);
		}
		return property;
	}

	@Override
	public void setPropertiesForUser(final UUID userId,
			PropertyGroups propertyGroups) {

		User user = userRepository.findOne(userId);
		com.enseirb.telecom.dngroup.dvd2c.modeldb.PropertyGroupsDB propertyGroupOnDB;
		if ((propertyGroupOnDB = propertyGroupsRepository.findByNameAndUser(
				propertyGroups.getName(), user)) == null) {
			propertyGroupOnDB = new com.enseirb.telecom.dngroup.dvd2c.modeldb.PropertyGroupsDB();
			propertyGroupOnDB.setName(propertyGroups.getName());
			propertyGroupOnDB.setUser(user);
		} else {
			propertyGroupsRepository.deleteProperties(propertyGroupOnDB.getId());
		}
		ArrayList<PropertyDB> propertyDBs = new ArrayList<PropertyDB>();
		for (Property property : propertyGroups.getProperty()) {
			PropertyDB p = new PropertyDB();
			p.setKey(property.getKey());
			p.setValue(property.getValue());
			p.setPropertyGroups(propertyGroupOnDB);
			propertyDBs.add(p);
		}
		propertyGroupOnDB.setProperty(propertyDBs);
		propertyGroupsRepository.save(propertyGroupOnDB);
	}
}
