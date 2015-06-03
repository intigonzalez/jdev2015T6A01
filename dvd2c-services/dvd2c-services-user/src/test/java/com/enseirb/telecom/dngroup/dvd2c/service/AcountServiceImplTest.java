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

import com.enseirb.telecom.dngroup.dvd2c.exception.NoSuchUserException;
import com.enseirb.telecom.dngroup.dvd2c.modeldb.User;
import com.enseirb.telecom.dngroup.dvd2c.repository.UserRepository;
import com.enseirb.telecom.dngroup.dvd2c.service.request.RequestUserService;

@Configuration
class AcountServiceImplTestImplTestConfig {

	@Bean
	public UserRepository userRepository() {
		return mock(UserRepository.class);
	}

	@Bean
	public RequestUserService requetUserService() {
		return mock(RequestUserService.class);
	}

	@Bean
	public AccountService ContentService() {
		return new AccountServiceImpl();
	}

}

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = { AcountServiceImplTestImplTestConfig.class }

)
public class AcountServiceImplTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Inject
	protected UserRepository userRepository;

	@Inject
	protected RequestUserService requetUserService;
	@Inject
	AccountServiceImpl toTest;

	private static final UUID UserUUID = UUID
			.fromString("25c61003-bb9b-43ad-bd5c-d1054574e4fc");
	private static final UUID UserServerUUID = UUID
			.fromString("23c61003-bb9b-43ad-bd5c-d1054574e4fc");
	private static final UUID falseUUID = UUID
			.fromString("81999c55-db91-41a1-becf-cf2d313ad09b");

	@Test
	public void testGetNull() throws IOException {
		boolean b = toTest.userExistOnLocal(null);
		assertTrue(!b);
	}

	@Test
	public void testGetNoUser() throws IOException {
		boolean b = toTest.userExistOnLocal(falseUUID);
		assertTrue(!b);
	}

	@Test
	public void testGetUser() throws IOException {
		User user = new User();
		user.setId(UserUUID);
		user.setEmail("email@email.fr");
		user.setSurname("surname");
		when(toTest.userRepository.findOne(UserUUID)).thenReturn(user);
		boolean b = toTest.userExistOnLocal(UserUUID);
		assertTrue(b);
	}

	@Test
	public void testGetNullOnServer() throws IOException {
		boolean b = toTest.userExistOnServer(null);
		assertTrue(!b);
	}

	@Test
	public void testGetNoUserOnServer() throws IOException, NoSuchUserException {
		when(toTest.requetUserService.get(falseUUID)).thenThrow(
				new NoSuchUserException());
		boolean b = toTest.userExistOnServer(falseUUID);

		assertTrue(!b);
	}

	@Test
	public void testGetUserLocalOnServer() throws IOException {
		User user = new User();
		user.setId(UserUUID);
		user.setEmail("email@email.fr");
		user.setSurname("surname");
		when(toTest.userRepository.findOne(UserUUID)).thenReturn(user);
		boolean b = toTest.userExistOnServer(UserUUID);
		assertTrue(b);
	}

	@Test
	public void testGetUserOnServer() throws IOException, NoSuchUserException {
		com.enseirb.telecom.dngroup.dvd2c.model.User userSer = new com.enseirb.telecom.dngroup.dvd2c.model.User();
		userSer.setUuid(UserServerUUID.toString());
		userSer.setUserID("email2@email.fr");
		userSer.setSurname("surname2");
		when(toTest.requetUserService.get(UserServerUUID)).thenReturn(userSer);
		boolean b = toTest.userExistOnServer(UserServerUUID);
		assertTrue(b);
	}

	@Test
	public void testGetContactInformation() throws NoSuchUserException {
		User user = new User();
		user.setId(UserUUID);
		user.setEmail("email@email.fr");
		user.setSurname("surname");
		when(toTest.userRepository.findOne(UserUUID)).thenReturn(user);
		User u = toTest.getContactInformation(UserUUID);
		if (!u.getId().equals(UserUUID)){
			assertTrue(false);
		}
	}

}
