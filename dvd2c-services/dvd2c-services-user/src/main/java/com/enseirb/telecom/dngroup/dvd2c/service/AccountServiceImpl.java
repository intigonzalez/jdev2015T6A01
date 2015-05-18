package com.enseirb.telecom.dngroup.dvd2c.service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.enseirb.telecom.dngroup.dvd2c.CliConfSingleton;
import com.enseirb.telecom.dngroup.dvd2c.db.BoxRepository;
import com.enseirb.telecom.dngroup.dvd2c.db.UserRepositoryOldObject;
import com.enseirb.telecom.dngroup.dvd2c.exception.NoSuchUserException;
import com.enseirb.telecom.dngroup.dvd2c.exception.SuchUserException;
import com.enseirb.telecom.dngroup.dvd2c.repository.ActorRepository;
import com.enseirb.telecom.dngroup.dvd2c.repository.UserRepository;
import com.enseirb.telecom.dngroup.dvd2c.service.request.RequestUserService;
import com.enseirb.telecom.dngroup.dvd2c.model.User;
import com.enseirb.telecom.dngroup.dvd2c.modeldb.Actor;

@Service
public class AccountServiceImpl implements AccountService {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(AccountServiceImpl.class);

	@Inject
	protected UserRepository userRepository;
	
	@Inject
	protected BoxRepository boxRepository;

	@Inject
	protected RequestUserService requetUserService;

	@Override
	public boolean userExistOnServer(String userID) {
		boolean exist = userExistOnLocal(userID);
		try {
			User userGet = requetUserService.get(userID);
			if (userGet == null)
				exist = false;
			else if (userGet.getUserID().equals(userID))
				exist = true;
		} catch (IOException e) {
			LOGGER.error("Can not connect on the server : {}", userID, e);
		} catch (NoSuchUserException e) {
			LOGGER.debug("User not found : {}", userID);
		}
		return exist;
	}
	
