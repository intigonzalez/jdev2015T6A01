package com.enseirb.telecom.dngroup.dvd2c.db;



import java.util.List;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.mongodb.repository.Query;
import com.enseirb.telecom.dngroup.dvd2c.repository.UserRepositoryObject;

//import java.io.Serializable;

public interface UserRepository
		extends
		CrudRepository<UserRepositoryObject, UUID> {
	
	@Query("{ 'firstname' : ?0 }")
	List<UserRepositoryObject> findByfirstname(String firstname);
	
	@Query("{ 'userEmail' : ?0 }")
	UserRepositoryObject findByEmail(String userEmail);
	
	
	@Query("{ 'uuid' : ?0 }")
	UserRepositoryObject findByUuid(UUID uuid);

}
