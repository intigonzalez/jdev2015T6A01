package com.enseirb.telecom.dngroup.dvd2c.exception;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.enseirb.telecom.dngroup.dvd2c.modeldb.Contact;

//import java.io.Serializable;
@Repository
public interface ContactRepository extends CrudRepository<Contact, Integer> {
	
	
}
