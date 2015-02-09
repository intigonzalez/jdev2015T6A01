package com.enseirb.telecom.dngroup.dvd2c.db;

//import java.io.Serializable;

public interface UserRepository extends
		CrudRepository<UserRepositoryObject, String> {

	// void findBoxFromBoxID(String boxID);

	BoxRepositoryObject findBoxFromUserID(String userID);

}
