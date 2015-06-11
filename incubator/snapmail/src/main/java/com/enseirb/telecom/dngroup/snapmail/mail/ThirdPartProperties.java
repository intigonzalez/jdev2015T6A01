package com.enseirb.telecom.dngroup.snapmail.mail;

public abstract class ThirdPartProperties implements MailerProperties {
	 
	private String tokenRefresh;
	 
	public String getTokenRefresh() {
		return tokenRefresh;
	}

	public void setTokenRefresh(String tokenRefresh) {
		this.tokenRefresh = tokenRefresh;
	}

	public ThirdPartProperties(String tokenRefresh) {
		super();
		this.tokenRefresh = tokenRefresh;
	}
}
