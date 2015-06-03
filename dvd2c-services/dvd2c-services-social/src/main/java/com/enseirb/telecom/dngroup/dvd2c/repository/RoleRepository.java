package com.enseirb.telecom.dngroup.dvd2c.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.enseirb.telecom.dngroup.dvd2c.modeldb.Role;

//import java.io.Serializable;
@Repository
public interface RoleRepository extends CrudRepository<Role, Integer> {

	@Query("select c from Role c where c.name = ?1 and c.actorId = ?2 and c.type = ?3")
	Role findByName(String name, UUID actorID, String type);

	@Query("select c from Role c where c.actorId = ?1 and c.type = ?2")
	List<Role> findByType(UUID actorID, String type);
}
