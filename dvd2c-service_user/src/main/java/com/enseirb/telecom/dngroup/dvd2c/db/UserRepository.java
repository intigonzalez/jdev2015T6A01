package com.enseirb.telecom.dngroup.dvd2c.db;

//import java.io.Serializable;

public interface UserRepository extends
		CrudRepository<UserRepositoryObject, String> {

	// void findBoxFromBoxID(String boxID);

	/**
	 * get a box of user
	 * @param userID the user to get box
	 * @return the box object
	 */
	BoxRepositoryObject findBoxFromUserID(String userID);

}
