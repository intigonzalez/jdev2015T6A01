package com.enseirb.telecom.dngroup.dvd2c.exception;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.enseirb.telecom.dngroup.dvd2c.modeldb.Actor;
import com.enseirb.telecom.dngroup.dvd2c.modeldb.Relation;

//import java.io.Serializable;
@Repository
public interface RelationRepository extends CrudRepository<Relation, Integer> {
	
	
}
