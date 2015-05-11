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

public class MediaHomeFacadeTest {
	MediaHomeFacadeImpl objectToTest;
	@Test
	public void testNoPropertiesConfigured(){
		objectToTest.user=new User();
		PropertyGroups propgroup = new PropertyGroups();
		propgroup.setName("snapmail");
		objectToTest.user.getPropertyGroups().add(propgroup);
		try{
		MailerProperties properties = objectToTest.getSmtpParam();
		assertFalse(true);
		}catch(NoSuchProperty e){
			//success
		}
		
		
	}
	
	@Test
	public void testGoogleToken() throws NoSuchProperty{
		objectToTest.user=new User();
		PropertyGroups propgroup = new PropertyGroups();
		propgroup.setName("snapmail");
		Property p= new Property();
		p.setKey("google");
		p.setValue("123A3413232324234");
		propgroup.getProps().add(p);
		objectToTest.user.getPropertyGroups().add(propgroup);
		
		MailerProperties properties = objectToTest.getSmtpParam();
		assertTrue(properties instanceof GoogleMailProperties);
		
		
		
	}
	
	@Before
	public void setUp(){
		
		objectToTest=new MediaHomeFacadeImpl();
	}

}
