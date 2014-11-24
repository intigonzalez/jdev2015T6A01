package com.enseirb.telecom.s9.db;

public class ContentRepositoryMongo implements CrudRepository<ContentRepositoryObject, String> {

	@Override
	public <S extends ContentRepositoryObject> S save(S entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends ContentRepositoryObject> Iterable<S> save(Iterable<S> entities) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ContentRepositoryObject findOne(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean exists(String id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Iterable<ContentRepositoryObject> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<ContentRepositoryObject> findAll(Iterable<String> ids) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long count() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void delete(String id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(ContentRepositoryObject entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Iterable<? extends ContentRepositoryObject> entities) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAll() {
		// TODO Auto-generated method stub
		
	}

}
