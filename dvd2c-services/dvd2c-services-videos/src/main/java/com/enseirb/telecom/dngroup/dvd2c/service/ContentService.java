package com.enseirb.telecom.dngroup.dvd2c.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.ws.rs.core.NoContentException;

import com.enseirb.telecom.dngroup.dvd2c.model.Content;
import com.enseirb.telecom.dngroup.dvd2c.model.Relation;


public interface ContentService {
	
	/**
	 * Verify if content exist on box and on server
	 * @param contentsID to verify
	 * @return
	 */
	public abstract boolean contentExist(String contentsID);

	/**
	 * get all contents from userID
	 * @param userID of get contents
	 * @return List<Content> 
	 */
	public abstract List<Content> getAllContentsFromUser(String userID);
	
	/**
	 * get more information from a contentsID
	 * @param contentsID to get
	 * @return Content 
	 * @throws NoContentException 
	 */
	public abstract Content getContent(String contentsID) throws NoContentException;

	/**
	 * send a new file (video actually) on box
	 * @param content
	 * @param srcfile
	 * @param id
	 * @return
	 */
	public abstract Content createContent(Content content, String srcfile, String id) throws IOException;

	/**
	 * update Content 
	 * for modification groupe or action
	 * @param content
	 */
	public abstract void saveContent(Content content);
	
	/**
	 * delete a contents id
	 * @param contentsID to delete
	 */
	public abstract void deleteContent(String contentsID);

//	/**
//	 * save uploaded file to new location
//	 * @param uploadedInputStream
//	 * @param uploadedFileLocation
//	 * @throws IOException 
//	 */
//	public abstract void writeToFile(InputStream uploadedInputStream,
//			File dest) throws IOException;

	/**
	 * //TODO : explain this method
	 * get all content for a userID
	 * is use to get video for a specific user
	 * @param userID
	 * @param relation
	 * @return a list a content
	 */
	public List<Content> getAllContent(String userID, Relation relation);

	/**
	 * is use just for change status a video
	 * @param contentsID
	 * @param status
	 */
	public abstract void updateContent(String contentsID, String status);

	public abstract Content createContent(String userID,
			InputStream uploadedInputStream, String[] fileType, File upload) throws IOException;

	public abstract Content createContent(String userID,
			InputStream uploadedInputStream, String contentDisposition) throws IOException;

}
