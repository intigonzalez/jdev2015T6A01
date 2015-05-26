package com.enseirb.telecom.dngroup.dvd2c.db;

import org.springframework.stereotype.Repository;

@Repository
public interface ContentRepositoryOld extends org.springframework.data.repository.CrudRepository<ContentRepositoryOldObject, String>{

//	/**
//	 * Get all content from a user
//	 * @param userID the user to get content
//	 * @return list of content
//	 */
//	public abstract List<ContentRepositoryOldObject> findAllFromUser(String userID);
}
