package com.enseirb.telecom.dngroup.snapmail.mail.impl;

import com.enseirb.telecom.dngroup.snapmail.mail.Mailer;

public class SMTPMailer implements Mailer{
	
	private SMTPProperties smtpProperties;
	
	public void send(){
		smtpProperties.getHost();
		smtpProperties.getPort();
		smtpProperties.getUsername();
		smtpProperties.getPassword();
		// TODO...
	}

	public SMTPMailer(SMTPProperties smtpProperties) {
		super();
		this.smtpProperties = smtpProperties;
	}
}
