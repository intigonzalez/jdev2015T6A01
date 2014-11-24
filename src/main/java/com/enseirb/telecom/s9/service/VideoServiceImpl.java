package com.enseirb.telecom.s9.service;

import com.enseirb.telecom.s9.Content;
import com.enseirb.telecom.s9.db.CrudRepository;
import com.enseirb.telecom.s9.db.UserRepositoryObject;

public class VideoServiceImpl implements VideoService {
	
	CrudRepository<UserRepositoryObject, String> videoDatabase;

	@Override
	public boolean videoExist(String contentsID) {
		
		return videoDatabase.exists(contentsID);
	}
	
	@Override
	public Content getVideo(String contentsID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Content createVideo(Content video) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveVideo(Content video) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteVideo(String contentsID) {
		// TODO Auto-generated method stub
		
	}


}
