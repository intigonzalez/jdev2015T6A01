package com.enseirb.telecom.s9.service;

import java.io.IOException;

import com.enseirb.telecom.s9.ApplicationContext;
import com.enseirb.telecom.s9.Box;
import com.enseirb.telecom.s9.User;
import com.enseirb.telecom.s9.db.BoxRepositoryObject;
import com.enseirb.telecom.s9.db.CrudRepository;
import com.enseirb.telecom.s9.exception.NoSuchBoxException;
import com.enseirb.telecom.s9.exception.NoSuchUserException;
import com.enseirb.telecom.s9.exception.SuchBoxException;
import com.enseirb.telecom.s9.request.*;

public class BoxServiceImpl implements BoxService{

	CrudRepository<BoxRepositoryObject, String> boxDatabase;
	RequestBoxService requetBoxService = new RequestBoxServiceImpl(
			ApplicationContext.getProperties().getProperty("CentralURL") + "/api/app/box/");
	
	
	public BoxServiceImpl(CrudRepository<BoxRepositoryObject, String> boxDatabase){
		
		this.boxDatabase = boxDatabase;
	}

	public boolean boxExist(Box box){
		
		// TODO: change to the correct page and add fontion for get addr of
		// server
		boolean exist = boxDatabase.exists(box.getBoxID());
		
		try {
			
			Box boxGet = requetBoxService.get(box);
			if (boxGet == null)
				exist = false;
			else if (boxGet.getBoxID().equals(box.getBoxID()))
				exist = true;
		} catch (IOException e) {
			e.printStackTrace();
			 System.err.printf("Can not connect on the server :(\n");
			 
		} catch (NoSuchBoxException e) {
			exist =false;
		}
		
		return exist;
	}
	
	public Box getBox(String boxID){
		
		Box box = boxDatabase.findOne(boxID).toBox();
		
		if(box == null){
			
			return null;
		}
		
		return box;
	}
	
	public Box createBox(Box box){
		
		box.setBoxID(ApplicationContext.getProperties().getProperty("BoxID"));
		try {
			requetBoxService.post(box);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SuchBoxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Box b = boxDatabase.save(new BoxRepositoryObject(box)).toBox();
		return b;
		
	}
	
	public void saveBox(Box box){
		
		try {
			requetBoxService.put(box);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchBoxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		boxDatabase.save(new BoxRepositoryObject(box));
	}
	
	public void deleteBox(String boxID){
		
		try {
			requetBoxService.delete(boxID);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchBoxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		boxDatabase.delete(boxID);
	}
	
}
