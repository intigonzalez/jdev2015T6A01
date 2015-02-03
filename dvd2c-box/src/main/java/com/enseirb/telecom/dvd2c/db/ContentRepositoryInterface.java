package com.enseirb.telecom.dvd2c.db;

import java.util.List;

public interface ContentRepositoryInterface extends CrudRepository<ContentRepositoryObject, String>{

	public abstract List<ContentRepositoryObject> findAllFromUser(String userID);
}
