package com.enseirb.telecom.s9.db;

import java.io.IOException;
import java.io.Serializable;
import java.net.UnknownHostException;

import com.enseirb.telecom.s9.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;

public class UserDB implements CrudRepository {

	@Override
	public Object save(Object entity) {
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
	public Iterable save(Iterable entities) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object findOne(Serializable id) {
		// TODO Auto-generated method stub
		//The id is the email address
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
					user = mapper.readValue(cursor.next().toString(), User.class);
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
	public boolean exists(Serializable id) {
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
			}
			else {
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
