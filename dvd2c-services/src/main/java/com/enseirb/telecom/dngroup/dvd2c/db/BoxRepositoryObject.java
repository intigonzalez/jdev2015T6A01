package com.enseirb.telecom.dngroup.dvd2c.db;

import java.util.List;

import org.bson.types.ObjectId;

import com.enseirb.telecom.dngroup.dvd2c.model.Box;
import com.enseirb.telecom.dngroup.dvd2c.model.User;


public class BoxRepositoryObject {

	
	String boxID;
	String ip;
	String pubKey;
	String privateKey;
	Integer TTL;
	List<User> user;

	public BoxRepositoryObject() {

	}

	public BoxRepositoryObject(ObjectId _id, String boxID, String ip, String pubKey, String privateKey, Integer tTL, List<User> user) {
		super();
//		this._id = _id;
		this.boxID = boxID;
		this.ip = ip;
		this.pubKey = pubKey;
		this.privateKey = privateKey;
		this.TTL = tTL;
		this.user = user;
	}

	public BoxRepositoryObject(Box box) {
		this.boxID = box.getBoxID();
		this.ip = box.getIp();
		this.pubKey = box.getPubKey();
		this.privateKey = box.getPrivateKey();
		this.TTL = box.getTTL();
		this.user = box.getUser();

	}

	public Box toBox() {
		Box box = new Box();
		box.setBoxID(boxID);
		box.setIp(ip);
		box.setPubKey(pubKey);
		box.setPrivateKey(privateKey);
		box.setTTL(TTL);
		box.getUser().addAll(user);
		return box;
	}

	public String getBoxID() {
		return boxID;
	}

	public void setBoxID(String boxID) {
		this.boxID = boxID;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPubKey() {
		return pubKey;
	}

	public String getPrivateKey() {
		return privateKey;
	}
	
	public void setPubKey(String pubKey) {
		this.pubKey = pubKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}
	
	
	public Integer getTTL() {
		return TTL;
	}

	public void setTTL(Integer tTL) {
		TTL = tTL;
	}

	public List<User> getUser() {
		return user;
	}

	public void setUser(List<User> user) {
		this.user = user;
	}
}
