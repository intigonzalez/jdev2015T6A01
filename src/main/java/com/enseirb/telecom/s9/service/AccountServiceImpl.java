package com.enseirb.telecom.s9.service;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.net.ContentHandler;
import java.net.URLConnection;

import javax.validation.Path.Node;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.Result;

import com.enseirb.telecom.s9.User;
import com.enseirb.telecom.s9.db.CrudRepository;
import com.enseirb.telecom.s9.db.UserRepositoryObject;
import com.mongodb.util.JSON;

public class AccountServiceImpl implements AccountService {

	CrudRepository<UserRepositoryObject, String> userDatabase;
	RequetUserServiceImpl requetUserServiceImpl= new RequetUserServiceImpl("http://localhost:9999/api/app/account/");

	public AccountServiceImpl(
			CrudRepository<UserRepositoryObject, String> userDatabase) {
		this.userDatabase = userDatabase;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enseirb.telecom.s9.service.AccountService#userExist(java.lang.String)
	 */
	@Override
	public boolean userExist(String email) {
		boolean exist = userDatabase.exists(email);
		// TODO: change to the correct page and add fontion for get addr of
		// server
		try {
			Response retourDeLaPage = requetUserServiceImpl
					.get("http://localhost:9999/api/app/account/" + email);
			if (retourDeLaPage.getStatusInfo().equals(Status.CONFLICT))
				exist = true;
			else if (!retourDeLaPage.getStatusInfo().equals(Status.OK)) {
				System.err.printf("The server say is not ok ! :(\n");
			}
		} catch (Exception e) {
			System.err.printf("Can not connect on the server :(\n");

		}

		return exist;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enseirb.telecom.s9.service.AccountService#getUser(java.lang.String)
	 */
	@Override
	public User getUser(String email) {
		UserRepositoryObject user = userDatabase.findOne(email);
		if (user == null) {
			return null;
		} else {
			return user.toUser();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enseirb.telecom.s9.service.AccountService#createUser(com.enseirb.
	 * telecom.s9.User)
	 */

	@Override
	public User createUser(User user) {
		return userDatabase.save(new UserRepositoryObject(user)).toUser();
	}

	@Override
	public void saveUser(User user) {
		Response returnValue = requetUserServiceImpl.post(
				"http://localhost:9999/api/app/account/", user);
		try {
			if (!returnValue.getStatusInfo().equals(Status.CREATED)) {
				System.err.printf(user.getUserID()
						+ " can not registerd on the server");
			}
		} catch (Exception e) {
			System.err.printf("Can not connect on the server :(\n");

		}
		userDatabase.save(new UserRepositoryObject(user));

	}

	@Override
	public void deleteUser(String email) {
		this.userDatabase.delete(email);

	}
}
