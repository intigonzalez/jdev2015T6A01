package com.enseirb.telecom.dngroup.snapmail.mail;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.enseirb.telecom.dngroup.dvd2c.model.User;
import com.enseirb.telecom.dngroup.snapmail.exception.NoSuchProperty;


public interface MediaHomeFacade {

	
/**
 * Take a bodypart from the mail and create a MH link from it
 * 
 * @param inputStream
 * @param filename
 * @param type
 * @param username
 * @param recipients
 * @return
 * @throws IOException
 */
	public abstract String getLinkFromBodyPart(InputStream inputStream,
			String filename, String type,User user,
			List<String> recipients) throws IOException;

/**
 * For a specific user, retrieve his smtp parameters
 * 
 * @param username
 * @return
 * @throws NoSuchProperty
 */
	public abstract MailerProperties getMailerPropertiesFromUser(User user) throws NoSuchProperty;

/**
 * Retrieve a user from Media@Home thanks to his username and his password.
 * 
 * @param username
 * @param password
 * @return User
 */
	public abstract User getUserORH(String username, String password);
	
}
