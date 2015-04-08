package com.enseirb.telecom.dngroup.dvd2c.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public interface MessageBrokerService {

	/**
	 * Add the task and ask for the end message
	 * 
	 * @param task
	 * @param id
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	public abstract void addTask(String task, String id)
			throws UnsupportedEncodingException, IOException;

}