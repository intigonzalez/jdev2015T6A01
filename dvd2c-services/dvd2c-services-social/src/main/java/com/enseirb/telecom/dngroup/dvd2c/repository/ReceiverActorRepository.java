package com.enseirb.telecom.dngroup.dvd2c.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.enseirb.telecom.dngroup.dvd2c.modeldb.ReceiverActor;
import com.enseirb.telecom.dngroup.dvd2c.modeldb.Relation;
import com.enseirb.telecom.dngroup.dvd2c.modeldb.User;

//import java.io.Serializable;
@Repository
public interface ReceiverActorRepository extends CrudRepository<ReceiverActor, Integer> {
	
	@Query("select u from ReceiverActor u where u.email = ?1")
	ReceiverActor findByEmail(String email);
}
