package com.enseirb.telecom.s9.service;

import java.io.File;
import java.io.InputStream;

import com.enseirb.telecom.s9.Content;


public interface ContentService {
	
	public abstract boolean contentExist(String contentsID);

	public abstract Content getContent(String contentsID);

	public abstract Content createContent(Content content, String srcfile, String id);

	public abstract void saveContent(Content content);
	
	public abstract void deleteContent(String contentsID);

	/**
	 * save uploaded file to new location
	 * @param uploadedInputStream
	 * @param uploadedFileLocation
	 */
	public abstract void writeToFile(InputStream uploadedInputStream,
			File dest);
	public abstract void updateContent(String contentsID);
}
