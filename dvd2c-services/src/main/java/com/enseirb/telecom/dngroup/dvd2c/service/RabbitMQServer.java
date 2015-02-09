package com.enseirb.telecom.dngroup.dvd2c.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import com.enseirb.telecom.dngroup.dvd2c.QueueConsumerApp;
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
		factory.setHost("localhost"); // To be changed???
		try {
			connection = factory.newConnection();
			channel = connection.createChannel();
			channel.queueDeclare(QUEUE_NAME, true, false, false, null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

/**
 * Add the task and ask for the end message
 * @param task
 * @param id
 * @throws UnsupportedEncodingException
 * @throws IOException
 */
	public void addTask(String task, String id)
			throws UnsupportedEncodingException, IOException {
		channel.basicPublish("", QUEUE_NAME, new AMQP.BasicProperties.Builder()
				.contentType("application/json").contentEncoding("utf-8")
				.build(), task.getBytes("utf-8"));

		try {
			QueueConsumerApp.getQueueMessage(id);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
