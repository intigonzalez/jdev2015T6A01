package com.enseirb.telecom.dngroup.dvd2c.modeldb;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Type;


/**
 * The persistent class for the relations database table.
 * 
 */
@Entity
@Table(name="role")
@NamedQuery(name="Role.findAll", query="SELECT r FROM Role r")
public class Role extends DBObject implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column(name="actor_id")
	@Type(type="uuid-char")
	private UUID actorId;

	private String ancestry;

	private String name;

	@Column(name="receiver_type")
	private String receiverType;

	@Column(name="sender_type")
	private String senderType;

	private String type;

	//bi-directional many-to-many association to Contact
	@ManyToMany
	@Column(name = "role_contacts")
	private List<Contact> contacts;

	//bi-directional many-to-many association to Permission
	@ManyToMany(mappedBy="relations")
	private List<Permission> permissions;
	
	@OneToMany(mappedBy="role")
	private List<ActivityObjectAudience> activityObjectAudiences;

	public Role() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public UUID getActorId() {
		return this.actorId;
	}

	public void setActorId(UUID actorId) {
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