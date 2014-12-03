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
		if (exist == false) {
			try {
				// change to the correct page
				String retourDeLaPage = RequetServiceImpl
						.doGet("http://localhost:9998/api/app/account/" + email);
				if (!retourDeLaPage.isEmpty())
					exist=true;
			} catch (IOException e) {
				e.printStackTrace();
			}

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
//		try {
//			java.io.StringWriter sw = new StringWriter();
//			JAXB.marshal(user, sw);
//			String returnValue = RequetServiceImpl.post("http://localhost:9998/api/app/account/",sw.toString());
//			String returnValue = RequetServiceImpl.post("http://localhost:9998/api/appserv/account/",user);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		userDatabase.save(new UserRepositoryObject(user));

	}
	
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	private String toXML(User user){
		
		return user.getClass().toString();		
	}

	@Override
	public void deleteUser(String email) {
		this.userDatabase.delete(email);

	}
}
