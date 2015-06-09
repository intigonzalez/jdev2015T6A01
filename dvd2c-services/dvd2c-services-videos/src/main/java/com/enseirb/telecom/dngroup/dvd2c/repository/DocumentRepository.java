package com.enseirb.telecom.dngroup.dvd2c.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.enseirb.telecom.dngroup.dvd2c.modeldb.Document;

@Repository
public interface DocumentRepository extends CrudRepository<Document, Integer> {
	
	@Query("select d from Document d where d.actorId = ?1")
	List<Document> findByActorUUID(UUID uuid);

}
