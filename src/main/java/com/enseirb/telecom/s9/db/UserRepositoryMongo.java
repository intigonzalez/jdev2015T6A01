package com.enseirb.telecom.s9.db;

import java.io.IOException;
import java.net.UnknownHostException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;

public class UserRepositoryMongo implements CrudRepository<UserRepositoryObject, String> {

	@Override
	public <S extends UserRepositoryObject> S save(S entity) {
		if (exists(entity.getUserID())) {
			delete(entity.getUserID());
		}
		try {
			MongoClient mongoClient = DbInit.Connect();
			DB db = mongoClient.getDB("mediahome");
			DBCollection dbUsers = db.getCollection("users");

			dbUsers.save(DbInit.createDBObject(entity));
			mongoClient.close();
		} catch (UnknownHostException | JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return entity;
	}

	@Override
	public UserRepositoryObject findOne(String id) {
		// The id is the email address
		try {
			MongoClient mongoClient = DbInit.Connect();
			DB db = mongoClient.getDB("mediahome");
			DBCollection dbUsers = db.getCollection("users");

			BasicDBObject query = new BasicDBObject("userID", id);
			DBCursor cursor = dbUsers.find(query);
			UserRepositoryObject user = null;
			ObjectMapper mapper = new ObjectMapper();
			if (cursor.hasNext()) {
				try {
					user = mapper.readValue(cursor.next().toString(), UserRepositoryObject.class);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.err.println("User Mapping failed ! ");
				}
			}
			return user;

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean exists(String id) {
		try {
			MongoClient mongoClient = DbInit.Connect();
			DB db = mongoClient.getDB("mediahome");
			DBCollection dbUsers = db.getCollection("users");

			BasicDBObject query = new BasicDBObject("userID", id);
			DBCursor cursor = dbUsers.find(query);

			if (cursor.hasNext()) {
				mongoClient.close();
				return true;
			} else {
				mongoClient.close();
				return false;
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("Connection to database failed ");
			return true;
		}
	}

	@Override
	public Iterable<UserRepositoryObject> findAll() {
		throw new RuntimeException("not yet invented");
	}

	@Override
	public Iterable<UserRepositoryObject> findAll(Iterable<String> ids) {
		throw new RuntimeException("not yet invented");
	}

	@Override
	public long count() {
		throw new RuntimeException("not yet invented");
	}

	@Override
	public void delete(String id) {
		try {
			MongoClient mongoClient = DbInit.Connect();
			DB db = mongoClient.getDB("mediahome");
			DBCollection dbUsers = db.getCollection("users");

			BasicDBObject query = new BasicDBObject("userID", id);
			dbUsers.remove(query);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.err.println("Connection to database failed ");
		}

	}

	@Override
	public void delete(UserRepositoryObject entity) {
		try {
			MongoClient mongoClient = DbInit.Connect();
			DB db = mongoClient.getDB("mediahome");
			DBCollection dbUsers = db.getCollection("users");
			BasicDBObject query = new BasicDBObject("userID", entity.getUserID());
			dbUsers.remove(query);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.err.println("Connection to database failed ");
		}

	}

	@Override
	public void delete(Iterable<? extends UserRepositoryObject> entities) {
		throw new RuntimeException("not yet invented");

	}

	@Override
	public void deleteAll() {
		throw new RuntimeException("not yet invented");

	}

	@Override
	public <S extends UserRepositoryObject> Iterable<S> save(Iterable<S> entities) {
		throw new RuntimeException("not yet invented");
	}

}
