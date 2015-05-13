//package com.enseirb.telecom.dngroup.dvd2c;
//
//import javax.inject.Inject;
//
//import org.junit.Before;
//import org.junit.Test;
//
//import static org.junit.Assert.*;
//
//import com.enseirb.telecom.dngroup.dvd2c.exception.NoSuchUserException;
//import com.enseirb.telecom.dngroup.dvd2c.model.User;
//import com.enseirb.telecom.dngroup.dvd2c.service.AccountServiceImpl;
//
//import junit.framework.Assert;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration
//public class acountTest {
//
//	@Inject
//	AccountServiceImpl objectToTest;
//
//	@Test
//	public void testGetNoUser() {
//		try {
//			objectToTest.getUserOnLocal("noReal@user.fr");
//			assertFalse(true);
//		} catch (NoSuchUserException e) {
//			// ok
//		}
//
//	}
//
//	@Test
//	public void testGetRealUser() {
//		User user = new User();
//		user.setUserID("real@user.fr");
//		user.setPassword("password");
//		user.setBoxID("boxIdTest");
//		user.setFirstname("firstName");
//		user.setSurname("surname");
//
//		objectToTest.createUserOnLocal(user);
//		try {
//			objectToTest.getUserOnLocal("real@user.fr");
//
//		} catch (NoSuchUserException e) {
//			assertFalse(true);
//		}
//
//	}
//
////	@Before
////	public void setUp() {
////
////		objectToTest = new AccountServiceImpl();
////
////	}
//
//}
