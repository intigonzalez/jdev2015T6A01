package com.enseirb.telecom.s9.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import com.enseirb.telecom.s9.Content;
import com.enseirb.telecom.s9.db.ContentRepositoryObject;
import com.enseirb.telecom.s9.db.CrudRepository;

public class ContentServiceImpl implements ContentService {

	CrudRepository<ContentRepositoryObject, String> contentDatabase;
	RabbitMQServer rabbitMq;

	public ContentServiceImpl(
			CrudRepository<ContentRepositoryObject, String> videoDatabase, RabbitMQServer rabbitMq) {
		this.contentDatabase = videoDatabase;
		this.rabbitMq = rabbitMq;
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
		UUID uuid = UUID.randomUUID();
//		System.out.println("UUID: " + uuid.toString());
		String message = "{\"id\": \""+uuid.toString()+"\", \"task\": \"tasks.print_shell\", \"args\": [\""+ srcfile + "\",\""+ content.getLink().substring(1) +"\"], \"kwargs\": {}, \"retries\": 0, \"eta\": \"2009-11-17T12:30:56.527191\"}";
		rabbitMq.addTask(message);
		System.out.println(" [x] Sent '" + message + "'");
//		rabbitMq.channel.close();
//		rabbitMq.connection.close();
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
