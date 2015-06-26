package com.enseirb.telecom.dngroup.dvd2c.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.enseirb.telecom.dngroup.dvd2c.modeldb.Document;
import com.enseirb.telecom.dngroup.dvd2c.modeldb.DocumentAlternative;

public interface DocumentAlternativeRepository extends
		CrudRepository<DocumentAlternative, Long> {

	List<DocumentAlternative> findByDocument(Document document);

}
