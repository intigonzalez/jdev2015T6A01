package com.enseirb.telecom.dngroup.dvd2c.db;



import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

//import java.io.Serializable;
@Repository
public interface UserRepositoryOld
		extends
		CrudRepository<UserRepositoryOldObject, String> {
	


}
