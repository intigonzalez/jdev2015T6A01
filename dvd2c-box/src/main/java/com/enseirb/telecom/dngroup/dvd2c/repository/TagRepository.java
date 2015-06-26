package com.enseirb.telecom.dngroup.dvd2c.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.enseirb.telecom.dngroup.dvd2c.modeldb.Tag;

@Repository
public interface TagRepository extends CrudRepository<Tag, Integer> {

}
