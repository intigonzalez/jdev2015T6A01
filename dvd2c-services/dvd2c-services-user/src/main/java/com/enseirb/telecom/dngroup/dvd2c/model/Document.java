package com.enseirb.telecom.dngroup.dvd2c.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the documents database table.
 * 
 */
@Entity
@Table(name="documents")
@NamedQuery(name="Document.findAll", query="SELECT d FROM Document d")
public class Document implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false)
	private int id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="created_at")
	private Date createdAt;

	@Column(name="file_content_type", length=255)
	private String fileContentType;

	@Column(name="file_file_name", length=255)
	private String fileFileName;

	@Column(name="file_file_size", length=255)
	private String fileFileSize;

	@Column(name="file_processing")
	private byte fileProcessing;

	@Column(length=255)
	private String type;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="updated_at")
	private Date updatedAt;

	//bi-directional many-to-one association to ActivityObject
	@ManyToOne
	@JoinColumn(name="activity_object_id")
	private ActivityObject activityObject;

	public Document() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getFileContentType() {
		return this.fileContentType;
	}

	public void setFileContentType(String fileContentType) {
		this.fileContentType = fileContentType;
	}

	public String getFileFileName() {
		return this.fileFileName;
	}

	public void setFileFileName(String fileFileName) {
		this.fileFileName = fileFileName;
	}

	public String getFileFileSize() {
		return this.fileFileSize;
	}

	public void setFileFileSize(String fileFileSize) {
		this.fileFileSize = fileFileSize;
	}

	public byte getFileProcessing() {
		return this.fileProcessing;
	}

	public void setFileProcessing(byte fileProcessing) {
		this.fileProcessing = fileProcessing;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getUpdatedAt() {
		return this.updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public ActivityObject getActivityObject() {
		return this.activityObject;
	}

	public void setActivityObject(ActivityObject activityObject) {
		this.activityObject = activityObject;
	}

}