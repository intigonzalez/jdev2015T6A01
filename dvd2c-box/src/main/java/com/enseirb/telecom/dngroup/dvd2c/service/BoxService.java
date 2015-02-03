package com.enseirb.telecom.dngroup.dvd2c.service;

import com.enseirb.telecom.dngroup.dvd2c.model.Box;

public interface BoxService {

	
	public abstract boolean boxExist(Box box);

	public abstract Box getBox(String boxID);

	public abstract Box createBox(Box box);

	public abstract void saveBox(Box box);
	
	public abstract void deleteBox(String boxID);
	
}
