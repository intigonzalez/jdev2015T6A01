package com.enseirb.telecom.dngroup.dvd2c.service;

import com.enseirb.telecom.dngroup.dvd2c.db.ContentRepositoryObject;
import com.enseirb.telecom.dngroup.dvd2c.db.CrudRepository;
import com.enseirb.telecom.dngroup.dvd2c.model.Content;

public class ContentServiceImpl implements ContentService {

	CrudRepository<ContentRepositoryObject, String> contentDatabase;

	public ContentServiceImpl(
			CrudRepository<ContentRepositoryObject, String> videoDatabase) {
		this.contentDatabase = videoDatabase;
	}

	@Override
	public boolean contentExist(String contentsID) {

		return contentDatabase.exists(contentsID);
	}

	@Override
	public Content getContent(String contentsID) {
		ContentRepositoryObject content = contentDatabase.findOne(contentsID);
		if (content == null) {
			return null;
		} else {
			return content.toContent();
		}
	}

	@Override
	public Content createContent(Content content) {
		return contentDatabase.save(new ContentRepositoryObject(content))
				.toContent();
	}

	@Override
	public void saveContent(Content content) {
		contentDatabase.save(new ContentRepositoryObject(content));
	}

	@Override
	public void deleteContent(String contentsID) {
		this.contentDatabase.delete(contentsID);

	}

}
