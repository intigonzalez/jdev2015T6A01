package com.enseirb.telecom.dngroup.snapmail.mail;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import com.enseirb.telecom.dngroup.dvd2c.model.Property;
import com.enseirb.telecom.dngroup.dvd2c.model.PropertyGroups;
import com.enseirb.telecom.dngroup.dvd2c.model.User;
import com.enseirb.telecom.dngroup.snapmail.exception.NoSuchProperty;
import com.enseirb.telecom.dngroup.snapmail.mail.impl.GoogleMailProperties;
import com.enseirb.telecom.dngroup.snapmail.mail.impl.MicrosoftMailProperties;

public class MediaHomeFacadeTest {
	MediaHomeFacadeImpl objectToTest;

	@Test
	public void testNoPropertiesConfigured() {
		User user = new User();
		PropertyGroups propgroup = new PropertyGroups();
		propgroup.setName("snapmail");
		user.getPropertyGroups().add(propgroup);
		try {
			MailerProperties properties = objectToTest.getSmtpParamORH(user);
			assertFalse(true);
		} catch (NoSuchProperty e) {
			// success
		}

	}

	@Test
	public void testGoogleToken() throws NoSuchProperty {
		User user = new User();
		PropertyGroups propgroup = new PropertyGroups();
		propgroup.setName("snapmail");
		Property p = new Property();
		p.setKey("google");
		p.setValue("123A3413232324234");
		propgroup.getProperty().add(p);
		user.getPropertyGroups().add(propgroup);

		MailerProperties properties = objectToTest.getSmtpParamORH(user);
		assertTrue(properties instanceof GoogleMailProperties);

	}

	@Test
	public void testMicrosoftToken() throws NoSuchProperty {
		User user = new User();
		PropertyGroups propgroup = new PropertyGroups();
		propgroup.setName("snapmail");
		Property p = new Property();
		p.setKey("microsoft");
		p.setValue("123A3413232324234");
		propgroup.getProperty().add(p);
	    user.getPropertyGroups().add(propgroup);

		MailerProperties properties = objectToTest.getSmtpParamORH(user);
		assertTrue(properties instanceof MicrosoftMailProperties);

	}

	@Before
	public void setUp() {

		objectToTest = new MediaHomeFacadeImpl();
	}

}
