package com.enseirb.telecom.dngroup.dvd2c.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the activities database table.
 * 
 */
@Entity
@Table(name="activities")
@NamedQuery(name="Activity.findAll", query="SELECT a FROM Activity a")
public class Activity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false)
	private int id;

	@Column(length=255)
	private String ancestry;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="created_at")
	private Date createdAt;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="updated_at")
	private Date updatedAt;

	//bi-directional many-to-one association to Actor
	@ManyToOne
	@JoinColumn(name="owner_id")
	private Actor actor1;

	//bi-directional many-to-one association to ActivityVerb
	@ManyToOne
	@JoinColumn(name="activity_verb_id")
	private ActivityVerb activityVerb;

	//bi-directional many-to-one association to Actor
	@ManyToOne
	@JoinColumn(name="author_id")
	private Actor actor2;

	//bi-directional many-to-one association to Actor
	@ManyToOne
	@JoinColumn(name="user_author_id")
	private Actor actor3;

	//bi-directional many-to-one association to ActivityObjectActivity
	@OneToMany(mappedBy="activity")
	private List<ActivityObjectActivity> activityObjectActivities;

	//bi-directional many-to-one association to Audience
	@OneToMany(mappedBy="activity")
	private List<Audience> audiences;

	public Activity() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAncestry() {
		return this.ancestry;
	}

	public void setAncestry(String ancestry) {
		this.ancestry = ancestry;
	}

	public Date getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
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

	public ActivityVerb getActivityVerb() {
		return this.activityVerb;
	}

	public void setActivityVerb(ActivityVerb activityVerb) {
		this.activityVerb = activityVerb;
	}

	public Actor getActor2() {
		return this.actor2;
	}

	public void setActor2(Actor actor2) {
		this.actor2 = actor2;
	}

	public Actor getActor3() {
		return this.actor3;
	}

	public void setActor3(Actor actor3) {
		this.actor3 = actor3;
	}

	public List<ActivityObjectActivity> getActivityObjectActivities() {
		return this.activityObjectActivities;
	}

	public void setActivityObjectActivities(List<ActivityObjectActivity> activityObjectActivities) {
		this.activityObjectActivities = activityObjectActivities;
	}

	public ActivityObjectActivity addActivityObjectActivity(ActivityObjectActivity activityObjectActivity) {
		getActivityObjectActivities().add(activityObjectActivity);
		activityObjectActivity.setActivity(this);

		return activityObjectActivity;
	}

	public ActivityObjectActivity removeActivityObjectActivity(ActivityObjectActivity activityObjectActivity) {
		getActivityObjectActivities().remove(activityObjectActivity);
		activityObjectActivity.setActivity(null);

		return activityObjectActivity;
	}

	public List<Audience> getAudiences() {
		return this.audiences;
	}

	public void setAudiences(List<Audience> audiences) {
		this.audiences = audiences;
	}

	public Audience addAudience(Audience audience) {
		getAudiences().add(audience);
		audience.setActivity(this);

		return audience;
	}

	public Audience removeAudience(Audience audience) {
		getAudiences().remove(audience);
		audience.setActivity(null);

		return audience;
	}

}