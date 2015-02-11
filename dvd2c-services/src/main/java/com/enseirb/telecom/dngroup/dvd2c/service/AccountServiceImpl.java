package com.enseirb.telecom.dngroup.dvd2c.service;

import java.io.IOException;

import com.enseirb.telecom.dngroup.dvd2c.ApplicationContext;
import com.enseirb.telecom.dngroup.dvd2c.db.CrudRepository;
import com.enseirb.telecom.dngroup.dvd2c.db.UserRepositoryObject;
import com.enseirb.telecom.dngroup.dvd2c.exception.NoSuchUserException;
import com.enseirb.telecom.dngroup.dvd2c.exception.SuchUserException;
import com.enseirb.telecom.dngroup.dvd2c.service.request.RequestUserService;
import com.enseirb.telecom.dngroup.dvd2c.service.request.RequestUserServiceImpl;
import com.enseirb.telecom.dngroup.dvd2c.model.ListUser;
import com.enseirb.telecom.dngroup.dvd2c.model.User;

public class AccountServiceImpl implements AccountService {

	CrudRepository<UserRepositoryObject, String> userDatabase;
	RequestUserService requetUserService = new RequestUserServiceImpl();

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
			User userGet = requetUserService.get(user.getUserID());
			if (userGet == null)
				exist = false;
			else if (userGet.getUserID().equals(user.getUserID()))
				exist = true;
		} catch (IOException e) {
			//NHE: no print stack trace allowed in the project. Please replace it with appropriate logger and Exception handling. 
e.printStackTrace();
			 System.err.printf("Can not connect on the server :(\n");
		} catch (NoSuchUserException e) {
			//NHE: no print stack trace allowed in the project. Please replace it with appropriate logger and Exception handling. 
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
	public ListUser getUserFromNameOnServer(String name) {
	    try {
		return requetUserService.getUserFromName(name);
	    } catch (IOException e) {
		// TODO Auto-generated catch block
		//NHE: no print stack trace allowed in the project. Please replace it with appropriate logger and Exception handling. 
e.printStackTrace();
	    }
	    return null;
	}

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
			//NHE: no print stack trace allowed in the project. Please replace it with appropriate logger and Exception handling. 
e.printStackTrace();
		} catch (SuchUserException e) {
			//NHE: no print stack trace allowed in the project. Please replace it with appropriate logger and Exception handling. 
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
			//NHE: no print stack trace allowed in the project. Please replace it with appropriate logger and Exception handling. 
e.printStackTrace();
		} catch (NoSuchUserException e) {
			//NHE: no print stack trace allowed in the project. Please replace it with appropriate logger and Exception handling. 
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
			//NHE: no print stack trace allowed in the project. Please replace it with appropriate logger and Exception handling. 
e.printStackTrace();
		} catch (NoSuchUserException e) {
			//NHE: no print stack trace allowed in the project. Please replace it with appropriate logger and Exception handling. 
e.printStackTrace();
			// System.err.printf("Can not connect on the server :(\n");

		}

	}
}
