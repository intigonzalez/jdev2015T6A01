package com.enseirb.telecom.dngroup.dvd2c.db;

import org.springframework.stereotype.Service;

import com.enseirb.telecom.dngroup.dvd2c.db.mock.CrudRepositoryMock;
import com.enseirb.telecom.dngroup.dvd2c.repository.BoxRepositoryObject;


public class BoxRepositoryImplInMemory extends
		CrudRepositoryMock<BoxRepositoryObject> implements BoxRepository {

	@Override
	protected String getID(BoxRepositoryObject t) {
		return t.getBoxID();
	}

}
