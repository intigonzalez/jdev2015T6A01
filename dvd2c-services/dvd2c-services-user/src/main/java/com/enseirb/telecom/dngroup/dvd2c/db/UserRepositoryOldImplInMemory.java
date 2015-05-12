package com.enseirb.telecom.dngroup.dvd2c.db;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.enseirb.telecom.dngroup.dvd2c.db.mock.CrudRepositoryMock;


public class UserRepositoryOldImplInMemory extends
		CrudRepositoryMock<UserRepositoryOldObject> implements UserRepositoryOld {

	@Inject
	BoxRepository boxrepo;

	

	@Override
	protected String getID(UserRepositoryOldObject t) {
		return t.userID;
	}

}
