package com.enseirb.telecom.dngroup.dvd2c.modeldb;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.*;

import com.enseirb.telecom.dngroup.dvd2c.model.Content;

/**
 * The persistent class for the documents database table.
 * 
 */
@Entity
@Table(name = "documents")
@NamedQuery(name = "Document.findAll", query = "SELECT d FROM Document d")
public class Document extends ActivityObject implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "file_content_type")
	private String fileContentType;

	@Column(name = "file_link")
	private String fileLink;

	@Column(name = "file_preview_link")
	private String filePreviewLink;

	@Column(name = "file_size")
	private String fileSize;

	@Column(name = "file_processing")
	private Integer fileProcessing;

	private String type;

	public Document() {
	}

	public String getFileContentType() {
		return this.fileContentType;
	}

	public void setFileContentType(String fileContentType) {
		this.fileContentType = fileContentType;
	}

	public String getFileLink() {
		return this.fileLink;
	}

	public void setFileLink(String fileLink) {
		this.fileLink = fileLink;
	}

	public String getFilePreviewLink() {
		return this.filePreviewLink;
	}

	public void setFilePreviewLink(String filePreviewLink) {
		this.filePreviewLink = filePreviewLink;
	}

	public String getFileSize() {
		return this.fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	public Integer getFileProcessing() {
		return this.fileProcessing;
	}

	public void setFileProcessing(int fileProcessing) {
		this.fileProcessing = fileProcessing;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Document(Content content) {
		if (content.getContentsID() != null)
			setId(Integer.valueOf(content.getContentsID()));
		setActorId(UUID.fromString(content.getActorID()));
		setTitle(content.getName());
		setType(content.getType());
		if (content.getUnixTime() != null)
			setCreatedAt(new java.util.Date(1000 * content.getUnixTime()));
		setFileLink(content.getLink());
		setFilePreviewLink(content.getPreviewLink());
		setFileProcessing(content.getStatus());
	}

	public Content toContent() {
		Content content = new Content();
		content.setContentsID(this.getId());
		content.setName(this.getTitle());
		content.setType(this.getType());
		content.setActorID(this.getActorId().toString());
		if (getCreatedAt() != null)
			content.setUnixTime(this.getCreatedAt().getTime() / 1000);
		content.setLink(this.getFileLink());
		content.setPreviewLink(this.getFilePreviewLink());
		content.setStatus(this.getFileProcessing());
		
		return content;
	}

}