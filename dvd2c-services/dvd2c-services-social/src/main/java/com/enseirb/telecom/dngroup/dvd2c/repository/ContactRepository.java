package com.enseirb.telecom.dngroup.dvd2c.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.enseirb.telecom.dngroup.dvd2c.modeldb.Contact;

//import java.io.Serializable;
@Repository
public interface ContactRepository extends CrudRepository<Contact, Integer> {

	@Query("select c from Contact c where c.ownerId = ?1 and c.receiverId = ?1")
	Contact findContact(String senderId, String reciverId);
	
	@Query("select c from Contact c where c.ownerId = ?1")
	List<Contact> findByOwner(String senderId);
}
