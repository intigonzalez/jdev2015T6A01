package com.enseirb.telecom.dngroup.dvd2c.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enseirb.telecom.dngroup.dvd2c.CliConfSingleton;
import com.enseirb.telecom.dngroup.dvd2c.QueueConsumerApp;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

@Deprecated
public class RabbitMQServiceImpl implements MessageBrokerService {
	private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQServiceImpl.class);
	String QUEUE_NAME;
	ConnectionFactory factory;
	Connection connection;
	Channel channel;

	public RabbitMQServiceImpl() {
		QUEUE_NAME = "celery";
		factory = new ConnectionFactory();
		factory.setHost(CliConfSingleton.rabbitHostname);
		factory.setPort(CliConfSingleton.rabbitPort );
		LOGGER.debug("Rabbit conection : host : {} , port : {} ",factory.getHost(),factory.getPort());
		try {
			connection = factory.newConnection();
			channel = connection.createChannel();
			channel.queueDeclare(QUEUE_NAME, true, false, false, null);
		} catch (IOException e) {
			LOGGER.error("Error to conect with RabbitMQ",e);
		}

	}

/* (non-Javadoc)
 * @see com.enseirb.telecom.dngroup.dvd2c.service.RabbitMQService#addTask(java.lang.String, java.lang.String)
 */
	@Override
	public void addTask(String task, String id)
			throws UnsupportedEncodingException, IOException {
		channel.basicPublish("", QUEUE_NAME, new AMQP.BasicProperties.Builder()
				.contentType("application/json").contentEncoding("utf-8")
				.build(), task.getBytes("utf-8"));

		try {
			QueueConsumerApp.getQueueMessage(id);
		} catch (InterruptedException e) {
		LOGGER.error("Queue interrupted",e);
		}
	}
}
