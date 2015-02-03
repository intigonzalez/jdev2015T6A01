package com.enseirb.telecom.dngroup.dvd2c.db;

import com.enseirb.telecom.dngroup.dvd2c.model.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRepositoryObject {

	protected String userID;
	protected String boxID;
	protected String name;
	protected String firstname;
	protected String password;
	protected String pubKey;

	public UserRepositoryObject() {

	}

	public UserRepositoryObject(String email, String boxID, String name,
			String firstname, String password, String pubKey) {
		super();
		this.userID = email;
		this.boxID = boxID;
		this.name = name;
		this.firstname = firstname;
		this.password = password;
		this.pubKey = pubKey;
	}

	public UserRepositoryObject(User user) {
		this.userID = user.getUserID();
		this.boxID = user.getBoxID();
		this.name = user.getName();
		this.firstname = user.getSurname();
		this.password = user.getPassword();
		this.pubKey = user.getPubKey();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
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

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getUserID() {
		return userID;
	}

	public String getBoxID() {
		return boxID;
	}

	public void setBoxID(String boxID) {
		this.boxID = boxID;
	}

	public User toUser() {
		User user = new User();
		user.setUserID(userID);
		user.setBoxID(boxID);
		user.setName(name);
		user.setSurname(firstname);
		user.setPassword(password);
		user.setPubKey(pubKey);
		return user;
	}

}
