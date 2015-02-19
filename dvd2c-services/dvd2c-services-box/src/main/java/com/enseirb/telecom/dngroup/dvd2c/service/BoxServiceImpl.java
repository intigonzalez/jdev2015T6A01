package com.enseirb.telecom.dngroup.dvd2c.service;

import java.io.IOException;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enseirb.telecom.dngroup.dvd2c.ApplicationContext;
import com.enseirb.telecom.dngroup.dvd2c.db.BoxRepositoryObject;
import com.enseirb.telecom.dngroup.dvd2c.db.CrudRepository;
import com.enseirb.telecom.dngroup.dvd2c.exception.NoSuchBoxException;
import com.enseirb.telecom.dngroup.dvd2c.exception.SuchBoxException;
import com.enseirb.telecom.dngroup.dvd2c.model.Box;
import com.enseirb.telecom.dngroup.dvd2c.model.ListBox;
import com.enseirb.telecom.dngroup.dvd2c.service.request.RequestBoxService;
import com.enseirb.telecom.dngroup.dvd2c.service.request.RequestBoxServiceImpl;

public class BoxServiceImpl implements BoxService {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(BoxServiceImpl.class);
	CrudRepository<BoxRepositoryObject, String> boxDatabase;
	RequestBoxService requetBoxService = new RequestBoxServiceImpl(
			ApplicationContext.getProperties().getProperty("CentralURL")
					+ "/api/app/box/");

	public BoxServiceImpl(
			CrudRepository<BoxRepositoryObject, String> boxDatabase) {

		this.boxDatabase = boxDatabase;
	}

	@Override
	public boolean boxExistOnServer(String boxID) {
		boolean exist = boxExistOnLocal(boxID);
		try {
			Box boxGet = requetBoxService.get(boxID);
			if ((boxGet == null))
				exist = false;
			else if (boxID.equals(boxGet.getBoxID()))
				exist = true;
			else{
				exist = false;
			}
		} catch (IOException e) {
			LOGGER.error("Can not connect on the server : {}",boxID, e);
		} catch (NoSuchBoxException e) {
			exist = false;
		}
		return exist;
	}

	public boolean boxExistOnLocal(String boxID) {
		return boxDatabase.exists(boxID);
	}

	public Box getBoxOnLocal(String boxID) {
		
		Box box = null;
		box = boxDatabase.findOne(boxID).toBox();
		if (box == null) {
			LOGGER.debug("No Box Found : {}",boxID);
			return null;
		}
		LOGGER.debug("Box Found : {}",box.getBoxID());
		return box;
	}

	public Box createBoxOnServer(Box box) {

		// box.setBoxID(ApplicationContext.getProperties().getProperty("BoxID"));
		try {
			requetBoxService.createBoxORH(box);
		} catch (IOException e) {
			LOGGER.error("Error during creating a box on server : ",
					box.getBoxID(), e);
		} catch (SuchBoxException e) {
			LOGGER.debug("Box already existing : ", box.getBoxID());
		}
		return createBoxOnLocal(box);

	}

	private Box createBoxOnLocal(Box box) {

		Box b = boxDatabase.save(new BoxRepositoryObject(box)).toBox();
		return b;

	}

	public void saveBoxOnServer(Box box) {

		try {

			requetBoxService.updateBoxORH(box);
		} catch (IOException e) {
			LOGGER.error("can't save Box On Server : {}", box.getBoxID(), e);
		} catch (NoSuchBoxException e) {
			LOGGER.error("Box not found: {}", box.getBoxID(), e);
		}
		saveBoxOnLocal(box);
	}

	public void saveBoxOnLocal(Box box) {
		boxDatabase.save(new BoxRepositoryObject(box));
	}

	public void deleteBoxOnServer(String boxID) {

		try {
			requetBoxService.deleteBoxORH(boxID);
		} catch (IOException e) {
			LOGGER.error("can't delete box : {}", boxID, e);
		} catch (NoSuchBoxException e) {
			LOGGER.error("box not found : {}", boxID, e);
		}

		// XXX: Good to delete on local ???
		deleteBoxOnLocal(boxID);
	}

	public void deleteBoxOnLocal(String boxID) {
		this.boxDatabase.delete(boxID);

	}

	@Override
	public ListBox getBoxListFromIP(String ip) {
		ListBox listBox = getBoxesFromIP(ip);
		return listBox;
	}

	@Override
	public ListBox getAllBox() {
		Iterable<BoxRepositoryObject> boxIterable = boxDatabase.findAll();
		ListBox listBox = new ListBox();
		if (boxIterable == null)
			return listBox;
		else {
			Iterator<BoxRepositoryObject> itr = boxIterable.iterator();
			while (itr.hasNext()) {
				BoxRepositoryObject box = itr.next();
//				int temp = box.getUser().size();
				listBox.getBox().add(box.toBox());

			}
			return listBox;
		}
	}

	@Override
	public ListBox getBoxesFromIP(String ip) {

		Iterable<BoxRepositoryObject> boxIterable = boxDatabase.findAll();
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


	


}