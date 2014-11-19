package com.enseirb.telecom.s9.db;

import java.io.Serializable;
import java.net.UnknownHostException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

public class UserDB implements CrudRepository {

	@Override
	public Object save(Object entity) {
		// Maybe the content should be checked....
		try {
			/* Init MangoDB */
			MongoClient mongoClient = DbInit.Connect();
			DB db = mongoClient.getDB("mediahome");
			DBCollection dbUsers = db.getCollection("users");
			
			dbUsers.save(DbInit.createDBObject(entity));
		} catch (UnknownHostException | JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return entity;
	}

	@Override
	public Iterable save(Iterable entities) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object findOne(Serializable id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean exists(Serializable id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Iterable findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable findAll(Iterable ids) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long count() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void delete(Serializable id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(Object entity) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(Iterable entities) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteAll() {
		// TODO Auto-generated method stub

	}

}
