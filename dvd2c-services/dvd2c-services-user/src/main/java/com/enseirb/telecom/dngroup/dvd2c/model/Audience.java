package com.enseirb.telecom.dngroup.dvd2c.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the audiences database table.
 * 
 */
@Entity
@Table(name="audiences")
@NamedQuery(name="Audience.findAll", query="SELECT a FROM Audience a")
public class Audience implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false)
	private int id;

	//bi-directional many-to-one association to Relation
	@ManyToOne
	@JoinColumn(name="relation_id")
	private Relation relation;

	//bi-directional many-to-one association to Activity
	@ManyToOne
	@JoinColumn(name="activity_id")
	private Activity activity;

	public Audience() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Relation getRelation() {
		return this.relation;
	}

	public void setRelation(Relation relation) {
		this.relation = relation;
	}

	public Activity getActivity() {
		return this.activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

}