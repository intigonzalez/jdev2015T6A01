package com.enseirb.telecom.dngroup.dvd2c.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.enseirb.telecom.dngroup.dvd2c.modeldb.ActivityObjectExtand;
import com.enseirb.telecom.dngroup.dvd2c.modeldb.Role;


@Repository
public interface ActivityObjectAudienceRepository extends CrudRepository<ActivityObjectExtand, Integer> {
	
//	@Query( "select o from ActivityObjectExtand o where roles in :ids" )	
	Set<ActivityObjectExtand> findByRolesIn(@Param("ids") List<Role> roles);

	// @Query("select c from ActivityObjectAudience c where c.activityObjectId = ?1 ")
	// ActivityObjectAudience findActivityObjectAudience(Integer activityObjectId);

}