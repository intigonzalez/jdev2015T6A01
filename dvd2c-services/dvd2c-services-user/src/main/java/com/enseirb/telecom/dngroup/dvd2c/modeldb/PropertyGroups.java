package com.enseirb.telecom.dngroup.dvd2c.modeldb;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * The persistent class for the key value database table.
 * 
 */
@Entity
@Table(name = "property_groups")
@NamedQuery(name = "PropertyGroups.findAll", query = "SELECT p FROM PropertyGroups p")
public class PropertyGroups extends DBObject implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@OneToMany(cascade={CascadeType.ALL})
	@JoinColumn(name = "property")
	private List<Property> properties;

	private String name;

	@ManyToOne()
	@JoinColumn(name = "property_groups")
	private User user;

	public PropertyGroups() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}


	public String getValue() {
		return name;
	}

	public void setName(String value) {
		this.name = value;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}