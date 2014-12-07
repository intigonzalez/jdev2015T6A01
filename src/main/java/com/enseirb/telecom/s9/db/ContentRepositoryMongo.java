package com.enseirb.telecom.s9.db;

import java.io.IOException;
import java.net.UnknownHostException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class ContentRepositoryMongo implements CrudRepository<ContentRepositoryObject, String> {

	@Override
	public <S extends ContentRepositoryObject> S save(S entity) {
		if (entity.getId() != null && exists(entity.getId().toString())) {
			delete(entity.getId().toString());
		}
		try {
			MongoClient mongoClient = DbInit.Connect();
			DBCollection db = mongoClient.getDB("mediahome").getCollection("contents");
			DBObject objectToSave = DbInit.createDBObject(entity);
			db.save(objectToSave);
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
	public ContentRepositoryObject findOne(String id) {
		try {
			MongoClient mongoClient = DbInit.Connect();
			DBCollection db = mongoClient.getDB("mediahome").getCollection("contents");

			BasicDBObject query = new BasicDBObject("id", id);
			DBCursor cursor = db.find(query);
			ContentRepositoryObject content = null;
			ObjectMapper mapper = new ObjectMapper();
			if (cursor.hasNext()) {
				try {
					content = mapper.readValue(cursor.next().toString(), ContentRepositoryObject.class);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.err.println("Content Mapping failed ! ");
				}
			};
			mongoClient.close();
			return content;
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("Connection to database failed ");
			return null;
		}
	}

	@Override
	public boolean exists(String id) {
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
	public Iterable<ContentRepositoryObject> findAll(Iterable<String> ids) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long count() {
		// TODO Auto-generated method stub
		
		long nbOfContents = 0;

		try {
			MongoClient mongoClient = DbInit.Connect();
			DB db = mongoClient.getDB("mediahome");
			DBCollection dbUsers = db.getCollection("contents");

			nbOfContents = dbUsers.getCount();
		}
		catch (UnknownHostException e) {
			e.printStackTrace();
			System.err.println("Connection to database failed ");
		}

		return nbOfContents;
		
	}

	@Override
	public void delete(String id) {
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

		try {
			MongoClient mongoClient = DbInit.Connect();
			DB db = mongoClient.getDB("mediahome");
			DBCollection dbUsers = db.getCollection("contents");
			BasicDBObject query = new BasicDBObject("_id", entity.getId().toString());
			dbUsers.remove(query);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.err.println("Connection to database failed ");
		}
		
	}

	@Override
	public void delete(Iterable<? extends ContentRepositoryObject> entities) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteAll() {
		// TODO Auto-generated method stub
		
		try{
			MongoClient mongoClient = DbInit.Connect();
			DB db = mongoClient.getDB("mediahome");
			DBCollection dbContents = db.getCollection("contents");
			
			dbContents.drop();
			mongoClient.close();
			
			}
		catch(UnknownHostException e){
			e.printStackTrace();
			System.err.println("Connnection to database failed");
		}
		

	}

}
