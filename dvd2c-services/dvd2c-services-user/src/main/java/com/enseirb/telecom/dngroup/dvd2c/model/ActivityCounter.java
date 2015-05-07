package com.enseirb.telecom.dngroup.dvd2c.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the activity_counter database table.
 * 
 */
@Entity
@Table(name="activity_counter")
@NamedQuery(name="ActivityCounter.findAll", query="SELECT a FROM ActivityCounter a")
public class ActivityCounter implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false)
	private int id;

	@Column(name="comment_count")
	private int commentCount;

	@Column(name="follower_count")
	private int followerCount;

	@Column(name="like_count")
	private int likeCount;

	@Column(name="visit_count")
	private int visitCount;

	//bi-directional many-to-one association to ActivityObject
	@ManyToOne
	@JoinColumn(name="activity_object_id")
	private ActivityObject activityObject;

	public ActivityCounter() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCommentCount() {
		return this.commentCount;
	}

	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}

	public int getFollowerCount() {
		return this.followerCount;
	}

	public void setFollowerCount(int followerCount) {
		this.followerCount = followerCount;
	}

	public int getLikeCount() {
		return this.likeCount;
	}

	public void setLikeCount(int likeCount) {
		this.likeCount = likeCount;
	}

	public int getVisitCount() {
		return this.visitCount;
	}

	public void setVisitCount(int visitCount) {
		this.visitCount = visitCount;
	}

	public ActivityObject getActivityObject() {
		return this.activityObject;
	}

	public void setActivityObject(ActivityObject activityObject) {
		this.activityObject = activityObject;
	}

}