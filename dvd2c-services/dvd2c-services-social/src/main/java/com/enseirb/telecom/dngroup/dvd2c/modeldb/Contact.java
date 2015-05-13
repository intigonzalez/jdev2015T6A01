package com.enseirb.telecom.dngroup.dvd2c.modeldb;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the contacts database table.
 * 
 */
@Entity
@Table(name="contacts")
@NamedQuery(name="Contact.findAll", query="SELECT c FROM Contact c")
public class Contact extends DBObject implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Integer id;

	@Column(name="inverse_id")
	private int inverseId;

	@Column(name="receiver_id")
	private int receiverId;

	@Column(name="sender_id")
	private int senderId;

	@Column(name="ties_count")
	private int tiesCount;

	//bi-directional many-to-many association to Relation
	@ManyToMany(mappedBy="contacts")
	private List<Relation> relations;

	public Contact() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getInverseId() {
		return this.inverseId;
	}

	public void setInverseId(int inverseId) {
		this.inverseId = inverseId;
	}

	public int getReceiverId() {
		return this.receiverId;
	}

	public void setReceiverId(int receiverId) {
		this.receiverId = receiverId;
	}

	public int getSenderId() {
		return this.senderId;
	}

	public void setSenderId(int senderId) {
		this.senderId = senderId;
	}

	public int getTiesCount() {
		return this.tiesCount;
	}

	public void setTiesCount(int tiesCount) {
		this.tiesCount = tiesCount;
	}

	public List<Relation> getRelations() {
		return this.relations;
	}

	public void setRelations(List<Relation> relations) {
		this.relations = relations;
	}

}