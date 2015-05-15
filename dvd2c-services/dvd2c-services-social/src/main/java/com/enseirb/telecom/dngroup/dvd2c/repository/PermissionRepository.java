package com.enseirb.telecom.dngroup.dvd2c.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.enseirb.telecom.dngroup.dvd2c.modeldb.Permission;

//import java.io.Serializable;
@Repository
public interface PermissionRepository extends CrudRepository<Permission, Integer> {
	
	
}
