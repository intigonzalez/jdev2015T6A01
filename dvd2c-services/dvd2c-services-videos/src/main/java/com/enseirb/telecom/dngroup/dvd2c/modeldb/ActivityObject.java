package com.enseirb.telecom.dngroup.dvd2c.modeldb;

import java.io.Serializable;

import javax.persistence.*;

import java.util.List;


/**
 * The persistent class for the activity_objects database table.
 * 
 */
@Entity
@Table(name="activity_objects")
@NamedQuery(name="ActivityObject.findAll", query="SELECT a FROM ActivityObject a")
@Inheritance(strategy=InheritanceType.JOINED)
public class ActivityObject extends DBObject implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Lob
	private String description;

	@Column(name="object_type")
	private String objectType;

	private String title;

	//bi-directional many-to-one association to ActivityAction
	@OneToMany(mappedBy="activityObject")
	private List<ActivityAction> activityActions;

	//bi-directional many-to-one association to ActivityObjectProperty
	@OneToMany(mappedBy="activityObject1")
	private List<ActivityObjectProperty> activityObjectProperties1;

	//bi-directional many-to-one association to ActivityObjectProperty
	@OneToMany(mappedBy="activityObject2")
	private List<ActivityObjectProperty> activityObjectProperties2;
	
	public ActivityObject() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getObjectType() {
		return this.objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<ActivityAction> getActivityActions() {
		return this.activityActions;
	}

	public void setActivityActions(List<ActivityAction> activityActions) {
		this.activityActions = activityActions;
	}

	public ActivityAction addActivityAction(ActivityAction activityAction) {
		getActivityActions().add(activityAction);
		activityAction.setActivityObject(this);

		return activityAction;
	}

	public ActivityAction removeActivityAction(ActivityAction activityAction) {
		getActivityActions().remove(activityAction);
		activityAction.setActivityObject(null);

		return activityAction;
	}

	public List<ActivityObjectProperty> getActivityObjectProperties1() {
		return this.activityObjectProperties1;
	}

	public void setActivityObjectProperties1(List<ActivityObjectProperty> activityObjectProperties1) {
		this.activityObjectProperties1 = activityObjectProperties1;
	}

	public ActivityObjectProperty addActivityObjectProperties1(ActivityObjectProperty activityObjectProperties1) {
		getActivityObjectProperties1().add(activityObjectProperties1);
		activityObjectProperties1.setActivityObject1(this);

		return activityObjectProperties1;
	}

	public ActivityObjectProperty removeActivityObjectProperties1(ActivityObjectProperty activityObjectProperties1) {
		getActivityObjectProperties1().remove(activityObjectProperties1);
		activityObjectProperties1.setActivityObject1(null);

		return activityObjectProperties1;
	}

	public List<ActivityObjectProperty> getActivityObjectProperties2() {
		return this.activityObjectProperties2;
	}

	public void setActivityObjectProperties2(List<ActivityObjectProperty> activityObjectProperties2) {
		this.activityObjectProperties2 = activityObjectProperties2;
	}

	public ActivityObjectProperty addActivityObjectProperties2(ActivityObjectProperty activityObjectProperties2) {
		getActivityObjectProperties2().add(activityObjectProperties2);
		activityObjectProperties2.setActivityObject2(this);

		return activityObjectProperties2;
	}

	public ActivityObjectProperty removeActivityObjectProperties2(ActivityObjectProperty activityObjectProperties2) {
		getActivityObjectProperties2().remove(activityObjectProperties2);
		activityObjectProperties2.setActivityObject2(null);

		return activityObjectProperties2;
	}
	
}