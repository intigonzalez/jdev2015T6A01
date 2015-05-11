package com.enseirb.telecom.dngroup.snapmail.mail;

import com.enseirb.telecom.dngroup.snapmail.mail.impl.GoogleMailProperties;
import com.enseirb.telecom.dngroup.snapmail.mail.impl.GoogleMailer;
import com.enseirb.telecom.dngroup.snapmail.mail.impl.MicrosoftMailer;
import com.enseirb.telecom.dngroup.snapmail.mail.impl.MicrosoftMailProperties;
import com.enseirb.telecom.dngroup.snapmail.mail.impl.SMTPMailer;
import com.enseirb.telecom.dngroup.snapmail.mail.impl.SMTPProperties;

public abstract class MailerFactory {
	
	public static Mailer getMailer(MailerProperties mailerProperties){
		if(mailerProperties.getClass().isInstance(SMTPProperties.class))
			return new SMTPMailer((SMTPProperties) mailerProperties);

		else if(mailerProperties.getClass().isInstance(GoogleMailProperties.class))
			return new GoogleMailer((GoogleMailProperties) mailerProperties);

		else if(mailerProperties.getClass().isInstance(MicrosoftMailProperties.class))
			return new MicrosoftMailer((MicrosoftMailProperties) mailerProperties);
			
		else
			return null; // TODO : need to be changed
	}
}
