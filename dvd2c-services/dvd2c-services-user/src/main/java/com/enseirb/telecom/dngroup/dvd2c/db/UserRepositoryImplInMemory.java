package com.enseirb.telecom.dngroup.dvd2c.db;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.enseirb.telecom.dngroup.dvd2c.db.mock.CrudRepositoryMock;


public class UserRepositoryImplInMemory extends
		CrudRepositoryMock<UserRepositoryObject> implements UserRepository {

	@Inject
	BoxRepository boxrepo;

	

	@Override
	protected String getID(UserRepositoryObject t) {
		return t.userID;
	}

}
