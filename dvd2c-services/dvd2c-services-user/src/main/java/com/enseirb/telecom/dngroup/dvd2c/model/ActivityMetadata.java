package com.enseirb.telecom.dngroup.dvd2c.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the activity_metadata database table.
 * 
 */
@Entity
@Table(name="activity_metadata")
@NamedQuery(name="ActivityMetadata.findAll", query="SELECT a FROM ActivityMetadata a")
public class ActivityMetadata implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false)
	private int id;

	//bi-directional many-to-one association to ActivityObject
	@ManyToOne
	@JoinColumn(name="activity_object_id")
	private ActivityObject activityObject;

	//bi-directional many-to-one association to Metadata
	@ManyToOne
	@JoinColumn(name="metadata_id")
	private Metadata metadata;

	public ActivityMetadata() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ActivityObject getActivityObject() {
		return this.activityObject;
	}

	public void setActivityObject(ActivityObject activityObject) {
		this.activityObject = activityObject;
	}

	public Metadata getMetadata() {
		return this.metadata;
	}

	public void setMetadata(Metadata metadata) {
		this.metadata = metadata;
	}

}