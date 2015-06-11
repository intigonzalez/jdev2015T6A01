package com.enseirb.telecom.dngroup.snapmail.mail;

import javax.mail.internet.MimeMessage;

public interface Mailer {
	
void send(MimeMessage message);
}
