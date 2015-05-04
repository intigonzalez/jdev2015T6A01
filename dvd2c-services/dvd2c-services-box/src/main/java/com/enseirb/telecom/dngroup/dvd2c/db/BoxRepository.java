package com.enseirb.telecom.dngroup.dvd2c.db;

import org.springframework.stereotype.Repository;

@Repository
public interface BoxRepository
		extends
		org.springframework.data.repository.CrudRepository<BoxRepositoryObject, String> {

}
