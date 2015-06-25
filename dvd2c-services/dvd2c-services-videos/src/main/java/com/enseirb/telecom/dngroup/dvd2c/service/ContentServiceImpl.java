package com.enseirb.telecom.dngroup.dvd2c.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.net.URI;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javassist.bytecode.annotation.NoSuchClassError;

import javax.inject.Inject;
import javax.management.RuntimeErrorException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.NoContentException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.apache.tika.Tika;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.enseirb.telecom.dngroup.dvd2c.CliConfSingleton;
import com.enseirb.telecom.dngroup.dvd2c.exception.AlternativeStorageException;
import com.enseirb.telecom.dngroup.dvd2c.model.Content;
import com.enseirb.telecom.dngroup.dvd2c.model.Resolution;
import com.enseirb.telecom.dngroup.dvd2c.model.Task;
import com.enseirb.telecom.dngroup.dvd2c.modeldb.Document;
import com.enseirb.telecom.dngroup.dvd2c.modeldb.DocumentAlternative;
import com.enseirb.telecom.dngroup.dvd2c.repository.DocumentAlternativeRepository;
import com.enseirb.telecom.dngroup.dvd2c.repository.DocumentRepository;
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
	DocumentRepository documentRepository;

	@Inject
	MessageBrokerService rabbitMq;

	static final Integer SUCCESS = 1;
	static final Integer FAILURE = -1;
	static final Integer INPROGRES = 0;

	@Override
	public boolean contentExist(Integer contentsID) {
		return documentRepository.exists(contentsID);
	}

	@Override
	public List<Content> getAllContentsFromUser(UUID userID) {
		List<Content> listContent = new ArrayList<Content>();
		try {
			Iterable<Document> contentsDb = documentRepository
					.findByActorUUID(userID);
			for (Document document : contentsDb) {
				listContent.add(document.toContent());
			}
		} catch (NumberFormatException e) {
			LOGGER.error("NumberFormatException", e);
		}
		return listContent;
	}

	@Inject
	DocumentAlternativeRepository altRepo;

	@Override
	public Content getContent(Integer contentsID) throws NoContentException {
		Document document = documentRepository.findOne(contentsID);
		if (document == null) {
			throw new NoContentException("Content ID can't be NULL");
		} else {
			Content res = document.toContent();
			for (DocumentAlternative alt : altRepo.findByDocument(document)) {
				Resolution resolution = new Resolution();
				resolution.setName(alt.getResolution());
				if (alt.getUri() == null) {
					// that's us, generate our own UR
					resolution.setUri(CliConfSingleton.getBaseApiURI()
							+ "/content/" + contentsID + "/"
							+ resolution.getName());
				} else {
					resolution.setUri(alt.getUri());
				}
				res.getResolution().add(resolution);

			}
			return res;
		}
	}

	@Override
	public Content createContent(String userID,
			InputStream uploadedInputStream, String contentDisposition)
			throws IOException, SecurityException {

		String filename;
		String[] tmp = null;

		filename = "original";

		// Temporary until we find a better way to deal with filenames
		filename = filename.replace(" ", "_");
		UUID uuid = UUID.randomUUID();
		File tempFile = File.createTempFile(uuid.toString(), null);
		LOGGER.info("Temporary file is here {}", tempFile.getAbsolutePath());

		writeToFile(uploadedInputStream, tempFile);
		//
		// String fileType = Files.probeContentType(tempFile);
		Tika tika = new Tika();
		String fileType = tika.detect(tempFile);
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
		// content.setContentsID(uuid.toString().replace("-", ""));
		content.setStatus(INPROGRES);

		switch (fileType) {
		case "video":
			link = "/videos/" + userID + "/" + uuid.toString();
			break;
		case "image":
			link = "/pictures/" + userID + "/" + uuid.toString();
			content.setStatus(INPROGRES);
			break;
		default:
			link = "/cloud/" + userID + "/" + uuid.toString();
			content.setStatus(SUCCESS);
			break;
		}
		content.setLink(link);
		long unixTime = System.currentTimeMillis() / 1000L;
		content.setUnixTime(unixTime);

		// Create new folders if they don't exist
		File file = new File("/var/www/html" + link);

		if (!file.exists())
			if (!file.mkdirs()) {
				LOGGER.error("Can not create the file are you corect right ?");
				throw new IOException(file.getAbsolutePath());
			}

		// Moving the file
		LOGGER.debug("Moving temporary file to /var/www/html" + link + "/"
				+ filename);
		File newFile = new File("/var/www/html" + link + "/" + filename);
		try {
			Files.move(tempFile, newFile);
			LOGGER.debug("File moved");
			content = createWorkTask(content, CliConfSingleton.getBaseApiURI());
			return content;
		} catch (IOException e) {
			LOGGER.error("Can not create the file are you corect right ?");
			throw e;
		}

	}

	protected Content createWorkTask(Content content, URI resourceURI)
			throws IOException {
		Document d = documentRepository.save(new Document(content));
		switch (content.getType()) {
		case "video":
			try {
				Task task = new Task();
				task.setTask("adaptation.commons.ddo");
				task.setId(d.getId().toString());
				task.getArgs().add(
						resourceURI.toString() + "/content/" + d.getId());

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
				LOGGER.error("can't connect to rabitMQ", e);
				throw Throwables.propagate(e);
			}
			break;
		case "image":
			try {
				Task task = new Task();
				task.setTask("adaptation.commons.image_processing");
				task.setId(d.getId().toString());
				task.getArgs().add(
						resourceURI.toString() + "/content/"
								+ content.getContentsID());

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
				LOGGER.error("can't connect to rabitMQ", e);
				throw Throwables.propagate(e);
			}
			break;
		default:
			LOGGER.info("Content without processing");
			break;
		}

		return d.toContent();
	}

	@Override
	public void saveContent(Content content) {
		// Manage the content update. Check all informations are done !
		documentRepository.save(new Document(content));
	}

	// save uploaded file to new location

	private void writeToFile(InputStream uploadedInputStream, File dest)
			throws IOException {

		try {
			// NHE: we are not in C

			ByteStreams.copy(uploadedInputStream, new FileOutputStream(dest));

		} catch (IOException e) {

			LOGGER.error("can not create file ", e);
			throw e;
		}

	}

	@Override
	public void deleteContent(Integer contentsID) {

		// The content then must be deleted into the folder !

		Document document = documentRepository.findOne(contentsID);

		String path = CliConfSingleton.contentPath + document.getFileLink();
		LOGGER.info("remove content : {}", path);
		try {
			FileService.deleteFolder(path);
		} catch (Exception e) {
			// XXX: ok ?
			LOGGER.error("Removing content failed for {}", new Object[] { path,
					e });
		}
		// Delete into database
		documentRepository.delete(contentsID);

	}

	@Override
	public void updateContent(String contentsID, Integer status) {
		try {

			Document document = documentRepository.findOne(Integer
					.parseInt(contentsID));
			document.setFileProcessing(status);
			documentRepository.save(document);
		} catch (NumberFormatException e) {
		}
	}

	@Inject
	ThridPartyStorageService tps;

	@Inject
	DocumentAlternativeRepository alternativeRepo;

	@Override
	public Content createNewContentResolution(String contentId,
			String resolutionName, InputStream iS, String contentDisposition)
			throws AlternativeStorageException, IOException {

		Document doc = documentRepository.findOne(Integer.valueOf(contentId));
		if (doc == null)
			throw new NoContentException("no content with contentId"
					+ contentId);
		List<URI> altUri = tps.generateRedirectURUri(contentId);
		if (altUri != null && altUri.size() > 0) {
			AlternativeStorageException ase = new AlternativeStorageException();
			ase.setUri(UriBuilder.fromUri(altUri.get(0)).path(resolutionName)
					.build());
			throw ase;

		} else {

			List<DocumentAlternative> alts = doc.getDocumentAlternative();
			if (alts == null) {
				alts = new ArrayList<DocumentAlternative>();
			}
			DocumentAlternative alt = new DocumentAlternative();
			alt.setDocument(doc);
			alt.setResolution(resolutionName);
			alt.setUri(null);

			File newFile = new File("/var/www/html/" + doc.getFileLink() + "/"
					+ resolutionName);
			writeToFile(iS, newFile);

			alternativeRepo.save(alt);
			return doc.toContent();

		}

	}

	@Override
	public void updateContentWithUrl(String contentId, String resolution,
			String url) throws NoContentException {

		Document doc = documentRepository.findOne(Integer.valueOf(contentId));
		if (doc == null) {
			throw new NoContentException(contentId);
		}
		DocumentAlternative alt = new DocumentAlternative();
		alt.setDocument(doc);
		alt.setResolution(resolution);
		alt.setUri(url);

		alternativeRepo.save(alt);

	}
}
