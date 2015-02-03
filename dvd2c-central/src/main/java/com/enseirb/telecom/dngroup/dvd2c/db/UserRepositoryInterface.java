package com.enseirb.telecom.dngroup.dvd2c.db;

//import java.io.Serializable;

public interface UserRepositoryInterface extends
		CrudRepository<UserRepositoryObject, String> {

	// void findBoxFromBoxID(String boxID);

	BoxServRepositoryObject findBoxFromUserID(String userID);

}
