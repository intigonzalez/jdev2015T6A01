package com.enseirb.telecom.s9.service;

import com.enseirb.telecom.s9.Contents;


public interface VideoService {

	public abstract Contents getVideo(String contentsID);

	public abstract Contents createVideo(Contents video);

	public abstract void saveVideo(Contents video);
	
	public abstract void deleteVideo(String contentsID);
}
