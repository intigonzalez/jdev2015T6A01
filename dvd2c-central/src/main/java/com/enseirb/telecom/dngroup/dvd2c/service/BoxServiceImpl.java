package com.enseirb.telecom.dngroup.dvd2c.service;

import java.util.Iterator;
import java.util.List;

import com.enseirb.telecom.dngroup.dvd2c.db.BoxServRepositoryObject;
import com.enseirb.telecom.dngroup.dvd2c.db.CrudRepository;
import com.enseirb.telecom.dngroup.dvd2c.db.UserRepositoryMongo;
import com.enseirb.telecom.dngroup.dvd2c.model.Box;
import com.enseirb.telecom.dngroup.dvd2c.model.ListBox;
import com.enseirb.telecom.dngroup.dvd2c.model.ListUser;
import com.enseirb.telecom.dngroup.dvd2c.model.User;

public class BoxServiceImpl implements BoxService {

	// add test
	CrudRepository<BoxServRepositoryObject, String> boxServDatabase;

	public BoxServiceImpl(
			CrudRepository<BoxServRepositoryObject, String> boxServDatabase) {
		this.boxServDatabase = boxServDatabase;
	}

	@Override
	public boolean boxExist(String boxID) {
		return boxServDatabase.exists(boxID);

	}

	@Override
	public Box getBox(String boxID) {
		BoxServRepositoryObject box = boxServDatabase.findOne(boxID);
		if (box == null) {
			return null;
		} else {
			return box.toBox();
		}

	}

	@Override
	public Box createBox(Box box) {
		return boxServDatabase.save(new BoxServRepositoryObject(box)).toBox();

	}

	@Override
	public void saveBox(Box box) {
		boxServDatabase.save(new BoxServRepositoryObject(box));

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

		Iterable<BoxServRepositoryObject> boxIterable = boxServDatabase
				.findAll();
		ListBox listBox = new ListBox();

		if (boxIterable == null)
			return listBox;
		else {
			Iterator<BoxServRepositoryObject> itr = boxIterable.iterator();
			while (itr.hasNext()) {
				BoxServRepositoryObject box = itr.next();

				if (box.getIp().equals(ip)) {
					listBox.getBox().add(box.toBox());
				}
			}
			return listBox;
		}

	}

	public ListUser getUsersFromBoxes(ListBox listBox) {

		AccountService uManager = new AccountServiceImpl(
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
		Iterable<BoxServRepositoryObject> boxIterable = boxServDatabase
				.findAll();
		ListBox listBox = new ListBox();
		if (boxIterable == null)
			return listBox;
		else {
			Iterator<BoxServRepositoryObject> itr = boxIterable.iterator();
			while (itr.hasNext()) {
				BoxServRepositoryObject box = itr.next();
				int temp = box.getUser().size();
				listBox.getBox().add(box.toBox());

			}
			return listBox;
		}
	}
}
