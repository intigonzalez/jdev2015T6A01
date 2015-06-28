package com.enseirb.telecom.dngroup.dvd2c.service.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.stereotype.Service;

import com.enseirb.telecom.dngroup.dvd2c.service.ContentService;
import com.enseirb.telecom.dngroup.dvd2c.service.MessageBrokerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;

@Service
public class RabbitMQServiceImpl implements MessageBrokerService {
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory
			.getLogger(RabbitMQServiceImpl.class);

	public static final String RESULTQUEUE = "transcode-result";

	@Inject
	ContentService contentService;

	@Inject
	volatile Channel channel;

	@Inject
	volatile RabbitAdmin admin;

	@Inject
	volatile ConnectionFactory conFact;

	@Inject
	ObjectMapper mapper;

	public static String QUEUE_NAME = "celery";

	public RabbitMQServiceImpl() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enseirb.telecom.dngroup.dvd2c.service.RabbitMQService#addTask(java
	 * .lang.String, java.lang.String)
	 */
	@Override
	public void addTask(String task)
			throws UnsupportedEncodingException, IOException {

		channel.basicPublish("", QUEUE_NAME, new AMQP.BasicProperties.Builder()
				.contentType("application/json").contentEncoding("utf-8")
				.build(), task.getBytes("utf-8"));

	}

}
