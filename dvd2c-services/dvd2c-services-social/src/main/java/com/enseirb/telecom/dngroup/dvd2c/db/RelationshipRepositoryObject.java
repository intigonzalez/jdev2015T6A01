package com.enseirb.telecom.dngroup.dvd2c.db;

import java.util.List;

import com.enseirb.telecom.dngroup.dvd2c.model.Relation;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class RelationshipRepositoryObject {
	public RelationshipRepositoryObject(){
		
	}
	public RelationshipRepositoryObject(String userId, String userIDOfRelation, String firstname, String surname, String pubKey, Integer aprouve, Long unixTime, List<Integer> group) {
		super();
		this.userId = userId;
		this.userIDOfRelation = userIDOfRelation;
		this.firstname = firstname;
		this.surname = surname;
		this.pubKey = pubKey;
		this.aprouve = aprouve;
		this.unixTime = unixTime;
		this.group = group;
	}
	public RelationshipRepositoryObject(String userId, Relation relation) {
		this.userId = userId;
		this.userIDOfRelation = relation.getUserIDOfRelation();
		this.firstname = relation.getFirstname();
		this.surname = relation.getSurname();
		this.pubKey = relation.getPubKey();
		this.aprouve = relation.getAprouve();
		this.unixTime = relation.getUnixTime();
		this.group = relation.getGroupID();
		
	}

	
	String userId;
	String userIDOfRelation;
    String firstname;
    String surname;
    String pubKey;
    Integer aprouve;
    Long unixTime;
    List<Integer> group;
    
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserIDOfRelation() {
		return userIDOfRelation;
	}
	public void setUserIDOfRelation(String userIDOfRelation) {
		this.userIDOfRelation = userIDOfRelation;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public String getPubKey() {
		return pubKey;
	}
	public void setPubKey(String pubKey) {
		this.pubKey = pubKey;
	}
	public Integer getAprouve() {
		return aprouve;
	}
	public void setAprouve(Integer aprouve) {
		this.aprouve = aprouve;
	}
	public Long getUnixTime() {
		return unixTime;
	}
	public void setUnixTime(Long unixTime) {
		this.unixTime = unixTime;
	}
	public List<Integer> getGroup() {
		return group;
	}
	public void setGroup(List<Integer> group) {
		this.group = group;
	}
	public Relation toRelation() {
		Relation relation = new Relation();
		relation.setUserIDOfRelation(userIDOfRelation);
		relation.setFirstname(firstname);
		relation.setSurname(surname);
		relation.setPubKey(pubKey);
		relation.setUnixTime(unixTime);
		relation.setAprouve(aprouve);
		relation.getGroupID().addAll(group);
		return relation;
		
	}
}
