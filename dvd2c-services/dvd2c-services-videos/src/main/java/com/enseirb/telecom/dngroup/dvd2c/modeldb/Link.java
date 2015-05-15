package com.enseirb.telecom.dngroup.dvd2c.modeldb;

import java.io.Serializable;

import javax.persistence.*;


/**
 * The persistent class for the links database table.
 * 
 */
@Entity
@Table(name="links")
@NamedQuery(name="Link.findAll", query="SELECT l FROM Link l")
public class Link extends ActivityObject implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="callback_url")
	private String callbackUrl;

	private int height;

	private String image;

	private String url;

	private int width;

	public Link() {
	}

	public String getCallbackUrl() {
		return this.callbackUrl;
	}

	public void setCallbackUrl(String callbackUrl) {
		this.callbackUrl = callbackUrl;
	}

	public int getHeight() {
		return this.height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public String getImage() {
		return this.image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getWidth() {
		return this.width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

}