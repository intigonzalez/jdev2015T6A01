package com.enseirb.telecom.dngroup.dvd2c.db;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoTimeoutException;

public class BoxRepositoryMongo implements BoxRepository {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(BoxRepositoryMongo.class);
	private String dbName;

	public BoxRepositoryMongo(String dbName) {
		this.dbName = dbName;

	}

	@Override
	public <S extends BoxRepositoryObject> S save(S entity) {
		if (exists(entity.getBoxID())) {
			entity = update(entity);
		} else {
			try {
				MongoClient mongoClient = DbInit.connect();
				DB db = mongoClient.getDB(dbName);
				DBCollection dbbox = db.getCollection("box");
				DBObject objectToSave = DbInit.createDBObject(entity);
				LOGGER.info("DB Object Saved {}", objectToSave.toString());
				dbbox.save(objectToSave);
				mongoClient.close();
			} catch (UnknownHostException | JsonProcessingException e) {
				LOGGER.error("Can not save the box : {}",entity.getBoxID(),e);

				//NHE please handle error correctly by rethrowing RuntimeException for example
//				return null;
			}
		}
	
		return entity;
	}

	private <S extends BoxRepositoryObject> S update(S entity) {
		MongoClient mongoClient;
		try {
			mongoClient = DbInit.connect();

			DB db = mongoClient.getDB(dbName);
			DBCollection dbBox = db.getCollection("box");
			BasicDBObject newDocument = new BasicDBObject();

			if (entity.getIp() != null) {
				newDocument.append("$set",
						new BasicDBObject().append("ip", entity.getIp()));
				BasicDBObject searchQuery = new BasicDBObject().append("boxID",
						entity.getBoxID());
				dbBox.update(searchQuery, newDocument);
			}
			if (entity.getPubKey() != null) {
				newDocument.append("$set", new BasicDBObject().append("pubKey",
						entity.getPubKey()));
				BasicDBObject searchQuery = new BasicDBObject().append("boxID",
						entity.getBoxID());
				dbBox.update(searchQuery, newDocument);
			}

			/*
			 * if (entity.getUser() != null) {// need to verify
			 * 
			 * List<User> users = entity.getUser(); Iterator<User> userIterator
			 * = users.iterator(); List<Object> entityDBList = new
			 * BasicDBList();
			 * 
			 * while(userIterator.hasNext()) { DBObject userDBObject = new
			 * BasicDBObject(); try { userDBObject =
			 * DbInit.createDBObject(userIterator.next()); } catch
			 * (JsonProcessingException e) { //  Auto-generated catch block
			 * //NHE : no print stack trace allowed in the project. Please
			 * replace it with appropriate logger and Exception handling.
			 * e.printStackTrace();
			 * System.out.println("Impossible to create userDBObject"); }
			 * 
			 * entityDBList.add(userDBObject);
			 * 
			 * }
			 * 
			 * newDocument.append("$set",new BasicDBObject().append("user",
			 * entityDBList)); BasicDBObject searchQuery = new
			 * BasicDBObject().append("boxID",entity.getBoxID());
			 * dbBox.update(searchQuery, newDocument); }
			 */

			if (entity.getTTL() != null) {
				newDocument.append("$set",
						new BasicDBObject().append("ttl", entity.getTTL()));
				BasicDBObject searchQuery = new BasicDBObject().append("boxID",
						entity.getBoxID());
				dbBox.update(searchQuery, newDocument);
			}

			mongoClient.close();
		} catch (UnknownHostException e) {
			LOGGER.error("Can not update the box : {}",entity.getBoxID(),e);

		}

		return entity;
	}

	public <S extends BoxRepositoryObject> Iterable<S> save(Iterable<S> entities) {
		throw new RuntimeException("not yet invented");
	}

