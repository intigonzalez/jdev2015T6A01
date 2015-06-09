package com.enseirb.telecom.dngroup.dvd2c.request;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.UUID;

import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.enseirb.telecom.dngroup.dvd2c.exception.NoSuchBoxException;
import com.enseirb.telecom.dngroup.dvd2c.exception.NoSuchUserException;
import com.enseirb.telecom.dngroup.dvd2c.model.Box;
import com.enseirb.telecom.dngroup.dvd2c.model.User;
import com.enseirb.telecom.dngroup.dvd2c.service.request.RequestUserService;

import static org.mockito.Mockito.*;

@Configuration
class RequestRelationServiceImplTestConfig {

	@Bean
	public Client client() {
		return mock(Client.class);
	}

	@Bean
	public RequestUserService requestUserService() {
		return mock(RequestUserService.class);
	}

	@Bean
	public RequestRelationService requestRelationService() {
		return new RequestRelationServiceImpl();
	}

}

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = { RequestRelationServiceImplTestConfig.class }

)
public class RequestRelationServiceImplTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Inject
	RequestRelationServiceImpl toTest;

	private static final UUID UserUUID = UUID
			.fromString("25c61003-bb9b-43ad-bd5c-d1054574e4fc");
	private static final UUID relationUUID = UUID
			.fromString("81999c55-db91-41a1-becf-cf2d313ad09b");

	@Test
	public void testNull() throws IOException {

		try {

			when(toTest.requestServ.getBoxByUserUuidORH(null)).thenThrow(
					new NoSuchUserException());
			toTest.get(null, null);
			assertTrue("should have thrown", false);
		} catch (NoSuchUserException e) {
			// good
		}

	}

	@Test
	public void testWithData() throws IOException, NoSuchUserException {

		User user1 = new User();
		user1.setFirstname("nicolas");

		bootstrapRequest("1.2.3.4", user1);

		assertEquals(toTest.get(UserUUID, relationUUID).getFirstname(),
				user1.getFirstname());

	}

	@Test
	public void testWithInvalidIP() throws IOException, NoSuchBoxException,
			NoSuchUserException {

		User user1 = new User();
		user1.setFirstname("nicolas");

		bootstrapRequest("329.125.125.125", user1);

		toTest.get(UserUUID, relationUUID).getFirstname();

	}

	private void bootstrapRequest(String ip, User u) throws IOException,
			NoSuchUserException {
		Box boxRelation = new Box();
		boxRelation.setIp(ip);

		when(toTest.requestServ.getBoxByUserUuidORH(relationUUID)).thenReturn(
				boxRelation);

		javax.ws.rs.client.Invocation.Builder builder = mock(javax.ws.rs.client.Invocation.Builder.class);
		when(builder.get(User.class)).thenReturn(u);

		WebTarget target = mock(WebTarget.class);
		when(target.request(MediaType.APPLICATION_XML_TYPE))
				.thenReturn(builder);

		when(
				toTest.client.target(UriBuilder.fromPath(boxRelation.getIp())
						.path("api").path("app").path(relationUUID.toString())
						.path("relation").path("from")
						.path(UserUUID.toString()).build())).thenReturn(target);

	}
}
