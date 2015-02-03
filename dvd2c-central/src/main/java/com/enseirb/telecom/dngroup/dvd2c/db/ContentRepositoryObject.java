package com.enseirb.telecom.dngroup.dvd2c.db;

import java.util.List;

import org.bson.types.ObjectId;

import com.enseirb.telecom.dngroup.dvd2c.model.Authorization;
import com.enseirb.telecom.dngroup.dvd2c.model.Content;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ContentRepositoryObject {
	public ContentRepositoryObject() {

	}

	public ContentRepositoryObject(ObjectId id, String name, String userId,
			Long unixTime, String link, String previewLink, String status, /*
																			 * List<
																			 * Comment
																			 * >
																			 * comment
																			 * ,
																			 */
			List<Authorization> authorizations) {
		super();
		_id = id;
		this.name = name;
		this.userId = userId;
		this.unixTime = unixTime;
		this.link = link;
		this.previewLink = previewLink;
		this.status = status;
		// this.comment = comment;
		this.authorizations = authorizations;
	}

	public ContentRepositoryObject(Content content) {
		if (content.getContentsID() != null) {
			_id = new ObjectId(content.getContentsID());
		}
		name = content.getName();
		userId = content.getLogin();
		unixTime = content.getUnixTime();
		link = content.getLink();
		previewLink = content.getPreviewLink();
		status = content.getStatus();
		// this.comment = content.getComment();
		this.authorizations = content.getAuthorization();
	}

	@JsonProperty("_id")
	ObjectId _id;
	String name;
	String userId;
	Long unixTime;
	String link;
	String previewLink;
	String status;
	// List<Comment> comment;
	List<Authorization> authorizations;

	public ObjectId getId() {
		return _id;
	}

	public void setId(ObjectId id) {
		_id = id;
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

	// public List<Comment> getComment() {
	// return comment;
	// }
	//
	// public void setComment(List<Comment> comment) {
	// this.comment = comment;
	// }

	public List<Authorization> getAuthorizations() {
		return authorizations;
	}

	public void setAuthorizations(List<Authorization> authorizations) {
		this.authorizations = authorizations;
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
		content.getAuthorization().addAll(this.getAuthorizations());

		return content;
	}

}