	@Override
	public User getContactInformation(String userID) {
		 com.enseirb.telecom.dngroup.dvd2c.modeldb.User user = userRepository.findByEmail(userID);

		if (user == null) {
			return null;
		} else {
			User userReturn = new User();
			userReturn.setFirstname(user.getFirstname());
			userReturn.setSurname(user.getSurname());
			userReturn.setUserID(user.getEmail());
			return userReturn;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enseirb.telecom.s9.service.AccountService#userExist(java.lang.String)
	 */
	@Override
	public boolean userExistOnLocal(String emailAddress) {
//RABC : need to verfy
		boolean exist = (userRepository.findByEmail(emailAddress) != null);
		
		return exist;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enseirb.telecom.s9.service.AccountService#getUserOnLocal(java.lang
	 * .String)
	 */
	@Override
	public User getUserOnLocal(String emailAddress) throws NoSuchUserException {
		 com.enseirb.telecom.dngroup.dvd2c.modeldb.User user = userRepository.findByEmail(emailAddress);
		
		if (user == null) {
			throw new NoSuchUserException();
		} else {
			User userXsd = new User();
			userXsd.setUserID(user.getEmail());
			userXsd.setFirstname(user.getFirstname());
			userXsd.setPassword(user.getEncryptedPassword());
			userXsd.setSurname(user.getSurname());
			userXsd.setBoxID(CliConfSingleton.boxID);
			return userXsd;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enseirb.telecom.s9.service.AccountService#createUser(com.enseirb.
	 * telecom.s9.User)
	 */
	@Override
	public List<User> getUserFromNameOnServer(String firstname) {
		try {
			return requetUserService.getUserFromName(firstname);
		} catch (IOException e) {
			LOGGER.error("error during geting users on server : {} ",
					firstname, e);
			return null;
		}
	}

	// RBAC: This class is only for central we don't want this here
//	@Override
//	public List<User> getUserFromName(String firstname) {
//		// DB: need to change
//		Iterable<UserRepositoryOldObject> userIterable = userRepository.findAll();
//		UserRepositoryOldObject userRepo = null;
//		List<User> listUser = new ArrayList<User>();
//
//		if (userIterable == null)
//			return listUser;
//		else {
//			Iterator<UserRepositoryOldObject> iterator = userIterable.iterator();
//
//			while (iterator.hasNext()) {
//				userRepo = iterator.next();
//
//				try {
//					if (userRepo.getFirstname().equalsIgnoreCase(firstname))
//						listUser.add(userRepo.toUser());
//				} catch (NullPointerException e) {
//					LOGGER.error("this user have not firstname {}",
//							userRepo.getUserID());
//				}
//			}
//			return listUser;
//		}
//	}
//
//	public List<User> getUserFromBoxID(String boxID) {
//		// DB: need to change
//		Iterable<UserRepositoryOldObject> userIterable = userRepository.findAll();
//		UserRepositoryOldObject userRepo = null;
//		List<User> listUser = new ArrayList<User>();
//
//		if (userIterable == null)
//			return listUser;
//		else {
//			Iterator<UserRepositoryOldObject> iterator = userIterable.iterator();
//
//			while (iterator.hasNext()) {
//				userRepo = iterator.next();
//				if (userRepo.getBoxID().equals(boxID))
//					listUser.add(userRepo.toUser());
//			}
//			return listUser;
//		}
//
//	}

	@Override
	public User createUserOnServer(User user) throws SuchUserException, IOException {

		// Spring: fixme
		try {
			UUID uuid = UUID.randomUUID();
			
			User userRestric = new User();
			userRestric.setBoxID(CliConfSingleton.boxID);
			userRestric.setFirstname(user.getFirstname());
			userRestric.setSurname(user.getSurname());
			userRestric.setUserID(user.getUserID());
			userRestric.setUuid(uuid.toString());
			requetUserService.createUserORH(userRestric);

			return createUserOnLocal(user);
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
		
		com.enseirb.telecom.dngroup.dvd2c.modeldb.User u = new com.enseirb.telecom.dngroup.dvd2c.modeldb.User();
		u.setEmail(user.getUserID());
		u.setFirstname(user.getFirstname());
		u.setSurname(user.getSurname());
		u.setEncryptedPassword(user.getPassword());
		u = userRepository.save(u);
		return user;
	}

	@Override
	public void saveUserOnServer(User user) {
		try {
			User userRestric = new User();
			userRestric.setBoxID(CliConfSingleton.boxID);
			userRestric.setFirstname(user.getFirstname());
			userRestric.setSurname(user.getSurname());
			userRestric.setUserID(user.getUserID());
			
			requetUserService.updateUserORH(userRestric);

			saveUserOnLocal(user);
		} catch (IOException e) {
			LOGGER.error("Error for Update this user on server : {}",
					user.getBoxID(), e);
		} catch (NoSuchUserException e) {
			LOGGER.error("Error for Update this user (no found user) : {}",
					user.getBoxID(), e);
		}
	}

	@Override
	public void saveUserOnLocal(User user) {
		com.enseirb.telecom.dngroup.dvd2c.modeldb.User u = new com.enseirb.telecom.dngroup.dvd2c.modeldb.User();
		u.setId(userRepository.findByEmail(user.getUserID()).getId());
		u.setEmail(user.getUserID());
		u.setFirstname(user.getFirstname());
		u.setEncryptedPassword(user.getPassword());
		
		userRepository.save(u);
	}

	@Override
	public void deleteUserOnServer(String userID) {
		try {
			requetUserService.deleteUserORH(userID);
			deleteUserOnLocal(userID);
		} catch (IOException e) {
			LOGGER.error("Error for delete this user on server : {}", userID, e);
		} catch (NoSuchUserException e) {
			LOGGER.error("Error for delete this user (no found user) : {}",
					userID, e);
		}
	}

	public void deleteUserOnLocal(String userID) {
		com.enseirb.telecom.dngroup.dvd2c.modeldb.User u = userRepository.findByEmail(userID);
		userRepository.delete(u);
	}

//	@Override
//	public Box getBox(String userID) throws NoSuchUserException {
//
//		UserRepositoryOldObject userRepositoryObject;
//		try {
//			userRepositoryObject = this.userRepository.findOne(userID);
//		} catch (Exception e) {
//			throw new NoSuchUserException();
//		}
//		String boxID = userRepositoryObject.getBoxID();
//		BoxRepositoryObject boxRepositoryObject = boxRepository.findOne(boxID);
//		return boxRepositoryObject.toBox();
//
//	}
//
//	@Override
//	public List<User> getUsersFromBoxes(List<Box> listBox) {
//
//		AccountService uManager = this;
//		List<User> listUsersFinal = new ArrayList<User>(), listUsersOfBoxes = new ArrayList<User>();
//
//		User user;
//		Box box = new Box();
//		List<Box> boxes = listBox;
//
//		Iterator<Box> itrBoxes = boxes.iterator();
//		while (itrBoxes.hasNext()) {
//
//			box = itrBoxes.next();
//			listUsersOfBoxes = uManager.getUserFromBoxID(box.getBoxID());
//
//			Iterator<User> itrUsers = listUsersOfBoxes.iterator();
//
//			while (itrUsers.hasNext()) {
//
//				user = itrUsers.next();
//				listUsersFinal.add(user);
//			}
//		}
//
//		return listUsersFinal;
//	}
//
//	@Override
//	public List<User> getUsersFromListBoxes(List<Box> listBox) {
//		return getUsersFromBoxes(listBox);
//	}

	@Override
	public boolean getUserVerification(String userID, String password)
			throws NoSuchUserException {
		if (password.equals(getUserOnLocal(userID).getPassword())) {
			return true;
		}
		return false;
	}

	
}
