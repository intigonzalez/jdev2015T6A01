package com.enseirb.telecom.dvd2c.db;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.enseirb.telecom.s9.Authorization;
import com.enseirb.telecom.s9.Comment;
import com.enseirb.telecom.s9.Content;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class ContentRepositoryObject {
	String id;
	String name;
	String userId;
	Long unixTime;
	String link;
	String previewLink;
	String status;
	List<Comment> comment;
	List<Authorization> authorization;

	public ContentRepositoryObject() {

	}

	public ContentRepositoryObject(String id, String name, String userId, Long unixTime, String link, String previewLink, String status, List<Comment> comment, List<Authorization> authorization) {
		super();
		this.id = id;
		this.name = name;
		this.userId = userId;
		this.unixTime = unixTime;
		this.link = link;
		this.previewLink = previewLink;
		this.status = status;
		this.comment = comment;
		this.authorization = authorization;
	}

	public ContentRepositoryObject(Content content) {
		id = content.getContentsID();
		name = content.getName();
		userId = content.getLogin();
		unixTime = content.getUnixTime();
		link = content.getLink();
		previewLink = content.getPreviewLink();
		status = content.getStatus();
		this.comment = content.getComment();
		this.authorization = content.getAuthorization();
	}
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Long getUnixTime() {
		return unixTime;
	}

	public void setUnixTime(Long unixTime) {
		this.unixTime = unixTime;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public List<Comment> getComment() {
		return comment;
	}

	public void setComment(List<Comment> comment) {
		this.comment = comment;
	}

	public List<Authorization> getAuthorization() {
		return authorization;
	}

	public void setAuthorizations(List<Authorization> authorization) {
		this.authorization = authorization;
	}
	public String getPreviewLink() {
		return previewLink;
	}
	public void setPreviewLink(String previewLink) {
		this.previewLink = previewLink;
	}
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Content toContent() {
		Content content = new Content();
		content.setContentsID(this.getId().toString());
		content.setName(this.getName());
		content.setLogin(this.getUserId());
		content.setLink(this.getLink());
		content.setPreviewLink(this.getPreviewLink());
		content.setUnixTime(this.getUnixTime());
		content.setStatus(this.getStatus());
		content.getAuthorization().addAll(this.getAuthorization());
		content.getComment().addAll(this.getComment());
		return content;
	}


	

}