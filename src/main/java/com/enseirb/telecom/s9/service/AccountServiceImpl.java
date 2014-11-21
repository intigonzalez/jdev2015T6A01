package com.enseirb.telecom.s9.service;

import com.enseirb.telecom.s9.User;
import com.enseirb.telecom.s9.db.CrudRepository;
import com.enseirb.telecom.s9.db.UserRepositoryMongo;

public class AccountServiceImpl implements AccountService {

	CrudRepository<User, String> userDatabase;

	public AccountServiceImpl(CrudRepository<User, String> userDatabase) {
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

		return userDatabase.findOne(email);
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
		return userDatabase.save(user);

	}

	@Override
	public void saveUser(User user) {
		this.userDatabase.save(user);

	}

	@Override
	public void deleteUser(String email) {
		this.userDatabase.delete(email);
		
	}
}
