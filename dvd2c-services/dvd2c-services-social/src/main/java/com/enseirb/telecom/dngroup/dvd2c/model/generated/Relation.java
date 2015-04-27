package com.enseirb.telecom.dngroup.dvd2c.model.generated;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the relations database table.
 * 
 */
@Entity
@Table(name="relations")
@NamedQuery(name="Relation.findAll", query="SELECT r FROM Relation r")
public class Relation implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	private String ancestry;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="created_at")
	private Date createdAt;

	private String name;

	@Column(name="receiver_type")
	private String receiverType;

	@Column(name="sender_type")
	private String senderType;

	private String type;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="updated_at")
	private Date updatedAt;

	//bi-directional many-to-one association to ActivityObjectAudience
	@OneToMany(mappedBy="relation")
	private List<ActivityObjectAudience> activityObjectAudiences;

	//bi-directional many-to-one association to Audience
	@OneToMany(mappedBy="relation")
	private List<Audience> audiences;

	//bi-directional many-to-one association to RelationPermission
	@OneToMany(mappedBy="relation")
	private List<RelationPermission> relationPermissions;

	//bi-directional many-to-one association to Actor
	@ManyToOne
	private Actor actor;

	//bi-directional many-to-one association to Ty
	@OneToMany(mappedBy="relation")
	private List<Ty> ties;

	public Relation() {
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

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getReceiverType() {
		return this.receiverType;
	}

	public void setReceiverType(String receiverType) {
		this.receiverType = receiverType;
	}

	public String getSenderType() {
		return this.senderType;
	}

	public void setSenderType(String senderType) {
		this.senderType = senderType;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getUpdatedAt() {
		return this.updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public List<ActivityObjectAudience> getActivityObjectAudiences() {
		return this.activityObjectAudiences;
	}

	public void setActivityObjectAudiences(List<ActivityObjectAudience> activityObjectAudiences) {
		this.activityObjectAudiences = activityObjectAudiences;
	}

	public ActivityObjectAudience addActivityObjectAudience(ActivityObjectAudience activityObjectAudience) {
		getActivityObjectAudiences().add(activityObjectAudience);
		activityObjectAudience.setRelation(this);

		return activityObjectAudience;
	}

	public ActivityObjectAudience removeActivityObjectAudience(ActivityObjectAudience activityObjectAudience) {
		getActivityObjectAudiences().remove(activityObjectAudience);
		activityObjectAudience.setRelation(null);

		return activityObjectAudience;
	}

	public List<Audience> getAudiences() {
		return this.audiences;
	}

	public void setAudiences(List<Audience> audiences) {
		this.audiences = audiences;
	}

	public Audience addAudience(Audience audience) {
		getAudiences().add(audience);
		audience.setRelation(this);

		return audience;
	}

	public Audience removeAudience(Audience audience) {
		getAudiences().remove(audience);
		audience.setRelation(null);

		return audience;
	}

	public List<RelationPermission> getRelationPermissions() {
		return this.relationPermissions;
	}

	public void setRelationPermissions(List<RelationPermission> relationPermissions) {
		this.relationPermissions = relationPermissions;
	}

	public RelationPermission addRelationPermission(RelationPermission relationPermission) {
		getRelationPermissions().add(relationPermission);
		relationPermission.setRelation(this);

		return relationPermission;
	}

	public RelationPermission removeRelationPermission(RelationPermission relationPermission) {
		getRelationPermissions().remove(relationPermission);
		relationPermission.setRelation(null);

		return relationPermission;
	}

	public Actor getActor() {
		return this.actor;
	}

	public void setActor(Actor actor) {
		this.actor = actor;
	}

	public List<Ty> getTies() {
		return this.ties;
	}

	public void setTies(List<Ty> ties) {
		this.ties = ties;
	}

	public Ty addTy(Ty ty) {
		getTies().add(ty);
		ty.setRelation(this);

		return ty;
	}

	public Ty removeTy(Ty ty) {
		getTies().remove(ty);
		ty.setRelation(null);

		return ty;
	}

}