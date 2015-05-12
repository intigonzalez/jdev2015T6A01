package com.enseirb.telecom.dngroup.dvd2c.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.enseirb.telecom.dngroup.dvd2c.modeldb.Profile;
import com.enseirb.telecom.dngroup.dvd2c.modeldb.User;

//import java.io.Serializable;
@Repository
public interface ProfileRepository extends CrudRepository<Profile, String> {

}
