package com.enseirb.telecom.dngroup.dvd2c.service;

import com.enseirb.telecom.dngroup.dvd2c.model.Box;
import com.enseirb.telecom.dngroup.dvd2c.model.ListBox;
import com.enseirb.telecom.dngroup.dvd2c.model.ListUser;

public interface BoxService {

	public abstract boolean boxExistOnServer(String boxID);
	
	public abstract boolean boxExistOnLocal(String boxID);

//	public abstract Box getBoxOnServer(String boxID);
	
	public abstract Box getBoxOnLocal(String boxID);

	public abstract Box createBoxOnServer(Box box);
	
//	public abstract Box createBoxOnLocal(Box box);

	public abstract void saveBoxOnServer(Box box);
	
	public abstract void saveBoxOnLocal(Box box);
	
	public abstract void deleteBoxOnServer(String boxID);
	
	public abstract void deleteBoxOnLocal(String boxID);
	
	public abstract ListUser getUserFromIP(String ip);

	public abstract ListBox getAllBox();

	public abstract ListBox getBoxesFromIP(String ip);

	public abstract ListUser getUsersFromBoxes(ListBox listBox);

//	public abstract Box getBox(String userID);
}
