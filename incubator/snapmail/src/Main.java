import org.subethamail.smtp.server.SMTPServer;
import org.subethamail.smtp.auth.*;
import org.subethamail.smtp.helper.*;

public class Main {
    public static void main(String[] args) {
        SimpleMessageListenerImpl mylistener = new SimpleMessageListenerImpl();
        SMTPServer smtpServer = new SMTPServer(new SimpleMessageListenerAdapter(mylistener));
        smtpServer.setAuthenticationHandlerFactory(new EasyAuthenticationHandlerFactory(mylistener));
        smtpServer.setPort(25004);
        smtpServer.start();
    }
}