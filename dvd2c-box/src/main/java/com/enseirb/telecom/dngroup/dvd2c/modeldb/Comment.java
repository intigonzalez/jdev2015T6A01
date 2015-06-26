package com.enseirb.telecom.dngroup.dvd2c.modeldb;

import java.io.Serializable;

import javax.persistence.*;


/**
 * The persistent class for the comments database table.
 * 
 */
@Entity
@Table(name="comments")
@NamedQuery(name="Comment.findAll", query="SELECT c FROM Comment c")
public class Comment extends ActivityObject implements Serializable {
	private static final long serialVersionUID = 1L;

	public Comment() {
	}

}