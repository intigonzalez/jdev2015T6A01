package com.enseirb.telecom.dngroup.snapmail.mail.impl;

import com.enseirb.telecom.dngroup.snapmail.mail.Mailer;

public class GoogleMailer implements Mailer{
	
	private GoogleMailProperties googleMailProperties;
	
	public void send(){
		googleMailProperties.getTokenRefresh();
		// TODO...
	}

	public GoogleMailer(GoogleMailProperties googleMailProperties) {
		super();
		this.googleMailProperties = googleMailProperties;
	}
}
