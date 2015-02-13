package com.enseirb.telecom.dngroup.dvd2c.db;

import java.util.List;

public interface ContentRepository extends CrudRepository<ContentRepositoryObject, String>{

	public abstract List<ContentRepositoryObject> findAllFromUser(String userID);
}
