package com.enseirb.telecom.dngroup.dvd2c.db;


import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.enseirb.telecom.dngroup.dvd2c.model.PropertyGroups;
import com.enseirb.telecom.dngroup.dvd2c.model.User;

@Document(collection="UserRepositoryObject")
public class UserRepositoryObject {
	
	@Id
	protected String userID;
	
	protected String boxID;
	
	protected String firstname;

	protected String surname;
	
	protected String password;

	protected String pubKey;

	protected String privateKey;
	
	protected List<PropertyGroups> propertyGroups;

	
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
			List<PropertyGroups> propertyGroups) {
		super();
		this.boxID = boxID;
		this.userID = userID;
		this.firstname = firstname;
		this.surname = surname;
		this.password = password;
		this.pubKey = pubKey;
		this.privateKey = privateKey;
		this.propertyGroups = propertyGroups;
	}

	public UserRepositoryObject(User user) {
		this.userID = user.getUserID();
		this.boxID = user.getBoxID();
		this.firstname = user.getFirstname();
		this.surname = user.getSurname();
		this.password = user.getPassword();
		this.pubKey = user.getPubKey();
		this.privateKey = user.getPrivateKey();
		this.propertyGroups = user.getPropertyGroups();
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
		this.userID = userID;
	}

	public User toUser() {
		User user = new User();
		user.setUserID(userID);
		user.setFirstname(firstname);
		user.setSurname(surname);
		user.setPassword(password);
		user.setPrivateKey(privateKey);
		user.setPubKey(pubKey);
		user.setBoxID(boxID);
		user.getPropertyGroups().addAll(propertyGroups);
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

	public List<PropertyGroups> getPropertyGroups() {
		return propertyGroups;
	}

	public void setPropertyGroups(List<PropertyGroups> propertyGroups) {
		this.propertyGroups = propertyGroups;
	}
}
