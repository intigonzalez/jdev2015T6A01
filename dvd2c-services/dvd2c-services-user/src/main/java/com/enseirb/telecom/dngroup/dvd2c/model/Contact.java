package com.enseirb.telecom.dngroup.dvd2c.model;

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
public class Contact implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false)
	private int id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="created_at")
	private Date createdAt;

	@Column(name="inverse_id")
	private int inverseId;

	@Column(name="ties_count")
	private int tiesCount;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="updated_at")
	private Date updatedAt;

	//bi-directional many-to-one association to Actor
	@ManyToOne
	@JoinColumn(name="sender_id")
	private Actor actor1;

	//bi-directional many-to-one association to Actor
	@ManyToOne
	@JoinColumn(name="receiver_id")
	private Actor actor2;

	//bi-directional many-to-one association to Ty
	@OneToMany(mappedBy="contact")
	private List<Ty> ties;

	public Contact() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public int getInverseId() {
		return this.inverseId;
	}

	public void setInverseId(int inverseId) {
		this.inverseId = inverseId;
	}

	public int getTiesCount() {
		return this.tiesCount;
	}

	public void setTiesCount(int tiesCount) {
		this.tiesCount = tiesCount;
	}

	public Date getUpdatedAt() {
		return this.updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Actor getActor1() {
		return this.actor1;
	}

	public void setActor1(Actor actor1) {
		this.actor1 = actor1;
	}

	public Actor getActor2() {
		return this.actor2;
	}

	public void setActor2(Actor actor2) {
		this.actor2 = actor2;
	}

	public List<Ty> getTies() {
		return this.ties;
	}

	public void setTies(List<Ty> ties) {
		this.ties = ties;
	}

	public Ty addTy(Ty ty) {
		getTies().add(ty);
		ty.setContact(this);

		return ty;
	}

	public Ty removeTy(Ty ty) {
		getTies().remove(ty);
		ty.setContact(null);

		return ty;
	}

}