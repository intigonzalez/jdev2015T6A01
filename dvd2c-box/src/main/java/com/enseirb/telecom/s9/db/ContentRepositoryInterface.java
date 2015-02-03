package com.enseirb.telecom.s9.db;

import java.util.List;

public interface ContentRepositoryInterface extends CrudRepository<ContentRepositoryObject, String>{

	public abstract List<ContentRepositoryObject> findAllFromUser(String userID);
}
