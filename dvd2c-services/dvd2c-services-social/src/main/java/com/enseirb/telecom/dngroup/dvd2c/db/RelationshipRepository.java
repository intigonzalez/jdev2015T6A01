package com.enseirb.telecom.dngroup.dvd2c.db;

public interface RelationshipRepository extends CrudRepository<RelationshipRepositoryObject, String> {
	
	/**
	 * Create a relation object for database with to parameter
	 * @param userId the main user
	 * @param relationUserIDOfRelation the relation
	 * @return Object for db
	 */
	public RelationshipRepositoryObject findOne(String userId, String relationUserIDOfRelation);
	
	/**
	 * verify if the relation exist in db
	 * @param userId the main user
	 * @param relationUserIDOfRelation the relation user
	 * @return
	 */
	public boolean exists(String userId, String relationUserIDOfRelation);
	
	/**
	 * delete a relation between this users
	 * @param userId the main user
	 * @param relationUserIDOfRelation the relation user
	 */
	public void delete(String userId, String relationUserIDOfRelation);
}
