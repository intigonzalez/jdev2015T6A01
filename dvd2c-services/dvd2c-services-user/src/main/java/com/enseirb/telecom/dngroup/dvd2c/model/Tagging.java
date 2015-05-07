package com.enseirb.telecom.dngroup.dvd2c.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the taggings database table.
 * 
 */
@Entity
@Table(name="taggings")
@NamedQuery(name="Tagging.findAll", query="SELECT t FROM Tagging t")
public class Tagging implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false)
	private int id;

	@Column(length=128)
	private String context;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="created_at")
	private Date createdAt;

	@Column(name="tag_id")
	private int tagId;

	@Column(name="taggable_id")
	private int taggableId;

	@Column(name="taggable_type", length=255)
	private String taggableType;

	@Column(name="tagger_id")
	private int taggerId;

	@Column(name="tagger_type", length=255)
	private String taggerType;

	public Tagging() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getContext() {
		return this.context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public Date getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public int getTagId() {
		return this.tagId;
	}

	public void setTagId(int tagId) {
		this.tagId = tagId;
	}

	public int getTaggableId() {
		return this.taggableId;
	}

	public void setTaggableId(int taggableId) {
		this.taggableId = taggableId;
	}

	public String getTaggableType() {
		return this.taggableType;
	}

	public void setTaggableType(String taggableType) {
		this.taggableType = taggableType;
	}

	public int getTaggerId() {
		return this.taggerId;
	}

	public void setTaggerId(int taggerId) {
		this.taggerId = taggerId;
	}

	public String getTaggerType() {
		return this.taggerType;
	}

	public void setTaggerType(String taggerType) {
		this.taggerType = taggerType;
	}

}