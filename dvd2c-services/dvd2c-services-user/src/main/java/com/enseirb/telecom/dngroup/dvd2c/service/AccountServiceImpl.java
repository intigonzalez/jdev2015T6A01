package com.enseirb.telecom.dngroup.dvd2c.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enseirb.telecom.dngroup.dvd2c.ApplicationContext;
import com.enseirb.telecom.dngroup.dvd2c.db.UserRepository;
import com.enseirb.telecom.dngroup.dvd2c.db.UserRepositoryObject;
import com.enseirb.telecom.dngroup.dvd2c.exception.NoSuchUserException;
import com.enseirb.telecom.dngroup.dvd2c.exception.SuchUserException;
import com.enseirb.telecom.dngroup.dvd2c.service.request.RequestUserService;
import com.enseirb.telecom.dngroup.dvd2c.service.request.RequestUserServiceImpl;
import com.enseirb.telecom.dngroup.dvd2c.model.Box;
import com.enseirb.telecom.dngroup.dvd2c.model.User;

public class AccountServiceImpl implements AccountService {
	private static final Logger LOGGER = LoggerFactory.getLogger(AccountServiceImpl.class);

	UserRepository userDatabase;
	RequestUserService requetUserService = new RequestUserServiceImpl();

	public AccountServiceImpl(UserRepository userDatabase ) {
		this.userDatabase = userDatabase;
	}

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
			LOGGER.error("Can not connect on the server : {}",userID,e);
		} catch (NoSuchUserException e) {
			LOGGER.debug("User not found : {}",userID);
		}
		return exist;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enseirb.telecom.s9.service.AccountService#userExist(java.lang.String)
	 */
	@Override
	public boolean userExistOnLocal(String userID) {
		boolean exist = userDatabase.exists(userID);
		return exist;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enseirb.telecom.s9.service.AccountService#getUserOnLocal(java.lang.String)
	 */
	@Override
	public User getUserOnLocal(String email) {
		UserRepositoryObject user = userDatabase.findOne(email);
		if (user == null) {
			return null;
		} else {
			return user.toUser();
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
	public List<User> getUserFromNameOnServer(String name) {
		try {
			return requetUserService.getUserFromName(name);
		} catch (IOException e) {
			LOGGER.error("error during geting users on server : {} ",name,e);
			return null;
		}
	}

	@Override
	public List<User> getUserFromName(String name) {
		//DB: need to change
		Iterable<UserRepositoryObject> userIterable = userDatabase.findAll();
		UserRepositoryObject userRepo = null;
		List<User> listUser = new ArrayList<User>();
	
		if (userIterable == null)
			return listUser;
		else {
			Iterator<UserRepositoryObject> iterator = userIterable.iterator();
	
			while (iterator.hasNext()) {
				userRepo = iterator.next();
				if (userRepo.getName().equalsIgnoreCase(name))
					listUser.add(userRepo.toUser());
			}
			return listUser;
		}
	}

	public List<User> getUserFromBoxID(String boxID) {
		//DB: need to change
		Iterable<UserRepositoryObject> userIterable = userDatabase.findAll();
		UserRepositoryObject userRepo = null;
		 List<User> listUser = new ArrayList<User>();
	
		if (userIterable == null)
			return listUser;
		else {
			Iterator<UserRepositoryObject> iterator = userIterable.iterator();
	
			while (iterator.hasNext()) {
				userRepo = iterator.next();
				if (userRepo.getBoxID().equals(boxID))
					listUser.add(userRepo.toUser());
			}
			return listUser;
		}
	
	}

	@Override
	public User createUserOnServer(User user) {

		try {
			user.setBoxID(ApplicationContext.getProperties().getProperty("BoxID"));
			
			requetUserService.createUserORH(user);
			return createUserOnLocal(user);
		} catch (IOException e) {
			LOGGER.debug("error during creating user on server : {} ",user.getUserID(),e);
		} catch (SuchUserException e) {
			LOGGER.debug("User already existing {}",user.getUserID());
		}

		return user;
	}
	@Override
	public User createUserOnLocal(User user) {
		
		User u = userDatabase.save(new UserRepositoryObject(user)).toUser();
		return u;
	}

	@Override
	public void saveUserOnServer(User user) {
		try {
			requetUserService.updateUserORH(user);
			saveUserOnLocal(user);
		} catch (IOException e) {
			LOGGER.error("Error for Update this user on server : {}",user.getBoxID(),e);
		} catch (NoSuchUserException e) {
			LOGGER.error("Error for Update this user (no found user) : {}",user.getBoxID(),e);
		}
	}

	@Override
	public void saveUserOnLocal(User user) {
		userDatabase.save(new UserRepositoryObject(user));
	}

	@Override
	public void deleteUserOnServer(String userID) {
		try {
			requetUserService.deleteUserORH(userID);
			deleteUserOnLocal(userID);
		} catch (IOException e) {
			LOGGER.error("Error for delete this user on server : {}",userID,e);
		} catch (NoSuchUserException e) {
			LOGGER.error("Error for delete this user (no found user) : {}",userID,e);
		}
	}

	public void deleteUserOnLocal(String userID) {	
		this.userDatabase.delete(userID);
	}
	
	@Override
	public Box getBox(String userID) {
		return this.userDatabase.findBoxFromUserID(userID).toBox();
	}
	
	@Override
	public List<User> getUsersFromBoxes(List<Box> listBox) {

		AccountService uManager = this;
		List<User> listUsersFinal = new ArrayList<User>(), listUsersOfBoxes = new ArrayList<User>();

		List<User> u;
		User user;
		Box box = new Box();
		List<Box> boxes = listBox;

		Iterator<Box> itrBoxes = boxes.iterator();
		while (itrBoxes.hasNext()) {

			box = itrBoxes.next();
			listUsersOfBoxes = uManager.getUserFromBoxID(box.getBoxID());
			
			Iterator<User> itrUsers = listUsersOfBoxes.iterator();

			while (itrUsers.hasNext()) {

				user = itrUsers.next();
				listUsersFinal.add(user);
			}
		}

		return listUsersFinal;
	}

	@Override
	public List<User> getUsersFromListBoxes(List<Box> listBox) {
		return getUsersFromBoxes(listBox);
	}
}
