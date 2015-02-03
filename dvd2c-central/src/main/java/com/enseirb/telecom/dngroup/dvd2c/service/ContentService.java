package com.enseirb.telecom.dngroup.dvd2c.service;

import com.enseirb.telecom.dngroup.dvd2c.model.Content;

public interface ContentService {

	public abstract boolean contentExist(String contentsID);

	public abstract Content getContent(String contentsID);

	public abstract Content createContent(Content content);

	public abstract void saveContent(Content content);

	public abstract void deleteContent(String contentsID);
}
