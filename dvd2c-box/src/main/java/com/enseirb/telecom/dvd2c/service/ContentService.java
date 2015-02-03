package com.enseirb.telecom.dvd2c.service;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import com.enseirb.telecom.s9.Content;
import com.enseirb.telecom.s9.ListContent;
import com.enseirb.telecom.s9.Relation;


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
	 * @return ListContent 
	 */
	public abstract ListContent getAllContentsFromUser(String userID);
	
	/**
	 * get more information from a contentsID
	 * @param contentsID to get
	 * @return Content 
	 */
	public abstract Content getContent(String contentsID);

	/**
	 * send a new file (video actually) on box
	 * @param content
	 * @param srcfile
	 * @param id
	 * @return
	 */
	public abstract Content createContent(Content content, String srcfile, String id);

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

	/**
	 * save uploaded file to new location
	 * @param uploadedInputStream
	 * @param uploadedFileLocation
	 */
	public abstract void writeToFile(InputStream uploadedInputStream,
			File dest);

	/**
	 * get all content for a list group (use from a author box)
	 * is use to get video for a specific user
	 * @param groupID list of group to get video
	 * @return a list a content
	 */
	public ListContent getAllContent(String userID, Relation relation);

	/**
	 * is use just for change status a video
	 * @param contentsID
	 * @param status
	 */
	public abstract void updateContent(String contentsID, String status);

}
