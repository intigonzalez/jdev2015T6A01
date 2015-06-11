package com.enseirb.telecom.dngroup.snapmail.mail;

import com.enseirb.telecom.dngroup.snapmail.mail.impl.GoogleMailProperties;
import com.enseirb.telecom.dngroup.snapmail.mail.impl.GoogleMailer;
import com.enseirb.telecom.dngroup.snapmail.mail.impl.MicrosoftMailer;
import com.enseirb.telecom.dngroup.snapmail.mail.impl.MicrosoftMailProperties;
import com.enseirb.telecom.dngroup.snapmail.mail.impl.SMTPMailer;
import com.enseirb.telecom.dngroup.snapmail.mail.impl.SMTPProperties;

public abstract class MailerFactory {
	
	public static Mailer getMailer(MailerProperties mailerProperties){
		if(mailerProperties instanceof SMTPProperties)
			return new SMTPMailer((SMTPProperties) mailerProperties);

		else if(mailerProperties instanceof GoogleMailProperties)
			return new GoogleMailer((GoogleMailProperties) mailerProperties);

		else if(mailerProperties instanceof MicrosoftMailProperties)
			return new MicrosoftMailer((MicrosoftMailProperties) mailerProperties);
			
		else
			return null; // TODO : need to be changed
	}
}
