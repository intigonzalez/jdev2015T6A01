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

import com.enseirb.telecom.s9.ApplicationContext;
import com.enseirb.telecom.s9.User;
import com.enseirb.telecom.s9.db.CrudRepository;
import com.enseirb.telecom.s9.db.UserRepositoryObject;
import com.enseirb.telecom.s9.exception.NoSuchUserException;
import com.enseirb.telecom.s9.exception.SuchUserException;
import com.enseirb.telecom.s9.request.RequestUserService;
import com.enseirb.telecom.s9.request.RequestUserServiceImpl;
import com.mongodb.util.JSON;

public class AccountServiceImpl implements AccountService {

	CrudRepository<UserRepositoryObject, String> userDatabase;
	RequestUserService requetUserService = new RequestUserServiceImpl(
			"http://localhost:9999/api/app/account/");

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
	public boolean userExist(User user) {
		boolean exist = userDatabase.exists(user.getUserID());
		// TODO: change to the correct page and add fontion for get addr of
		// server
		try {
			User userGet = requetUserService.get(user);
			if (userGet == null)
				exist = false;
			else if (userGet.getUserID().equals(user.getUserID()))
				exist = true;
		} catch (IOException e) {
			e.printStackTrace();
			// System.err.printf("Can not connect on the server :(\n");
		} catch (NoSuchUserException e) {
			e.printStackTrace();
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

		try {
			user.setBoxID(ApplicationContext.getProperties().getProperty("BoxID"));
			requetUserService.post(user);
			User u = userDatabase.save(new UserRepositoryObject(user)).toUser();
			return u;
		} catch (IOException e) {
			// System.err.printf(user.getUserID()+
			// " can not registerd on the server");
			e.printStackTrace();
		} catch (SuchUserException e) {
			e.printStackTrace();
			// System.err.printf("Can not connect on the server :(\n");

		}

		return null;
	}

	@Override
	public void saveUser(User user) {
		try {
			requetUserService.put(user);
			userDatabase.save(new UserRepositoryObject(user));
		} catch (IOException e) {
			// System.err.printf(user.getUserID()+
			// " can not registerd on the server");
			e.printStackTrace();
		} catch (NoSuchUserException e) {
			e.printStackTrace();
			// System.err.printf("Can not connect on the server :(\n");

		}

	}

	@Override
	public void deleteUser(String userID) {
		try {
			requetUserService.delete(userID);
			this.userDatabase.delete(userID);

		} catch (IOException e) {
			// System.err.printf(user.getUserID()+
			// " can not registerd on the server");
			e.printStackTrace();
		} catch (NoSuchUserException e) {
			e.printStackTrace();
			// System.err.printf("Can not connect on the server :(\n");

		}

	}
}
