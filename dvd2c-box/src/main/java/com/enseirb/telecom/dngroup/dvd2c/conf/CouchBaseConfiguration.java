package com.enseirb.telecom.dngroup.dvd2c.conf;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration;

import com.enseirb.telecom.dngroup.dvd2c.CliConfSingleton;

@Configuration
public class CouchBaseConfiguration extends AbstractCouchbaseConfiguration{


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