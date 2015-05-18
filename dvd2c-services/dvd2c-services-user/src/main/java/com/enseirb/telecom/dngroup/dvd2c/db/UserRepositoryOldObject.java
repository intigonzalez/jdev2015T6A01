package com.enseirb.telecom.dngroup.dvd2c.db;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.enseirb.telecom.dngroup.dvd2c.model.User;
//import javax.persistence.Entity;

//@Entity
@Entity
// @NamedQuery(name = "User.findByTheUsersName", query =
// "from User u where u.userID = ?1")
public class UserRepositoryOldObject implements Serializable {

	@Id
	// @GeneratedValue(strategy = GenerationType.AUTO)
	// private Long id;
	@Column(unique = true)
	protected String userID;

	protected String boxID;

	protected String firstname;

	protected String surname;

	protected String password;

	protected String pubKey;

	protected String privateKey;
	/* SnapMail add-on */

	protected String smtpHost;

	protected String smtpPort;

	protected String smtpUsername;

	protected String smtpPassword;

	protected String smtpToken;

	// private List<Role> role;

	public UserRepositoryOldObject() {
	};

	public UserRepositoryOldObject(String userID, String boxID, String firstname,
			String surname, String password, String pubKey, String privateKey,
			String smtpHost, String smtpPort, String smtpUsername,
			String smtpPassword, String smtpToken/* , List<Role> role */) {
		super();
		this.boxID = boxID;
		this.userID = userID;
		this.firstname = firstname;
		this.surname = surname;
		this.password = password;
		this.pubKey = pubKey;
		this.privateKey = privateKey;
		// this.role = role;
		/* SnapMail add-on */
		this.smtpHost = smtpHost;
		this.smtpPort = smtpPort;
		this.smtpUsername = smtpUsername;
		this.smtpPassword = smtpPassword;
		this.smtpToken = smtpToken;
	}

	public UserRepositoryOldObject(User user) {
		this.userID = user.getUserID();
		this.boxID = user.getBoxID();
		this.firstname = user.getFirstname();
		this.surname = user.getSurname();
		this.password = user.getPassword();
		this.pubKey = user.getPubKey();
		this.privateKey = user.getPrivateKey();
		// this.role = user.getRole();
		/* SnapMail add-on */
		this.smtpHost = user.getSmtpHost();
		this.smtpPort = user.getSmtpPort();
		this.smtpUsername = user.getSmtpUsername();
		this.smtpPassword = user.getSmtpPassword();
		this.smtpToken = user.getSmtpToken();
	}

	//
	// /**
	// * @return the role
	// */
	// @OneToMany(targetEntity=Role.class, mappedBy="UserRepositoryObject",
	// fetch=FetchType.EAGER)
	// public List<Role> getRole() {
	// return role;
	// }
	//
	// /**
	// * @param role
	// * the role to set
	// */
	// public void setRole(List<Role> role) {
	// this.role = role;
	// }

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

	public String getUserID() {
		return userID;
	}

	public String getBoxID() {
		return boxID;
	}

	public void setBoxID(String boxID) {
		this.boxID = boxID;
	}

	/* SnapMail add-on */
	public String getSmtpHost() {
		return smtpHost;
	}

	public void setSmtpHost(String smtpHost) {
		this.smtpHost = smtpHost;
	}

	public String getSmtpPort() {
		return smtpPort;
	}

	public void setSmtpPort(String smtpPort) {
		this.smtpPort = smtpPort;
	}

	public String getSmtpUsername() {
		return smtpUsername;
	}

	public void setSmtpUsername(String smtpUsername) {
		this.smtpUsername = smtpUsername;
	}

	public String getSmtpPassword() {
		return smtpPassword;
	}

	public void setSmtpPassword(String smtpPassword) {
		this.smtpPassword = smtpPassword;
	}

	public String getSmtpToken() {
		return smtpToken;
	}

	public void setSmtpToken(String smtpToken) {
		this.smtpToken = smtpToken;
	}

	public User toUserRestrited() {
		User user = new User();
		user.setUserID(userID);
		user.setFirstname(firstname);
		user.setSurname(surname);
		user.setBoxID(boxID);
		return user;
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
		// user.getRole().addAll(role);
		/* SnapMail add-on */
		user.setSmtpHost(smtpHost);
		user.setSmtpPort(smtpPort);
		user.setSmtpUsername(smtpUsername);
		user.setSmtpPassword(smtpPassword);
		user.setSmtpToken(smtpToken);
		return user;
	}

}