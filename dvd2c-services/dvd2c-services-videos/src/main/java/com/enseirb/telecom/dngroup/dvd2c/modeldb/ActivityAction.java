package com.enseirb.telecom.dngroup.dvd2c.modeldb;

import java.io.Serializable;

import javax.persistence.*;


/**
 * The persistent class for the activity_actions database table.
 * 
 */
@Entity
@Table(name="activity_actions")
@NamedQuery(name="ActivityAction.findAll", query="SELECT a FROM ActivityAction a")
public class ActivityAction extends DBObject implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column(name="actor_id")
	private int actorId;

	private byte author;

	private byte follow;

	private byte like;

	private byte owner;

	@Column(name="user_author")
	private byte userAuthor;

	//bi-directional many-to-one association to ActivityObject
	@ManyToOne
	@JoinColumn(name="activity_object_id")
	private ActivityObject activityObject;

	public ActivityAction() {
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

	public byte getAuthor() {
		return this.author;
	}

	public void setAuthor(byte author) {
		this.author = author;
	}

	public byte getFollow() {
		return this.follow;
	}

	public void setFollow(byte follow) {
		this.follow = follow;
	}

	public byte getLike() {
		return this.like;
	}

	public void setLike(byte like) {
		this.like = like;
	}

	public byte getOwner() {
		return this.owner;
	}

	public void setOwner(byte owner) {
		this.owner = owner;
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

}