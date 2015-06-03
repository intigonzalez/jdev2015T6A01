package com.enseirb.telecom.dngroup.dvd2c.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.enseirb.telecom.dngroup.dvd2c.modeldb.Contact;

//import java.io.Serializable;
@Repository
public interface ContactRepository extends CrudRepository<Contact, Integer> {

	@Query("select c from Contact c where c.ownerId = ?1 and c.receiverActor.id = ?2")
	Contact findContact(UUID senderId, UUID a);

	@Query("select c from Contact c where c.ownerId = ?1")
	List<Contact> findByOwner(UUID senderId);

	// @Query("select c from Contact c JOIN FETCH c.role where c.ownerId = ?1 and c.receiverActor.id = ?2")
	// Contact findContactAndFetchRoles(UUID senderId, UUID a);
	//
	// @Query("FROM Contact c JOIN FETCH c.role")
	// public List<Contact> getAllUsersAndFetchRoles();
}
