package com.enseirb.telecom.dngroup.dvd2c.modeldb;

import java.io.Serializable;

import javax.persistence.*;



/**
 * The persistent class for the activity_object_audiences database table.
 * 
 */
@Entity
@Table(name="activity_object_audiences")
@NamedQuery(name="ActivityObjectAudience.findAll", query="SELECT a FROM ActivityObjectAudience a")
public class ActivityObjectAudience implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Integer id;

	@Column(name="activity_object_id")
	private Integer activityObjectId;

	//bi-directional many-to-one association to Relation
	@ManyToOne
	private Role role;

	public ActivityObjectAudience() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getActivityObjectId() {
		return this.activityObjectId;
	}

	public void setActivityObjectId(Integer activityObjectId) {
		this.activityObjectId = activityObjectId;
	}

	public Role getRole() {
		return this.role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

}