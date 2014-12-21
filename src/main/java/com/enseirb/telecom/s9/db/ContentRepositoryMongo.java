package com.enseirb.telecom.s9.db;

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

public class ContentRepositoryMongo implements ContentRepositoryInterface {
	private static final Logger LOGGER = LoggerFactory.getLogger(ContentRepositoryMongo.class);

	@Override
	public <S extends ContentRepositoryObject> S save(S entity) {
		if (exists(entity.getId())) {
			entity = update(entity);
		} else {

			try {

				MongoClient mongoClient = DbInit.Connect();
				DBCollection db = mongoClient.getDB("mediahome").getCollection(
						"contents");
				DBObject objectToSave = DbInit.createDBObject(entity);
				db.save(objectToSave);
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
	 * The method update a content's profile
	 * 
	 * @param entity
	 * @return entity
	 */
	public <S extends ContentRepositoryObject> S update(S entity) {

		MongoClient mongoClient;
		try {
			mongoClient = DbInit.Connect();

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
						new BasicDBObject().append("name", entity.getName()));
				BasicDBObject searchQuery = new BasicDBObject().append("id",
						entity.getId());
				dbBox.update(searchQuery, newDocument);
			}
			if (entity.getUserId() != null) {// need to verify
				newDocument.append("$set", new BasicDBObject().append("userId",
						entity.getUserId()));
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
			if (entity.getAuthorization() != null) {
				newDocument.append(
						"$set",
						new BasicDBObject().append("authorization",
								entity.getAuthorization()));
				BasicDBObject searchQuery = new BasicDBObject().append("id",
						entity.getId());
				dbBox.update(searchQuery, newDocument);
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
			// TODO Auto-generated catch block
			LOGGER.error("User Saving Failed");
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

			BasicDBObject query = new BasicDBObject("id", id);
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
					// System.err.println("Content Mapping failed ! ");
					LOGGER.error("Connection to database failed");

				}
			}
			;
			mongoClient.close();
			return content;
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// System.err.println("Connection to database failed ");
			LOGGER.error("Connection to database failed");

			return null;
		}
	}

	@Override
	public boolean exists(String id) {

		try {
			MongoClient mongoClient = DbInit.Connect();
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
			// TODO Auto-generated catch block
			e.printStackTrace();
			// System.err.println("Connection to database failed ");
			LOGGER.error("Connection to database failed");

			return true;
		}
	}

	@Override
	public Iterable<ContentRepositoryObject> findAll() {
		// Iterable <UserRepositoryObject> listOfAllUsers = null;
		List<ContentRepositoryObject> listOfAllContent = new ArrayList<ContentRepositoryObject>();

		try {
			MongoClient mongoClient = DbInit.Connect();
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
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.err.println("User Mapping failed ! ");
				}

				listOfAllContent.add(content);
			}

		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.err.println("Connection to database failed ");
		}
		return listOfAllContent;

		// throw new RuntimeException("not yet invented");
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
		} catch (UnknownHostException e) {
			e.printStackTrace();
			// System.err.println("Connection to database failed ");
			LOGGER.error("Connection to database failed");

		}

		return nbOfContents;

	}

	@Override
	public List<ContentRepositoryObject> findAllFromUser(String userID) {
		List<ContentRepositoryObject> list = new ArrayList<ContentRepositoryObject>();

		try {
			MongoClient mongoClient = DbInit.Connect();
			DB db = mongoClient.getDB("mediahome");
			DBCollection dbContents = db.getCollection("contents");
			BasicDBObject query = new BasicDBObject("userId", userID);

			ObjectMapper mapper = new ObjectMapper();
			ContentRepositoryObject content = null;

			DBCursor cursor = dbContents.find(query);

			while (cursor.hasNext()) {
				try {
					content = mapper.readValue(cursor.next().toString(),
							ContentRepositoryObject.class);
				} catch (IOException e) {
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
			MongoClient mongoClient = DbInit.Connect();
			DBCollection db = mongoClient.getDB("mediahome").getCollection(
					"contents");

			BasicDBObject query = new BasicDBObject("_id", id);
			db.remove(query);
			mongoClient.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// System.err.println("Connection to database failed ");
			LOGGER.error("Connection to database failed");

		}
	}

	@Override
	public void delete(ContentRepositoryObject entity) {
		// TODO Auto-generated method stub

		try {
			MongoClient mongoClient = DbInit.Connect();
			DB db = mongoClient.getDB("mediahome");
			DBCollection dbUsers = db.getCollection("contents");
			BasicDBObject query = new BasicDBObject("_id", entity.getId()
					.toString());
			dbUsers.remove(query);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			// System.err.println("Connection to database failed ");
			LOGGER.error("Connnection to database failed");

		}

	}

	@Override
	public void delete(Iterable<? extends ContentRepositoryObject> entities) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteAll() {
		// TODO Auto-generated method stub

		try {
			MongoClient mongoClient = DbInit.Connect();
			DB db = mongoClient.getDB("mediahome");
			DBCollection dbContents = db.getCollection("contents");

			dbContents.drop();
			mongoClient.close();

		} catch (UnknownHostException e) {
			e.printStackTrace();
			// System.err.println("Connection to database failed");
			LOGGER.error("Connnection to database failed");

		}

	}

}
