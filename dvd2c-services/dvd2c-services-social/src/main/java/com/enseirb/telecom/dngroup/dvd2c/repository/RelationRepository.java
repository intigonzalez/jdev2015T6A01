package com.enseirb.telecom.dngroup.dvd2c.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.enseirb.telecom.dngroup.dvd2c.modeldb.Relation;

//import java.io.Serializable;
@Repository
public interface RelationRepository extends CrudRepository<Relation, Integer> {

	@Query("select c from Relation c where c.name = ?1 and c.actorId = ?2")
	Relation findByName(String senderId, String actor);
}
