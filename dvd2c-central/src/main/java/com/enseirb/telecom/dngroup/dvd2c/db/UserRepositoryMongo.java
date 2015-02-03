package com.enseirb.telecom.dngroup.dvd2c.db;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.enseirb.telecom.dngroup.dvd2c.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;

public class UserRepositoryMongo implements UserRepositoryInterface {

	@Override
	public <S extends UserRepositoryObject> S save(S entity) {
		if (exists(entity.getUserID())) {
			entity = update(entity);
		} else {

			try {

				MongoClient mongoClient = DbInit.Connect();
				DB db = mongoClient.getDB("mediaCentral");
				DBCollection dbUsers = db.getCollection("users");

				dbUsers.save(DbInit.createDBObject(entity));
				mongoClient.close();
			} catch (UnknownHostException | JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}

		return entity;
	}

	/**
	 * The method update a user's profile
	 * 
	 * @param entity
	 * @return entity
	 */

	public <S extends UserRepositoryObject> S update(S entity) {

		MongoClient mongoClient;
		try {
			mongoClient = DbInit.Connect();

			DB db = mongoClient.getDB("mediaCentral");
			DBCollection dbBox = db.getCollection("users");
			BasicDBObject newDocument = new BasicDBObject();

			if (entity.getUserID() != null) {
				newDocument.append("$set", new BasicDBObject().append("userID",
						entity.getUserID()));
				BasicDBObject searchQuery = new BasicDBObject().append(
						"userID", entity.getUserID());
				dbBox.update(searchQuery, newDocument);
			}
			if (entity.getBoxID() != null) {
				newDocument.append("$set",
						new BasicDBObject().append("boxID", entity.getBoxID()));
				BasicDBObject searchQuery = new BasicDBObject().append(
						"userID", entity.getUserID());
				dbBox.update(searchQuery, newDocument);
			}
			if (entity.getName() != null) {// need to verify
				newDocument.append("$set",
						new BasicDBObject().append("name", entity.getName()));
				BasicDBObject searchQuery = new BasicDBObject().append(
						"userID", entity.getUserID());
				dbBox.update(searchQuery, newDocument);
			}
			if (entity.getFirstname() != null) {
				newDocument.append(
						"$set",
						new BasicDBObject().append("firstname",
								entity.getFirstname()));
				BasicDBObject searchQuery = new BasicDBObject().append(
						"userID", entity.getUserID());
				dbBox.update(searchQuery, newDocument);
			}
			if (entity.getPassword() != null) {
				newDocument.append(
						"$set",
						new BasicDBObject().append("password",
								entity.getPassword()));
				BasicDBObject searchQuery = new BasicDBObject().append(
						"userID", entity.getUserID());
				dbBox.update(searchQuery, newDocument);
			}
			if (entity.getPubKey() != null) {
				newDocument.append("$set", new BasicDBObject().append("pubKey",
						entity.getPubKey()));
				BasicDBObject searchQuery = new BasicDBObject().append(
						"userID", entity.getUserID());
				dbBox.update(searchQuery, newDocument);
			}

			mongoClient.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return entity;
	}

	/**
	 * Find a UserRepositoryObject in the database from the userID
	 * 
	 * @param userID
	 * @return UserRepositoryObject
	 */

	@Override
	public UserRepositoryObject findOne(String userID) {
		// The id is the email address
		try {
			MongoClient mongoClient = DbInit.Connect();
			DB db = mongoClient.getDB("mediaCentral");
			DBCollection dbUsers = db.getCollection("users");

			BasicDBObject query = new BasicDBObject("userID", userID);
			DBCursor cursor = dbUsers.find(query);
			UserRepositoryObject user = null;
			ObjectMapper mapper = new ObjectMapper();
			if (cursor.hasNext()) {
				try {
					user = mapper.readValue(cursor.next().toString(),
							UserRepositoryObject.class);
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

	/**
	 * Verify in the database if a UserRepositoryObject already exists
	 * 
	 * @param id
	 * @return boolean (true or false)
	 */

	@Override
	public boolean exists(String id) {
		try {
			MongoClient mongoClient = DbInit.Connect();
			DB db = mongoClient.getDB("mediaCentral");
			DBCollection dbUsers = db.getCollection("users");

			BasicDBObject query = new BasicDBObject();
			query.put("userID", Pattern.compile(id, Pattern.CASE_INSENSITIVE));
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

	/**
	 * Retrieve all the UserRepositoryObject in the database
	 * 
	 * @return List<UserRepositoryObject>
	 */

	@Override
	public Iterable<UserRepositoryObject> findAll() {

		List<UserRepositoryObject> listOfAllUsers = new ArrayList<UserRepositoryObject>();

		try {
			MongoClient mongoClient = DbInit.Connect();
			DB db = mongoClient.getDB("mediaCentral");
			DBCollection dbUsers = db.getCollection("users");

			ObjectMapper mapper = new ObjectMapper();
			UserRepositoryObject user = null;

			DBCursor cursor = dbUsers.find();

			while (cursor.hasNext()) {
				try {
					user = mapper.readValue(cursor.next().toString(),
							UserRepositoryObject.class);

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.err.println("User Mapping failed ! ");
				}

				listOfAllUsers.add(user);
			}

		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.err.println("Connection to database failed ");
		}
		return listOfAllUsers;

	}

	@Override
	public Iterable<UserRepositoryObject> findAll(Iterable<String> ids) {
		throw new RuntimeException("not yet invented");
	}

	/**
	 * Determine the total number of users
	 * 
	 * @return numberOfUser
	 */

	@Override
	public long count() {

		long numberOfUser = 0;

		try {
			MongoClient mongoClient = DbInit.Connect();
			DB db = mongoClient.getDB("mediaCentral");
			DBCollection dbUsers = db.getCollection("users");

			numberOfUser = dbUsers.getCount();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.err.println("Connection to database failed ");
		}

		return numberOfUser;

	}

	/**
	 * Delete a UserRepositoryObject from the database from the userID
	 * 
	 * @param id
	 */

	@Override
	public void delete(String id) {

		try {
			MongoClient mongoClient = DbInit.Connect();
			DB db = mongoClient.getDB("mediaCentral");
			DBCollection dbUsers = db.getCollection("users");

			BasicDBObject query = new BasicDBObject("userID", id);
			dbUsers.remove(query);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.err.println("Connection to database failed ");
		}

	}

	/**
	 * Delete a UserRepositoryObject from the database
	 * 
	 * @param id
	 */

	@Override
	public void delete(UserRepositoryObject entity) {
		try {
			MongoClient mongoClient = DbInit.Connect();
			DB db = mongoClient.getDB("mediaCentral");
			DBCollection dbUsers = db.getCollection("users");
			BasicDBObject query = new BasicDBObject("userID",
					entity.getUserID());
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
	public <S extends UserRepositoryObject> Iterable<S> save(
			Iterable<S> entities) {
		throw new RuntimeException("not yet invented");
	}

	@Override
	public BoxServRepositoryObject findBoxFromUserID(String userID) {
		// TODO Auto-generated method stub

		UserRepositoryMongo userRepositoryMongo = new UserRepositoryMongo();
		BoxRepositoryMongo boxRepositoryMongo = new BoxRepositoryMongo();

		UserRepositoryObject userRepositoryObject = userRepositoryMongo
				.findOne(userID);
		if (userRepositoryObject == null) {
			System.err.println("userRepositoryObject is null");
			return null;
		}

		User user = userRepositoryObject.toUser();
		String boxID = user.getBoxID();
		if (boxID == null) {
			System.err.println("The user box ID is null");
			return null;
		}
		BoxServRepositoryObject boxRepositoryObject = boxRepositoryMongo
				.findOne(boxID);

		return boxRepositoryObject;
	}

}
