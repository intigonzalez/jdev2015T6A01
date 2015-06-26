package com.enseirb.telecom.dngroup.dvd2c.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.enseirb.telecom.dngroup.dvd2c.modeldb.Post;

@Repository
public interface PostRepository extends CrudRepository<Post, Integer> {

}