	public BoxRepositoryObject findOne(String id) {
		// The id is the userIDOfRelation address
		try {
			MongoClient mongoClient = DbInit.connect();
			DB db = mongoClient.getDB(dbName);
			DBCollection dbUsers = db.getCollection("box");

			BasicDBObject query = new BasicDBObject("boxID", id);
			DBCursor cursor = dbUsers.find(query);
			BoxRepositoryObject box = null;
			ObjectMapper mapper = new ObjectMapper();
			if (cursor.hasNext()) {
				try {
					DBObject temp = cursor.next();
					LOGGER.debug("cursor.next() ={}",temp.toString());
					box = mapper.readValue(temp.toString(),
							BoxRepositoryObject.class);
				} catch (IOException e) {
					LOGGER.error("Box Mapping failed ! ",e);	
				}
			}
			return box;

		} catch (UnknownHostException e) {
			LOGGER.error("Can not connect to the database (mongoDB installed ?)",e);
			return null;
		}
	}

	public boolean exists(String id) {
		try {
			MongoClient mongoClient = DbInit.connect();
			DB db = mongoClient.getDB(dbName);
			DBCollection dbUsers = db.getCollection("box");

			BasicDBObject query = new BasicDBObject("boxID", id);
			DBCursor cursor = dbUsers.find(query);

			if (cursor.hasNext()) {
				mongoClient.close();
				return true;
			} else {
				mongoClient.close();
				return false;
			}
		} catch (UnknownHostException e) {
			LOGGER.error("Connection to database failed (mongoDB installed and run ?)",e);
			return true;
		} catch (MongoTimeoutException e) {
			LOGGER.error("Connection to database failed (mongoDB installed and run ? Central server is runing? ) ",e);
			return true;
		}
	}

	public Iterable<BoxRepositoryObject> findAll() {
		List<BoxRepositoryObject> listOfAllBox = new ArrayList<BoxRepositoryObject>();

		try {
			MongoClient mongoClient = DbInit.connect();
			DB db = mongoClient.getDB(dbName);
			DBCollection dbBox = db.getCollection("box");

			DBCursor cursor = dbBox.find();
			BoxRepositoryObject box = null;
			ObjectMapper mapper = new ObjectMapper();

			while (cursor.hasNext()) {
				try {
					box = mapper.readValue(cursor.next().toString(),
							BoxRepositoryObject.class);
				} catch (IOException e) {
					e.printStackTrace();
					LOGGER.error("User Mapping failed ! ",e);
				}

				listOfAllBox.add(box);
			}

		} catch (UnknownHostException e) {
		LOGGER.error("error fail to connecte to mongoDB. It's install ?",e);
		
			return null;
		}

		return listOfAllBox;

	}

	public Iterable<BoxRepositoryObject> findAll(Iterable<String> ids) {
		throw new RuntimeException("not yet invented");

	}

	public long count() {
		throw new RuntimeException("not yet invented");
	}

	public void delete(String id) {
		try {
			MongoClient mongoClient = DbInit.connect();
			DB db = mongoClient.getDB(dbName);
			DBCollection dbUsers = db.getCollection("box");

			BasicDBObject query = new BasicDBObject("boxID", id);
			dbUsers.remove(query);
		} catch (UnknownHostException e) {
			LOGGER.error("Connection to database failed (mongoDB installed and run ?)",e);

		}

	}

	public void delete(BoxRepositoryObject entity) {
		try {
			MongoClient mongoClient = DbInit.connect();
			DB db = mongoClient.getDB(dbName);
			DBCollection dbUsers = db.getCollection("box");
			BasicDBObject query = new BasicDBObject("boxID", entity.getBoxID());
			dbUsers.remove(query);
		} catch (UnknownHostException e) {
			LOGGER.error("Connection to database failed (mongoDB installed and run ?)",e);
		}

	}

	public void delete(Iterable<? extends BoxRepositoryObject> entities) {
		throw new RuntimeException("not yet invented");
	}

	public void deleteAll() {
		throw new RuntimeException("not yet invented");
	}

}