package com.enseirb.telecom.dngroup.dvd2c.modeldb;

import java.io.Serializable;

import javax.persistence.*;


/**
 * The persistent class for the documents database table.
 * 
 */
@Entity
@Table(name="documents")
@NamedQuery(name="Document.findAll", query="SELECT d FROM Document d")
public class Document extends ActivityObject implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="file_content_type")
	private String fileContentType;

	@Column(name="file_file_name")
	private String fileFileName;

	@Column(name="file_file_size")
	private String fileFileSize;

	@Column(name="file_processing")
	private byte fileProcessing;

	private String type;

	public Document() {
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

}