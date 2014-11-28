package com.enseirb.telecom.s9.db;

import java.io.IOException;
import java.net.UnknownHostException;

import org.bson.types.ObjectId;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.enseirb.telecom.s9.Comment;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class CommentRepositoryMongo implements CrudRepository<CommentRepositoryObject, String>{	

	private static final ObjectMapper mapper = null;

	@Override
	public <S extends CommentRepositoryObject> S save(S entity){
		// TODO Auto-generated method stub
		
		if(entity.getCommentId() != null & exists(entity.getCommentId().toString())){
			delete(entity.getCommentId().toString());
		}
				
		try{
			MongoClient mongoClient = DbInit.Connect();
			DBCollection collection = mongoClient.getDB("mediahome").getCollection("comments");
			DBObject dbObjectToSave = DbInit.createDBObject(entity);
			
			collection.save(dbObjectToSave);
			entity.setCommentId((ObjectId)dbObjectToSave.get("_id"));
			mongoClient.close();
			
		} catch (UnknownHostException e){
			e.printStackTrace();
			System.err.println("Connection to database failed");
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("Creation of dbObjectToSave failed");
		}
		
		return entity;
	}

	@Override
	public <S extends CommentRepositoryObject> Iterable<S> save(
			Iterable<S> entities) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommentRepositoryObject findOne(String id) {
		// TODO Auto-generated method stub
		
		CommentRepositoryObject comment = null;
		
		try{
			
			MongoClient mongoClient = DbInit.Connect();
			DBCollection collection = mongoClient.getDB("mediahome").getCollection("comments");
			BasicDBObject query = new BasicDBObject("_id",id);
			
			DBCursor cursor = collection.find(query);
			if(cursor.hasNext()){
				try {
					comment = mapper.readValue(cursor.next().toString(), CommentRepositoryObject.class);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
						
		}catch (UnknownHostException e){
			e.printStackTrace();
			System.err.println("Connection to database failed");
		}
		
		return comment;
	}

	@Override
	public boolean exists(String id) {
		// TODO Auto-generated method stub
		
		boolean result = false;
		
		try{
			
			MongoClient mongoClient = DbInit.Connect();
			DB db = mongoClient.getDB("mediahome");
			DBCollection collection = db.getCollection("comments");
			
			BasicDBObject query = new BasicDBObject("_id",id);
			DBCursor cursor = collection.find(query);
			result = cursor.hasNext();
			mongoClient.close();
			
		}
		catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("Connection to database failed ");
		}
		
		return result;
	}

	@Override
	public Iterable<CommentRepositoryObject> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<CommentRepositoryObject> findAll(Iterable<String> ids) {
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(CommentRepositoryObject entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Iterable<? extends CommentRepositoryObject> entities) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAll() {
		// TODO Auto-generated method stub
		
	}
	
	
	
}
