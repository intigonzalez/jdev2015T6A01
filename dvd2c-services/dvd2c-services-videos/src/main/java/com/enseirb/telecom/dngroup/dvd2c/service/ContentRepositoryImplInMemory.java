package com.enseirb.telecom.dngroup.dvd2c.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.enseirb.telecom.dngroup.dvd2c.db.ContentRepository;
import com.enseirb.telecom.dngroup.dvd2c.db.ContentRepositoryObject;
import com.enseirb.telecom.dngroup.dvd2c.db.mock.CrudRepositoryMock;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

@Service
public class ContentRepositoryImplInMemory extends
		CrudRepositoryMock<ContentRepositoryObject> implements
		ContentRepository {

	@Override
	public List<ContentRepositoryObject> findAllFromUser(String userID) {
		return Lists.newArrayList(Iterables.filter(this.findAll(),
				new Predicate<ContentRepositoryObject>() {

					@Override
					public boolean apply(ContentRepositoryObject input) {
						return input.getActorID().equals(userID);
					}
				}));

	}

	@Override
	protected String getID(ContentRepositoryObject t) {

		return t.getId();
	}

}
