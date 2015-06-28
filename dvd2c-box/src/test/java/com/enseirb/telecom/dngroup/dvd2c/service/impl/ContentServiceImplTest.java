package com.enseirb.telecom.dngroup.dvd2c.service.impl;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.UUID;

import javax.inject.Inject;
import javax.ws.rs.core.NoContentException;
import javax.ws.rs.core.UriBuilder;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.agileapes.utils.spring.KeyGeneration;
import com.agileapes.utils.spring.impl.RepositoryMock;
import com.enseirb.telecom.dngroup.dvd2c.CliConfSingleton;
import com.enseirb.telecom.dngroup.dvd2c.exception.AlternativeStorageException;
import com.enseirb.telecom.dngroup.dvd2c.model.Content;
import com.enseirb.telecom.dngroup.dvd2c.model.Task;
import com.enseirb.telecom.dngroup.dvd2c.modeldb.Document;
import com.enseirb.telecom.dngroup.dvd2c.modeldb.DocumentAlternative;
import com.enseirb.telecom.dngroup.dvd2c.repository.DocumentAlternativeRepository;
import com.enseirb.telecom.dngroup.dvd2c.repository.DocumentRepository;
import com.enseirb.telecom.dngroup.dvd2c.service.AbstractFSFacade;
import com.enseirb.telecom.dngroup.dvd2c.service.FSFacade;
import com.enseirb.telecom.dngroup.dvd2c.service.FileTypeEnum;
import com.enseirb.telecom.dngroup.dvd2c.service.FileTypeResolverFacade;
import com.enseirb.telecom.dngroup.dvd2c.service.MessageBrokerService;
import com.enseirb.telecom.dngroup.dvd2c.service.ThridPartyStorageService;
import com.enseirb.telecom.dngroup.dvd2c.service.impl.ContentServiceImpl;
import com.google.common.io.ByteStreams;
import com.google.common.io.CharSource;
import com.google.common.io.Files;
import com.google.gson.Gson;

public class ContentServiceImplTest {

	private static final class FileTypeResolverFacadeMock implements
			FileTypeResolverFacade {

		private FileTypeEnum type;

		private FileTypeResolverFacadeMock(FileTypeEnum type) {
			this.type = type;
		}

		public static FileTypeResolverFacadeMock withType(FileTypeEnum type) {
			return new FileTypeResolverFacadeMock(type);
		}

		@Override
		public FileTypeEnum detect(File tempFile) {
			return this.type;
		}
	}

	ContentServiceImpl cs;
	MessageBrokerService messBrocker;

	ThridPartyStorageService tps;
	AbstractFSFacade facade;
	DocumentRepository docRepo;
	DocumentAlternativeRepository altRepo;

	@Before
	public void setup() {
		cs = new ContentServiceImpl();
		messBrocker = mock(MessageBrokerService.class);
		tps = mock(ThridPartyStorageService.class);

		final String fsRoot = Files.createTempDir().toString();
		facade = new AbstractFSFacade() {

			@Override
			public String getOriginalName() {
				return "ORIGINAL";
			}

			@Override
			public String getFSRoot() {
				return fsRoot;
			}
		};

		docRepo = RepositoryMock.forRepository(DocumentRepository.class, "id",
				KeyGeneration.INT_SEQUENTIAL).mock();

		altRepo = RepositoryMock.forRepository(
				DocumentAlternativeRepository.class, "id",
				KeyGeneration.LONG_SEQUENTIAL).mock();

	}

	@BeforeClass
	public static void setUpCliConf() {
		CliConfSingleton.appHostName = "enseirb.fr";
		CliConfSingleton.appPort = 2606;
	}

	@Test
	public void testFileWrite() throws SecurityException, IOException,
			AlternativeStorageException {

		final String test = UUID.randomUUID().toString();

		cs.fsFacade = facade;
		cs.fileTypeResolverFacade = FileTypeResolverFacadeMock
				.withType(FileTypeEnum.CLOUD);
		cs.alternativeRepo = altRepo;
		cs.documentRepository = docRepo;
		ByteArrayInputStream bis = new ByteArrayInputStream(test.getBytes());
		Content content = cs.createContent("toto", bis);

		File target = Paths.get(this.facade.getFSRoot(), content.getLink(),
				this.facade.getOriginalName()).toFile();
		CharSource charSource = Files.asCharSource(target,
				Charset.defaultCharset());

		Assert.assertTrue(charSource.read().compareTo(test) == 0);
		Assert.assertTrue(content.getLink().contains("toto"));

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ByteStreams.copy(cs.getContentStream(content.getContentsID()), bos);
		assertEquals(test, bos.toString());

	}

