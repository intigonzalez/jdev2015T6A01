package com.enseirb.telecom.dngroup.snapmail.mail;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * 
 *
 */
public interface MediaHomeFacade {
	
	/**
	 * take a bodypart from the mail and create a MH link from it 
	 */
	public abstract String bodyPart2Link(InputStream inputStream, String filename, String type,  String username, String password, List<String> recipients) throws IOException;
	
	/**
	 * for a specific user, retrieve his smtp parameters
	 */
	public abstract MailerProperties getSmtpParam(String username, String password);
}
