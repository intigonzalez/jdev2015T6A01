package com.enseirb.telecom.dngroup.dvd2c.service;

import java.io.IOException;
import javax.inject.Inject;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;
import jersey.repackaged.com.google.common.base.Throwables;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.enseirb.telecom.dngroup.dvd2c.CliConfSingleton;
import com.enseirb.telecom.dngroup.dvd2c.exception.NoSuchBoxException;
import com.enseirb.telecom.dngroup.dvd2c.model.Box;
import com.enseirb.telecom.dngroup.dvd2c.service.request.RequestBoxService;

@Service
public class BoxServiceImpl implements BoxService {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(BoxServiceImpl.class);

	@Inject
	RequestBoxService requetBoxService;

	@Override
	public boolean boxExistOnServer(String boxID) {
		boolean exist = false;
		try {
			Box boxGet = requetBoxService.get(boxID);
			if ((boxGet == null))
				exist = false;
			else if (boxID.equals(boxGet.getBoxID()))
				exist = true;
			else {
				exist = false;
			}
		} catch (IOException e) {
			LOGGER.error("Can not connect on the server : {}", boxID, e);
		} catch (NoSuchBoxException e) {
			exist = false;
		}
		return exist;
	}

	public Box createBoxOnServer(Box box) throws IOException {

		try {
			requetBoxService.createBoxORH(box);
			// RBAC: Return the correct link
			return box;
		} catch (IOException e) {
			LOGGER.error("Error during creating a box on server : ",
					box.getBoxID(), e);
			throw e;
		} catch (ProcessingException e) {
			LOGGER.error(
					"Error can't converte the responce of the RH, RH is enable ",
					box.getBoxID(), e);
			throw new WebApplicationException(404);
		}

	}

	@Override
	public void updateBox() throws IOException {
		Box box = new Box();
		box.setBoxID(CliConfSingleton.boxID);
		box.setIp(CliConfSingleton.publicAddr);
		createBoxOnServer(box);
	}

	public void saveBoxOnServer(Box box) {

		try {

			requetBoxService.updateBoxORH(box);

		} catch (IOException e) {
			LOGGER.error("can't save Box On Server : {}", box.getBoxID(), e);
			throw Throwables.propagate(e);
		} catch (NoSuchBoxException e) {
			LOGGER.error("Box not found: {}", box.getBoxID(), e);
			throw new WebApplicationException(Status.NOT_FOUND);
		}

	}

	public void deleteBoxOnServer(String boxID) {

		try {
			requetBoxService.deleteBoxORH(boxID);
		} catch (IOException e) {
			LOGGER.error("can't delete box : {}", boxID, e);
		} catch (NoSuchBoxException e) {
			LOGGER.error("box not found : {}", boxID, e);
		}

	}

}
