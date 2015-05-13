package com.enseirb.telecom.dngroup.dvd2c.db;



import com.enseirb.telecom.dngroup.dvd2c.db.mock.CrudRepositoryMock;


public class RelationshipRepositoryOldImplInMemory extends
		CrudRepositoryMock<RelationshipRepositoryOldObject> implements
		RelationshipRepositoryOld {

	@Override
	protected String getID(RelationshipRepositoryOldObject t) {
		return t.iD;
	}

//	@Override
//	public RelationshipRepositoryObject findOne(String userId,
//			String relationActorID) {
//		return findOne(userId + relationActorID);
//	}
//
//	@Override
//	public boolean exists(String userId, String relationActorID) {
//		return findOne(userId, relationActorID) != null;
//	}
//
//	@Override
//	public void delete(String userId, String relationActorID) {
//		delete(userId + relationActorID);
//
//	}

}
