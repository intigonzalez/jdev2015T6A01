package com.enseirb.telecom.s9.db;

import java.io.IOException;
import java.net.UnknownHostException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;

public class UserRepositoryMongo implements CrudRepository<UserRepositoryObject, String> {

	@Override
	public <S extends UserRepositoryObject> S save(S entity) {
		if (exists(entity.getUserID())) {
			delete(entity.getUserID());
		}
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
	public UserRepositoryObject findOne(String id) {
		// The id is the email address
		try {
			MongoClient mongoClient = DbInit.Connect();
			DB db = mongoClient.getDB("mediahome");
			DBCollection dbUsers = db.getCollection("users");

			BasicDBObject query = new BasicDBObject("userID", id);
			DBCursor cursor = dbUsers.find(query);
			UserRepositoryObject user = null;
			ObjectMapper mapper = new ObjectMapper();
			if (cursor.hasNext()) {
				try {
					user = mapper.readValue(cursor.next().toString(), UserRepositoryObject.class);
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
		try {
			MongoClient mongoClient = DbInit.Connect();
			DB db = mongoClient.getDB("mediahome");
			DBCollection dbUsers = db.getCollection("users");

			BasicDBObject query = new BasicDBObject("userID", id);
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
	public Iterable<UserRepositoryObject> findAll() {
		//Iterable <UserRepositoryObject> listOfAllUsers = null;
		List <UserRepositoryObject> listOfAllUsers = new ArrayList<UserRepositoryObject>();
		
		try{
			MongoClient mongoClient = DbInit.Connect();
			DB db = mongoClient.getDB("mediahome");
			DBCollection dbUsers = db.getCollection("users");
			
			ObjectMapper mapper = new ObjectMapper();
			UserRepositoryObject user = null;
			
			DBCursor cursor = dbUsers.find();
					
			while(cursor.hasNext()){
				try {
					user = mapper.readValue(cursor.next().toString(), UserRepositoryObject.class);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.err.println("User Mapping failed ! ");
				}
				
				listOfAllUsers.add(user);
			}
			
		}
		catch (UnknownHostException e){
			e.printStackTrace();
			System.err.println("Connection to database failed ");			
		}
		return listOfAllUsers;
		
		//throw new RuntimeException("not yet invented");
>>>>>>> d48c71bf015ac6c7c3a7dc7d47a96cac45529624
	}

	@Override
	public Iterable<UserRepositoryObject> findAll(Iterable<String> ids) {
<<<<<<< HEAD
		throw new RuntimeException("not yet invented");
=======

		// throw new RuntimeException("not yet invented");

		List<UserRepositoryObject> listOfAllUsers = new ArrayList<UserRepositoryObject>();
		Iterator<String> iterator = ids.iterator();

		try {
			MongoClient mongoClient = DbInit.Connect();
			DB db = mongoClient.getDB("mediahome");
			DBCollection dbUsers = db.getCollection("users");

			ObjectMapper mapper = new ObjectMapper();
			UserRepositoryObject user = null;

			while (iterator.hasNext()) {
				BasicDBObject query = new BasicDBObject("userID",
						iterator.next());
				DBCursor cursor = dbUsers.find(query);

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

			}

		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.err.println("Connection to database failed ");
		}
		return listOfAllUsers;

>>>>>>> d48c71bf015ac6c7c3a7dc7d47a96cac45529624
	}

	@Override
	public long count() {

		// throw new RuntimeException("not yet invented");

		long numberOfUser = 0;

		try {
			MongoClient mongoClient = DbInit.Connect();
			DB db = mongoClient.getDB("mediahome");
			DBCollection dbUsers = db.getCollection("users");

			DBCursor cursor = dbUsers.find();
			try {
				mongoClient.close();
				while (cursor.hasNext()) {
					numberOfUser++;
				}
			} finally {
				cursor.close();
			}
		}

		catch (UnknownHostException e) {

		}

		return numberOfUser;

	}

	@Override
	public void delete(String id) {
		try {
			MongoClient mongoClient = DbInit.Connect();
			DB db = mongoClient.getDB("mediahome");
			DBCollection dbUsers = db.getCollection("users");

			BasicDBObject query = new BasicDBObject("userID", id);
			dbUsers.remove(query);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.err.println("Connection to database failed ");
		}

	}

	@Override
	public void delete(UserRepositoryObject entity) {
		try {
			MongoClient mongoClient = DbInit.Connect();
			DB db = mongoClient.getDB("mediahome");
			DBCollection dbUsers = db.getCollection("users");
			BasicDBObject query = new BasicDBObject("userID", entity.getUserID());
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
		
		//throw new RuntimeException("not yet invented");

		try{
			MongoClient mongoClient = DbInit.Connect();
			DB db = mongoClient.getDB("mediahome");
			DBCollection dbUsers = db.getCollection("users");
			
			dbUsers.drop();
			mongoClient.close();
			
			}
		catch(UnknownHostException e){
			e.printStackTrace();
			System.err.println();
		}
	}

	@Override
	public <S extends UserRepositoryObject> Iterable<S> save(Iterable<S> entities) {
		//throw new RuntimeException("not yet invented");
		
		List<S> listOfAllUsers = new ArrayList<S>();
		Iterator<S> iterator = entities.iterator();
		
		while(iterator.hasNext()){
			if(exists(iterator.next().getUserID())){
				delete(iterator.next().getUserID());
			}
			
			try{
				MongoClient mongoClient = DbInit.Connect();
				DB db = mongoClient.getDB("mediahome");
				DBCollection dbUsers = db.getCollection("user");
				
				try {
					dbUsers.save(DbInit.createDBObject(iterator.next()));
				} catch (JsonProcessingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			catch(UnknownHostException e){
				e.printStackTrace();
				System.err.println("Connection to database failed ");
				
			}
		}
		
		
		return listOfAllUsers ;
		
	}

}
