package com.enseirb.telecom.dngroup.dvd2c.model.generated;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the activity_actions database table.
 * 
 */
@Entity
@Table(name="activity_actions")
@NamedQuery(name="ActivityAction.findAll", query="SELECT a FROM ActivityAction a")
public class ActivityAction implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	private byte author;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="created_at")
	private Date createdAt;

	private byte follow;

	private byte owner;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="updated_at")
	private Date updatedAt;

	@Column(name="user_author")
	private byte userAuthor;

	//bi-directional many-to-one association to ActivityObject
	@ManyToOne
	@JoinColumn(name="activity_object_id")
	private ActivityObject activityObject;

	//bi-directional many-to-one association to Actor
	@ManyToOne
	private Actor actor;

	public ActivityAction() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public byte getAuthor() {
		return this.author;
	}

	public void setAuthor(byte author) {
		this.author = author;
	}

	public Date getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public byte getFollow() {
		return this.follow;
	}

	public void setFollow(byte follow) {
		this.follow = follow;
	}

	public byte getOwner() {
		return this.owner;
	}

	public void setOwner(byte owner) {
		this.owner = owner;
	}

	public Date getUpdatedAt() {
		return this.updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public byte getUserAuthor() {
		return this.userAuthor;
	}

	public void setUserAuthor(byte userAuthor) {
		this.userAuthor = userAuthor;
	}

	public ActivityObject getActivityObject() {
		return this.activityObject;
	}

	public void setActivityObject(ActivityObject activityObject) {
		this.activityObject = activityObject;
	}

	public Actor getActor() {
		return this.actor;
	}

	public void setActor(Actor actor) {
		this.actor = actor;
	}

}