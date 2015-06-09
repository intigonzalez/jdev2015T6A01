package com.enseirb.telecom.dngroup.dvd2c.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.enseirb.telecom.dngroup.dvd2c.modeldb.Permission;

//import java.io.Serializable;
@Repository
public interface PermissionRepository extends CrudRepository<Permission, Integer> {
	@Query("select c from Role c where c.name = ?1")
	Permission findByName(String name);
	
	
}
