package com.enseirb.telecom.s9.db.mock;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.enseirb.telecom.s9.User;
import com.enseirb.telecom.s9.db.CrudRepository;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * this class has an in memory repo for objects with string primary key
 * 
 * @author nherbaut
 *
 */
public abstract class CrudRepositoryMock<T> implements
		CrudRepository<T, String> {

	private Map<String, T> backupMap = new ConcurrentHashMap<String, T>();

	protected abstract String getID(T t);

	@Override
	public <S extends T> S save(S entity) {

		backupMap.put(getID(entity), entity);
		return entity;

	}

	@Override
	public <S extends T> Iterable<S> save(Iterable<S> entities) {
		for (S u : entities) {
			this.save(u);
		}

		return entities;
	}

	@Override
	public T findOne(String id) {
		return backupMap.get(id);
	}

	@Override
	public boolean exists(String id) {
		if (id == null)
			return false;
		return backupMap.containsKey(id);
	}

	@Override
	public Iterable<T> findAll() {
		return backupMap.values();
	}

	@Override
	public Iterable<T> findAll(Iterable<String> ids) {

		Set<T> users = new HashSet<T>();
		Set<String> keys = Sets.newHashSet(ids);
		for (T u : this.findAll()) {
			if (keys.contains(getID(u))) {
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
	public void delete(T entity) {
		this.delete(getID(entity));
		;

	}

	@Override
	public void delete(Iterable<? extends T> entities) {

		for (T u : this.findAll(Lists.transform(Lists.newArrayList(entities),
				new Function<T, String>() {

					@Override
					public String apply(T arg0) {
						return getID(arg0);
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
