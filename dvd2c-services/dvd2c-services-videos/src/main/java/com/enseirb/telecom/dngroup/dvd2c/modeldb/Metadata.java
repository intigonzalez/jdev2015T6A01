package com.enseirb.telecom.dngroup.dvd2c.modeldb;

import java.io.Serializable;

import javax.persistence.*;

import java.util.List;


/**
 * The persistent class for the metadata database table.
 * 
 */
@Entity
@Table(name="metadata")
@NamedQuery(name="Metadata.findAll", query="SELECT m FROM Metadata m")
public class Metadata extends DBObject implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private String metadata;

	@ManyToMany
	@JoinTable(name = "ACTIVITYOBJECT_METADATA", joinColumns = { @JoinColumn(name = "ACTIVITYOBJECT_ID", referencedColumnName = "ID") }, inverseJoinColumns = { @JoinColumn(name = "ACTOR_ID", referencedColumnName = "ID") })
	private List<ActivityObject> activityObjects;

	public Metadata() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMetadata() {
		return this.metadata;
	}

	public void setMetadata(String metadata) {
		this.metadata = metadata;
	}
	
	public List<ActivityObject> getActivityObjects() {
		return this.activityObjects;
	}

	public void setActivityObjects(List<ActivityObject> activityObjects) {
		this.activityObjects = activityObjects;
	}


}