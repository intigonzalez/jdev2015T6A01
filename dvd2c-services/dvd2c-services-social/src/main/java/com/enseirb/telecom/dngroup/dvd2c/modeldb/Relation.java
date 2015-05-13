package com.enseirb.telecom.dngroup.dvd2c.modeldb;

import java.io.Serializable;

import javax.persistence.*;

import java.util.Date;
import java.util.List;


/**
 * The persistent class for the relations database table.
 * 
 */
@Entity
@Table(name="relations")
@NamedQuery(name="Relation.findAll", query="SELECT r FROM Relation r")
public class Relation extends DBObject implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Integer id;

	@Column(name="actor_id")
	private int actorId;

	private String ancestry;

	private String name;

	@Column(name="receiver_type")
	private String receiverType;

	@Column(name="sender_type")
	private String senderType;

	private String type;

	//bi-directional many-to-many association to Contact
	@ManyToMany
	@JoinColumn(name="id")
	private List<Contact> contacts;

	//bi-directional many-to-many association to Permission
	@ManyToMany(mappedBy="relations")
	private List<Permission> permissions;

	public Relation() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getActorId() {
		return this.actorId;
	}

	public void setActorId(int actorId) {
		this.actorId = actorId;
	}

	public String getAncestry() {
		return this.ancestry;
	}

	public void setAncestry(String ancestry) {
		this.ancestry = ancestry;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getReceiverType() {
		return this.receiverType;
	}

	public void setReceiverType(String receiverType) {
		this.receiverType = receiverType;
	}

	public String getSenderType() {
		return this.senderType;
	}

	public void setSenderType(String senderType) {
		this.senderType = senderType;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<Contact> getContacts() {
		return this.contacts;
	}

	public void setContacts(List<Contact> contacts) {
		this.contacts = contacts;
	}

	public List<Permission> getPermissions() {
		return this.permissions;
	}

	public void setPermissions(List<Permission> permissions) {
		this.permissions = permissions;
	}

}