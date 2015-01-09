package com.enseirb.telecom.s9.service;

import com.enseirb.telecom.s9.Box;

public interface BoxService {

	
	public abstract boolean boxExist(Box box);

	public abstract Box getBox(String boxID);

	public abstract Box createBox(Box box);

	public abstract void saveBox(Box box);
	
	public abstract void deleteBox(String boxID);
	
}
