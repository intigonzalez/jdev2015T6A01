package com.enseirb.telecom.dngroup.dvd2c.db;

public interface RelationshipRepository extends CrudRepository<RelationshipRepositoryObject, String> {
	
	/**
	 * Create a relation object for database with to parameter
	 * @param userId the main user
	 * @param relationEmail the relation
	 * @return Object for db
	 */
	public RelationshipRepositoryObject findOne(String userId, String relationEmail);
	
	/**
	 * verify if the relation exist in db
	 * @param userId the main user
	 * @param relationEmail the relation user
	 * @return
	 */
	public boolean exists(String userId, String relationEmail);
	
	/**
	 * delete a relation between this users
	 * @param userId the main user
	 * @param relationEmail the relation user
	 */
	public void delete(String userId, String relationEmail);
}
