package com.enseirb.telecom.dngroup.dvd2c.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.enseirb.telecom.dngroup.dvd2c.db.ContentRepositoryOld;
import com.enseirb.telecom.dngroup.dvd2c.db.ContentRepositoryOldObject;
import com.enseirb.telecom.dngroup.dvd2c.db.mock.CrudRepositoryMock;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;


public class ContentRepositoryImplInMemory extends
		CrudRepositoryMock<ContentRepositoryOldObject> implements
		ContentRepositoryOld {

	
	public List<ContentRepositoryOldObject> findAllFromUser(String userID) {
		return Lists.newArrayList(Iterables.filter(this.findAll(),
				new Predicate<ContentRepositoryOldObject>() {

					@Override
					public boolean apply(ContentRepositoryOldObject input) {
						return input.getActorID().equals(userID);
					}
				}));

	}

	@Override
	protected String getID(ContentRepositoryOldObject t) {

		return t.getId();
	}

}
