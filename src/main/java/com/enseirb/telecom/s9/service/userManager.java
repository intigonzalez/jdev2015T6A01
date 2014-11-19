package com.enseirb.telecom.s9.service;

import java.io.Serializable;

import com.enseirb.telecom.s9.db.UserDB;

import sun.security.provider.certpath.OCSPResponse.ResponseStatus;

public class userManager {
	public static ResponseStatus userExist(Serializable id) {
		UserDB userDatabase = new UserDB();
		if (true == userDatabase.exists(id)) {
			return ResponseStatus.UNAUTHORIZED;
		} else {
			return ResponseStatus.SUCCESSFUL;
		}
	}
}
