package com.enseirb.telecom.dngroup.dvd2c.service;

import java.util.Iterator;
import java.util.List;

import com.enseirb.telecom.dngroup.dvd2c.db.BoxRepositoryObject;
import com.enseirb.telecom.dngroup.dvd2c.db.CrudRepository;
import com.enseirb.telecom.dngroup.dvd2c.db.UserRepositoryMongo;
import com.enseirb.telecom.dngroup.dvd2c.model.Box;
import com.enseirb.telecom.dngroup.dvd2c.model.ListBox;
import com.enseirb.telecom.dngroup.dvd2c.model.ListUser;
import com.enseirb.telecom.dngroup.dvd2c.model.User;

public class BoxServiceCentralImpl implements BoxServiceCentral {

	// add test
	CrudRepository<BoxRepositoryObject, String> boxServDatabase;

	public BoxServiceCentralImpl(
			CrudRepository<BoxRepositoryObject, String> boxServDatabase) {
		this.boxServDatabase = boxServDatabase;
	}

	@Override
	public boolean boxExist(String boxID) {
		return boxServDatabase.exists(boxID);

	}

	@Override
	public Box getBox(String boxID) {
		BoxRepositoryObject box = boxServDatabase.findOne(boxID);
		if (box == null) {
			return null;
		} else {
			return box.toBox();
		}

	}

	@Override
	public Box createBox(Box box) {
		return boxServDatabase.save(new BoxRepositoryObject(box)).toBox();

	}

	@Override
	public void saveBox(Box box) {
		boxServDatabase.save(new BoxRepositoryObject(box));

	}

	@Override
	public void deleteBox(String boxID) {
		this.boxServDatabase.delete(boxID);

	}

	@Override
	public ListUser getUserFromIP(String ip) {

		ListBox listBox = getBoxesFromIP(ip);

		return getUsersFromBoxes(listBox);
	}

	public ListBox getBoxesFromIP(String ip) {

		Iterable<BoxRepositoryObject> boxIterable = boxServDatabase
				.findAll();
		ListBox listBox = new ListBox();

		if (boxIterable == null)
			return listBox;
		else {
			Iterator<BoxRepositoryObject> itr = boxIterable.iterator();
			while (itr.hasNext()) {
				BoxRepositoryObject box = itr.next();

				if (box.getIp().equals(ip)) {
					listBox.getBox().add(box.toBox());
				}
			}
			return listBox;
		}

	}

	public ListUser getUsersFromBoxes(ListBox listBox) {

		AccountServiceCentral uManager = new AccountServiceCentralImpl(
				new UserRepositoryMongo());
		ListUser listUsersFinal = new ListUser(), listUsersOfBoxes = new ListUser();

		List<User> u;
		User user;
		Box box = new Box();
		List<Box> boxes = listBox.getBox();

		Iterator<Box> itrBoxes = boxes.iterator();
		while (itrBoxes.hasNext()) {

			box = itrBoxes.next();
			listUsersOfBoxes = uManager.getUserFromBoxID(box.getBoxID());
			u = listUsersOfBoxes.getUser();
			Iterator<User> itrUsers = u.iterator();

			while (itrUsers.hasNext()) {

				user = itrUsers.next();
				listUsersFinal.getUser().add(user);
			}
		}

		return listUsersFinal;
	}

	@Override
	public ListBox getAllBox() {
		Iterable<BoxRepositoryObject> boxIterable = boxServDatabase
				.findAll();
		ListBox listBox = new ListBox();
		if (boxIterable == null)
			return listBox;
		else {
			Iterator<BoxRepositoryObject> itr = boxIterable.iterator();
			while (itr.hasNext()) {
				BoxRepositoryObject box = itr.next();
				int temp = box.getUser().size();
				listBox.getBox().add(box.toBox());

			}
			return listBox;
		}
	}
}
