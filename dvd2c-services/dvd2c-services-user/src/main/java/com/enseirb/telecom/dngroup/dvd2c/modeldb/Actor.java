package com.enseirb.telecom.dngroup.dvd2c.modeldb;

import java.io.Serializable;

import javax.persistence.*;

import java.util.Date;
import java.util.List;

/**
 * The persistent class for the actors database table.
 * 
 */
@Entity
@Table(name = "actors")
@NamedQuery(name = "Actor.findAll", query = "SELECT a FROM Actor a")
@Inheritance(strategy=InheritanceType.JOINED)
public class Actor extends DBObject implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column(name = "activity_object_id")
	private int activityObjectId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_at")
	private Date createdAt;

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

	private String name;

	@Column(name = "notification_settings")
	private String notificationSettings;

	@Column(name = "notify_by_email")
	private byte notifyByEmail;

	@Column(name = "subject_type")
	private String subjectType;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_at")
	private Date updatedAt;

	@ManyToMany
	@JoinTable(name = "ACTOR_GROUPS", joinColumns = { @JoinColumn(name = "ACTOR_ID", referencedColumnName = "ID") }, inverseJoinColumns = { @JoinColumn(name = "GROUP_ID", referencedColumnName = "ID") })
	private List<Group> groups;

	@OneToOne(mappedBy = "actor")
	private Profile profile;

	public Actor() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getActivityObjectId() {
		return this.activityObjectId;
	}

	public void setActivityObjectId(int activityObjectId) {
		this.activityObjectId = activityObjectId;
	}

	public Date getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
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

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
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

	public Date getUpdatedAt() {
		return this.updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
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

}