	@Test
	public void testFileWriteWIthResolution() throws SecurityException,
			IOException, AlternativeStorageException {

		final String test = UUID.randomUUID().toString();
		final String test2 = UUID.randomUUID().toString();

		cs.fsFacade = facade;
		cs.fileTypeResolverFacade = FileTypeResolverFacadeMock
				.withType(FileTypeEnum.CLOUD);
		cs.alternativeRepo = altRepo;
		cs.documentRepository = docRepo;
		cs.tps = tps;
		ByteArrayInputStream bis = new ByteArrayInputStream(test.getBytes());

		Content content = cs.createContent("anonymous", bis);
		bis.close();
		bis = new ByteArrayInputStream(test2.getBytes());
		cs.createNewContentResolution("" + content.getContentsID(), "bof", bis);

		File target = Paths.get(this.facade.getFSRoot(), content.getLink(),
				this.facade.getOriginalName()).toFile();
		CharSource charSource = Files.asCharSource(target,
				Charset.defaultCharset());

		Assert.assertTrue(charSource.read().compareTo(test) == 0);
		Assert.assertTrue(content.getLink().contains("anonymous"));

		{
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ByteStreams.copy(cs.getContentStream(content.getContentsID()), bos);
			assertEquals(test, bos.toString());
		}

		{
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ByteStreams.copy(
					cs.getContentStream(content.getContentsID(), "bof"), bos);
			assertEquals(test2, bos.toString());
		}

	}

	@Test
	public void testFileWriteWIthResolutionThirdPArtyStorage()
			throws SecurityException, IOException, AlternativeStorageException {

		final String test = UUID.randomUUID().toString();
		final String test2 = UUID.randomUUID().toString();
		final String altUrl = "http://baseURL/api/storage";

		cs.fsFacade = facade;
		cs.fileTypeResolverFacade = FileTypeResolverFacadeMock
				.withType(FileTypeEnum.CLOUD);
		cs.alternativeRepo = altRepo;
		cs.documentRepository = docRepo;
		cs.tps = tps;

		ByteArrayInputStream bis = new ByteArrayInputStream(test.getBytes());

		Content content = cs.createContent("anonymous", bis);
		bis.close();
		bis = new ByteArrayInputStream(test2.getBytes());
		cs.createNewContentResolution("" + content.getContentsID(), "bof", bis);

		cs.updateContentWithUrl("" + content.getContentsID(), "bif", altUrl);

		File target = Paths.get(this.facade.getFSRoot(), content.getLink(),
				this.facade.getOriginalName()).toFile();
		CharSource charSource = Files.asCharSource(target,
				Charset.defaultCharset());

		Assert.assertTrue(charSource.read().compareTo(test) == 0);
		Assert.assertTrue(content.getLink().contains("anonymous"));

		try {
			cs.getContentStream(content.getContentsID(), "bif");
		} catch (AlternativeStorageException aes) {
			Assert.assertEquals(altUrl, aes.getUri().toString());
		}

	}

	@Test
	public void testFileWriteWithTask() throws SecurityException, IOException {

		// ///////////////// MOCKING /////////////////////////////

		// /////////////// CONFIGURING ///////////////////////////////

		final String test = UUID.randomUUID().toString();

		cs.fsFacade = facade;
		cs.brockerService = messBrocker;
		cs.fileTypeResolverFacade = FileTypeResolverFacadeMock
				.withType(FileTypeEnum.VIDEO);
		cs.documentRepository = docRepo;
		ByteArrayInputStream bis = new ByteArrayInputStream(test.getBytes());

		// /////////// RUNNING //////////////
		Content content = cs.createContent("toto", bis);

		// ///////////////////// ASSERTS ////////////////////////
		File target = Paths.get(facade.getFSRoot(), content.getLink(),
				facade.getOriginalName()).toFile();
		CharSource charSource = Files.asCharSource(target,
				Charset.defaultCharset());
		Assert.assertTrue(charSource.read().compareTo(test) == 0);

		ArgumentCaptor<String> argument = ArgumentCaptor.forClass(String.class);
		verify(messBrocker).addTask(argument.capture());

		Task task = new Gson().fromJson(argument.getValue(), Task.class);
		Assert.assertEquals(task.getTask(), "adaptation.commons.ddo");
		Assert.assertEquals(1, task.getArgs().size());
		Assert.assertEquals(CliConfSingleton.getBaseApiURI() + "/content/2",
				task.getArgs().get(0));

	}

	@Test
	public void testNullContent() throws SecurityException, IOException {
		try {
			cs.createContent(null, null);
			fail("should have thrown");
		} catch (IllegalArgumentException iae) {

		}
	}

	@Test
	public void testcreateNewContentResolutionBadDocNumber()
			throws AlternativeStorageException, IOException {

		// ////////////// MOCKING /////////////////////

		Document doc = new Document();

		docRepo.save(doc);

		// ///////////// CONFIGURING /////////////////

		// ///////////// WIRING //////////////////////
		cs.documentRepository = docRepo;
		// ///////////// VERIFYING ///////////////////
		try {
			cs.createNewContentResolution("dummy" + doc.getId() + 1, "", null);
			fail("should have thrown");
		} catch (IllegalArgumentException e) {
			// OK
		}

	}

