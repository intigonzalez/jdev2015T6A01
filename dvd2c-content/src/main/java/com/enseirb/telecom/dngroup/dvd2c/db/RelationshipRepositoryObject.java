package com.enseirb.telecom.dngroup.dvd2c.db;

import java.util.List;
import com.enseirb.telecom.dngroup.dvd2c.model.Relation;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class RelationshipRepositoryObject {
	public RelationshipRepositoryObject(){
		
	}
	public RelationshipRepositoryObject(String userId, String email, String name, String surname, String pubKey, Integer aprouve, Long unixTime, List<Integer> group) {
		super();
		this.userId = userId;
		this.email = email;
		this.name = name;
		this.surname = surname;
		this.pubKey = pubKey;
		this.aprouve = aprouve;
		this.unixTime = unixTime;
		this.group = group;
	}
	public RelationshipRepositoryObject(String userId, Relation relation) {
		this.userId = userId;
		this.email = relation.getEmail();
		this.name = relation.getName();
		this.surname = relation.getSurname();
		this.pubKey = relation.getPubKey();
		this.aprouve = relation.getAprouve();
		this.unixTime = relation.getUnixTime();
		this.group = relation.getGroupID();
		
	}

	
	String userId;
	String email;
    String name;
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
		relation.setEmail(email);
		relation.setName(name);
		relation.setSurname(surname);
		relation.setPubKey(pubKey);
		relation.setUnixTime(unixTime);
		relation.setAprouve(aprouve);
		relation.getGroupID().addAll(group);
		return relation;
		
	}
}
