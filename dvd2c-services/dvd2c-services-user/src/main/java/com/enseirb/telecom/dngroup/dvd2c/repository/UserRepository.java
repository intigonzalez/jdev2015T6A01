package com.enseirb.telecom.dngroup.dvd2c.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.enseirb.telecom.dngroup.dvd2c.modeldb.User;

//import java.io.Serializable;
@Repository
public interface UserRepository extends CrudRepository<User, UUID> {
	
	@Query("select u from User u where u.email = ?1")
	User findByEmail(String email);

	@Query("select u from User u where u.firstname like %?1")
	List<User> findByFirstnameEndsWith(String firstname);

}
