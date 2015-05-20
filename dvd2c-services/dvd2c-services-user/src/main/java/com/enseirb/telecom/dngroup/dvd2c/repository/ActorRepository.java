package com.enseirb.telecom.dngroup.dvd2c.repository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.enseirb.telecom.dngroup.dvd2c.modeldb.Actor;

//import java.io.Serializable;
@Repository
public interface ActorRepository extends CrudRepository<Actor, UUID> {
	
	
}
