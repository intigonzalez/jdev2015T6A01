package com.enseirb.telecom.dvd2c.db;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enseirb.telecom.dvd2c.endpoints.ContentEndPoints;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class BoxRepositoryMongo implements CrudRepository<BoxRepositoryObject, String>{
	private static final Logger LOGGER = LoggerFactory.getLogger(BoxRepositoryMongo.class);
	public BoxRepositoryMongo(){
		
	}
	
	public <S extends BoxRepositoryObject> S save(S entity) {
		if (exists(entity.getBoxID())) {
			entity = update(entity);
		} else {
			try {
				MongoClient mongoClient = DbInit.Connect();
				DB db = mongoClient.getDB("mediahome");
				DBCollection dbbox = db.getCollection("box");
				DBObject objectToSave = DbInit.createDBObject(entity);
				LOGGER.info("DB Object Saved {}",objectToSave.toString());
				dbbox.save(objectToSave);
				mongoClient.close();
			} catch (UnknownHostException | JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}

		return entity;
	}

	private <S extends BoxRepositoryObject> S update(S entity) {
		MongoClient mongoClient;
		try {
			mongoClient = DbInit.Connect();

			DB db = mongoClient.getDB("mediahome");
			DBCollection dbBox = db.getCollection("box");
			BasicDBObject newDocument = new BasicDBObject();

			if (entity.getIp() != null) {
				newDocument.append("$set",new BasicDBObject().append("ip", entity.getIp()));
				BasicDBObject searchQuery = new BasicDBObject().append("boxID",entity.getBoxID());
				dbBox.update(searchQuery, newDocument);
			}
			if (entity.getPubKey() != null) {
				newDocument.append("$set", new BasicDBObject().append("pubKey",entity.getPubKey()));
				BasicDBObject searchQuery = new BasicDBObject().append("boxID",entity.getBoxID());
				dbBox.update(searchQuery, newDocument);
			}

			/*if (entity.getUser() != null) {// need to verify
				
				List<User> users = entity.getUser();
				Iterator<User> userIterator = users.iterator();														
				List<Object> entityDBList = new BasicDBList();
								
				while(userIterator.hasNext()) {	
				    DBObject userDBObject = new BasicDBObject();
				    try {
						userDBObject = DbInit.createDBObject(userIterator.next());
					} catch (JsonProcessingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						System.out.println("Impossible to create userDBObject");
					}			    
				    
					entityDBList.add(userDBObject);
					
				}
				
				newDocument.append("$set",new BasicDBObject().append("user", entityDBList));
				BasicDBObject searchQuery = new BasicDBObject().append("boxID",entity.getBoxID());
				dbBox.update(searchQuery, newDocument);
			}*/
			
			if (entity.getTTL() != null) {
				newDocument.append("$set",new BasicDBObject().append("ttl", entity.getTTL()));
				BasicDBObject searchQuery = new BasicDBObject().append("boxID",entity.getBoxID());
				dbBox.update(searchQuery, newDocument);
			}

			mongoClient.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return entity;
	}

	public <S extends BoxRepositoryObject> Iterable<S> save(
			Iterable<S> entities) {
		// TODO Auto-generated method stub
		throw new RuntimeException("not yet invented");
	}

	public BoxRepositoryObject findOne(String id) {
		// The id is the email address
		try {
			MongoClient mongoClient = DbInit.Connect();
			DB db = mongoClient.getDB("mediahome");
			DBCollection dbUsers = db.getCollection("box");

			BasicDBObject query = new BasicDBObject("boxID", id);
			DBCursor cursor = dbUsers.find(query);
			BoxRepositoryObject box = null;
			ObjectMapper mapper = new ObjectMapper();
			if (cursor.hasNext()) {
				try {
					box = mapper.readValue(cursor.next().toString(),
							BoxRepositoryObject.class);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.err.println("Box Mapping failed ! ");
				}
			}
			return box;

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public boolean exists(String id) {
		try {
			MongoClient mongoClient = DbInit.Connect();
			DB db = mongoClient.getDB("mediahome");
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
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("Connection to database failed ");
			return true;
		}
	}

	public Iterable<BoxRepositoryObject> findAll() {
		// TODO Auto-generated method stub
		
		List <BoxRepositoryObject> listOfAllBox = new ArrayList<BoxRepositoryObject>();
		
		try {
			MongoClient mongoClient = DbInit.Connect();
			DB db = mongoClient.getDB("mediahome");
			DBCollection dbBox = db.getCollection("box");
			
			DBCursor cursor = dbBox.find();
			BoxRepositoryObject box = null;
			ObjectMapper mapper = new ObjectMapper();
			
			while(cursor.hasNext()){
				try {
					box = mapper.readValue(cursor.next().toString(), BoxRepositoryObject.class);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.err.println("User Mapping failed ! ");
				}
				
				listOfAllBox.add(box);
			}

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		return listOfAllBox;
		
	}

	public Iterable<BoxRepositoryObject> findAll(Iterable<String> ids) {
		throw new RuntimeException("not yet invented");
		
		
	}

	public long count() {
		// TODO Auto-generated method stub
		throw new RuntimeException("not yet invented");
	}

	public void delete(String id) {
		try {
			MongoClient mongoClient = DbInit.Connect();
			DB db = mongoClient.getDB("mediahome");
			DBCollection dbUsers = db.getCollection("box");

			BasicDBObject query = new BasicDBObject("boxID", id);
			dbUsers.remove(query);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.err.println("Connection to database failed ");
		}

	}

	public void delete(BoxRepositoryObject entity) {
		try {
			MongoClient mongoClient = DbInit.Connect();
			DB db = mongoClient.getDB("mediahome");
			DBCollection dbUsers = db.getCollection("box");
			BasicDBObject query = new BasicDBObject("boxID", entity.getBoxID());
			dbUsers.remove(query);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.err.println("Connection to database failed ");
		}

	}

	public void delete(Iterable<? extends BoxRepositoryObject> entities) {
		// TODO Auto-generated method stub
		throw new RuntimeException("not yet invented");
	}

	public void deleteAll() {
		// TODO Auto-generated method stub
		throw new RuntimeException("not yet invented");
	}
	
	
}
