package com.enseirb.telecom.dngroup.snapmail.mail;

/**
 * 
 * @author nherbaut
 *
 */
public interface MediaHomeFacade {
	
	/**
	 * take a bodypart from the mail and create a MH link from it 
	 */
	void bodyPart2Link();
	
	/**
	 * for a specific user, retreive his smtp parameters
	 */
	void getSmtpParam();

}
