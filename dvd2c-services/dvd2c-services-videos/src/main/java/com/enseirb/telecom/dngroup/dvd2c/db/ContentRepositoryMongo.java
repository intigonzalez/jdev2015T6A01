package com.enseirb.telecom.dngroup.dvd2c.db;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enseirb.telecom.dngroup.dvd2c.model.Metadata;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.WriteResult;

public class ContentRepositoryMongo implements ContentRepository {
	private static final Logger LOGGER = LoggerFactory.getLogger(ContentRepositoryMongo.class);

	@Override
	public <S extends ContentRepositoryObject> S save(S entity) {
		if (exists(entity.getId())) {
			delete(entity);
		}

			try {

				MongoClient mongoClient = DbInit.connect();
				DBCollection db = mongoClient.getDB("mediahome").getCollection(
						"contents");
				DBObject objectToSave = DbInit.createDBObject(entity);
				LOGGER.debug("object to save {}",objectToSave);
				WriteResult a = db.save(objectToSave);
				mongoClient.close();
			} catch (UnknownHostException | JsonProcessingException e) {
				LOGGER.error("Connection to database failed (mongoDB installed and run ?) OR error for parsing json",e);
				return null;
			}
		

		return entity;
	}

	/**
	 * The method update a content's profile
	 * 
	 * @param entity
	 * @return entity
	 */
	public <S extends ContentRepositoryObject> S update(S entity) {

		MongoClient mongoClient;
		try {
			mongoClient = DbInit.connect();

			DB db = mongoClient.getDB("mediahome");
			DBCollection dbBox = db.getCollection("contents");
			BasicDBObject newDocument = new BasicDBObject();

//			if (entity.getId() != null) {
//				newDocument.append("$set",
//						new BasicDBObject().append("id", entity.getId()));
//				BasicDBObject searchQuery = new BasicDBObject().append("id",
//						entity.getId());
//				dbBox.update(searchQuery, newDocument);
//			}
			if (entity.getName() != null) {
				newDocument.append("$set",
						new BasicDBObject().append("firstname", entity.getName()));
				BasicDBObject searchQuery = new BasicDBObject().append("id",
						entity.getId());
				dbBox.update(searchQuery, newDocument);
			}
			if (entity.getActorID() != null) {// need to verify
				newDocument.append("$set", new BasicDBObject().append("userId",
						entity.getActorID()));
				BasicDBObject searchQuery = new BasicDBObject().append("id",
						entity.getId());
				dbBox.update(searchQuery, newDocument);
			}
			if (entity.getLink() != null) {
				newDocument.append("$set",
						new BasicDBObject().append("link", entity.getLink()));
				BasicDBObject searchQuery = new BasicDBObject().append("id",
						entity.getId());
				dbBox.update(searchQuery, newDocument);
			}
			if (entity.getPreviewLink() != null) {
				newDocument.append(
						"$set",
						new BasicDBObject().append("previewLink",
								entity.getPreviewLink()));
				BasicDBObject searchQuery = new BasicDBObject().append("id",
						entity.getId());
				dbBox.update(searchQuery, newDocument);
			}
			if (entity.getUnixTime() != null) {
				newDocument.append(
						"$set",
						new BasicDBObject().append("unixTime",
								entity.getUnixTime()));
				BasicDBObject searchQuery = new BasicDBObject().append("id",
						entity.getId());
				dbBox.update(searchQuery, newDocument);
			}
			if (entity.getStatus() != null) {
				newDocument.append("$set", new BasicDBObject().append("status",
						entity.getStatus()));
				BasicDBObject searchQuery = new BasicDBObject().append("id",
						entity.getId());
				dbBox.update(searchQuery, newDocument);
			}

			if (entity.getMetadata() != null) {try {
				// need to verify
					
					List<Integer> metadatas = entity.getMetadata();
												
					List<Object> entityDBList = new BasicDBList();
					for (Integer metadata : metadatas) {
					
					    DBObject userDBObject = new BasicDBObject();
					    try {
							userDBObject = DbInit.createDBObject(metadata);
						} catch (JsonProcessingException e) {
						
							LOGGER.error("Impossible to create userDBObject",e);
						}			    
					    
						entityDBList.add(userDBObject);
						
					}
					
					newDocument.append("$set",new BasicDBObject().append("authorization", entityDBList));
					BasicDBObject searchQuery = new BasicDBObject().append("id",entity.getId());
					dbBox.update(searchQuery, newDocument);
			} catch (Exception e) {
				LOGGER.debug("Can't update metadata",e);
			}
				}
			if (entity.getComment() != null) {
				newDocument.append(
						"$set",
						new BasicDBObject().append("comment",
								entity.getComment()));
				BasicDBObject searchQuery = new BasicDBObject().append("id",
						entity.getId());
				dbBox.update(searchQuery, newDocument);
			}

			mongoClient.close();
		} catch (UnknownHostException e) {
			LOGGER.error("Connection to database failed (mongoDB installed and run ?)",e);
		}

		return entity;
	}

	@Override
	public <S extends ContentRepositoryObject> Iterable<S> save(
			Iterable<S> entities) {
		throw new RuntimeException("not yet invented");
	}