	@Test
	public void testcreateNewContentResolutionGoodDocNumber()
			throws AlternativeStorageException, IOException {

		// ////////////// MOCKING /////////////////////

		final String test = UUID.randomUUID().toString();
		Document doc = new Document();
		doc.setFileLink("/test/" + UUID.randomUUID().toString());

		docRepo.save(doc);

		DocumentAlternativeRepository altRepo = RepositoryMock.forRepository(
				DocumentAlternativeRepository.class, "id",
				KeyGeneration.LONG_SEQUENTIAL).mock();

		// ///////////// CONFIGURING /////////////////

		cs.fsFacade = facade;
		cs.brockerService = messBrocker;
		cs.fileTypeResolverFacade = FileTypeResolverFacadeMock
				.withType(FileTypeEnum.VIDEO);
		cs.tps = tps;

		// ///////////// WIRING //////////////////////
		cs.documentRepository = docRepo;
		cs.alternativeRepo = altRepo;
		// ///////////// VERIFYING ///////////////////
		Content content = cs.createNewContentResolution("" + doc.getId(),
				"medium", new ByteArrayInputStream(test.getBytes()));
		File target = Paths.get(this.facade.getFSRoot(), content.getLink(),
				"medium").toFile();
		CharSource charSource = Files.asCharSource(target,
				Charset.defaultCharset());

		Assert.assertTrue(charSource.read().compareTo(test) == 0);

	}

	@Test
	public void testcreateNewContentResolutionGoodDocNumberThirdPartyStorage()
			throws AlternativeStorageException, IOException {

		// ////////////// MOCKING /////////////////////

		final String test = UUID.randomUUID().toString();
		Document doc = new Document();
		doc.setFileLink("/test/" + UUID.randomUUID().toString());

		docRepo.save(doc);

		URI redirectUri = UriBuilder.fromUri("https://toto.com/push_data")
				.build();
		when(tps.generateRedirectUri("" + doc.getId())).thenReturn(
				Arrays.asList(redirectUri));

		// ///////////// CONFIGURING /////////////////

		cs.fsFacade = null;
		cs.brockerService = messBrocker;
		cs.fileTypeResolverFacade = FileTypeResolverFacadeMock
				.withType(FileTypeEnum.VIDEO);
		cs.tps = tps;

		// ///////////// WIRING //////////////////////
		cs.documentRepository = docRepo;
		cs.alternativeRepo = altRepo;
		// ///////////// VERIFYING ///////////////////
		try {
			Content content = cs.createNewContentResolution("" + doc.getId(),
					"medium", new ByteArrayInputStream(test.getBytes()));
			fail("should have thrown");
		} catch (AlternativeStorageException ase) {
			Assert.assertEquals(redirectUri.toString() + "/medium", ase
					.getUri().toString());
		}

	}

	@Test
	public void getContentNull() throws NoContentException {
		try {
			cs.documentRepository = this.docRepo;
			cs.getContent(null);
			fail("should have thrown");
		} catch (IllegalArgumentException iae) {

		}
	}

	@Test
	public void getContentNull2() throws NoContentException {
		try {
			cs.documentRepository = this.docRepo;

			cs.getContent(1);
			fail("should have thrown");
		} catch (IllegalArgumentException iae) {

		}
	}

	@Test
	public void getContentNull3() throws NoContentException {

		cs.documentRepository = this.docRepo;
		Document doc = new Document();
		doc.setFileLink("toto");
		doc.setType("truc");
		this.docRepo.save(doc);

		cs.alternativeRepo = this.altRepo;
		Content content = cs.getContent(doc.getId());

		Assert.assertEquals("truc", content.getType());
		Assert.assertEquals("toto", content.getLink());
		Assert.assertEquals(0, content.getResolution().size());
	}

	@Test
	public void getContentNull4() throws NoContentException {

		DocumentAlternative alt = new DocumentAlternative();
		alt.setResolution("medium");
		alt.setUri("tata");

		cs.documentRepository = this.docRepo;
		cs.alternativeRepo = this.altRepo;

		Document doc = new Document();
		doc.setFileLink("toto");
		doc.setType("truc");

		this.docRepo.save(doc);
		alt.setDocument(doc);

		cs.alternativeRepo.save(alt);
		cs.documentRepository.save(doc);

		Content content = cs.getContent(doc.getId());

		Assert.assertEquals("truc", content.getType());
		Assert.assertEquals("toto", content.getLink());
		Assert.assertEquals(1, content.getResolution().size());

		Assert.assertEquals("tata", content.getResolution().get(0).getUri());
		Assert.assertEquals("medium", content.getResolution().get(0).getName());
	}

}
