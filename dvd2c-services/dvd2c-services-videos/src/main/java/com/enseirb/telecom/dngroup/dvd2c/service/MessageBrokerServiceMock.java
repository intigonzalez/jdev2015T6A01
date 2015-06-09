package com.enseirb.telecom.dngroup.dvd2c.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


public class MessageBrokerServiceMock implements MessageBrokerService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(MessageBrokerServiceMock.class);

	@Override
	public void addTask(String task, Integer id)
			throws UnsupportedEncodingException, IOException {
		LOGGER.warn("Not implemented, nobody will ever perform this task!");

	}



}
