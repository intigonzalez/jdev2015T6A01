package com.enseirb.telecom.dngroup.dvd2c.db;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.enseirb.telecom.dngroup.dvd2c.model.Box;
import com.enseirb.telecom.dngroup.dvd2c.model.User;

@Document(collection="BoxRepositoryObject")
public class BoxRepositoryObject {

	@Id
	String boxID;


	String ip;

	String pubKey;

	String privateKey;

	Integer TTL;

	public BoxRepositoryObject() {

	}

	public BoxRepositoryObject(ObjectId _id, String boxID, String ip,
			String pubKey, String privateKey, Integer tTL, List<User> user) {
		super();
		// this._id = _id;
		this.boxID = boxID;
		this.ip = ip;
		this.pubKey = pubKey;
		this.privateKey = privateKey;
		this.TTL = tTL;

	}

	public BoxRepositoryObject(String boxID, String ip, String pubKey,
			String privateKey, Integer tTL, List<User> user) {
		super();
		// this._id = _id;
		this.boxID = boxID;
		this.ip = ip;
		this.pubKey = pubKey;
		this.privateKey = privateKey;
		this.TTL = tTL;
	}

	public BoxRepositoryObject(Box box) {
		this.boxID = box.getBoxID();
		this.ip = box.getIp();
		this.pubKey = box.getPubKey();
		this.privateKey = box.getPrivateKey();
		this.TTL = box.getTTL();

	}

	public Box toBox() {
		Box box = new Box();
		box.setBoxID(boxID);
		box.setIp(ip);
		box.setPubKey(pubKey);
		box.setPrivateKey(privateKey);
		box.setTTL(TTL);
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

}
