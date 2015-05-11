package com.enseirb.telecom.dngroup.snapmail.mail.impl;

import com.enseirb.telecom.dngroup.snapmail.mail.Mailer;

public class MicrosoftMailer implements Mailer {
	
	private MicrosorfMailProperties microsorfMailProperties;
	
	public void send(){
		microsorfMailProperties.getTokenRefresh();
		// TODO...
	}

	public MicrosoftMailer(MicrosorfMailProperties microsorfMailProperties) {
		super();
		this.microsorfMailProperties = microsorfMailProperties;
	}
}
