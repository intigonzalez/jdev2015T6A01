package com.enseirb.telecom.dngroup.dvd2c.utils;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileService {
	private static final Logger LOGGER = LoggerFactory.getLogger(FileService.class);
	public void deleteFolder(String folder) throws IOException {
		File directory = new File(folder);
		delete(directory);
	}
	private static void delete(File file) throws IOException{
		
	    	if(file.isDirectory()){
	 
	    		//directory is empty, then delete it
	    		if(file.list().length==0){
	 
	    		   file.delete();
	    		   LOGGER.debug("Directory is deleted : {}", file.getAbsolutePath());
	 
	    		}else{
	 
	    		   //list all the directory contents
	        	   String files[] = file.list();
	 
	        	   for (String temp : files) {
	        	      //construct the file structure
	        	      File fileDelete = new File(file, temp);
	 
	        	      //recursive delete
	        	     delete(fileDelete);
	        	   }
	 
	        	   //check the directory again, if empty then delete it
	        	   if(file.list().length==0){
	           	     file.delete();
	           	  LOGGER.debug("Directory is deleted : {}", file.getAbsolutePath());
	        	   }
	    		}
	 
	    	}else{
	    		//if file, then delete it
	    		file.delete();
	    		LOGGER.debug("File is deleted : {}",  file.getAbsolutePath());
	    	}
	    }
}
