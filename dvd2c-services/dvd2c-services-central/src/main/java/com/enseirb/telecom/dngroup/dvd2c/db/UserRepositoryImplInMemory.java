//package com.enseirb.telecom.dngroup.dvd2c.db;
//
//import java.util.List;
//import java.util.UUID;
//
//import javax.inject.Inject;
//
//import org.springframework.stereotype.Service;
//
//import com.enseirb.telecom.dngroup.dvd2c.db.mock.CrudRepositoryMock;
//import com.enseirb.telecom.dngroup.dvd2c.repository.UserRepositoryObject;
//
//
//public class UserRepositoryImplInMemory extends
//		CrudRepositoryMock<UserRepositoryObject> implements UserRepository {
//
//	@Inject
//	BoxRepository boxrepo;
//
//	
//
//	@Override
//	protected UUID getID(UserRepositoryObject t) {
//		return t.getUuid();
//	}
//
//
//
//	@Override
//	public List<UserRepositoryObject> findByfirstname(String firstname) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//
//
//	@Override
//	public UserRepositoryObject findByUuid(String uuid) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//
//
//	@Override
//	public UserRepositoryObject findByEmail(String userEmail) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//}
