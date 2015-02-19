package com.enseirb.telecom.dngroup.dvd2c.db;

import java.util.List;

public interface ContentRepository extends CrudRepository<ContentRepositoryObject, String>{

	/**
	 * Get all content from a user
	 * @param userID the user to get content
	 * @return list of content
	 */
	public abstract List<ContentRepositoryObject> findAllFromUser(String userID);
}
