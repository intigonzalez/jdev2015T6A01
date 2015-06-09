package com.enseirb.telecom.dngroup.dvd2c.service;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.UUID;

import javax.inject.Inject;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

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

	

	@Test
	public void testNull() throws IOException {
		when(toTest.documentRepository.exists(null)).thenReturn(false );
		boolean b = toTest.contentExist(null);
		assertTrue(!b);

	}
	@Test
	public void testNotInteger() throws IOException {
		boolean b = toTest.contentExist(-1);
		assertTrue(!b);

	}
	@Test
	public void testFound() throws IOException {
		when(toTest.documentRepository.exists(2)).thenReturn(true );
		boolean b = toTest.contentExist(2);
		assertTrue(b);

	}

	
}
