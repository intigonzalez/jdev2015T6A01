package com.enseirb.telecom.s9.db;

import java.net.UnknownHostException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;

public class ContentRepositoryMongo implements CrudRepository<ContentRepositoryObject, MongoId> {

	@Override
	public <S extends ContentRepositoryObject> S save(S entity) {
		if (entity.getId() != null && exists(entity.getId())) {
			delete(entity.getId());
		}
		try {
			MongoClient mongoClient = DbInit.Connect();
			DBCollection db = mongoClient.getDB("mediahome").getCollection("contents");
			db.save(DbInit.createDBObject(entity));
			mongoClient.close();
		} catch (UnknownHostException | JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return entity;
	}

	@Override
	public <S extends ContentRepositoryObject> Iterable<S> save(Iterable<S> entities) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ContentRepositoryObject findOne(MongoId id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean exists(MongoId id) {
		try {
			MongoClient mongoClient = DbInit.Connect();
			DBCollection db = mongoClient.getDB("mediahome").getCollection("contents");

			BasicDBObject query = new BasicDBObject("_id", id);
			DBCursor cursor = db.find(query);
			boolean result = cursor.hasNext();
			mongoClient.close();
			return result;
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("Connection to database failed ");
			return true;
		}

	}

	@Override
	public Iterable<ContentRepositoryObject> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<ContentRepositoryObject> findAll(Iterable<MongoId> ids) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long count() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void delete(MongoId id) {
		try {
			MongoClient mongoClient = DbInit.Connect();
			DBCollection db = mongoClient.getDB("mediahome").getCollection("contents");

			BasicDBObject query = new BasicDBObject("_id", id);
			db.remove(query);
			mongoClient.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("Connection to database failed ");
		}
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
