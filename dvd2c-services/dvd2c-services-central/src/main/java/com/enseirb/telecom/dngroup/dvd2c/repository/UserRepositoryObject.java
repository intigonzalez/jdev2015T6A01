package com.enseirb.telecom.dngroup.dvd2c.repository;


import java.util.UUID;

import org.hibernate.annotations.Type;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.enseirb.telecom.dngroup.dvd2c.model.User;

@Document(collection="UserCentral")
public class UserRepositoryObject {
	
	@Id
	@Type(type = "uuid-char")
	protected UUID uuid;
	
	protected String userEmail;
	
	protected String boxID;
	
	protected String firstname;

	protected String surname;
	
	protected String password;

	protected String pubKey;

	protected String privateKey;
	
	
	

	
	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	protected BoxRepositoryObject box;

	public BoxRepositoryObject getBox() {
		return box;
	}

	public void setBox(BoxRepositoryObject box) {
		this.box = box;
	}

	public UserRepositoryObject() {

	}

	public UserRepositoryObject(String userID, String boxID, String firstname,
			String surname, String password, String pubKey, String privateKey,
			) {
		super();
		this.boxID = boxID;
		this.userEmail = userID;
		this.firstname = firstname;
		this.surname = surname;
		this.password = password;
		this.pubKey = pubKey;
		this.privateKey = privateKey;
	}

	public UserRepositoryObject(User user) {
		this.userEmail = user.getUserID();
		this.boxID = user.getBoxID();
		this.firstname = user.getFirstname();
		this.surname = user.getSurname();
		this.password = user.getPassword();
		this.pubKey = user.getPubKey();
		this.privateKey = user.getPrivateKey();
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPubKey() {
		return pubKey;
	}

	public void setPubKey(String pubKey) {
		this.pubKey = pubKey;
	}

	public String getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	public void setUserID(String userID) {
		this.userEmail = userID;
	}

	public User toUser() {
		User user = new User();
		user.setUuid(uuid.toString());
		user.setUserID(userEmail);
		user.setFirstname(firstname);
		user.setSurname(surname);
		user.setPassword(password);
		user.setPrivateKey(privateKey);
		user.setPubKey(pubKey);
		user.setBoxID(boxID);
		/* SnapMail add-on */
		user.setSmtpHost(smtpHost);
		user.setSmtpPort(smtpPort);
		user.setSmtpUsername(smtpUsername);
		user.setSmtpPassword(smtpPassword);
		user.setSmtpToken(smtpToken);
		return user;
	}

	public String getUserID() {
		return userEmail;
	}

	public String getBoxID() {
		return boxID;
	}

	public void setBoxID(String boxID) {
		this.boxID = boxID;
	}

	
}
