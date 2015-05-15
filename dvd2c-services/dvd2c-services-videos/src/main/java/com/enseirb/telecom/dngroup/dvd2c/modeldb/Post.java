package com.enseirb.telecom.dngroup.dvd2c.modeldb;

import java.io.Serializable;

import javax.persistence.*;


/**
 * The persistent class for the posts database table.
 * 
 */
@Entity
@Table(name="posts")
@NamedQuery(name="Post.findAll", query="SELECT p FROM Post p")
public class Post extends ActivityObject implements Serializable {
	private static final long serialVersionUID = 1L;

	public Post() {
	}

}