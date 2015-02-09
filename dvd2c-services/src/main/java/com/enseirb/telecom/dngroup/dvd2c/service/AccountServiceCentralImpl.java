package com.enseirb.telecom.dngroup.dvd2c.service;

import java.util.Iterator;

import com.enseirb.telecom.dngroup.dvd2c.db.UserRepository;
import com.enseirb.telecom.dngroup.dvd2c.db.UserRepositoryMongo;
import com.enseirb.telecom.dngroup.dvd2c.db.UserRepositoryObject;
import com.enseirb.telecom.dngroup.dvd2c.model.Box;
import com.enseirb.telecom.dngroup.dvd2c.model.ListUser;
import com.enseirb.telecom.dngroup.dvd2c.model.User;

public class AccountServiceCentralImpl implements AccountServiceCentral {

	// CrudRepository<UserRepositoryObject, String> userDatabase;
	UserRepository userDatabase;

	// public AccountServiceImpl(CrudRepository<UserRepositoryObject, String>
	// userDatabase) {
	// this.userDatabase = userDatabase;
	// }
	public AccountServiceCentralImpl(UserRepository userDatabase) {
		this.userDatabase = userDatabase;
	}




	





	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enseirb.telecom.s9.service.AccountService#userExist(java.lang.String)
	 */
	@Override
	public boolean userExist(String email) {

		return userDatabase.exists(email);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enseirb.telecom.s9.service.AccountService#getUser(java.lang.String)
	 */
	@Override
	public User getUser(String email) {
		UserRepositoryObject user = userDatabase.findOne(email);
		if (user == null) {
			return null;
		} else {
			return user.toUser();
		}

	}

	@Override
	public ListUser getUserFromName(String name) {
		// TODO Auto-generated method stub

		Iterable<UserRepositoryObject> userIterable = userDatabase.findAll();
		UserRepositoryObject userRepo = null;
		ListUser listUser = new ListUser();

		if (userIterable == null)
			return listUser;
		else {
			Iterator<UserRepositoryObject> iterator = userIterable.iterator();

			while (iterator.hasNext()) {
				userRepo = iterator.next();
				if (userRepo.getName().equalsIgnoreCase(name))
					listUser.getUser().add(userRepo.toUser());
			}
			return listUser;
		}
	}

	public ListUser getUserFromBoxID(String boxID) {

		Iterable<UserRepositoryObject> userIterable = userDatabase.findAll();
		UserRepositoryObject userRepo = null;
		ListUser listUser = new ListUser();

		if (userIterable == null)
			return listUser;
		else {
			Iterator<UserRepositoryObject> iterator = userIterable.iterator();

			while (iterator.hasNext()) {
				userRepo = iterator.next();
				if (userRepo.getBoxID().equals(boxID))
					listUser.getUser().add(userRepo.toUser());
			}
			return listUser;
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
	public User createUser(User user) {

		/*
		 * BoxRepositoryMongo boxRepositoryMongo = new BoxRepositoryMongo();
		 * 
		 * String boxID = user.getBoxID(); if(boxID == null){
		 * System.err.println("The user box ID is null"); return null; }
		 * BoxServRepositoryObject boxRepositoryObject =
		 * boxRepositoryMongo.findOne(boxID); Box box =
		 * boxRepositoryObject.toBox(); User u = new User();
		 * u.setUserID(user.getUserID()); box.getUser().add(u);
		 * 
		 * BoxService boxService = new BoxServiceImpl(new BoxRepositoryMongo());
		 * boxService.saveBox(box);
		 */

		return userDatabase.save(new UserRepositoryObject(user)).toUser();

	}

	@Override
	public void saveUser(User user) {
		userDatabase.save(new UserRepositoryObject(user));

	}

	@Override
	public void deleteUser(String email) {

		/*
		 * UserRepositoryMongo userRepositoryMongo = new UserRepositoryMongo();
		 * BoxRepositoryMongo boxRepositoryMongo = new BoxRepositoryMongo();
		 * 
		 * UserRepositoryObject userRepositoryObject =
		 * userRepositoryMongo.findOne(email);
		 * 
		 * User user = userRepositoryObject.toUser();
		 * 
		 * String boxID = user.getBoxID(); if(boxID == null){
		 * System.out.println(user.getUserID());
		 * System.err.println("The user box ID is null"); return; }
		 * BoxServRepositoryObject boxRepositoryObject =
		 * boxRepositoryMongo.findOne(boxID); Box box =
		 * boxRepositoryObject.toBox(); User u = new User();
		 * 
		 * u.setUserID(user.getUserID());
		 * 
		 * if(!box.getUser().remove(u)){
		 * 
		 * System.out.println("User has not been removed from the List");
		 * return; } BoxService boxService = new BoxServiceImpl(new
		 * BoxRepositoryMongo()); boxService.saveBox(box);
		 */

		this.userDatabase.delete(email);

	}

	@Override
	public Box getBox(String userID) {
		// TODO Auto-generated method stub

		return this.userDatabase.findBoxFromUserID(userID).toBox();
	}

}
