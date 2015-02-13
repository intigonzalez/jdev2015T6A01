package com.enseirb.telecom.dngroup.dvd2c.db;

public interface RelationshipRepository extends CrudRepository<RelationshipRepositoryObject, String> {
	
	public RelationshipRepositoryObject findOne(String userId, String relationEmail);
	
	public boolean exists(String userId, String relationEmail);
	
	public void delete(String userId, String relationEmail);
}
