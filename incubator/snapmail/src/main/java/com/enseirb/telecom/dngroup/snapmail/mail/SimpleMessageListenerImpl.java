package com.enseirb.telecom.dngroup.snapmail.mail;

import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.subethamail.smtp.auth.UsernamePasswordValidator;
import org.subethamail.smtp.helper.*;

import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.inject.Inject;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import javax.mail.util.SharedFileInputStream;

import org.subethamail.smtp.MessageContext;
import org.subethamail.smtp.TooMuchDataException;

import com.enseirb.telecom.dngroup.dvd2c.model.User;
import com.enseirb.telecom.dngroup.snapmail.cli.CliConfSingleton;
import com.enseirb.telecom.dngroup.snapmail.exception.NoSuchProperty;
import com.enseirb.telecom.dngroup.snapmail.mail.MediaHomeFacade;
import com.philvarner.clamavj.ClamScan;
import com.philvarner.clamavj.ScanResult;

public class SimpleMessageListenerImpl implements SimpleMessageListener,
		UsernamePasswordValidator {
	private final static Logger LOGGER = LoggerFactory
			.getLogger(SimpleMessageListener.class);

	
	private MediaHomeFacade mediaHomeFacade = new MediaHomeFacadeImpl();

	//TODO : supprimer quand la sécurité sera réglée
	protected String username;
//	protected String password;
	//User user;

	// to display all recipients
	List<String> recipientArray = new ArrayList<String>();
	private String allrecipients;
	private int counter = 0;

	// to certify that recipients are real adresses
	private int position = 0;

	MessageContext context;
	private String subject;
	private String text;
	private String type;
	private InputStream data;

	// private final static Logger LOGGER =
	// Logger.getLogger(SimpleMessageListenerImpl.class.getName());

	private String Clamav_report = "\n----------- ClamAV -----------------\n----------- SCAN SUMMARY -----------\n";
	private boolean mail_infected = false;

	@Override
	public void login(String username, String password) {
//		user = new User();
//		user.setUserID(username);
//		user.setPassword(password);
		this.username=username;
		//user=mediaHomeFacade.getUserORH(username, password);
	}

	@Override
	public boolean accept(String from, String recipient) {
		position = recipient.indexOf("@");
		if (position != -1) {// If the address is correct mail address
			recipientArray.add("" + recipient);
			counter++;
		}
		return true;
	}

	// Receive and deliver the mail
	@Override
	public void deliver(String from, String recipient, InputStream data)
			throws TooMuchDataException, IOException {
		// Emptying the array
		counter--;
		if (counter == 0) {
			allrecipients = recipientArray.toString();
			this.allrecipients = this.allrecipients.substring(1,
					this.allrecipients.length() - 1);
			LOGGER.info("\n--------------------------------------------------");
			LOGGER.info("FROM: " + from + "\n TO: " + allrecipients);
			// data
			this.data = data;

			Properties props = new Properties();
			Session session = Session.getInstance(props, null);

			// Creation of a new MIME Message
			MimeMessage newMessage = null;
			Multipart multiPart = new MimeMultipart();

			try {
				// The mail received as a stream of data by the app is stored
				// into the MIME message
				File mimeFile = saveFile("temp_mime", data);

				SharedFileInputStream fis = new SharedFileInputStream(mimeFile);
				newMessage = new MimeMessage(session, fis);
				mimeFile.delete();

				this.subject = newMessage.getSubject();
				String contentType = newMessage.getContentType();
				this.type = contentType;
				Boolean isMultipart = contentType.contains("multipart");

				// Returns the text of the message
				this.text = getTextFromMessage(newMessage);

				LOGGER.info("Text : \n\t" + this.text);

				// If the message contains an attachment it is parsed to upload
				// the file on the cloud
				if (isMultipart) {
					LOGGER.info("This mail is multipart");
					parseMessage(newMessage, multiPart);

				}
			} catch (MessagingException e1) {

				e1.printStackTrace();

			} catch (IOException e) {

				e.printStackTrace();

			}

			try {
				sendMail(recipient, multiPart);
			} catch (MessagingException e) {
				e.printStackTrace();
			} catch (NoSuchProperty e) {

				e.printStackTrace();
			}
			recipientArray.clear();

			// If infected send an email to the sender
			Clamav_report += "------------------------------------\nClamAV Antivirus is an open source antivirus engine for detecting trojans, viruses,malware & other malicious threats\n------------------------------------";
			if (mail_infected) {
				try {
					sendClamavReport(from, Clamav_report);
				} catch (NoSuchProperty e) {
					e.printStackTrace();
				}
				mail_infected = false;
			}
			Clamav_report = "\n----------- ClamAV -----------------\n----------- SCAN SUMMARY -----------\n";
		}
	}

	/**
	 * Rebuild and send mails, using the SMTPProperties of the sender
	 * 
	 * @param host
	 * @param recipient
	 * @param multiPart
	 * @throws IOException
	 * @throws MessagingException
	 * @throws NoSuchProperty
	 */
	private void sendMail(String recipient, Multipart multiPart)
			throws IOException, MessagingException, NoSuchProperty {
		// Get system properties
//		MediaHomeFacade mediahome = new MediaHomeFacadeImpl(this.username,
//				this.password);
		MailerProperties prop = mediaHomeFacade.getMailerPropertiesFromUser(username);
		Session session = Session.getDefaultInstance(System.getProperties());
		try {
			// Creation of the message that will be send in place of the
			// received mail
			// The attachments are removed and replaced by links which redirect
			// to the file stored in the cloud
			//
			MimeMessage message = new MimeMessage(session);
			// Set From: header field of the header.
			message.setFrom(new InternetAddress(username));
			// message.setHeader("Content-Type", this.type);
			LOGGER.info("CONTENT-TYPE : " + this.type);
			// Set To: header field of the header.
			message.addRecipients(Message.RecipientType.TO, allrecipients);
			// Set Subject: header field
			message.setSubject(this.subject);
			// Now set the actual message
			// message.setContent(this.text, this.type);
			MimeBodyPart textPart = new MimeBodyPart();
			textPart.setContent(this.text, this.type);
			multiPart.addBodyPart(textPart);
			message.setContent(multiPart);
			LOGGER.info("Mail rebuilt and ready to be sent");

			MailerFactory.getMailer(prop).send(message);
			LOGGER.info("Mail sent successfully !");
			LOGGER.info("--------------------------------------------------\n\n");
		} catch (MessagingException mex) {
			mex.printStackTrace();
		}
	}

	/**
	 * Send a mail to the user when a file is infected
	 * 
	 * @param host
	 * @param from
	 * @param Clamav_report
	 * @throws IOException
	 * @throws NoSuchProperty
	 */
	private void sendClamavReport(String from, String Clamav_report)
			throws IOException, NoSuchProperty {
		MailerProperties prop = mediaHomeFacade.getMailerPropertiesFromUser(username);
		Session session = Session.getDefaultInstance(System.getProperties());
		MimeMessage message = new MimeMessage(session);
		try {
			message.setFrom(new InternetAddress("ClamAV@snapmail.com",
					"INFO Snapmail"));
			message.addRecipients(Message.RecipientType.TO, from);
			message.setSubject("ClamAV Scan Report");
			message.setContent(Clamav_report, "text/plain");
			MailerFactory.getMailer(prop).send(message);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		LOGGER.info("ClamAV Report sent successfully !\n\n");
	}

	/**
	 * Parse the message between the text and the attachment part, and scan it
	 * with clamAV
	 * 
	 * @param message
	 * @param output
	 * @throws MessagingException
	 * @throws IOException
	 */
	private void parseMessage(Message message, Multipart output)
			throws MessagingException, IOException {
		this.text += "\n------------------------------------\n";
		// Since the message is multipart, it can be casted as such
		Multipart multipart = (Multipart) message.getContent();

		// Iteration through the different parts of the message
		for (int j = 0; j < multipart.getCount(); j++) {
			BodyPart bodyPart = multipart.getBodyPart(j);
			// A signed email will put the attachments into a MIME multipart,
			// thus if a part is a MIME multipart it is likely to contain
			// attachments
			if (!Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition())) {
				// If the part is not an attachment, but a MimeMultipart...
				if (bodyPart.getContent().getClass()
						.equals(MimeMultipart.class)) {
					MimeMultipart mimemultipart = (MimeMultipart) bodyPart
							.getContent();
					// Iteration through the different parts of the MIME
					// multipart
					for (int k = 0; k < mimemultipart.getCount(); k++) {
						if (mimemultipart.getBodyPart(k).getContentType()
								.indexOf("text") == -1
								&& mimemultipart.getBodyPart(k)
										.getContentType().indexOf("multipart") == -1) {
							if (Part.INLINE != null
									| mimemultipart.getBodyPart(k).getHeader(
											"Content-ID")[0] != null) {// if
																		// attachment
																		// +
																		// embedded
																		// picture
																		// then
																		// the
																		// inline
																		// disposition
																		// is
																		// not
																		// writen
								LOGGER.info("embedded picture");
								try {
									// Scan viruses,trojans & malware for INLINE
									// Pictures in MimemMultipart
									LOGGER.info("-----------------------------------------------------------");
									LOGGER.info("Starting ClamAv Scan for file: "
											+ mimemultipart.getBodyPart(k)
													.getFileName());
									ClamScan clamScan = new ClamScan(
											CliConfSingleton.clamav_host,
											Integer.parseInt(CliConfSingleton.clamav_port),
											0);
									ScanResult result = clamScan
											.scan(mimemultipart.getBodyPart(k)
													.getInputStream());
									LOGGER.info("ClamAv Scan Done !");
									if ((result.getStatus().toString())
											.equals("FAILED")) {
										mail_infected = true;
										LOGGER.info("The file is infected !");
										LOGGER.info("-----------------------------------------------------------");
										Clamav_report += "---\nFile infected : "
												+ mimemultipart.getBodyPart(k)
														.getFileName()
												+ "\nSignature : "
												+ result.getSignature()
												+ "\nData Size : "
												+ mimemultipart.getBodyPart(k)
														.getSize()
												+ "\nDescription : "
												+ mimemultipart.getBodyPart(k)
														.getDescription()
												+ "\nContent-Type : "
												+ mimemultipart.getBodyPart(k)
														.getContentType()
												+ "\n---\n";
									} else {
										LOGGER.info("No virus found in the file !");
										LOGGER.info("-----------------------------------------------------------");
										// add picture inline hmtl part
										DataSource ds = new ByteArrayDataSource(
												mimemultipart.getBodyPart(k)
														.getInputStream(),
												mimemultipart.getBodyPart(k)
														.getContentType());
										MimeBodyPart imagePart = new MimeBodyPart();
										imagePart
												.setDataHandler(new DataHandler(
														ds));
										if (mimemultipart.getBodyPart(k)
												.getFileName() != null)
											imagePart.setFileName(mimemultipart
													.getBodyPart(k)
													.getFileName());
										if (mimemultipart.getBodyPart(k)
												.getFileName() != null)
											imagePart
													.setDisposition(MimeBodyPart.INLINE);
										imagePart
												.setHeader(
														"Content-ID",
														mimemultipart
																.getBodyPart(k)
																.getHeader(
																		"Content-ID")[0]);
										output.addBodyPart(imagePart);
										this.text += "\nThis email is free of viruses and malware because ClamAV Antivirus protection is enabled.";
									}
								} catch (IOException clam) {
									LOGGER.info("-----------------------------------------------------------");
									// add picture inline hmtl part
									DataSource ds = new ByteArrayDataSource(
											mimemultipart.getBodyPart(k)
													.getInputStream(),
											mimemultipart.getBodyPart(k)
													.getContentType());
									MimeBodyPart imagePart = new MimeBodyPart();
									imagePart
											.setDataHandler(new DataHandler(ds));
									if (mimemultipart.getBodyPart(k)
											.getFileName() != null)
										imagePart.setFileName(mimemultipart
												.getBodyPart(k).getFileName());
									if (mimemultipart.getBodyPart(k)
											.getFileName() != null)
										imagePart
												.setDisposition(MimeBodyPart.INLINE);
									imagePart
											.setHeader(
													"Content-ID",
													mimemultipart
															.getBodyPart(k)
															.getHeader(
																	"Content-ID")[0]);
									output.addBodyPart(imagePart);
									this.text += "\nClamAV is disabled. Your attachments haven't been checked by an antivirus.";
								}
							} else if (Part.ATTACHMENT != null) {
								try {
									// Scan viruses,trojans & malware for
									// ATTACHMENT in MimemMultipart
									LOGGER.info("embedded picture");
									// Scan viruses,trojans & malware for INLINE
									// PICTURES
									LOGGER.info("-----------------------------------------------------------");
									LOGGER.info("Starting ClamAv Scan for file: "
											+ mimemultipart.getBodyPart(k)
													.getFileName());
									ClamScan clamScan = new ClamScan(
											CliConfSingleton.clamav_host,
											Integer.parseInt(CliConfSingleton.clamav_port),
											0);
									ScanResult result = clamScan
											.scan(mimemultipart.getBodyPart(k)
													.getInputStream());
									LOGGER.info("ClamAv Scan Done !");
									if ((result.getStatus().toString())
											.equals("FAILED")) {
										mail_infected = true;
										LOGGER.info("The file is infected !");
										LOGGER.info("-----------------------------------------------------------");
										Clamav_report += "---\nFile infected : "
												+ mimemultipart.getBodyPart(k)
														.getFileName()
												+ "\nSignature : "
												+ result.getSignature()
												+ "\nData Size : "
												+ mimemultipart.getBodyPart(k)
														.getSize()
												+ "\nDescription : "
												+ mimemultipart.getBodyPart(k)
														.getDescription()
												+ "\nContent-Type : "
												+ mimemultipart.getBodyPart(k)
														.getContentType()
												+ "\n---\n";
									} else {
										LOGGER.info("No virus found in the file !");
										LOGGER.info("-----------------------------------------------------------");
										processAttachment(
												mimemultipart.getBodyPart(k)
														.getFileName(),
												mimemultipart.getBodyPart(k)
														.getInputStream(),
												mimemultipart
														.getBodyPart(k)
														.getContentType()
														.substring(
																0,
																bodyPart.getContentType()
																		.indexOf(
																				";")));
										this.text += "\nThis email is free of viruses and malware because ClamAV Antivirus protection is enabled.";
									}
								} catch (IOException clam) {
									LOGGER.info("-----------------------------------------------------------");
									processAttachment(
											mimemultipart.getBodyPart(k)
													.getFileName(),
											mimemultipart.getBodyPart(k)
													.getInputStream(),
											mimemultipart
													.getBodyPart(k)
													.getContentType()
													.substring(
															0,
															bodyPart.getContentType()
																	.indexOf(
																			";")));
									this.text += "\nClamAV is disabled. Your attachments haven't been checked by an antivirus.";
								}
							}
						}
						// if there is another mimemultipart
						if (mimemultipart.getBodyPart(k).getContentType()
								.indexOf("multipart") != -1) {
							MimeMultipart mimemultipart_bis = (MimeMultipart) mimemultipart
									.getBodyPart(k).getContent();
							// Iteration through the different parts of the MIME
							// multipart
							for (int l = 0; l < mimemultipart_bis.getCount(); l++) {
								if (mimemultipart_bis.getBodyPart(l)
										.getContentType().indexOf("text") == -1) {
									if (Part.INLINE != null
											| mimemultipart_bis.getBodyPart(l)
													.getHeader("Content-ID")[0] != null) {// if
																							// attachment
																							// +
																							// embedded
																							// picture
																							// then
																							// the
																							// inline
																							// disposition
																							// is
																							// not
																							// wrote
										LOGGER.info("embedded picture");
										try {
											// Scan viruses,trojans & malware
											// for INLINE PICTURES
											LOGGER.info("-----------------------------------------------------------");
											LOGGER.info("Starting ClamAv Scan for file: "
													+ mimemultipart_bis
															.getBodyPart(l)
															.getFileName());
											ClamScan clamScan = new ClamScan(
													CliConfSingleton.clamav_host,
													Integer.parseInt(CliConfSingleton.clamav_port),
													0);
											ScanResult result = clamScan
													.scan(mimemultipart_bis
															.getBodyPart(l)
															.getInputStream());
											LOGGER.info("ClamAv Scan Done !");
											if ((result.getStatus().toString())
													.equals("FAILED")) {
												mail_infected = true;
												LOGGER.info("The file is infected !");
												LOGGER.info("-----------------------------------------------------------");
												Clamav_report += "---\nFile infected : "
														+ mimemultipart
																.getBodyPart(l)
																.getFileName()
														+ "\nSignature : "
														+ result.getSignature()
														+ "\nData Size : "
														+ mimemultipart
																.getBodyPart(l)
																.getSize()
														+ "\nDescription : "
														+ mimemultipart
																.getBodyPart(l)
																.getDescription()
														+ "\nContent-Type : "
														+ mimemultipart
																.getBodyPart(l)
																.getContentType()
														+ "\n---\n";
											} else {
												LOGGER.info("No virus found in the file !");
												LOGGER.info("-----------------------------------------------------------");
												// add picture inline hmtl part
												DataSource ds = new ByteArrayDataSource(
														mimemultipart_bis
																.getBodyPart(l)
																.getInputStream(),
														mimemultipart_bis
																.getBodyPart(l)
																.getContentType());
												MimeBodyPart imagePart = new MimeBodyPart();
												imagePart
														.setDataHandler(new DataHandler(
																ds));
												if (mimemultipart_bis
														.getBodyPart(l)
														.getFileName() != null)
													imagePart
															.setFileName(mimemultipart_bis
																	.getBodyPart(
																			l)
																	.getFileName());
												if (mimemultipart_bis
														.getBodyPart(l)
														.getFileName() != null)
													imagePart
															.setDisposition(MimeBodyPart.INLINE);
												imagePart
														.setHeader(
																"Content-ID",
																mimemultipart_bis
																		.getBodyPart(
																				l)
																		.getHeader(
																				"Content-ID")[0]);
												output.addBodyPart(imagePart);
												this.text += "\nThis email is free of viruses and malware because ClamAV Antivirus protection is enabled.";
											}
										} catch (IOException clam) {

											LOGGER.info("-----------------------------------------------------------");
											// add picture inline hmtl part
											DataSource ds = new ByteArrayDataSource(
													mimemultipart_bis
															.getBodyPart(l)
															.getInputStream(),
													mimemultipart_bis
															.getBodyPart(l)
															.getContentType());
											MimeBodyPart imagePart = new MimeBodyPart();
											imagePart
													.setDataHandler(new DataHandler(
															ds));
											if (mimemultipart_bis
													.getBodyPart(l)
													.getFileName() != null)
												imagePart
														.setFileName(mimemultipart_bis
																.getBodyPart(l)
																.getFileName());
											if (mimemultipart_bis
													.getBodyPart(l)
													.getFileName() != null)
												imagePart
														.setDisposition(MimeBodyPart.INLINE);
											imagePart
													.setHeader(
															"Content-ID",
															mimemultipart_bis
																	.getBodyPart(
																			l)
																	.getHeader(
																			"Content-ID")[0]);
											output.addBodyPart(imagePart);
											this.text += "\nClamAV is disabled. Your attachments haven't been checked by an antivirus.";
										}
									} else if (Part.ATTACHMENT != null) {
										try {
											// Scan viruses,trojans & malware
											// for ATTACHMENTS IN MIMEMULTIPART
											LOGGER.info("-----------------------------------------------------------");
											LOGGER.info("Starting ClamAv Scan for file: "
													+ mimemultipart_bis
															.getBodyPart(l)
															.getFileName());
											ClamScan clamScan = new ClamScan(
													CliConfSingleton.clamav_host,
													Integer.parseInt(CliConfSingleton.clamav_port),
													0);
											ScanResult result = clamScan
													.scan(mimemultipart_bis
															.getBodyPart(l)
															.getInputStream());
											LOGGER.info("ClamAv Scan Done !");
											if ((result.getStatus().toString())
													.equals("FAILED")) {
												mail_infected = true;
												LOGGER.info("The file is infected !");
												LOGGER.info("-----------------------------------------------------------");
												Clamav_report += "---\nFile infected : "
														+ mimemultipart
																.getBodyPart(l)
																.getFileName()
														+ "\nSignature : "
														+ result.getSignature()
														+ "\nData Size : "
														+ mimemultipart
																.getBodyPart(l)
																.getSize()
														+ "\nDescription : "
														+ mimemultipart
																.getBodyPart(l)
																.getDescription()
														+ "\nContent-Type : "
														+ mimemultipart
																.getBodyPart(l)
																.getContentType()
														+ "\n---\n";
											} else {
												LOGGER.info("No virus found in the file !");
												LOGGER.info("-----------------------------------------------------------");
												processAttachment(
														mimemultipart_bis
																.getBodyPart(l)
																.getFileName(),
														mimemultipart_bis
																.getBodyPart(l)
																.getInputStream(),
														mimemultipart_bis
																.getBodyPart(l)
																.getContentType()
																.substring(
																		0,
																		bodyPart.getContentType()
																				.indexOf(
																						";")));
												this.text += "\nThis email is free of viruses and malware because ClamAV Antivirus protection is enabled.";
											}
										} catch (IOException clam) {
											LOGGER.info("ClamAV isn't working");
											LOGGER.info("-----------------------------------------------------------");
											processAttachment(
													mimemultipart_bis
															.getBodyPart(l)
															.getFileName(),
													mimemultipart_bis
															.getBodyPart(l)
															.getInputStream(),
													mimemultipart_bis
															.getBodyPart(l)
															.getContentType()
															.substring(
																	0,
																	bodyPart.getContentType()
																			.indexOf(
																					";")));
											this.text += "\nClamAV is disabled. Your attachments haven't been checked by an antivirus.";

										}
									}
								}
							}
						}
					}
				}
				continue;
			}

			try {
				// Scan viruses,trojans & malware for ATTACHMENTS
				LOGGER.info("-----------------------------------------------------------");
				LOGGER.info("Starting ClamAv Scan for file: "
						+ bodyPart.getFileName());
				ClamScan clamScan = new ClamScan(CliConfSingleton.clamav_host,
						Integer.parseInt(CliConfSingleton.clamav_port), 0);
				// other solution : Cast inputstream to a FileInputstream
				// other solution : Input Stream into Byte Array
				ScanResult result = clamScan.scan(bodyPart.getInputStream());
				LOGGER.info("ClamAv Scan Done !");

				if ((result.getStatus().toString()).equals("FAILED")) {
					mail_infected = true;
					LOGGER.info("The file is infected !");
					LOGGER.info("-----------------------------------------------------------");
					Clamav_report += "---\nFile infected : "
							+ bodyPart.getFileName() + "\nSignature : "
							+ result.getSignature() + "\nData Size : "
							+ bodyPart.getSize() + "\nDescription : "
							+ bodyPart.getDescription() + "\nContent-Type : "
							+ bodyPart.getContentType() + "\n---\n";
				} else {
					LOGGER.info("No virus found in the file !");
					LOGGER.info("-----------------------------------------------------------");
					// If the current part is explicitly an attachment...
					String contentType = bodyPart.getContentType().substring(0,
							bodyPart.getContentType().indexOf(";"));
					processAttachment(bodyPart.getFileName(),
							bodyPart.getInputStream(), contentType);
					LOGGER.info("Content type : " + contentType);
					this.text += "\nThis email is free of viruses and malware because ClamAV Antivirus protection is enabled.";
				}
			} catch (IOException clam) {
				LOGGER.info("ClamAV isn't working");
				LOGGER.info("-----------------------------------------------------------");
				String contentType = bodyPart.getContentType().substring(0,
						bodyPart.getContentType().indexOf(";"));
				processAttachment(bodyPart.getFileName(),
						bodyPart.getInputStream(), contentType);
				LOGGER.info("Content type : " + contentType);
				this.text += "\nClamAV is disabled. Your attachments haven't been checked by an antivirus.";

			}
		}
	}

	/**
	 * Modify the mail and upload the attachment
	 * 
	 * @param filename
	 * @param is
	 * @param Type
	 * @throws IOException
	 */
	// TODO Move it
	private void processAttachment(String filename, InputStream is, String Type)
			throws IOException {
		this.text += "Attachment : " + filename + "\n";
		
		String link = mediaHomeFacade.getLinkFromBodyPart(is, filename, Type,
				this.username, recipientArray);
		// String link = postFile(is, filename, Type);
		this.text += "Link :" + link + "\n";
	}

	/**
	 * Get the text part from the mail
	 * 
	 * @param message
	 * @return
	 * @throws MessagingException
	 * @throws IOException
	 */
	private String getTextFromMessage(Part message) throws MessagingException,
			IOException {
		String s = new String();
		this.type = message.getContentType();

		// If the type is text/html or text/plain, the text is extracted
		// directly
		if (message.isMimeType("text/*")) {
			s = (String) message.getContent();
			return s;
		}

		// Each part of a multipart/alternative type has the same content as the
		// others,
		// but in a different format
		if (message.isMimeType("multipart/alternative")) {
			Multipart mp = (Multipart) message.getContent();
			String text = null;

			for (int i = 0; i < mp.getCount(); i++) {
				Part bp = mp.getBodyPart(i);

				// If the type of this part is "text/plain" or "text/html",
				// the algorithm is used recursively on this specific part
				// in order to find the text inside it
				if (bp.isMimeType("text/plain")) {
					// The text stored in the part of type "text/plain"
					// is kept just in case nothing better is found
					// The priority is put on the type "text/html"
					if (text == null)
						text = getTextFromMessage(bp);
					continue;
				} else if (bp.isMimeType("text/html")) {
					s = getTextFromMessage(bp);
					if (s != null)
						return s;
				}
				// If the type is neither "text/html" nor "text/plain",
				// the algorithm is used recursively on this specific part
				else {
					return getTextFromMessage(bp);
				}
			}
			return text;
		} else if (message.isMimeType("multipart/*")) {
			Multipart mp = (Multipart) message.getContent();
			// Iteration through all the parts in order to find the one
			// containing the text
			for (int i = 0; i < mp.getCount(); i++) {
				s = getTextFromMessage(mp.getBodyPart(i));
				if (s != null)
					return s;
			}
		}

		return null;
	}

	/**
	 * Save the temporary file who contains the mail inputstream in /tmp
	 * 
	 * @param filename
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	private File saveFile(String filename, InputStream inputStream)
			throws IOException {
		// nh: please use Files.createTempFile(prefix, suffix, attrs)
		File f = new File("/tmp/" + filename);
		try {
			// Temporary until we find a better way to do it
			Files.copy(inputStream, f.toPath(),
					StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			LOGGER.error("Cannot create temporary file", e);
		}
		return f;
	}

	/**
	 * Upload the file into the user Media@Home account
	 * 
	 * @param is
	 * @param filename
	 * @param Type
	 * @return (String) link to see the document
	 * @throws IOException
	 */
	// replace by the new post in the interface
	/*
	 * public String postFile(InputStream is, String filename, String Type)
	 * throws IOException { // nh: to extract in a dedicated service class try {
	 * HttpAuthenticationFeature feature = HttpAuthenticationFeature
	 * .basic(this.username, this.password);
	 * 
	 * // Configuration of the client to allow a post with a large file // nh:
	 * please create only 1 client and close it properly ClientConfig cc = new
	 * ClientConfig(); cc.property(ClientProperties.REQUEST_ENTITY_PROCESSING,
	 * "CHUNKED"); cc.property(ClientProperties.CHUNKED_ENCODING_SIZE,
	 * Integer.valueOf(128));
	 * cc.property(ClientProperties.OUTBOUND_CONTENT_LENGTH_BUFFER,
	 * Integer.valueOf(128));
	 * 
	 * Client client = ClientBuilder.newClient(cc);
	 * client.register(feature).register(MultiPartFeature.class);
	 * 
	 * // nh: create WebTarget target =
	 * client.target(CliConfSingleton.mediahome_host + "/api/app/" +
	 * this.username + "/content");
	 * 
	 * LOGGER.info("Filename : " + filename); Response response = target
	 * .request() .header("Content-Disposition", "attachment; filename=" +
	 * filename) .post(Entity.entity(is, Type), Response.class);
	 * 
	 * if (response.getLocation() != null) return
	 * CliConfSingleton.mediahome_host + "/snapmail/" + "snapmail.html#/" +
	 * this.username + "/" +
	 * response.getLocation().toString().split("/content/")[1];
	 * 
	 * else {
	 * LOGGER.error("Error during the upload : Media@Home did not return a location"
	 * ); return "Error during the upload"; }
	 * 
	 * } catch (WebApplicationException e) { if (e.getResponse().getStatus() ==
	 * 403) { LOGGER.error("Error 403 (post content)"); } else { throw e; } }
	 * return "Error"; }
	 */
}
