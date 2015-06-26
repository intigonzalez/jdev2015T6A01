package com.enseirb.telecom.dngroup.dvd2c.modeldb;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Entity implementation class for Entity: DBObject
 *
 */

@MappedSuperclass
//@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class DBObject implements Serializable {

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_at")
	private Date createdAt;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_at")
	private Date updatedAt;

	@PrePersist
	public void createdAt() {
		this.createdAt = this.updatedAt = new Date();
	}

	@PreUpdate
	public void updatedAt() {
		this.updatedAt = new Date();
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

}
