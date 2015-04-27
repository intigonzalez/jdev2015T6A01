package com.enseirb.telecom.dngroup.dvd2c.model.generated;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the remote_subjects database table.
 * 
 */
@Entity
@Table(name="remote_subjects")
@NamedQuery(name="RemoteSubject.findAll", query="SELECT r FROM RemoteSubject r")
public class RemoteSubject implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="created_at")
	private Date createdAt;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="updated_at")
	private Date updatedAt;

	@Column(name="webfinger_id")
	private String webfingerId;

	@Lob
	@Column(name="webfinger_info")
	private String webfingerInfo;

	//bi-directional many-to-one association to Actor
	@ManyToOne
	private Actor actor;

	public RemoteSubject() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return this.updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getWebfingerId() {
		return this.webfingerId;
	}

	public void setWebfingerId(String webfingerId) {
		this.webfingerId = webfingerId;
	}

	public String getWebfingerInfo() {
		return this.webfingerInfo;
	}

	public void setWebfingerInfo(String webfingerInfo) {
		this.webfingerInfo = webfingerInfo;
	}

	public Actor getActor() {
		return this.actor;
	}

	public void setActor(Actor actor) {
		this.actor = actor;
	}

}