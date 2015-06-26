package com.enseirb.telecom.dngroup.dvd2c.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.enseirb.telecom.dngroup.dvd2c.modeldb.ActivityObjectProperty;

@Repository
public interface ActivityObjectPropertyRepository extends CrudRepository<ActivityObjectProperty, Integer> {

}
