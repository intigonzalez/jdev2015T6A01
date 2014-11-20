package com.enseirb.telecom.s9.service;

import java.io.Serializable;

import com.enseirb.telecom.s9.db.UserDB;

public class userManager {
	// Check the existence of the user
	public static boolean userExist(Serializable id) {
		UserDB userDatabase = new UserDB();
		return userDatabase.exists(id);
	}
}
