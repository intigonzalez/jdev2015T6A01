package com.enseirb.telecom.s9.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import com.enseirb.telecom.s9.ApplicationContext;
import com.enseirb.telecom.s9.Content;
import com.enseirb.telecom.s9.Task;
import com.enseirb.telecom.s9.db.ContentRepositoryObject;
import com.enseirb.telecom.s9.db.CrudRepository;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;
import com.thoughtworks.xstream.io.json.JsonWriter;

public class ContentServiceImpl implements ContentService {

	static CrudRepository<ContentRepositoryObject, String> contentDatabase;
	RabbitMQServer rabbitMq;

	public ContentServiceImpl(
			CrudRepository<ContentRepositoryObject, String> videoDatabase, RabbitMQServer rabbitMq) {
		this.contentDatabase = videoDatabase;
		this.rabbitMq = rabbitMq;
	}
	public ContentServiceImpl(){
		
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
	public Content createContent(Content content, String srcfile, String id) {

		// Only if the file is a video content
		if(content.getType().equals("video")){
			try {

			Task task = new Task();
			task.setTask("tasks.print_shell");
			task.setId(id);
			task.getArgs().add(srcfile);
			task.getArgs().add(ApplicationContext.getProperties().getProperty("contentPath") + content.getLink());
	 
			XStream xstream = new XStream(new JsonHierarchicalStreamDriver() {
				public HierarchicalStreamWriter createWriter(Writer writer) {
					return new JsonWriter(writer, JsonWriter.DROP_ROOT_MODE);
				}
			});
			
			rabbitMq.addTask(xstream.toXML(task), task.getId());

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		return contentDatabase.save(new ContentRepositoryObject(content))
				.toContent();
	}

	@Override
	public void saveContent(Content content) {
		//TODO(0) : Manage the content update. Check all informations are done ! 
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

	@Override
	public void updateContent(String contentsID) {
		// TODO Auto-generated method stub
		String test;
		contentDatabase.findOne(contentsID).setStatus("success");
		test = contentDatabase.findOne(contentsID).getStatus();
		System.out.println("Done"+ test);
	}

}
