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
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;

public class RelationshipRepositoryMongo implements RelationshipRepository {
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
			LOGGER.error("Connection to database failed (mongoDB installed and run ?)",e);
			return null;
		}
		return entity;
	}

	@Override
	public <S extends RelationshipRepositoryObject> Iterable<S> save(Iterable<S> entities) {
		throw new RuntimeException("not yet invented");
	}

	@Override
	public RelationshipRepositoryObject findOne(String id) {
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
					LOGGER.error("User Mapping failed !",e);
				}
			}
			mongoClient.close();
			return relation;
		} catch (UnknownHostException e) {
			LOGGER.error("Connection to database failed (mongoDB installed and run ?)",e);
			return null;
		}
	}

	@Override
	public boolean exists(String id) {
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
			LOGGER.error("Connection to database failed (mongoDB installed and run ?)",e);

			return true;
		}
	}

	@Override
	public Iterable<RelationshipRepositoryObject> findAll() {
		List <RelationshipRepositoryObject> listOfAllRelation = new ArrayList<RelationshipRepositoryObject>();
		
		try{
			MongoClient mongoClient = DbInit.Connect();
			DBCollection db = mongoClient.getDB("mediahome").getCollection("relationships");
			
			ObjectMapper mapper = new ObjectMapper();
			RelationshipRepositoryObject relation = null;
			
			DBCursor cursor = db.find();
					
			while(cursor.hasNext()){
				try {
					relation = mapper.readValue(cursor.next().toString(), RelationshipRepositoryObject.class);
				} catch (IOException e) {
					LOGGER.error("User Mapping failed ! ",e);
				}
				listOfAllRelation.add(relation);
			}
		}
		catch (UnknownHostException e){
			LOGGER.error("Connection to database failed (mongoDB installed and run ?)",e);
		}
		return listOfAllRelation;
	}

	@Override
	public Iterable<RelationshipRepositoryObject> findAll(Iterable<String> ids) {
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
	public void delete(String userId, String relationEmail) {
		try {
			MongoClient mongoClient = DbInit.Connect();
			DBCollection db = mongoClient.getDB("mediahome").getCollection("relationships");

			BasicDBObject query = new BasicDBObject("userId", userId).append("email", relationEmail);
			db.remove(query);
		} catch (UnknownHostException e) {
			LOGGER.error("Connection to database failed (mongoDB installed and run ?)",e);
		}
	}

	@Override
	public void delete(RelationshipRepositoryObject entity) {
		throw new RuntimeException("not yet invented");
	}

	@Override
	public void delete(Iterable<? extends RelationshipRepositoryObject> entities) {
		throw new RuntimeException("not yet invented");
	}

	@Override
	public void deleteAll() {
		throw new RuntimeException("not yet invented");
	}

}
