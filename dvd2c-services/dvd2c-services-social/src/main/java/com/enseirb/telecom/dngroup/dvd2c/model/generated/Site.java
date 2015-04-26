package com.enseirb.telecom.dngroup.dvd2c.model.generated;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the sites database table.
 * 
 */
@Entity
@Table(name="sites")
@NamedQuery(name="Site.findAll", query="SELECT s FROM Site s")
public class Site implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	@Lob
	private String config;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="created_at")
	private Date createdAt;

	private String type;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="updated_at")
	private Date updatedAt;

	//bi-directional many-to-one association to Oauth2Token
	@OneToMany(mappedBy="site")
	private List<Oauth2Token> oauth2Tokens;

	//bi-directional many-to-one association to Actor
	@ManyToOne
	private Actor actor;

	public Site() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getConfig() {
		return this.config;
	}

	public void setConfig(String config) {
		this.config = config;
	}

	public Date getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getUpdatedAt() {
		return this.updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public List<Oauth2Token> getOauth2Tokens() {
		return this.oauth2Tokens;
	}

	public void setOauth2Tokens(List<Oauth2Token> oauth2Tokens) {
		this.oauth2Tokens = oauth2Tokens;
	}

	public Oauth2Token addOauth2Token(Oauth2Token oauth2Token) {
		getOauth2Tokens().add(oauth2Token);
		oauth2Token.setSite(this);

		return oauth2Token;
	}

	public Oauth2Token removeOauth2Token(Oauth2Token oauth2Token) {
		getOauth2Tokens().remove(oauth2Token);
		oauth2Token.setSite(null);

		return oauth2Token;
	}

	public Actor getActor() {
		return this.actor;
	}

	public void setActor(Actor actor) {
		this.actor = actor;
	}

}