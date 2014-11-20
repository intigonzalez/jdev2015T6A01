package com.enseirb.telecom.s9.service;

import com.enseirb.telecom.s9.User;

public interface AccountService {

	/**
	 * 
	 * @param id
	 * @return
	 */
	public abstract boolean userExist(String email);

	public abstract User getUser(String email);

	public abstract User createUser(User user);

	public abstract void saveUser(User user);

}