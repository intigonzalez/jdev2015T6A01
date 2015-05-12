package com.enseirb.telecom.dngroup.dvd2c.modeldb;

import java.io.Serializable;

import javax.persistence.*;

import java.util.Date;
import java.util.List;

/**
 * The persistent class for the groups database table.
 * 
 */
@Entity
@Table(name = "groups")
@NamedQuery(name = "Group.findAll", query = "SELECT g FROM Group g")
public class Group extends Actor implements Serializable {
	private static final long serialVersionUID = 1L;

	@ManyToMany
	@JoinTable(name = "ACTOR_GROUPS", joinColumns = { @JoinColumn(name = "ACTOR_ID", referencedColumnName = "ID") }, inverseJoinColumns = { @JoinColumn(name = "GROUP_ID", referencedColumnName = "ID") })
	private List<Actor> actors;

	public List<Actor> getActors() {
		return actors;
	}

	public void setActors(List<Actor> actors) {
		this.actors = actors;
	}

	public Group() {
	}

}