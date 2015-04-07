package com.enseirb.telecom.dngroup.snapmail;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import javax.net.ssl.SSLContext;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.subethamail.smtp.MessageHandlerFactory;

import org.subethamail.smtp.server.SMTPServer;

public class SmtpServer extends SMTPServer{

  private final static Logger log = LoggerFactory.getLogger(SMTPServer.class);
  private SSLContext context;

  protected SmtpServer(MessageHandlerFactory factory, SSLContext context) {
    super(factory);
    this.context = context;
  }

  /**
  * Create a SSL socket that wraps the existing socket. This method
  * is called after the client issued the STARTTLS command.
  */
  @Override
  public SSLSocket createSSLSocket(Socket socket) throws IOException
  {
    InetSocketAddress remoteAddress = (InetSocketAddress) socket.getRemoteSocketAddress();
    SSLSocketFactory sf = context.getSocketFactory();
    SSLSocket s = (SSLSocket) sf.createSocket(socket, remoteAddress.getHostName(), socket.getPort(), true);
    // we are a server
    s.setUseClientMode(false);
    // allow all supported cipher suites
    s.setEnabledCipherSuites(s.getSupportedCipherSuites());
    //using StrongTLS
    // select strong protocols and cipher suites
    s.setEnabledProtocols(StrongTls.intersection(
    s.getSupportedProtocols(), StrongTls.ENABLED_PROTOCOLS));
    s.setEnabledCipherSuites(StrongTls.intersection(
    s.getSupportedCipherSuites(), StrongTls.ENABLED_CIPHER_SUITES));
    log.info("SSL socket created");
    return s;
  }
}