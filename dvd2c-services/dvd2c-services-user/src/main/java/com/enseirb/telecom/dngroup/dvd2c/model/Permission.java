package com.enseirb.telecom.dngroup.dvd2c.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the permissions database table.
 * 
 */
@Entity
@Table(name="permissions")
@NamedQuery(name="Permission.findAll", query="SELECT p FROM Permission p")
public class Permission implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false)
	private int id;

	@Column(length=255)
	private String action;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="created_at")
	private Date createdAt;

	@Column(length=255)
	private String object;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="updated_at")
	private Date updatedAt;

	//bi-directional many-to-one association to RelationPermission
	@OneToMany(mappedBy="permission")
	private List<RelationPermission> relationPermissions;

	public Permission() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAction() {
		return this.action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Date getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getObject() {
		return this.object;
	}

	public void setObject(String object) {
		this.object = object;
	}

	public Date getUpdatedAt() {
		return this.updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public List<RelationPermission> getRelationPermissions() {
		return this.relationPermissions;
	}

	public void setRelationPermissions(List<RelationPermission> relationPermissions) {
		this.relationPermissions = relationPermissions;
	}

	public RelationPermission addRelationPermission(RelationPermission relationPermission) {
		getRelationPermissions().add(relationPermission);
		relationPermission.setPermission(this);

		return relationPermission;
	}

	public RelationPermission removeRelationPermission(RelationPermission relationPermission) {
		getRelationPermissions().remove(relationPermission);
		relationPermission.setPermission(null);

		return relationPermission;
	}

}