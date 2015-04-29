package main.java.model;

import java.io.Serializable;
import java.lang.String;

import javax.persistence.*;

import model.ActivityObjects;

/**
 * Entity implementation class for Entity: Actor
 *
 */
@Entity
public class Actor extends ActivityObjects implements Serializable {

	private String name;
	private static final long serialVersionUID = 1L;

	public Actor() {
		super();
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
