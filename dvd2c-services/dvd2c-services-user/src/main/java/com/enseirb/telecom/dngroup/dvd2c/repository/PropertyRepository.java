package com.enseirb.telecom.dngroup.dvd2c.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.enseirb.telecom.dngroup.dvd2c.modeldb.PropertyGroupsDB;
import com.enseirb.telecom.dngroup.dvd2c.modeldb.Profile;
import com.enseirb.telecom.dngroup.dvd2c.modeldb.User;

//import java.io.Serializable;
@Repository
public interface PropertyRepository extends CrudRepository<PropertyGroupsDB, Integer> {
	
//	@Query("select u from KeyValue u where u.key = ?1")
//	User findByKeyAndUser(String email,);


}
