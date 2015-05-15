package com.enseirb.telecom.dngroup.dvd2c.modeldb;

import java.io.Serializable;

import javax.persistence.*;

import model.Actor;

import java.util.List;

/**
 * The persistent class for the contacts database table.
 * 
 */
@Entity
@Table(name = "contacts")
@NamedQuery(name = "Contact.findAll", query = "SELECT c FROM Contact c")
public class Contact extends DBObject implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Integer id;

//	@Column(name = "inverse_id")
//	private int inverseId;

	@ManyToOne
	@JoinColumn(name = "receiver_id")
	private ReceiverActor receiverActor;

	@Column(name = "owner_id")
	private String ownerId;

	private int status;

//	@Column(name = "ties_count")
//	private int tiesCount;

	// bi-directional many-to-many association to Relation
	@ManyToMany(mappedBy = "contacts")
	private List<Relation> relations;

	public Contact() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

//	public int getInverseId() {
//		return this.inverseId;
//	}
//
//	public void setInverseId(int inverseId) {
//		this.inverseId = inverseId;
//	}
//	
//	public int getTiesCount() {
//		return this.tiesCount;
//	}
//
//	public void setTiesCount(int tiesCount) {
//		this.tiesCount = tiesCount;
//	}

	public List<Relation> getRelations() {
		return this.relations;
	}

	public void setRelations(List<Relation> relations) {
		this.relations = relations;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public ReceiverActor getReceiverActor() {
		return receiverActor;
	}

	public void setReceiverActor(ReceiverActor receiverActor) {
		this.receiverActor = receiverActor;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

}