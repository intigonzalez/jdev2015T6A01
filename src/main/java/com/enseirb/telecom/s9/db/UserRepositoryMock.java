package com.enseirb.telecom.s9.db;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.enseirb.telecom.s9.User;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * this class has an in memory user repo
 * 
 * @author nherbaut
 *
 */
public class UserRepositoryMock implements CrudRepository<User, String> {

	private static ConcurrentHashMap<String, User> backupMap;
	{
		backupMap = new ConcurrentHashMap<String, User>();
		User nicolas = new User();
		nicolas.setUserID("nicolas@mirlitone.com");
//		nicolas.setLogin("nico");
		nicolas.setName("nicolas");
		nicolas.setPassword("1234");
		nicolas.setPrivateKey("private");
		nicolas.setPubKey("pub");
		nicolas.setSurname("Hrb");

		backupMap.put(nicolas.getUserID(), nicolas);

	}

	@Override
	public <S extends User> S save(S entity) {

		backupMap.put(entity.getUserID(), entity);
		return entity;

	}

	@Override
	public <S extends User> Iterable<S> save(Iterable<S> entities) {
		for (User u : entities) {
			this.save(u);
		}

		return entities;
	}

	@Override
	public User findOne(String id) {
		return backupMap.get(id);
	}

	@Override
	public boolean exists(String id) {
		if (id == null)
			return false;
		return backupMap.contains(id);
	}

	@Override
	public Iterable<User> findAll() {
		return backupMap.values();
	}

	@Override
	public Iterable<User> findAll(Iterable<String> ids) {

		Set<User> users = new HashSet<User>();
		Set<String> keys = Sets.newHashSet(ids);
		for (User u : this.findAll()) {
			if (keys.contains(u.getUserID())) {
				users.add(u);
			}
		}

		return users;

	}

	@Override
	public long count() {
		return Integer
				.valueOf(new ArrayList<String>(backupMap.keySet()).size());
	}

	@Override
	public void delete(String id) {
		backupMap.remove(id);

	}

	@Override
	public void delete(User entity) {
		this.delete(entity.getUserID());
		;

	}

	@Override
	public void delete(Iterable<? extends User> entities) {

		for (User u : this.findAll(Lists.transform(
				Lists.newArrayList(entities), new Function<User, String>() {

					@Override
					public String apply(User arg0) {
						return arg0.getUserID();
					}

				}))) {

			this.delete(u);

		}
		;

	}

	@Override
	public void deleteAll() {
		this.backupMap.clear();

	}

}
