package com.enseirb.telecom.dngroup.dvd2c.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enseirb.telecom.dngroup.dvd2c.ApplicationContext;
import com.enseirb.telecom.dngroup.dvd2c.CliConfSingleton;
import com.enseirb.telecom.dngroup.dvd2c.db.ContentRepository;
import com.enseirb.telecom.dngroup.dvd2c.db.ContentRepositoryObject;
import com.enseirb.telecom.dngroup.dvd2c.model.Content;
import com.enseirb.telecom.dngroup.dvd2c.model.Relation;
import com.enseirb.telecom.dngroup.dvd2c.model.Task;
import com.enseirb.telecom.dngroup.dvd2c.utils.FileService;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;
import com.thoughtworks.xstream.io.json.JsonWriter;

public class ContentServiceImpl implements ContentService {
	private static final Logger LOGGER = LoggerFactory.getLogger(ContentServiceImpl.class);
	ContentRepository contentDatabase;
	RabbitMQServer rabbitMq;
	//    private RequestUserService requetUserService = new RequestUserServiceImpl();

	public ContentServiceImpl(ContentRepository videoDatabase, RabbitMQServer rabbitMq) {
		this.contentDatabase = videoDatabase;
		this.rabbitMq = rabbitMq;
	}

//	public ContentServiceImpl() {
//
//	}

	@Override
	public boolean contentExist(String contentsID) {

		return contentDatabase.exists(contentsID);
	}

