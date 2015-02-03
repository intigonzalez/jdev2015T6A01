package com.enseirb.telecom.s9.db;

import com.enseirb.telecom.s9.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown=true)
public class UserRepositoryObject {
	protected String userID;
	protected String boxID;
	protected String name;
	protected String surname;
	protected String password;
	protected String pubKey;
	protected String privateKey;
	public UserRepositoryObject(){
		
	}
	
	public UserRepositoryObject(String userID,String boxID, String name, String surname, String password, String pubKey, String privateKey) {
		super();
		this.boxID=boxID;
		this.userID = userID;
		this.name = name;
		this.surname = surname;
		this.password = password;
		this.pubKey = pubKey;
		this.privateKey = privateKey;
	}
	
	public UserRepositoryObject(User user) {
		this.userID = user.getUserID();
		this.boxID=user.getBoxID();
		this.name = user.getName();
		this.surname = user.getSurname();
		this.password = user.getPassword();
		this.pubKey = user.getPubKey();
		this.privateKey = user.getPrivateKey();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
		this.userID = userID;
	}
	
	public User toUser() {
		User user = new User();
		user.setUserID(userID);
		user.setName(name);
		user.setSurname(surname);
		user.setPassword(password);
		user.setPrivateKey(privateKey);
		user.setPubKey(pubKey);
		return user;
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

}
