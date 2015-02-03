package com.enseirb.telecom.dvd2c.db;

import java.net.UnknownHostException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;

public class DbInit {
	String hostname = "localhost";
	int port = 27017;
	String username;
	String password;

	public static MongoClient Connect() throws UnknownHostException {
		DbInit Database = new DbInit();
		MongoClient mongoClient = new MongoClient(Database.hostname, Database.port);
		return mongoClient;
	}
	public static DBObject createDBObject(Object o) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		DBObject dbo = (DBObject)JSON.parse(mapper.writeValueAsString(o));
		return dbo;
	}
}
