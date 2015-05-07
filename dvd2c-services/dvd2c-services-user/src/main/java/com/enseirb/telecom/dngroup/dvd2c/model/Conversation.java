package com.enseirb.telecom.dngroup.dvd2c.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the conversations database table.
 * 
 */
@Entity
@Table(name="conversations")
@NamedQuery(name="Conversation.findAll", query="SELECT c FROM Conversation c")
public class Conversation implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false)
	private int id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="created_at", nullable=false)
	private Date createdAt;

	@Column(length=255)
	private String subject;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="updated_at", nullable=false)
	private Date updatedAt;

	//bi-directional many-to-one association to Notification
	@OneToMany(mappedBy="conversation")
	private List<Notification> notifications;

	public Conversation() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getSubject() {
		return this.subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Date getUpdatedAt() {
		return this.updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public List<Notification> getNotifications() {
		return this.notifications;
	}

	public void setNotifications(List<Notification> notifications) {
		this.notifications = notifications;
	}

	public Notification addNotification(Notification notification) {
		getNotifications().add(notification);
		notification.setConversation(this);

		return notification;
	}

	public Notification removeNotification(Notification notification) {
		getNotifications().remove(notification);
		notification.setConversation(null);

		return notification;
	}

}