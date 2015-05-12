package com.enseirb.telecom.dngroup.dvd2c.modeldb;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Entity implementation class for Entity: DBObject
 *
 */

@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class DBObject implements Serializable {

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_at")
	private Date createdAt;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_at")
	private Date updatedAt;

	public Date getCreatedAt() {
		return null;
	}

	public void setCreatedAt(Date createdAt) {

	}

	public Date getUpdatedAt() {
		return null;
	}

	public void setUpdatedAt(Date updatedAt) {

	}

}
