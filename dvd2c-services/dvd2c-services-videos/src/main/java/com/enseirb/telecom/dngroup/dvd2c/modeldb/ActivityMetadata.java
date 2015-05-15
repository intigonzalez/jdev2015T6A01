package com.enseirb.telecom.dngroup.dvd2c.modeldb;

import java.io.Serializable;

import javax.persistence.*;


/**
 * The persistent class for the activity_metadata database table.
 * 
 */
@Entity
@Table(name="activity_metadata")
@NamedQuery(name="ActivityMetadata.findAll", query="SELECT a FROM ActivityMetadata a")
public class ActivityMetadata extends ActivityObject implements Serializable {
	private static final long serialVersionUID = 1L;

	//bi-directional many-to-one association to Metadata
	@ManyToOne
	private Metadata metadata;

	public ActivityMetadata() {
	}

	public Metadata getMetadata() {
		return this.metadata;
	}

	public void setMetadata(Metadata metadata) {
		this.metadata = metadata;
	}

}