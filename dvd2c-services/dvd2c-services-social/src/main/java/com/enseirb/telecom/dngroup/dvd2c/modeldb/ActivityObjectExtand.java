package com.enseirb.telecom.dngroup.dvd2c.modeldb;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;




/**
 * The persistent class for the activity_object_audiences database table.
 * 
 */
@Entity
@Table(name="activity_object_extand")
@NamedQuery(name="ActivityObjectExtand.findAll", query="SELECT a FROM ActivityObjectExtand a")
public class ActivityObjectExtand implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Integer id;


//	ActivityObjectAudience
	//bi-directional many-to-many association to Relation
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "activity_object_audiences", joinColumns = @JoinColumn(name = "activityObjectExtand_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))	
	private List<Role> roles;

	public ActivityObjectExtand() {
	}

	public Integer getId() {
		return this.id;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
//	@Column(name="activity_object_id")
//	private Integer activityObjectId;
//	public Integer getActivityObjectId() {
//		return this.activityObjectId;
//	}
//
//	public void setActivityObjectId(Integer activityObjectId) {
//		this.activityObjectId = activityObjectId;
//	}



}