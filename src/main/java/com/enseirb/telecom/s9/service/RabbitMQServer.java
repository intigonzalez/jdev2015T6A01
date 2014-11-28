package com.enseirb.telecom.s9.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;

public class RabbitMQServer {
	String QUEUE_NAME;
	ConnectionFactory factory;
	Connection connection;
	Channel channel;
	
	public RabbitMQServer() {
		QUEUE_NAME = "celery";
		factory = new ConnectionFactory();
		factory.setHost("localhost");
		try {
			connection = factory.newConnection();
			channel = connection.createChannel();
			channel.queueDeclare(QUEUE_NAME, true, false, false, null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void addTask(String task) throws UnsupportedEncodingException, IOException {
		channel.basicPublish("", QUEUE_NAME, new AMQP.BasicProperties.Builder()
		.contentType("application/json").contentEncoding("utf-8")
		.build(), task.getBytes("utf-8"));

	}

}