	@Override
	public List<Content> getAllContentsFromUser(String userID) {
		
		List<Content> listContent = new ArrayList<Content>();
		Iterable<ContentRepositoryObject> contentsDb = contentDatabase.findAllFromUser(userID);
		Iterator<ContentRepositoryObject> itr = contentsDb.iterator();
		while (itr.hasNext()) {
			ContentRepositoryObject contentRepositoryObject = itr.next();
			if (contentRepositoryObject.getActorID().equals(userID)) {
				listContent.add(contentRepositoryObject.toContent());
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
	public Content createContent(String userID,
			InputStream uploadedInputStream, String[] fileType, File upload) {
	
			writeToFile(uploadedInputStream, upload);	
			LOGGER.debug("New file uploaded with the type {}",fileType[0]);	
			Content content = new Content();
			content.setName(upload.getName());
			content.setActorID(userID);
			content.setStatus("In progress");
			content.setType(fileType[0]);
			UUID uuid = UUID.randomUUID();
			content.setContentsID(uuid.toString().replace("-", ""));
			String link = "/videos/"+userID+"/"+uuid.toString();
			content.setLink(link);
			long unixTime = System.currentTimeMillis() / 1000L;
			content.setUnixTime(unixTime);
	
			content = createContent(content,upload.getAbsolutePath(), content.getContentsID());
			return content;
		}

	@Override
	public Content createContent(	String userID,
									InputStream uploadedInputStream,
									String contentDisposition	) throws IOException, SecurityException {
		
		
		String filename;
		String[] tmp = null;
		if(contentDisposition != null)
			tmp = contentDisposition.split("filename=");
		
		if(tmp != null && tmp.length >= 2)
			filename = tmp[1];
		else
			filename = userID;
		
		filename=filename.replace(" ", "_");
		Path tempFile = Files.createTempFile(null, null);
		LOGGER.debug("Temporary file is here {}",tempFile.toAbsolutePath());
		
		writeToFile(uploadedInputStream, tempFile.toFile());
		String fileType = Files.probeContentType(tempFile);
		LOGGER.debug("MIME : {}", fileType);
		
		tmp = fileType.split("/");
		fileType = tmp[0];
		
		if(filename.matches("(.*).MOV"))
			fileType = "video";
		
		LOGGER.debug("File type: "+fileType+", filename: "+filename);
		
		String link;
		UUID uuid = UUID.randomUUID();
		Content content = new Content();
		content.setName(filename);
		content.setActorID(userID);
		content.setType(fileType);
		content.setContentsID(uuid.toString().replace("-", ""));
		content.setStatus("In progress");

		switch (fileType) {
		case "video":
			link = "/videos/"+userID+"/"+uuid.toString();	
			break;
		case "image":
			link = "/pictures/"+userID+"/"+uuid.toString();
			content.setStatus("success");
			break;
		default:
			link = "/cloud/"+userID+"/"+uuid.toString();
			content.setStatus("success");
			break;
		}
		content.setLink(link);
		long unixTime = System.currentTimeMillis() / 1000L;
		content.setUnixTime(unixTime);

		// Create new folders if they don't exist
		File file = new File("/var/www/html"+link);
		if(!file.exists())
			file.mkdirs();
		
		// Moving the file
		LOGGER.debug("Moving temporary file to /var/www/html"+link+"/"+filename);
		File newFile = new File("/var/www/html"+link+"/"+filename);
		Files.move(tempFile, newFile.toPath());
		LOGGER.debug("File moved");
		content = createContent(content, newFile.getAbsolutePath(), content.getContentsID());
		return content;
	}

	@Override
	public Content createContent(Content content, String srcfile, String id) {

		// Only if the file is a video content
		switch (content.getType()) {
		case "video":
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
				LOGGER.error("can't connect to rabitMQ",e);
			}
			break;
		case "image":
			try {
				Task task = new Task();
				task.setTask("adaptation.commons.image_processing");
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
				LOGGER.error("can't connect to rabitMQ",e);
			}
			break;
		default:
			break;
		}
		//Initialise with public authorization by default ! 
		//	Authorization authorization = new Authorization();
		//	authorization.setGroupID(0);
		//	authorization.getAction().add("action");
		//	content.getAuthorization().add(authorization);
		return contentDatabase.save(new ContentRepositoryObject(content)).toContent();
	}

	@Override
	public void saveContent(Content content) {
		// Manage the content update. Check all informations are done !
		contentDatabase.save(new ContentRepositoryObject(content));
	}

	// save uploaded file to new location
	@Override
	public void writeToFile(InputStream uploadedInputStream, File dest) {

		try {
			// NHE: we are not in C
			Files.copy(uploadedInputStream, dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {

			LOGGER.error("can not create file ",e);
		}

	}

	@Override
	public void deleteContent(String contentsID) {
		FileService fileservice = new FileService();
		// The content then must be deleted into the folder !
		Content content = contentDatabase.findOne(contentsID).toContent();
		String path = CliConfSingleton.contentPath + content.getLink();
		LOGGER.info("remove content : {}", path);
		try {
			fileservice.deleteFolder(path);
		} catch (Exception e) {
			//XXX: ok ? 
			LOGGER.error("Removing content failed for {}", new Object[] { path, e });
		}
		// Delete into database
		contentDatabase.delete(contentsID);

	}

	public List<Content> getAllContent(String userID, Relation relation) {
		// List that will be return.
		List<Content> listContent = new ArrayList<Content>();

		// Get all the content the UserID stores
		Iterable<ContentRepositoryObject> content = contentDatabase.findAll();// FromUser(userID);
		Iterator<ContentRepositoryObject> itr = content.iterator();


		try {
			while (itr.hasNext()) { // For each content
				ContentRepositoryObject contentRepositoryObject = itr.next();
				search: 

					if ((contentRepositoryObject.getActorID()!=null)&&(contentRepositoryObject.getActorID().equals(userID))) {
						for (int i = 0; i < relation.getRoleID().size(); i++) { // For each group the relation belongs to
							if (contentRepositoryObject.getMetadata() != null) {
								if (contentRepositoryObject.getMetadata().size() == 0) {

									break search;
								}
							}
							for (int j = 0; j < contentRepositoryObject.getMetadata().size(); j++) {


								if (relation.getRoleID().get(i) == contentRepositoryObject.getMetadata().get(j)) {
									contentRepositoryObject.getMetadata().clear();
									contentRepositoryObject.setLink(ApplicationContext.getProperties().getProperty("PublicAddr")+contentRepositoryObject.getLink());
									listContent.add(contentRepositoryObject.toContent());
									break search;
								} else {
									LOGGER.debug("Group is not the same. ");
								}
							}
						}
					}
			}
		} catch (Exception e) {
			LOGGER.error("error for get contents",e);
		}

		return listContent;
	}

	@Override
	public void updateContent(String contentsID, String status) {
		ContentRepositoryObject c = contentDatabase.findOne(contentsID);
		Content content = c.toContent();
		content.setContentsID(contentsID);
		content.setStatus(status);
		contentDatabase.save(new ContentRepositoryObject(content));
	}
	

}
