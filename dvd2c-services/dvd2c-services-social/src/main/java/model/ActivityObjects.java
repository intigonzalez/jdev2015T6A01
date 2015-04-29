package main.java.model;

import java.io.Serializable;
import java.lang.Integer;
import java.util.Date;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: ActivityObjects
 *
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class ActivityObjects implements Serializable {

	private Date createdAt;
	@Id
	protected Integer id;

	private static final long serialVersionUID = 1L;

	public ActivityObjects() {
		super();
	}

	public Date getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
