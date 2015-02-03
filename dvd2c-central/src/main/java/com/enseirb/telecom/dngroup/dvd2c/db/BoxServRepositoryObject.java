package com.enseirb.telecom.dngroup.dvd2c.db;

import java.util.List;

import org.bson.types.ObjectId;

import com.enseirb.telecom.dngroup.dvd2c.model.Box;
import com.enseirb.telecom.dngroup.dvd2c.model.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BoxServRepositoryObject {
	String boxID;
	String ip;
	String pubKey;
	Integer TTL;
	List<User> user;

	public BoxServRepositoryObject() {

	}

	public BoxServRepositoryObject(ObjectId _id, String boxID, String ip,
			String pubKey, Integer tTL, List<User> user) {
		super();
		// this._id = _id;
		this.boxID = boxID;
		this.ip = ip;
		this.pubKey = pubKey;
		this.TTL = tTL;
		this.user = user;
	}

	public BoxServRepositoryObject(Box box) {
		this.boxID = box.getBoxID();
		this.ip = box.getIp();
		this.pubKey = box.getPubKey();
		this.TTL = box.getTTL();
		this.user = box.getUser();

	}

	public Box toBox() {
		Box box = new Box();
		box.setBoxID(boxID);
		box.setIp(ip);
		box.setPubKey(pubKey);
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

	public void setPubKey(String pubKey) {
		this.pubKey = pubKey;
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

	// public ObjectId getId() {
	// return _id;
	// }
	//
	// public void setId(ObjectId _id) {
	// this._id = _id;
	// }

}
