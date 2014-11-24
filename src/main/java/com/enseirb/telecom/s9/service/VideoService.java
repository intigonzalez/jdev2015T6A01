package com.enseirb.telecom.s9.service;

import com.enseirb.telecom.s9.Content;


public interface VideoService {

	public abstract boolean videoExist(String contentsID);
	
	public abstract Content getVideo(String contentsID);

	public abstract Content createVideo(Content video);

	public abstract void saveVideo(Content video);
	
	public abstract void deleteVideo(String contentsID);
}
