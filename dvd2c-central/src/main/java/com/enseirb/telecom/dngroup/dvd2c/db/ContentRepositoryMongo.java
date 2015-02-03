package com.enseirb.telecom.dngroup.dvd2c.db;

import java.io.IOException;
import java.net.UnknownHostException;

import org.bson.types.ObjectId;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class ContentRepositoryMongo implements
		CrudRepository<ContentRepositoryObject, String> {

	@Override
	public <S extends ContentRepositoryObject> S save(S entity) {
		if (entity.getId() != null && exists(entity.getId().toString())) {
			delete(entity.getId().toString());
		}
		try {
			MongoClient mongoClient = DbInit.Connect();
			DBCollection db = mongoClient.getDB("mediahome").getCollection(
					"contents");
			DBObject objectToSave = DbInit.createDBObject(entity);
			db.save(objectToSave);
			entity.setId((ObjectId) objectToSave.get("_id"));
			mongoClient.close();
		} catch (UnknownHostException | JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return entity;
	}

	@Override
	public <S extends ContentRepositoryObject> Iterable<S> save(
			Iterable<S> entities) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ContentRepositoryObject findOne(String id) {
		try {
			MongoClient mongoClient = DbInit.Connect();
			DBCollection db = mongoClient.getDB("mediahome").getCollection(
					"contents");

			BasicDBObject query = new BasicDBObject("_id", id);
			DBCursor cursor = db.find(query);
			ContentRepositoryObject content = null;
			ObjectMapper mapper = new ObjectMapper();
			if (cursor.hasNext()) {
				try {
					content = mapper.readValue(cursor.next().toString(),
							ContentRepositoryObject.class);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.err.println("Content Mapping failed ! ");
				}
			}
			;
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
			DBCollection db = mongoClient.getDB("mediahome").getCollection(
					"contents");

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
		return 0;
	}

	@Override
	public void delete(String id) {
		try {
			MongoClient mongoClient = DbInit.Connect();
			DBCollection db = mongoClient.getDB("mediahome").getCollection(
					"contents");

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

	}

	@Override
	public void delete(Iterable<? extends ContentRepositoryObject> entities) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteAll() {
		// TODO Auto-generated method stub

	}

}
