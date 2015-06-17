package com.enseirb.telecom.dngroup.dvd2c.modeldb;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the key value database table.
 * 
 */
@Entity
@Table(name = "property")
@NamedQuery(name = "PropertyDB.findAll", query = "SELECT p FROM PropertyDB p")
public class PropertyDB extends DBObject implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private String keyName;

	@Column(length=511)
	private String value;

	@ManyToOne()
	@JoinColumn(name = "property")
	private PropertyGroupsDB propertyGroups;


	public PropertyGroupsDB getPropertyGroups() {
		return propertyGroups;
	}

	public void setPropertyGroups(PropertyGroupsDB propertyGroups) {
		this.propertyGroups = propertyGroups;
	}

	public PropertyDB() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getKey() {
		return keyName;
	}

	public void setKey(String key) {
		this.keyName = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}