package com.enseirb.telecom.dngroup.dvd2c.model;

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
public class Metadata implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false)
	private int id;

	@Column(length=45)
	private String metadata;

	//bi-directional many-to-one association to ActivityMetadata
	@OneToMany(mappedBy="metadata")
	private List<ActivityMetadata> activityMetadata;

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

	public List<ActivityMetadata> getActivityMetadata() {
		return this.activityMetadata;
	}

	public void setActivityMetadata(List<ActivityMetadata> activityMetadata) {
		this.activityMetadata = activityMetadata;
	}

	public ActivityMetadata addActivityMetadata(ActivityMetadata activityMetadata) {
		getActivityMetadata().add(activityMetadata);
		activityMetadata.setMetadata(this);

		return activityMetadata;
	}

	public ActivityMetadata removeActivityMetadata(ActivityMetadata activityMetadata) {
		getActivityMetadata().remove(activityMetadata);
		activityMetadata.setMetadata(null);

		return activityMetadata;
	}

}