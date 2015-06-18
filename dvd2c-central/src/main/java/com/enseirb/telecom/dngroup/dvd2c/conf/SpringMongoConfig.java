package com.enseirb.telecom.dngroup.dvd2c.conf;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.Mongo;

@Configuration
@EnableMongoRepositories(basePackages = { "com.enseirb.telecom.dngroup.dvd2c.db" })
public class SpringMongoConfig extends AbstractMongoConfiguration {


	  @Override
	  protected String getDatabaseName() {
	    return "mediahome";
	  }

	  @Override
	  public Mongo mongo() throws Exception {
	    return new Mongo(CliConfSingletonC.dbHostname,CliConfSingletonC.dbPort);
	  }

	  @Override
	  protected String getMappingBasePackage() {
	    return "com.enseirb.telecom.dngroup.dvd2c.db";
	  }

		
}