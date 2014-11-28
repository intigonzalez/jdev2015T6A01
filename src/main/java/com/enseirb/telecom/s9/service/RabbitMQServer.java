package com.enseirb.telecom.s9.service;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;

public class RabbitMQServer {
	String QUEUE_NAME;
	ConnectionFactory factory;
	com.rabbitmq.client.Connection connection;
	Channel channel;
	
	public RabbitMQServer() throws IOException {
		QUEUE_NAME = "celery";
		factory = new ConnectionFactory();
		factory.setHost("localhost");
		connection = factory.newConnection();
		channel = connection.createChannel();
		channel.queueDeclare(QUEUE_NAME, true, false, false, null);
	}

}
