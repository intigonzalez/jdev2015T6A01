package com.enseirb.telecom.s9.db;

import java.io.IOException;
import java.net.UnknownHostException;

import com.enseirb.telecom.s9.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;

public class UserRepositoryMongo implements CrudRepository<User, String> {

	@Override
	public <S extends User> S save(S entity) {
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
	public User findOne(String id) {
		// TODO Auto-generated method stub
		// The id is the email address
		try {
			MongoClient mongoClient = DbInit.Connect();
			DB db = mongoClient.getDB("mediahome");
			DBCollection dbUsers = db.getCollection("users");

			BasicDBObject query = new BasicDBObject("mail", id);
			DBCursor cursor = dbUsers.find(query);
			User user = null;
			ObjectMapper mapper = new ObjectMapper();
			if (cursor.hasNext()) {
				try {
					user = mapper.readValue(cursor.next().toString(),
							User.class);
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
		// TODO Auto-generated method stub
		try {
			MongoClient mongoClient = DbInit.Connect();
			DB db = mongoClient.getDB("mediahome");
			DBCollection dbUsers = db.getCollection("users");

			BasicDBObject query = new BasicDBObject("mail", id);
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
	public Iterable<User> findAll() {
		throw new RuntimeException("not yet invented");
	}

	@Override
	public Iterable<User> findAll(Iterable<String> ids) {
		throw new RuntimeException("not yet invented");
	}

	@Override
	public long count() {
		throw new RuntimeException("not yet invented");
	}

	@Override
	public void delete(String id) {
		throw new RuntimeException("not yet invented");

	}

	@Override
	public void delete(User entity) {
		throw new RuntimeException("not yet invented");

	}

	@Override
	public void delete(Iterable<? extends User> entities) {
		throw new RuntimeException("not yet invented");

	}

	@Override
	public void deleteAll() {
		throw new RuntimeException("not yet invented");

	}

	@Override
	public <S extends User> Iterable<S> save(Iterable<S> entities) {
		throw new RuntimeException("not yet invented");
	}

}
