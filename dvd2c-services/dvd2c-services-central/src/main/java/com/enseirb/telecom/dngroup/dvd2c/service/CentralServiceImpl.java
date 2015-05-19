package com.enseirb.telecom.dngroup.dvd2c.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;

import jersey.repackaged.com.google.common.base.Throwables;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.enseirb.telecom.dngroup.dvd2c.CliConfSingleton;
import com.enseirb.telecom.dngroup.dvd2c.db.BoxRepository;
import com.enseirb.telecom.dngroup.dvd2c.db.BoxRepositoryObject;
import com.enseirb.telecom.dngroup.dvd2c.db.CrudRepository;
import com.enseirb.telecom.dngroup.dvd2c.db.UserRepository;
import com.enseirb.telecom.dngroup.dvd2c.db.UserRepositoryObject;
import com.enseirb.telecom.dngroup.dvd2c.exception.NoSuchBoxException;
import com.enseirb.telecom.dngroup.dvd2c.exception.NoSuchUserException;
import com.enseirb.telecom.dngroup.dvd2c.exception.SuchUserException;
import com.enseirb.telecom.dngroup.dvd2c.model.Box;
import com.enseirb.telecom.dngroup.dvd2c.model.User;
import com.enseirb.telecom.dngroup.dvd2c.service.request.RequestBoxService;
import com.enseirb.telecom.dngroup.dvd2c.service.request.RequestBoxServiceImpl;
import com.enseirb.telecom.dngroup.dvd2c.service.request.RequestUserService;

@Service
public class CentralServiceImpl implements CentralService {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(CentralServiceImpl.class);
	@Inject
	BoxRepository boxRepository;

	@Inject
	RequestBoxService requetBoxService;

	@Inject
	protected UserRepository userRepository;

	@Inject
	protected RequestUserService requetUserService;

	@Override
	public boolean boxExistOnServer(String boxID) {
		boolean exist = boxExistOnLocal(boxID);
		try {
			Box boxGet = requetBoxService.get(boxID);
			if ((boxGet == null))
				exist = false;
			else if (boxID.equals(boxGet.getBoxID()))
				exist = true;
			else {
				exist = false;
			}
		} catch (IOException e) {
			LOGGER.error("Can not connect on the server : {}", boxID, e);
		} catch (NoSuchBoxException e) {
			exist = false;
		}
		return exist;
	}

	public boolean boxExistOnLocal(String boxID) {
		return boxRepository.exists(boxID);
	}

	public Box getBoxOnLocal(String boxID) throws NoSuchBoxException {

		BoxRepositoryObject box = null;
		box = boxRepository.findOne(boxID);

		if (box == null) {
			LOGGER.debug("No Box Found : {}", boxID);
			throw new NoSuchBoxException();
		}

		LOGGER.debug("Box Found : {}", box.getBoxID());
		return box.toBox();

	}

	public Box createBoxOnServer(Box box) {

		try {
			requetBoxService.createBoxORH(box);
		} catch (IOException e) {
			LOGGER.error("Error during creating a box on server : ",
					box.getBoxID(), e);
		}
		return createBoxOnLocal(box);

	}

	@Override
	public void updateBox() {
		Box box = new Box();
		box.setBoxID(CliConfSingleton.boxID);
		box.setIp(CliConfSingleton.publicAddr);
		createBoxOnServer(box);
	}

	public Box createBoxOnLocal(Box box) {

		Box b = boxRepository.save(new BoxRepositoryObject(box)).toBox();
		return b;

	}

	public void saveBoxOnServer(Box box) {

		try {

			requetBoxService.updateBoxORH(box);
			saveBoxOnLocal(box);
		} catch (IOException e) {
			LOGGER.error("can't save Box On Server : {}", box.getBoxID(), e);
			throw Throwables.propagate(e);
		} catch (NoSuchBoxException e) {
			LOGGER.error("Box not found: {}", box.getBoxID(), e);
			throw new WebApplicationException(Status.NOT_FOUND);
		}

	}

	public void saveBoxOnLocal(Box box) {
		boxRepository.save(new BoxRepositoryObject(box));
	}

	public void deleteBoxOnServer(String boxID) {

		try {
			requetBoxService.deleteBoxORH(boxID);
		} catch (IOException e) {
			LOGGER.error("can't delete box : {}", boxID, e);
		} catch (NoSuchBoxException e) {
			LOGGER.error("box not found : {}", boxID, e);
		}

		deleteBoxOnLocal(boxID);
	}

	public void deleteBoxOnLocal(String boxID) {
		this.boxRepository.delete(boxID);

	}

	@Override
	public List<Box> getBoxListFromIP(String ip) {
		List<Box> listBox = getBoxesFromIP(ip);
		return listBox;
	}

	@Override
	public List<Box> getAllBox() {
		Iterable<BoxRepositoryObject> boxIterable = boxRepository.findAll();
		List<Box> listBox = new ArrayList<Box>();
		if (boxIterable == null)
			return listBox;
		else {
			Iterator<BoxRepositoryObject> itr = boxIterable.iterator();
			while (itr.hasNext()) {
				BoxRepositoryObject box = itr.next();
				listBox.add(box.toBox());
			}
			return listBox;
		}
	}

	@Override
	public List<Box> getBoxesFromIP(String ip) {

		Iterable<BoxRepositoryObject> boxIterable = boxRepository.findAll();
		List<Box> listBox = new ArrayList<Box>();

		if (boxIterable == null)
			return listBox;
		else {
			Iterator<BoxRepositoryObject> itr = boxIterable.iterator();
			while (itr.hasNext()) {
				BoxRepositoryObject box = itr.next();

				if (box.getIp().equals(ip)) {
					listBox.add(box.toBox());
				}
			}
			return listBox;
		}

	}

	@Override
	public void sendGoogleCode(String actorID, Box box, String code)
			throws IOException {
		requetBoxService.sendOauthORH(actorID, box, code);
	}

	// /////////////////

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enseirb.telecom.s9.service.AccountService#userExist(java.lang.String)
	 */
	@Override
	public boolean userExistOnLocal(String userID) {
		// UserRepositoryObject user = userRepository.findOne(userID);
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

	@Override
	public List<User> getUserFromName(String firstname) {
		// DB: need to change
		// UserRepositoryObject userIterable1 = userRepository.findOne("da");
		// ArrayList<String> string = new ArrayList<String>() ;
		// string.add("da");
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
	public User createUserOnLocal(User user) {

		User u = userRepository.save(new UserRepositoryObject(user)).toUser();
		return u;
	}

	@Override
	public void saveUserOnLocal(User user) {
		userRepository.save(new UserRepositoryObject(user));
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

		List<User> listUsersFinal = new ArrayList<User>(), listUsersOfBoxes = new ArrayList<User>();

		User user;
		Box box = new Box();
		List<Box> boxes = listBox;

		Iterator<Box> itrBoxes = boxes.iterator();
		while (itrBoxes.hasNext()) {

			box = itrBoxes.next();
			listUsersOfBoxes = getUserFromBoxID(box.getBoxID());

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
