package com.enseirb.telecom.dngroup.dvd2c.db;

import java.net.UnknownHostException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;


//NHE a class with only static methods should be final or abstract
public class DbInit {
	//NHE no hard coded values allowed in the code
	String hostname = "localhost";
	//NHE no hard coded values allowed in the code
	int port = 27017;
	String username;
	String password;

	//NHE bad method firstname https://google-styleguide.googlecode.com/svn/trunk/javaguide.html#s5.2.3-method-names
	public static MongoClient Connect() throws UnknownHostException {
		DbInit Database = new DbInit();
		MongoClient mongoClient = new MongoClient(Database.hostname, Database.port);
		//NHE where is it closed ? http://api.mongodb.org/java/2.10.0/com/mongodb/Mongo.html#close%28%29
		return mongoClient;
	}
	public static DBObject createDBObject(Object o) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		DBObject dbo = (DBObject)JSON.parse(mapper.writeValueAsString(o));
		return dbo;
	}
}
