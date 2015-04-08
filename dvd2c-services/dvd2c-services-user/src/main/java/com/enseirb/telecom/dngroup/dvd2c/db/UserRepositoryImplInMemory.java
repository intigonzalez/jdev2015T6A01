package com.enseirb.telecom.dngroup.dvd2c.db;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.enseirb.telecom.dngroup.dvd2c.db.mock.CrudRepositoryMock;

@Service
public class UserRepositoryImplInMemory extends
		CrudRepositoryMock<UserRepositoryObject> implements UserRepository {

	@Inject
	BoxRepository boxrepo;

	@Override
	public BoxRepositoryObject findBoxFromUserID(String userID) {
		return boxrepo.findOne(this.findOne(userID).boxID);
	}

	@Override
	protected String getID(UserRepositoryObject t) {
		return t.userID;
	}

}
