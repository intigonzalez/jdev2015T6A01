package com.enseirb.telecom.s9.service;

import com.enseirb.telecom.s9.User;
import com.enseirb.telecom.s9.db.CrudRepository;
import com.enseirb.telecom.s9.db.UserRepositoryObject;

public class AccountServiceImpl implements AccountService {

	CrudRepository<UserRepositoryObject, String> userDatabase;

	public AccountServiceImpl(CrudRepository<UserRepositoryObject, String> userDatabase) {
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
		UserRepositoryObject user =userDatabase.findOne(email);
		if (user == null ) {
			return null;
		}
		else {
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
	public User createUser(User user) {
		return userDatabase.save(new UserRepositoryObject(user)).toUser();

	}

	@Override
	public void saveUser(User user) {
		userDatabase.save(new UserRepositoryObject(user));

	}

	@Override
	public void deleteUser(String email) {
		this.userDatabase.delete(email);
		
	}
}
