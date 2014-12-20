package com.enseirb.telecom.s9.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.enseirb.telecom.s9.ApplicationContext;
import com.enseirb.telecom.s9.Content;
import com.enseirb.telecom.s9.ListContent;
import com.enseirb.telecom.s9.Task;
import com.enseirb.telecom.s9.db.ContentRepositoryInterface;
import com.enseirb.telecom.s9.db.ContentRepositoryObject;
import com.enseirb.telecom.s9.db.RelationshipRepositoryObject;
import com.enseirb.telecom.s9.request.RequestUserService;
import com.enseirb.telecom.s9.request.RequestUserServiceImpl;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;
import com.thoughtworks.xstream.io.json.JsonWriter;

public class ContentServiceImpl implements ContentService {

	static ContentRepositoryInterface contentDatabase;
	RabbitMQServer rabbitMq;
	private RequestUserService requetUserService = new RequestUserServiceImpl(
			ApplicationContext.getProperties().getProperty("CentralURL")
					+ "/api/app/account/");

	public ContentServiceImpl(
			ContentRepositoryInterface videoDatabase,
			RabbitMQServer rabbitMq) {
		this.contentDatabase = videoDatabase;
		this.rabbitMq = rabbitMq;
	}

	public ContentServiceImpl() {

	}

	@Override
	public boolean contentExist(String contentsID) {

		return contentDatabase.exists(contentsID);
	}
	@Override
	public ListContent getAllContentsFromUser(String userID) {
		contentDatabase.findAllFromUser(userID);
		
		ListContent listContent = new ListContent();
		Iterable<ContentRepositoryObject> contentsDb = contentDatabase.findAllFromUser(userID);
		Iterator<ContentRepositoryObject> itr = contentsDb.iterator();
		while (itr.hasNext()) {
			ContentRepositoryObject contentRepositoryObject = itr.next();
			if (contentRepositoryObject.getUserId().equals(userID)) {
				listContent.getContent().add(contentRepositoryObject.toContent());
			}
		}
		return listContent;
		
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
		if (content.getType().equals("video")) {
			try {

				Task task = new Task();
				task.setTask("tasks.print_shell");
				task.setId(id);
				task.getArgs().add(srcfile);
				task.getArgs().add(
						ApplicationContext.getProperties().getProperty(
								"contentPath")
								+ content.getLink());

				XStream xstream = new XStream(
						new JsonHierarchicalStreamDriver() {
							public HierarchicalStreamWriter createWriter(
									Writer writer) {
								return new JsonWriter(writer,
										JsonWriter.DROP_ROOT_MODE);
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
		// TODO(0) : Manage the content update. Check all informations are done
		// !
		contentDatabase.save(new ContentRepositoryObject(content));
	}

	// save uploaded file to new location
	@Override
	public void writeToFile(InputStream uploadedInputStream, File dest) {

		try {
			// NHE: we are not in C
			Files.copy(uploadedInputStream, dest.toPath(),
					StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			// TODO deal with it
			e.printStackTrace();
		}

	}

	@Override
	public void deleteContent(String contentsID) {
		contentDatabase.delete(contentsID);

	}

	public ListContent getAllContent(List<Integer> groupID) {
		ListContent listContent = new ListContent();
		Iterable<ContentRepositoryObject> content = contentDatabase.findAll();
		Iterator<ContentRepositoryObject> itr = content.iterator();
		while (itr.hasNext()) {
			Boolean found = false;
			ContentRepositoryObject contentRepositoryObject = itr.next();
			for (int i = 0; i < groupID.size(); i++) {
				if (found)
					break;
				if (contentRepositoryObject.getAuthorization() != null)
					if (contentRepositoryObject.getAuthorization().size() == 0) {
						System.err.println("Groupe information err");
						listContent.getContent().add(
								contentRepositoryObject.toContent());
					}
				for (int j = 0; j < contentRepositoryObject.getAuthorization()
						.size(); j++) {
					if (found)
						break;
					if (groupID.get(i) == contentRepositoryObject
							.getAuthorization().get(j).getGroupID()) {
						listContent.getContent().add(
								contentRepositoryObject.toContent());
						found = true;
					} else {
						System.err.println("Groupe is not the same");
					}
				}
			}
		}
		return listContent;
	}

	@Override
	public void updateContent(String contentsID) {
		// TODO Auto-generated method stub
		Content content = new Content();
		content.setContentsID(contentsID);
		content.setStatus("success");
		contentDatabase.save(new ContentRepositoryObject(content));
	}

}
