package com.enseirb.telecom.s9.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import com.enseirb.telecom.s9.Content;
import com.enseirb.telecom.s9.db.ContentRepositoryObject;
import com.enseirb.telecom.s9.db.CrudRepository;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;

public class ContentServiceImpl implements ContentService {

	CrudRepository<ContentRepositoryObject, String> contentDatabase;

	public ContentServiceImpl(
			CrudRepository<ContentRepositoryObject, String> videoDatabase) {
		this.contentDatabase = videoDatabase;
	}

	@Override
	public boolean contentExist(String contentsID) {

		return contentDatabase.exists(contentsID);
	}

	@Override
	public Content getContent(String contentsID) {
		ContentRepositoryObject content = contentDatabase.findOne(contentsID);
		if (content == null) {
			return null;
		} else {
			return content.toContent();
		}
	}

	@Override
	public Content createContent(Content content, String srcfile) {
		
		try {
		String QUEUE_NAME = "celery";
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		com.rabbitmq.client.Connection connection;
		connection = factory.newConnection();
		Channel channel = connection.createChannel();
		channel.queueDeclare(QUEUE_NAME, true, false, false, null);
		String message = "{\"id\": \"4cc7436e-afd4-4f8f-a2f3-f46567e7ca77\", \"task\": \"tasks.print_shell\", \"args\": [\""+ srcfile + "\",\""+ content.getLink().substring(1) +"\"], \"kwargs\": {}, \"retries\": 0, \"eta\": \"2009-11-17T12:30:56.527191\"}";
		channel.basicPublish("", QUEUE_NAME, new AMQP.BasicProperties.Builder()
				.contentType("application/json").contentEncoding("utf-8")
				.build(), message.getBytes("utf-8"));
		System.out.println(" [x] Sent '" + message + "'");
		channel.close();
		connection.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return contentDatabase.save(new ContentRepositoryObject(content))
				.toContent();
	}

	@Override
	public void saveContent(Content content) {
		contentDatabase.save(new ContentRepositoryObject(content));
	}

	// save uploaded file to new location
	@Override
	public void writeToFile(InputStream uploadedInputStream, File dest) {

		try {
			//NHE: we are not in C
			Files.copy(uploadedInputStream, dest.toPath(),StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			// TODO deal with it
			e.printStackTrace();
		}

	}

	@Override
	public void deleteContent(String contentsID) {
		this.contentDatabase.delete(contentsID);

	}

}
