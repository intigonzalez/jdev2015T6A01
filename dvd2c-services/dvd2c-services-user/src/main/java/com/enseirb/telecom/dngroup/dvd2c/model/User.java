package com.enseirb.telecom.dngroup.dvd2c.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the users database table.
 * 
 */
@Entity
@Table(name="users")
@NamedQuery(name="User.findAll", query="SELECT u FROM User u")
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false)
	private int id;

	@Column(name="authentication_token", length=255)
	private String authenticationToken;

	@Column(name="chat_enabled")
	private byte chatEnabled;

	private byte connected;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="created_at", nullable=false)
	private Date createdAt;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="current_sign_in_at")
	private Date currentSignInAt;

	@Column(name="current_sign_in_ip", length=255)
	private String currentSignInIp;

	@Column(name="encrypted_password", nullable=false, length=128)
	private String encryptedPassword;

	@Column(length=255)
	private String language;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="last_sign_in_at")
	private Date lastSignInAt;

	@Column(name="last_sign_in_ip", length=255)
	private String lastSignInIp;

	@Column(name="password_salt", length=255)
	private String passwordSalt;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="remember_created_at")
	private Date rememberCreatedAt;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="reset_password_sent_at")
	private Date resetPasswordSentAt;

	@Column(name="reset_password_token", length=255)
	private String resetPasswordToken;

	@Column(name="sign_in_count")
	private int signInCount;

	@Column(length=255)
	private String status;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="updated_at", nullable=false)
	private Date updatedAt;

	//bi-directional many-to-one association to Authentication
	@OneToMany(mappedBy="user")
	private List<Authentication> authentications;

	//bi-directional many-to-one association to Device
	@OneToMany(mappedBy="user")
	private List<Device> devices;

	//bi-directional many-to-one association to Oauth2Token
	@OneToMany(mappedBy="user")
	private List<Oauth2Token> oauth2Tokens;

	//bi-directional many-to-one association to Actor
	@ManyToOne
	@JoinColumn(name="actor_id")
	private Actor actor;

	public User() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAuthenticationToken() {
		return this.authenticationToken;
	}

	public void setAuthenticationToken(String authenticationToken) {
		this.authenticationToken = authenticationToken;
	}

	public byte getChatEnabled() {
		return this.chatEnabled;
	}

	public void setChatEnabled(byte chatEnabled) {
		this.chatEnabled = chatEnabled;
	}

	public byte getConnected() {
		return this.connected;
	}

	public void setConnected(byte connected) {
		this.connected = connected;
	}

	public Date getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getCurrentSignInAt() {
		return this.currentSignInAt;
	}

	public void setCurrentSignInAt(Date currentSignInAt) {
		this.currentSignInAt = currentSignInAt;
	}

	public String getCurrentSignInIp() {
		return this.currentSignInIp;
	}

	public void setCurrentSignInIp(String currentSignInIp) {
		this.currentSignInIp = currentSignInIp;
	}

	public String getEncryptedPassword() {
		return this.encryptedPassword;
	}

	public void setEncryptedPassword(String encryptedPassword) {
		this.encryptedPassword = encryptedPassword;
	}

	public String getLanguage() {
		return this.language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public Date getLastSignInAt() {
		return this.lastSignInAt;
	}

	public void setLastSignInAt(Date lastSignInAt) {
		this.lastSignInAt = lastSignInAt;
	}

	public String getLastSignInIp() {
		return this.lastSignInIp;
	}

	public void setLastSignInIp(String lastSignInIp) {
		this.lastSignInIp = lastSignInIp;
	}

	public String getPasswordSalt() {
		return this.passwordSalt;
	}

	public void setPasswordSalt(String passwordSalt) {
		this.passwordSalt = passwordSalt;
	}

	public Date getRememberCreatedAt() {
		return this.rememberCreatedAt;
	}

	public void setRememberCreatedAt(Date rememberCreatedAt) {
		this.rememberCreatedAt = rememberCreatedAt;
	}

	public Date getResetPasswordSentAt() {
		return this.resetPasswordSentAt;
	}

	public void setResetPasswordSentAt(Date resetPasswordSentAt) {
		this.resetPasswordSentAt = resetPasswordSentAt;
	}

	public String getResetPasswordToken() {
		return this.resetPasswordToken;
	}

	public void setResetPasswordToken(String resetPasswordToken) {
		this.resetPasswordToken = resetPasswordToken;
	}

	public int getSignInCount() {
		return this.signInCount;
	}

	public void setSignInCount(int signInCount) {
		this.signInCount = signInCount;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getUpdatedAt() {
		return this.updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public List<Authentication> getAuthentications() {
		return this.authentications;
	}

	public void setAuthentications(List<Authentication> authentications) {
		this.authentications = authentications;
	}

	public Authentication addAuthentication(Authentication authentication) {
		getAuthentications().add(authentication);
		authentication.setUser(this);

		return authentication;
	}

	public Authentication removeAuthentication(Authentication authentication) {
		getAuthentications().remove(authentication);
		authentication.setUser(null);

		return authentication;
	}

	public List<Device> getDevices() {
		return this.devices;
	}

	public void setDevices(List<Device> devices) {
		this.devices = devices;
	}

	public Device addDevice(Device device) {
		getDevices().add(device);
		device.setUser(this);

		return device;
	}

	public Device removeDevice(Device device) {
		getDevices().remove(device);
		device.setUser(null);

		return device;
	}

	public List<Oauth2Token> getOauth2Tokens() {
		return this.oauth2Tokens;
	}

	public void setOauth2Tokens(List<Oauth2Token> oauth2Tokens) {
		this.oauth2Tokens = oauth2Tokens;
	}

	public Oauth2Token addOauth2Token(Oauth2Token oauth2Token) {
		getOauth2Tokens().add(oauth2Token);
		oauth2Token.setUser(this);

		return oauth2Token;
	}

	public Oauth2Token removeOauth2Token(Oauth2Token oauth2Token) {
		getOauth2Tokens().remove(oauth2Token);
		oauth2Token.setUser(null);

		return oauth2Token;
	}

	public Actor getActor() {
		return this.actor;
	}

	public void setActor(Actor actor) {
		this.actor = actor;
	}

}