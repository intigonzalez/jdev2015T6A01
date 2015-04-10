package com.enseirb.telecom.dngroup.dvd2c.conf;

import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration;
import org.springframework.data.couchbase.repository.config.EnableCouchbaseRepositories;

@Configuration
@EnableCouchbaseRepositories(basePackages = { "com.enseirb.telecom.dngroup.dvd2c.db" })
public class CouchBaseConfiguration extends AbstractCouchbaseConfiguration {

	@Override
	protected List<String> bootstrapHosts() {
		return Collections.singletonList("127.0.0.1");
	}

	@Override
	protected String getBucketName() {
		return "beer-sample";
	}

	@Override
	protected String getBucketPassword() {
		return "";
	}

}