package com.enseirb.telecom.s9.db;

import java.io.IOException;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;

public class RelationshipRepositoryMongo implements RelationshipRepositoryInterface {
	private static final Logger LOGGER = LoggerFactory.getLogger(RelationshipRepositoryMongo.class);


	@Override
	public <S extends RelationshipRepositoryObject> S save(S entity) {
		if (exists(entity.getUserId(), entity.getEmail())) {
			delete(entity.getUserId(), entity.getEmail());
		}
		try {
			MongoClient mongoClient = DbInit.Connect();
			DBCollection db = mongoClient.getDB("mediahome").getCollection("relationships");

			db.save(DbInit.createDBObject(entity));
			mongoClient.close();
		} catch (UnknownHostException | JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LOGGER.error("???");
			return null;
		}
		return entity;
	}

	@Override
	public <S extends RelationshipRepositoryObject> Iterable<S> save(Iterable<S> entities) {
		// TODO Auto-generated method stub
		throw new RuntimeException("not yet invented");
	}

	@Override
	public RelationshipRepositoryObject findOne(String id) {
		// TODO Auto-generated method stub
		throw new RuntimeException("not yet invented");
	}

	public RelationshipRepositoryObject findOne(String userId, String relationEmail) {
		try {
			MongoClient mongoClient = DbInit.Connect();
			DBCollection db = mongoClient.getDB("mediahome").getCollection("relationships");

			BasicDBObject query = new BasicDBObject("userId", userId).append("email", relationEmail);
			DBCursor cursor = db.find(query);
			ObjectMapper mapper = new ObjectMapper();
			RelationshipRepositoryObject relation = null;
			if (cursor.hasNext()) {
				try {
					relation = mapper.readValue(cursor.next().toString(), RelationshipRepositoryObject.class);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				//	System.err.println("User Mapping failed ! ");
					LOGGER.error("User Mapping failed !");

				}
			}
			mongoClient.close();
			return relation;
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		//	System.err.println("Connection to database failed ");
			LOGGER.error("Connection to database failed");

			return null;
		}
	}

	@Override
	public boolean exists(String id) {
		// TODO Auto-generated method stub
		throw new RuntimeException("not yet invented");
	}
	
	// This function won't be really used... 
	public boolean exists(String userId, String relationEmail) {
		try {
			MongoClient mongoClient = DbInit.Connect();
			DBCollection db = mongoClient.getDB("mediahome").getCollection("relationships");

			BasicDBObject query = new BasicDBObject("userId", userId).append("email", relationEmail);
			DBCursor cursor = db.find(query);
			boolean exists = cursor.hasNext();
			return exists;
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		//	System.err.println("Connection to database failed ");
			LOGGER.error("Connection to database failed");

			return true;
		}
	}

	@Override
	public Iterable<RelationshipRepositoryObject> findAll() {
		// TODO Auto-generated method stub
		throw new RuntimeException("not yet invented");
	}

	@Override
	public Iterable<RelationshipRepositoryObject> findAll(Iterable<String> ids) {
		// TODO Auto-generated method stub
		throw new RuntimeException("not yet invented");
	}

	@Override
	public long count() {
		// TODO Auto-generated method stub
		throw new RuntimeException("not yet invented");
	}

	@Override
	public void delete(String id) {
		// TODO Auto-generated method stub
		throw new RuntimeException("not yet invented");
	}
	public void delete(String userId, String relationEmail) {
		try {
			MongoClient mongoClient = DbInit.Connect();
			DBCollection db = mongoClient.getDB("mediahome").getCollection("relationships");

			BasicDBObject query = new BasicDBObject("userId", userId).append("email", relationEmail);
			db.remove(query);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		//	System.err.println("Connection to database failed ");
			LOGGER.error("Connection to database failed");

		}
	}

	@Override
	public void delete(RelationshipRepositoryObject entity) {
		// TODO Auto-generated method stub
		throw new RuntimeException("not yet invented");
	}

	@Override
	public void delete(Iterable<? extends RelationshipRepositoryObject> entities) {
		// TODO Auto-generated method stub
		throw new RuntimeException("not yet invented");
	}

	@Override
	public void deleteAll() {
		// TODO Auto-generated method stub
		throw new RuntimeException("not yet invented");
	}

}
