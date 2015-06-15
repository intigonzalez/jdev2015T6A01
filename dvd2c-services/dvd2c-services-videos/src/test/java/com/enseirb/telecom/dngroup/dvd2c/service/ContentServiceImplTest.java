package com.enseirb.telecom.dngroup.dvd2c.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.UUID;

import javax.inject.Inject;
import javax.ws.rs.core.NoContentException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.enseirb.telecom.dngroup.dvd2c.model.Content;
import com.enseirb.telecom.dngroup.dvd2c.modeldb.Document;
import com.enseirb.telecom.dngroup.dvd2c.repository.DocumentRepository;

@Configuration
class ContentServiceImplTestImplTestConfig {

	@Bean
	public DocumentRepository documentRepository() {
		return mock(DocumentRepository.class);
	}

	@Bean
	public MessageBrokerService rabbitMq() {
		return mock(MessageBrokerService.class);
	}
	@Bean
	public ContentService ContentService() {
		return new ContentServiceImpl();
	}

	
}

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = { ContentServiceImplTestImplTestConfig.class }

)
public class ContentServiceImplTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Inject
	DocumentRepository documentRepository;
	@Inject
	MessageBrokerService rabbitMq;
	@Inject
	ContentServiceImpl toTest;

	private static final Integer DOCUMENT_ID = 1;
	private static final Integer DOCUMENT_FALSE_ID = -1;
	private static final UUID UserUUID = UUID.fromString("25c61003-bb9b-43ad-bd5c-d1054574e4fc");
			
	@Test
	public void testCheckNullContent() throws IOException {
		boolean b = toTest.contentExist(null);
		assertTrue(!b);
	}
	
	@Test
	public void testCheckNotFoundContent() throws IOException {
		boolean b = toTest.contentExist(DOCUMENT_FALSE_ID);
		assertTrue(!b);
	}
	
	@Test
	public void testCheckFoundContent() throws IOException {
		when(toTest.documentRepository.exists(DOCUMENT_ID)).thenReturn(true );
		boolean b = toTest.contentExist(DOCUMENT_ID);
		assertTrue(b);
	}

	@Test
	public void testGetNullContent() throws IOException {
		try {
			toTest.getContent(null);
			fail("NoContentException not thrown");
		} catch (NoContentException e) {
			assertNotNull(e.getMessage());
		}
	}
	
	@Test
	public void testGetNotFoundContent() throws IOException {
		try {
			toTest.getContent(DOCUMENT_FALSE_ID);
			fail("NoContentException not thrown");
		} catch (NoContentException e) {
			assertNotNull(e.getMessage());
		}
	}
	
	@Test
	public void testGetFoundContent() throws IOException {
		Document document = new Document();
		document.setId(DOCUMENT_ID);
		document.setActorId(UserUUID);
		when(toTest.documentRepository.findOne(DOCUMENT_ID)).thenReturn(document);
		Content content = toTest.getContent(DOCUMENT_ID);
		if (!(content.getContentsID() == DOCUMENT_ID)) {
			assertTrue(false);
		}
	}

}
