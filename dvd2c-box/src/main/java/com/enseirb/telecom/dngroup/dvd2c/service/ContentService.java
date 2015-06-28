package com.enseirb.telecom.dngroup.dvd2c.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.core.NoContentException;

import com.enseirb.telecom.dngroup.dvd2c.exception.AlternativeStorageException;
import com.enseirb.telecom.dngroup.dvd2c.model.Content;

public interface ContentService {

	public abstract Content getContent(Integer contentsID)
			throws NoContentException;

	public abstract Content createContent(String userID,
			InputStream uploadedInputStream) throws IOException;

	public abstract Content createNewContentResolution(String contentId,
			String resolutionName, InputStream iS)
			throws AlternativeStorageException, IOException;

	public abstract void updateContentWithUrl(String contentId,
			String resolution, String url) throws NoContentException;

	public abstract InputStream getContentStream(Integer contentsID)
			throws AlternativeStorageException, NoContentException;

	public abstract InputStream getContentStream(Integer contentsID,
			String resolutionName) throws AlternativeStorageException,
			NoContentException;

}
