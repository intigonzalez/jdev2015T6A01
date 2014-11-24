package com.enseirb.telecom.s9.db;

import java.util.List;

import com.enseirb.telecom.s9.Friend;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class RelationshipRepositoryObject {
	public RelationshipRepositoryObject(){
		
	}
	public RelationshipRepositoryObject(String userId, String email, String name, String surname, String pubKey, Boolean aprouve, Long unixTime, List<Integer> group) {
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
	public RelationshipRepositoryObject(String userId, Friend friend) {
		this.userId = userId;
		this.email = friend.getEmail();
		this.name = friend.getName();
		this.surname = friend.getSurname();
		this.pubKey = friend.getPubKey();
		this.aprouve = friend.isAprouve();
		this.unixTime = friend.getUnixTime();
		this.group = friend.getGroupID();
		
	}
	String userId;
	String email;
    String name;
    String surname;
    String pubKey;
    Boolean aprouve;
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
	public Boolean getAprouve() {
		return aprouve;
	}
	public void setAprouve(Boolean aprouve) {
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
	public void toFriend() {
		Friend friend = new Friend();
		friend.setEmail(email);
		friend.setName(name);
		friend.setSurname(surname);
		friend.setPubKey(pubKey);
		friend.setUnixTime(unixTime);
		friend.setAprouve(aprouve);
		friend.getGroupID().addAll(group);
		
	}
}
