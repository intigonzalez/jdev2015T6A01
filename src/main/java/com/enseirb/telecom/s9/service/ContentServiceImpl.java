package com.enseirb.telecom.s9.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.enseirb.telecom.s9.Content;
import com.enseirb.telecom.s9.db.ContentRepositoryObject;
import com.enseirb.telecom.s9.db.CrudRepository;

public class ContentServiceImpl implements ContentService {
	
	CrudRepository<ContentRepositoryObject, String> contentDatabase;

	public ContentServiceImpl(CrudRepository<ContentRepositoryObject, String> videoDatabase) {
		this.contentDatabase = videoDatabase;
	}

	@Override
	public boolean contentExist(String contentsID) {
		
		return contentDatabase.exists(contentsID);
	}

	@Override
	public Content getContent(String contentsID) {
		ContentRepositoryObject content = contentDatabase.findOne(contentsID);
		if (content == null ) {
			return null;
		}
		else {
			return content.toContent();
		}
	}

	@Override
	public Content createContent(Content content) {
		return contentDatabase.save(new ContentRepositoryObject(content)).toContent();
	}

	@Override
	public void saveContent(Content content) {
		contentDatabase.save(new ContentRepositoryObject(content));		
	}
	
	
	@Override
	public void writeToFile(InputStream uploadedInputStream,
			String uploadedFileLocation) {

		try {
			OutputStream out = new FileOutputStream(new File(
					uploadedFileLocation));
			int read = 0;
			byte[] bytes = new byte[1024];

			out = new FileOutputStream(new File(uploadedFileLocation));
			while ((read = uploadedInputStream.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
			out.flush();
			out.close();
		} catch (IOException e) {

			e.printStackTrace();
		}

	}
	

	@Override
	public void deleteContent(String contentsID) {
		this.contentDatabase.delete(contentsID);
		
	}

}
