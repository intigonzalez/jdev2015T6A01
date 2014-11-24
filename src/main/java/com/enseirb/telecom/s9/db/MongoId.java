package com.enseirb.telecom.s9.db;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MongoId {

	@JsonProperty("$oid")
	String $oid;
	
	public MongoId() {
		this.$oid= null;
	}
	public MongoId(String oid) {
		this.$oid=oid;
	}
	
	public String get$oid() {
		return $oid;
	}
	
	public void set$oid(String oid) {
		this.$oid = oid;
	}
	@JsonCreator
	  public static String fromJSON(String val) throws JsonParseException, JsonMappingException, IOException {
	    ObjectMapper mapper = new ObjectMapper();
	    MongoId a = mapper.readValue(val,MongoId.class);
	    return a.get$oid();
	  }
}
