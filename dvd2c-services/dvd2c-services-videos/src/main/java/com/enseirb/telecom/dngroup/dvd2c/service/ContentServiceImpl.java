package com.enseirb.telecom.dngroup.dvd2c.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import org.apache.tika.Tika;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.enseirb.telecom.dngroup.dvd2c.CliConfSingleton;
import com.enseirb.telecom.dngroup.dvd2c.db.ContentRepository;
import com.enseirb.telecom.dngroup.dvd2c.db.ContentRepositoryObject;
import com.enseirb.telecom.dngroup.dvd2c.model.Content;
import com.enseirb.telecom.dngroup.dvd2c.model.Relation;
import com.enseirb.telecom.dngroup.dvd2c.model.Task;
import com.enseirb.telecom.dngroup.dvd2c.utils.FileService;
import com.google.common.base.Throwables;
import com.google.common.io.ByteStreams;
import com.google.common.io.Files;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;
import com.thoughtworks.xstream.io.json.JsonWriter;

@Service
public class ContentServiceImpl implements ContentService {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ContentServiceImpl.class);
	@Inject
	ContentRepository contentDatabase;
	@Inject
	MessageBrokerService rabbitMq;

	// private RequestUserService requetUserService = new
	// RequestUserServiceImpl();

	// public ContentServiceImpl() {
	//
	// }

	@Override
	public boolean contentExist(String contentsID) {

		return contentDatabase.exists(contentsID);
	}

	@Override
	public List<Content> getAllContentsFromUser(String userID) {

		List<Content> listContent = new ArrayList<Content>();
		Iterable<ContentRepositoryObject> contentsDb = contentDatabase.findAll();
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
	public Content getContent(String contentsID) /*throws NoContentException*/ {
		ContentRepositoryObject content = contentDatabase.findOne(contentsID);
		if (content == null) {
			return null;
		} else {
			return content.toContent();
		}
		/*ContentRepositoryObject content = contentDatabase.findOne(contentsID);
		if (content == null) {
			throw new NoContentException(contentsID);
		} else {
			return content.toContent();
		}*/
	}

	@Override
	public Content createContent(String userID,
			InputStream uploadedInputStream, String[] fileType, File upload) throws IOException {
		LOGGER.debug("New file write on system {}", upload.getAbsolutePath());
		writeToFile(uploadedInputStream, upload);
		LOGGER.debug("New file uploaded with the type {}", fileType[0]);
		Content content = new Content();
		content.setName(upload.getName());
		content.setActorID(userID);
		content.setStatus("In progress");
		content.setType(fileType[0]);
		UUID uuid = UUID.randomUUID();
		content.setContentsID(uuid.toString().replace("-", ""));
		String link = "/videos/" + userID + "/" + uuid.toString();
		content.setLink(link);
		long unixTime = System.currentTimeMillis() / 1000L;
		content.setUnixTime(unixTime);

		content = createContent(content, upload.getAbsolutePath(),
				content.getContentsID());
		return content;
	}

	@Override
	public Content createContent(String userID,
			InputStream uploadedInputStream, String contentDisposition)
			throws IOException, SecurityException {

		String filename;
		String[] tmp = null;
		if (contentDisposition != null)
			tmp = contentDisposition.split("filename=");

		if (tmp != null && tmp.length >= 2)
			filename = tmp[1];
		else
			filename = userID;
		
		// Temporary until we find a better way to deal with filenames
		filename=filename.replace(" ", "_");
		UUID uuid = UUID.randomUUID();
		File tempFile = File.createTempFile(uuid.toString(), null);
		LOGGER.debug("Temporary file is here {}", tempFile.getAbsolutePath());

		writeToFile(uploadedInputStream, tempFile);
//		
//		String fileType = Files.probeContentType(tempFile);
		Tika tika = new Tika();
		String fileType= tika.detect(tempFile);
		LOGGER.debug("MIME : {}", fileType);

		tmp = fileType.split("/");
		fileType = tmp[0];

		if (filename.matches("(.*).MOV"))
			fileType = "video";

		LOGGER.debug("File type: " + fileType + ", filename: " + filename);

		String link;
		Content content = new Content();
		content.setName(filename);
		content.setActorID(userID);
		content.setType(fileType);
		content.setContentsID(uuid.toString().replace("-", ""));
		content.setStatus("In progress");

		switch (fileType) {
		case "video":
			link = "/videos/" + userID + "/" + uuid.toString();
			break;
		case "image":
			link = "/pictures/" + userID + "/" + uuid.toString();
			//content.setStatus("success");
			break;
		default:
			link = "/cloud/" + userID + "/" + uuid.toString();
			content.setStatus("success");
			break;
		}
		content.setLink(link);
		long unixTime = System.currentTimeMillis() / 1000L;
		content.setUnixTime(unixTime);

		// Create new folders if they don't exist
		File file = new File("/var/www/html" + link);
		
		if (!file.exists())
			if (!file.mkdirs()){
			LOGGER.error("Can not create the file are you corect right ?");
			throw new IOException(file.getAbsolutePath());}
		



		// Moving the file
		LOGGER.debug("Moving temporary file to /var/www/html" + link + "/"
				+ filename);
		
		File newFile = new File("/var/www/html" + link + "/" + filename);
		try {
			Files.move(tempFile, newFile);
			LOGGER.debug("File moved");
			content = createContent(content, newFile.getAbsolutePath(),
					content.getContentsID());
			return content;
		} catch (IOException e) {
			LOGGER.error("Can not create the file are you corect right ?");
			throw e;
		}
		
	}

	@Override
	public Content createContent(Content content, String srcfile, String id) throws IOException {

		
		switch (content.getType()) {
		case "video":
			try {
				Task task = new Task();
				task.setTask("adaptation.commons.ddo");
				task.setId(id);
				task.getArgs().add(srcfile);
				task.getArgs().add(content.getLink());

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
				LOGGER.error("can't connect to rabitMQ",e);
				throw Throwables.propagate(e);
			}
			break;
		case "image":
			try {
				Task task = new Task();
				task.setTask("adaptation.commons.image_processing");
				task.setId(id);
				task.getArgs().add(srcfile);
				task.getArgs().add(content.getLink());

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
				LOGGER.error("can't connect to rabitMQ",e);
				throw Throwables.propagate(e);
			}
			break;
		default:
			LOGGER.info("Content without processing");
			break;
		}
		// Initialise with public authorization by default !
		// Authorization authorization = new Authorization();
		// authorization.setGroupID(0);
		// authorization.getAction().add("action");
		// content.getAuthorization().add(authorization);
		return contentDatabase.save(new ContentRepositoryObject(content))
				.toContent();
	}

	@Override
	public void saveContent(Content content) {
		// Manage the content update. Check all informations are done !
		contentDatabase.save(new ContentRepositoryObject(content));
	}

	// save uploaded file to new location

	private void writeToFile(InputStream uploadedInputStream, File dest) throws IOException {

		try {
			// NHE: we are not in C
			ByteStreams.copy(uploadedInputStream,new FileOutputStream(dest));
		} catch (IOException e) {

			LOGGER.error("can not create file ", e);
			throw e;
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
			// XXX: ok ?
			LOGGER.error("Removing content failed for {}", new Object[] { path,
					e });
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

				if ((contentRepositoryObject.getActorID() != null)
						&& (contentRepositoryObject.getActorID().equals(userID))) {
					for (int i = 0; i < relation.getRoleID().size(); i++) { // For
																			// each
																			// group
																			// the
																			// relation
																			// belongs
																			// to
						if (contentRepositoryObject.getMetadata() != null) {
							if (contentRepositoryObject.getMetadata().size() == 0) {

								break search;
							}
						}
						for (int j = 0; j < contentRepositoryObject
								.getMetadata().size(); j++) {

							if (relation.getRoleID().get(i) == contentRepositoryObject
									.getMetadata().indexOf(j)) {
								contentRepositoryObject.getMetadata().clear();
								contentRepositoryObject
										.setLink(CliConfSingleton.publicAddr
												+ contentRepositoryObject
														.getLink());
								listContent.add(contentRepositoryObject
										.toContent());
								break search;
							} else {
								LOGGER.debug("Group is not the same. ");
							}
						}
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("error for get contents", e);
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
