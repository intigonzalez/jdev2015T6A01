package com.enseirb.telecom.dngroup.dvd2c.service.impl;

import java.io.File;
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

import javax.inject.Inject;
import javax.ws.rs.core.NoContentException;
import javax.ws.rs.core.UriBuilder;

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
import com.enseirb.telecom.dngroup.dvd2c.service.ContentService;
import com.enseirb.telecom.dngroup.dvd2c.service.FileTypeEnum;
import com.enseirb.telecom.dngroup.dvd2c.service.FileTypeResolverFacade;
import com.enseirb.telecom.dngroup.dvd2c.service.MessageBrokerService;
import com.enseirb.telecom.dngroup.dvd2c.service.ThridPartyStorageService;
import com.enseirb.telecom.dngroup.dvd2c.utils.FSFacade;
import com.google.common.base.Throwables;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;
import com.thoughtworks.xstream.io.json.JsonWriter;

@Service
public class ContentServiceImpl implements ContentService {
	static final Integer FAILURE = -1;
	static final Integer INPROGRES = 0;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ContentServiceImpl.class);

	static final Integer SUCCESS = 1;

	@Inject
	DocumentAlternativeRepository alternativeRepo;

	@Inject
	DocumentAlternativeRepository altRepo;

	@Inject
	DocumentRepository documentRepository;

	@Inject
	FileTypeResolverFacade fileTypeResolverFacade;

	@Inject
	FSFacade fsFacade;

	@Inject
	MessageBrokerService rabbitMq;

	@Inject
	ThridPartyStorageService tps;

	@Override
	public Content createContent(String userID,
			InputStream uploadedInputStream, String contentDisposition)
			throws IOException, SecurityException {

		UUID uuid = UUID.randomUUID();
		File tempFile = fsFacade.dumpToTempFile(uuid.toString(),
				uploadedInputStream);
		FileTypeEnum type = fileTypeResolverFacade.detect(tempFile);

		Content content = new Content();

		switch (type) {
		case CLOUD:
			content.setLink("/cloud/" + userID + "/" + uuid.toString());
			content.setStatus(SUCCESS);
			break;
		case VIDEO:
			content.setLink("/videos/" + userID + "/" + uuid.toString());
			break;
		default:
			break;

		}
		content.setActorID(userID);
		content.setType(type.name());
		content.setStatus(INPROGRES);
		content.setUnixTime(System.currentTimeMillis() / 1000L);

		fsFacade.placeContentInTargetFolder(content, tempFile);
		return createWorkTask(content, CliConfSingleton.getBaseApiURI());

	}

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

			fsFacade.saveNewResolution(alt, iS);

			alternativeRepo.save(alt);
			return doc.toContent();

		}

	}

	protected Content createWorkTask(Content content, URI resourceURI)
			throws IOException {
		Document d = documentRepository.save(new Document(content));
		switch (FileTypeEnum.valueOf(content.getType())) {

		case VIDEO:
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
		case CLOUD:
		default:
			break;
		}

		return d.toContent();
	}

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
