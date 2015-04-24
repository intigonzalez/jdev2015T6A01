package com.enseirb.telecom.dngroup.dvd2c.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.enseirb.telecom.dngroup.dvd2c.CliConfSingleton;
import com.enseirb.telecom.dngroup.dvd2c.db.BoxRepository;
import com.enseirb.telecom.dngroup.dvd2c.db.BoxRepositoryObject;
import com.enseirb.telecom.dngroup.dvd2c.db.UserRepository;
import com.enseirb.telecom.dngroup.dvd2c.db.UserRepositoryObject;
import com.enseirb.telecom.dngroup.dvd2c.exception.NoSuchUserException;
import com.enseirb.telecom.dngroup.dvd2c.exception.SuchUserException;
import com.enseirb.telecom.dngroup.dvd2c.service.request.RequestUserService;
import com.enseirb.telecom.dngroup.dvd2c.model.Box;
import com.enseirb.telecom.dngroup.dvd2c.model.Permision;
import com.enseirb.telecom.dngroup.dvd2c.model.Role;
import com.enseirb.telecom.dngroup.dvd2c.model.User;

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enseirb.telecom.s9.service.AccountService#userExist(java.lang.String)
	 */
	@Override
	public boolean userExistOnLocal(String userID) {
//		UserRepositoryObject user = userRepository.findOne(userID);
		boolean exist = userRepository.exists(userID);
		
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
	public User getUserOnLocal(String email) throws NoSuchUserException {
		UserRepositoryObject user = userRepository.findOne(email);
		if (user == null) {
			throw new NoSuchUserException();
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
	public List<User> getUserFromNameOnServer(String firstname) {
		try {
			return requetUserService.getUserFromName(firstname);
		} catch (IOException e) {
			LOGGER.error("error during geting users on server : {} ",
					firstname, e);
			return null;
		}
	}

	@Override
	public List<User> getUserFromName(String firstname) {
		// DB: need to change
//		UserRepositoryObject userIterable1 = userRepository.findOne("da");
//		ArrayList<String> string = new ArrayList<String>() ;
//				string.add("da");
		Iterable<UserRepositoryObject> userIterable = userRepository.findAll();
		UserRepositoryObject userRepo = null;
		List<User> listUser = new ArrayList<User>();

		if (userIterable == null)
			return listUser;
		else {
			Iterator<UserRepositoryObject> iterator = userIterable.iterator();

			while (iterator.hasNext()) {
				userRepo = iterator.next();

				try {
					if (userRepo.getFirstname().equalsIgnoreCase(firstname))
						listUser.add(userRepo.toUser());
				} catch (NullPointerException e) {
					LOGGER.error("this user have not firstname {}",
							userRepo.getUserID());
				}
			}
			return listUser;
		}
	}

	public List<User> getUserFromBoxID(String boxID) {
		// DB: need to change
		Iterable<UserRepositoryObject> userIterable = userRepository.findAll();
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
	public User createUserOnServer(User user) throws SuchUserException, IOException {

		// Spring: fixme
		try {
			user.setBoxID(CliConfSingleton.boxID);


			requetUserService.createUserORH((new UserRepositoryObject(user)).toUser());

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
		initUser(user);
		User u = userRepository.save(new UserRepositoryObject(user)).toUser();
		return u;
	}

	@Override
	public void saveUserOnServer(User user) {
		try {

			requetUserService.updateUserORH((new UserRepositoryObject(user)).toUser());

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
		userRepository.save(new UserRepositoryObject(user));
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
		this.userRepository.delete(userID);
	}

	@Override
	public Box getBox(String userID) throws NoSuchUserException {

		UserRepositoryObject userRepositoryObject;
		try {
			userRepositoryObject = this.userRepository.findOne(userID);
		} catch (Exception e) {
			throw new NoSuchUserException();
		}
		String boxID = userRepositoryObject.getBoxID();
		BoxRepositoryObject boxRepositoryObject = boxRepository.findOne(boxID);
		return boxRepositoryObject.toBox();

	}

	@Override
	public List<User> getUsersFromBoxes(List<Box> listBox) {

		AccountService uManager = this;
		List<User> listUsersFinal = new ArrayList<User>(), listUsersOfBoxes = new ArrayList<User>();

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

	@Override
	public boolean getUserVerification(String userID, String password)
			throws NoSuchUserException {
		if (password.equals(getUserOnLocal(userID).getPassword())) {
			return true;
		}
		return false;
	}

	/**
	 * @param user
	 */
	private static String READ = "read";

	private static String COMMENT = "comment";

	private static String PUBLIC = "public";
	private static String PRIVATE = "private";
	private static String FRIEND = "firend";

	private static String METADATA = "metadata";
	private static String RELATION = "relation";

	private void initUser(User user) {
		Permision permisonPublic = new Permision();
		permisonPublic.setAction(READ);
		permisonPublic.setObject(PUBLIC);
		permisonPublic.setPermisionType(METADATA);
		permisonPublic.setInfo("can read tagged \"public\" content");
		Permision permisonPublicComment = new Permision();
		permisonPublicComment.setAction(READ);
		permisonPublicComment.setObject(PUBLIC);
		permisonPublicComment.setPermisionType(METADATA);
		permisonPublicComment.setInfo("can read tagged \"public\" content");

		Role rolePublic = new Role();
		rolePublic.setRoleId(PUBLIC);
		rolePublic.setInfo("This tie is the default group. It allow viewing your public content");
		rolePublic.getPermision().add(permisonPublic);
		rolePublic.getPermision().add(permisonPublicComment);
		user.getRole().add(rolePublic);
		
		Permision permisonPrivateRead = new Permision();
		permisonPrivateRead.setAction(READ);
		permisonPrivateRead.setObject(PRIVATE);
		permisonPrivateRead.setPermisionType(METADATA);
		permisonPrivateRead.setInfo("can see tagged \"private\" content");
		Permision permisonPrivateComment = new Permision();
		permisonPrivateComment.setAction(COMMENT);
		permisonPrivateComment.setObject(PRIVATE);
		permisonPrivateComment.setPermisionType(METADATA);
		permisonPrivateComment.setInfo("can see tagged \"private\" content");
		Permision permisonPrivateRelation = new Permision();
		permisonPrivateRelation.setAction(READ);
		permisonPrivateRelation.setObject(PRIVATE);
		permisonPrivateRelation.setPermisionType(RELATION);
		permisonPrivateRelation.setInfo("can see tagged \"private\" content");
		Role rolePrive = new Role();
		rolePrive.setRoleId(PRIVATE);
		rolePrive.setInfo("it can read and comment on the content tagged \"private\"");
		rolePrive.getPermision().add(permisonPrivateRead);
		rolePrive.getPermision().add(permisonPrivateComment);
		rolePrive.getPermision().add(permisonPrivateRelation);
		rolePrive.getPermision().addAll(rolePublic.getPermision());
		user.getRole().add(rolePrive);
		
		Permision permisonFriendRead = new Permision();
		permisonFriendRead.setAction(READ);
		permisonFriendRead.setObject(FRIEND);
		permisonFriendRead.setPermisionType(METADATA);
		permisonFriendRead.setInfo("can see tagged \"private\" content");
		Permision permisonFriendComment = new Permision();
		permisonFriendComment.setAction(COMMENT);
		permisonFriendComment.setObject(FRIEND);
		permisonFriendComment.setPermisionType(METADATA);
		permisonFriendComment.setInfo("can comment tagged \"private\" content");
		Permision permisonFriendRelation = new Permision();
		permisonFriendRelation.setAction(READ);
		permisonFriendRelation.setObject(FRIEND);
		permisonFriendRelation.setPermisionType(RELATION);

		permisonFriendRelation.setInfo("can see friendlist");
		Role role = new Role();
		role.setRoleId("Friend");
		role.setInfo("it can read and comment on the content tagged \"private\"");
		role.getPermision().add(permisonFriendRead);
		role.getPermision().add(permisonFriendComment);
		role.getPermision().add(permisonFriendRelation);
		user.getRole().add(role);
	}
}
