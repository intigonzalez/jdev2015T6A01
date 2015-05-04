//package com.enseirb.telecom.dngroup.dvd2c.db;
//
//import java.net.UnknownHostException;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.enseirb.telecom.dngroup.dvd2c.ApplicationContext;
//import com.enseirb.telecom.dngroup.dvd2c.CliConfSingleton;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.mongodb.DBObject;
//import com.mongodb.MongoClient;
//import com.mongodb.util.JSON;
//
//
////NHE a class with only static methods should be final or abstract
//public class DbInit {
//	private static final Logger LOGGER = LoggerFactory.getLogger(DbInit.class);
//	
//
//	//NHE no hard coded values allowed in the code
//
//	String username;
//	String password;
//
//
//
//
//	//NHE bad method firstname https://google-styleguide.googlecode.com/svn/trunk/javaguide.html#s5.2.3-method-names
//	public static MongoClient connect() throws UnknownHostException {
//
//		String hostname=CliConfSingleton.dbHostname;	
//		int port = CliConfSingleton.dbPort;
//		
//		MongoClient mongoClient;
//		mongoClient = new MongoClient(hostname, port);
//
//		//NHE where is it closed ? http://api.mongodb.org/java/2.10.0/com/mongodb/Mongo.html#close%28%29
//		return mongoClient;
//	}
//	public static DBObject createDBObject(Object o) throws JsonProcessingException {
//		ObjectMapper mapper = new ObjectMapper();
//		DBObject dbo = (DBObject)JSON.parse(mapper.writeValueAsString(o));
//		return dbo;
//	}
//}
