package com.enseirb.telecom.s9.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enseirb.telecom.s9.ApplicationContext;
import com.enseirb.telecom.s9.Authorization;
import com.enseirb.telecom.s9.Content;
import com.enseirb.telecom.s9.ListContent;
import com.enseirb.telecom.s9.Relation;
import com.enseirb.telecom.s9.Task;
import com.enseirb.telecom.s9.db.ContentRepositoryInterface;
import com.enseirb.telecom.s9.db.ContentRepositoryObject;
import com.enseirb.telecom.s9.endpoints.ContentEndPoints;
import com.enseirb.telecom.s9.request.RequestUserService;
import com.enseirb.telecom.s9.request.RequestUserServiceImpl;
import com.enseirb.telecom.s9.utils.FileService;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;
import com.thoughtworks.xstream.io.json.JsonWriter;

public class ContentServiceImpl implements ContentService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ContentServiceImpl.class);
    static FileService fileservice;
    static ContentRepositoryInterface contentDatabase;
    RabbitMQServer rabbitMq;
//    private RequestUserService requetUserService = new RequestUserServiceImpl();

    public ContentServiceImpl(ContentRepositoryInterface videoDatabase, RabbitMQServer rabbitMq) {
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
		task.setTask("adaptation.commons.ddo");
		task.setId(id);
		task.getArgs().add(srcfile);
		task.getArgs().add(content.getLink());

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
	//Initialise with public authorization by default ! 
	Authorization authorization = new Authorization();
	authorization.setGroupID(0);
	authorization.getAction().add("action");
	content.getAuthorization().add(authorization);
	return contentDatabase.save(new ContentRepositoryObject(content)).toContent();
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
	    Files.copy(uploadedInputStream, dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
	} catch (IOException e) {
	    // TODO deal with it
	    e.printStackTrace();
	}

    }

    @Override
    public void deleteContent(String contentsID) {
	// The content then must be deleted into the folder !
	Content content = contentDatabase.findOne(contentsID).toContent();
	String path = ApplicationContext.getProperties().getProperty("contentPath") + content.getLink();
	LOGGER.info("remove content : {}", path);
	try {
	    fileservice.deleteFolder(path);
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    LOGGER.error("Removing content failed for {}", new Object[] { path, e });
	}
	// Delete into database
	contentDatabase.delete(contentsID);

    }

    public ListContent getAllContent(String userID, Relation relation) {
	// List that will be return.
	ListContent listContent = new ListContent();

	// Get all the content the UserID stores
	Iterable<ContentRepositoryObject> content = contentDatabase.findAll();// FromUser(userID);
	Iterator<ContentRepositoryObject> itr = content.iterator();
	while (itr.hasNext()) { // For each content
	    ContentRepositoryObject contentRepositoryObject = itr.next();
	    search: if (contentRepositoryObject.getUserId().equals(userID)) {
		for (int i = 0; i < relation.getGroupID().size(); i++) { // For each group the relation belongs to
		    if (contentRepositoryObject.getAuthorization() != null) {
			if (contentRepositoryObject.getAuthorization().size() == 0) {
			    LOGGER.error("Groupe information : Relation in no group or video never allowed");
			    // listContent.getContent().add(contentRepositoryObject.toContent());
			    break search;
			}
		    }
		    for (int j = 0; j < contentRepositoryObject.getAuthorization().size(); j++) {
			if (relation.getGroupID().get(i) == contentRepositoryObject.getAuthorization().get(j).getGroupID()) {
			    contentRepositoryObject.getAuthorization().clear();
			    contentRepositoryObject.setLink(ApplicationContext.getProperties().getProperty("PublicAddr")+contentRepositoryObject.getLink());
			   
			    listContent.getContent().add(contentRepositoryObject.toContent());
			    break search;
			} else {
//			    LOGGER.debug("Group is not the same. ");
			}
		    }
		}
	    }
	}
	return listContent;
    }

    @Override
    public void updateContent(String contentsID, String status) {
	// TODO Auto-generated method stub
	Content content = new Content();
	content.setContentsID(contentsID);
	content.setStatus(status);
	contentDatabase.save(new ContentRepositoryObject(content));
    }

}
