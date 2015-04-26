package com.enseirb.telecom.dngroup.dvd2c.model.generated;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the activity_object_properties database table.
 * 
 */
@Entity
@Table(name="activity_object_properties")
@NamedQuery(name="ActivityObjectProperty.findAll", query="SELECT a FROM ActivityObjectProperty a")
public class ActivityObjectProperty implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	private byte main;

	private String type;

	//bi-directional many-to-one association to ActivityObject
	@ManyToOne
	@JoinColumn(name="activity_object_id")
	private ActivityObject activityObject1;

	//bi-directional many-to-one association to ActivityObject
	@ManyToOne
	@JoinColumn(name="property_id")
	private ActivityObject activityObject2;

	public ActivityObjectProperty() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public byte getMain() {
		return this.main;
	}

	public void setMain(byte main) {
		this.main = main;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public ActivityObject getActivityObject1() {
		return this.activityObject1;
	}

	public void setActivityObject1(ActivityObject activityObject1) {
		this.activityObject1 = activityObject1;
	}

	public ActivityObject getActivityObject2() {
		return this.activityObject2;
	}

	public void setActivityObject2(ActivityObject activityObject2) {
		this.activityObject2 = activityObject2;
	}

}