package com.enseirb.telecom.dngroup.dvd2c.modeldb;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
@NamedQuery(name = "PropertyGroupsDB.findAll", query = "SELECT p FROM PropertyGroupsDB p")
public class PropertyGroupsDB extends DBObject implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER, mappedBy = "propertyGroups")
	// @OneToMany(fetch=FetchType.EAGER)
	// @JoinColumn(name = "property_db")
	private List<PropertyDB> property;

	private String name;

	@ManyToOne()
	@JoinColumn(name = "property_groups")
	private User user;

	public PropertyGroupsDB() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
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

	public List<PropertyDB> getProperty() {
		return property;
	}

	public void setProperty(List<PropertyDB> properties) {
		this.property = properties;
	}
}