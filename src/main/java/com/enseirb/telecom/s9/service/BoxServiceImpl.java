package com.enseirb.telecom.s9.service;

import com.enseirb.telecom.s9.Box;
import com.enseirb.telecom.s9.db.BoxRepositoryObject;
import com.enseirb.telecom.s9.db.CrudRepository;

public class BoxServiceImpl implements BoxService{

	CrudRepository<BoxRepositoryObject, String> boxDatabase;
	
	public BoxServiceImpl(CrudRepository<BoxRepositoryObject, String> boxDatabase){
		
		this.boxDatabase = boxDatabase;
	}

	public boolean boxExist(String boxID){
		
		return boxDatabase.exists(boxID);
	}
	
	public Box getBox(String boxID){
		
		return boxDatabase.findOne(boxID).toBox();
	}
	
	public Box createBox(Box box){
		
		return boxDatabase.save(new BoxRepositoryObject(box)).toBox();
	}
	
	public void saveBox(Box box){
		boxDatabase.save(new BoxRepositoryObject(box));
	}
	
	public void deleteBox(String boxID){
		
		boxDatabase.delete(boxID);
	}
	
	
}
