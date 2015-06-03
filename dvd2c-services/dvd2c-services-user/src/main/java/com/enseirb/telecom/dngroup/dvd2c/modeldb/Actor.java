package com.enseirb.telecom.dngroup.dvd2c.modeldb;

import java.io.Serializable;

import javax.persistence.*;

import org.hibernate.annotations.Type;

import com.enseirb.telecom.dngroup.dvd2c.model.User;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * The persistent class for the actors database table.
 * 
 */
@Entity
@Table(name = "actors")
@NamedQuery(name = "Actor.findAll", query = "SELECT a FROM Actor a")
@Inheritance(strategy = InheritanceType.JOINED)
public class Actor extends DBObject implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	// @GeneratedValue(strategy = GenerationType.AUTO)
	@Type(type = "uuid-char")
	private UUID id;

	@Column(name = "activity_object_id")
	private int activityObjectId;

	private String email;

	@Column(name = "logo_content_type")
	private String logoContentType;

	@Column(name = "logo_file_name")
	private String logoFileName;

	@Column(name = "logo_file_size")
	private int logoFileSize;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "logo_updated_at")
	private Date logoUpdatedAt;

	private String firstname;

	private String surname;

	@Column(name = "notification_settings")
	private String notificationSettings;

	@Column(name = "notify_by_email")
	private byte notifyByEmail;

	@Column(name = "subject_type")
	private String subjectType;

	@ManyToMany
	@JoinTable(name = "ACTOR_GROUPS", joinColumns = { @JoinColumn(name = "ACTOR_ID", referencedColumnName = "ID") }, inverseJoinColumns = { @JoinColumn(name = "GROUP_ID", referencedColumnName = "ID") })
	private List<Group> groups;

	@OneToOne(mappedBy = "actor")
	private Profile profile;

	public Actor() {
	}

	public Actor(User user) {
		
		
			this.firstname = user.getFirstname();
			this.surname = user.getSurname();
			this.email = user.getUserID();
			this.id = UUID.fromString(user.getUuid());
		
	}

	public UUID getId() {
		return this.id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public int getActivityObjectId() {
		return this.activityObjectId;
	}

	public void setActivityObjectId(int activityObjectId) {
		this.activityObjectId = activityObjectId;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLogoContentType() {
		return this.logoContentType;
	}

	public void setLogoContentType(String logoContentType) {
		this.logoContentType = logoContentType;
	}

	public String getLogoFileName() {
		return this.logoFileName;
	}

	public void setLogoFileName(String logoFileName) {
		this.logoFileName = logoFileName;
	}

	public int getLogoFileSize() {
		return this.logoFileSize;
	}

	public void setLogoFileSize(int logoFileSize) {
		this.logoFileSize = logoFileSize;
	}

	public Date getLogoUpdatedAt() {
		return this.logoUpdatedAt;
	}

	public void setLogoUpdatedAt(Date logoUpdatedAt) {
		this.logoUpdatedAt = logoUpdatedAt;
	}

	public String getFirstname() {
		return this.firstname;
	}

	public void setFirstname(String name) {
		this.firstname = name;
	}

	public String getNotificationSettings() {
		return this.notificationSettings;
	}

	public void setNotificationSettings(String notificationSettings) {
		this.notificationSettings = notificationSettings;
	}

	public byte getNotifyByEmail() {
		return this.notifyByEmail;
	}

	public void setNotifyByEmail(byte notifyByEmail) {
		this.notifyByEmail = notifyByEmail;
	}

	public String getSubjectType() {
		return this.subjectType;
	}

	public void setSubjectType(String subjectType) {
		this.subjectType = subjectType;
	}

	public List<Group> getGroups() {
		return this.groups;
	}

	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}

	public Profile getProfile() {
		return this.profile;
	}

	public void setProfiles(Profile profile) {
		this.profile = profile;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

}