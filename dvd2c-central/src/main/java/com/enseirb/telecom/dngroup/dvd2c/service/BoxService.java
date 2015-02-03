package com.enseirb.telecom.dngroup.dvd2c.service;

import com.enseirb.telecom.dngroup.dvd2c.model.Box;
import com.enseirb.telecom.dngroup.dvd2c.model.ListBox;
import com.enseirb.telecom.dngroup.dvd2c.model.ListUser;

public interface BoxService {

	/**
	 * 
	 * @param id
	 * @return
	 */
	public abstract boolean boxExist(String boxID);

	public abstract Box getBox(String boxID);

	public abstract Box createBox(Box box);

	public abstract void saveBox(Box box);

	public abstract void deleteBox(String boxID);

	public abstract ListUser getUserFromIP(String ip);

	public abstract ListBox getAllBox();

	public ListBox getBoxesFromIP(String ip);

	public abstract ListUser getUsersFromBoxes(ListBox listBox);
}
