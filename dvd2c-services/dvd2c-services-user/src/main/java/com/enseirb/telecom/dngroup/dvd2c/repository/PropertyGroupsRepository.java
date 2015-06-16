package com.enseirb.telecom.dngroup.dvd2c.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.enseirb.telecom.dngroup.dvd2c.modeldb.PropertyGroupsDB;
import com.enseirb.telecom.dngroup.dvd2c.modeldb.User;

//import java.io.Serializable;
@Repository
@Transactional(readOnly = true)
public interface PropertyGroupsRepository extends CrudRepository<PropertyGroupsDB, Integer> {
	
	@Query("select u from PropertyGroupsDB u where u.name = ?1 and u.user = ?2")
	PropertyGroupsDB findByNameAndUser(String name,User user);
	
	
	//TODO: not so beautiful
	@Modifying
	@Transactional
	@Query(value="DELETE FROM mediahome.property WHERE property.property = ?1", nativeQuery=true)
	public void deleteProperties(int id);

}