	@Override
	public ContentRepositoryObject findOne(String id) {
		try {
			MongoClient mongoClient = DbInit.connect();
			DBCollection db = mongoClient.getDB("mediahome").getCollection(
					"contents");

			BasicDBObject query = new BasicDBObject("id", id);
			DBCursor cursor = db.find(query);
			ContentRepositoryObject content = null;
			ObjectMapper mapper = new ObjectMapper();
			if (cursor.hasNext()) {
				try {
					content = mapper.readValue(cursor.next().toString(),
							ContentRepositoryObject.class);
				} catch (IOException e) {
					LOGGER.error("Connection to database failed",e);

				}
			}
			;
			mongoClient.close();
			return content;
		} catch (UnknownHostException e) {
			
			LOGGER.error("Connection to database failed (mongoDB installed and run ?)",e);

			return null;
		}
	}

	@Override
	public boolean exists(String id) {

		try {
			MongoClient mongoClient = DbInit.connect();
			DB db = mongoClient.getDB("mediahome");
			DBCollection dbUsers = db.getCollection("contents");

			BasicDBObject query = new BasicDBObject("id", id);
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
		}
	}

	@Override
	public Iterable<ContentRepositoryObject> findAll() {
		// Iterable <UserRepositoryObject> listOfAllUsers = null;
		List<ContentRepositoryObject> listOfAllContent = new ArrayList<ContentRepositoryObject>();

		try {
			MongoClient mongoClient = DbInit.connect();
			DB db = mongoClient.getDB("mediahome");
			DBCollection dbUsers = db.getCollection("contents");

			ObjectMapper mapper = new ObjectMapper();
			ContentRepositoryObject content = null;

			DBCursor cursor = dbUsers.find();

			while (cursor.hasNext()) {
				try {
					content = mapper.readValue(cursor.next().toString(),
							ContentRepositoryObject.class);
				} catch (IOException e) {
				
					LOGGER.error("User Mapping failed ! ",e);
				}

				listOfAllContent.add(content);
			}

		} catch (UnknownHostException e) {
			LOGGER.error("Connection to database failed ");
		}
		return listOfAllContent;

		// throw new RuntimeException("not yet invented");
	}

	@Override
	public Iterable<ContentRepositoryObject> findAll(Iterable<String> ids) {
		throw new RuntimeException("not yet invented");
	}

	@Override
	public long count() {

		long nbOfContents = 0;

		try {
			MongoClient mongoClient = DbInit.connect();
			DB db = mongoClient.getDB("mediahome");
			DBCollection dbUsers = db.getCollection("contents");

			nbOfContents = dbUsers.getCount();
		} catch (UnknownHostException e) {
	
LOGGER.error("Connection to database failed ");

		}

		return nbOfContents;

	}

	@Override
	public List<ContentRepositoryObject> findAllFromUser(String userID) {
		List<ContentRepositoryObject> list = new ArrayList<ContentRepositoryObject>();

		try {
			MongoClient mongoClient = DbInit.connect();
			DB db = mongoClient.getDB("mediahome");
			DBCollection dbContents = db.getCollection("contents");
			BasicDBObject query = new BasicDBObject("actorID", userID);

			ObjectMapper mapper = new ObjectMapper();
			ContentRepositoryObject content = null;

			DBCursor cursor = dbContents.find(query);

			while (cursor.hasNext()) {
				try {
					content = mapper.readValue(cursor.next().toString(),
							ContentRepositoryObject.class);
				} catch (JsonMappingException e) {
					LOGGER.error("Can not cast json",e);
				}
				 catch (IOException e) {
					LOGGER.error("Connection to database failed to get contents");
				}
				list.add(content);
			}
		} catch (UnknownHostException e) {
			LOGGER.error("Connection to database failed", e);
		}
		return list;
	}

	@Override
	public void delete(String id) {
		try {
			MongoClient mongoClient = DbInit.connect();
			DBCollection db = mongoClient.getDB("mediahome").getCollection(
					"contents");

			BasicDBObject query = new BasicDBObject("id", id);
			db.remove(query);
			mongoClient.close();
		} catch (UnknownHostException e) {
			LOGGER.error("Connection to database failed (mongoDB installed and run ?)",e);

		}
	}

	@Override
	public void delete(ContentRepositoryObject entity) {

		try {
			MongoClient mongoClient = DbInit.connect();
			DB db = mongoClient.getDB("mediahome");
			DBCollection dbUsers = db.getCollection("contents");
			BasicDBObject query = new BasicDBObject("id", entity.getId()
					.toString());
			dbUsers.remove(query);
		} catch (UnknownHostException e) {
			LOGGER.error("Connection to database failed ");

		}

	}

	@Override
	public void delete(Iterable<? extends ContentRepositoryObject> entities) {
		throw new RuntimeException("not yet invented");
	}

	@Override
	public void deleteAll() {

		try {
			MongoClient mongoClient = DbInit.connect();
			DB db = mongoClient.getDB("mediahome");
			DBCollection dbContents = db.getCollection("contents");

			dbContents.drop();
			mongoClient.close();

		} catch (UnknownHostException e) {

			LOGGER.error("Connnection to database failed",e);

		}

	}

}
