package com.enseirb.telecom.dngroup.dvd2c.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the activity_objects database table.
 * 
 */
@Entity
@Table(name="activity_objects")
@NamedQuery(name="ActivityObject.findAll", query="SELECT a FROM ActivityObject a")
public class ActivityObject implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false)
	private int id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="created_at")
	private Date createdAt;

	@Lob
	private String description;

	@Column(name="object_type", length=45)
	private String objectType;

	@Column(length=255)
	private String title;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="updated_at")
	private Date updatedAt;

	//bi-directional many-to-one association to ActivityAction
	@OneToMany(mappedBy="activityObject")
	private List<ActivityAction> activityActions;

	//bi-directional many-to-one association to ActivityCounter
	@OneToMany(mappedBy="activityObject")
	private List<ActivityCounter> activityCounters;

	//bi-directional many-to-one association to ActivityMetadata
	@OneToMany(mappedBy="activityObject")
	private List<ActivityMetadata> activityMetadata;

	//bi-directional many-to-one association to ActivityObjectActivity
	@OneToMany(mappedBy="activityObject")
	private List<ActivityObjectActivity> activityObjectActivities;

	//bi-directional many-to-one association to ActivityObjectAudience
	@OneToMany(mappedBy="activityObject")
	private List<ActivityObjectAudience> activityObjectAudiences;

	//bi-directional many-to-one association to ActivityObjectProperty
	@OneToMany(mappedBy="activityObject1")
	private List<ActivityObjectProperty> activityObjectProperties1;

	//bi-directional many-to-one association to ActivityObjectProperty
	@OneToMany(mappedBy="activityObject2")
	private List<ActivityObjectProperty> activityObjectProperties2;

	//bi-directional many-to-one association to Actor
	@OneToMany(mappedBy="activityObject")
	private List<Actor> actors;

	//bi-directional many-to-one association to Comment
	@OneToMany(mappedBy="activityObject")
	private List<Comment> comments;

	//bi-directional many-to-one association to Document
	@OneToMany(mappedBy="activityObject")
	private List<Document> documents;

	//bi-directional many-to-one association to Event
	@OneToMany(mappedBy="activityObject")
	private List<Event> events;

	//bi-directional many-to-one association to Link
	@OneToMany(mappedBy="activityObject")
	private List<Link> links;

	//bi-directional many-to-one association to Post
	@OneToMany(mappedBy="activityObject")
	private List<Post> posts;

	public ActivityObject() {
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

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getObjectType() {
		return this.objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getUpdatedAt() {
		return this.updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public List<ActivityAction> getActivityActions() {
		return this.activityActions;
	}

	public void setActivityActions(List<ActivityAction> activityActions) {
		this.activityActions = activityActions;
	}

	public ActivityAction addActivityAction(ActivityAction activityAction) {
		getActivityActions().add(activityAction);
		activityAction.setActivityObject(this);

		return activityAction;
	}

	public ActivityAction removeActivityAction(ActivityAction activityAction) {
		getActivityActions().remove(activityAction);
		activityAction.setActivityObject(null);

		return activityAction;
	}

	public List<ActivityCounter> getActivityCounters() {
		return this.activityCounters;
	}

	public void setActivityCounters(List<ActivityCounter> activityCounters) {
		this.activityCounters = activityCounters;
	}

	public ActivityCounter addActivityCounter(ActivityCounter activityCounter) {
		getActivityCounters().add(activityCounter);
		activityCounter.setActivityObject(this);

		return activityCounter;
	}

	public ActivityCounter removeActivityCounter(ActivityCounter activityCounter) {
		getActivityCounters().remove(activityCounter);
		activityCounter.setActivityObject(null);

		return activityCounter;
	}

	public List<ActivityMetadata> getActivityMetadata() {
		return this.activityMetadata;
	}

	public void setActivityMetadata(List<ActivityMetadata> activityMetadata) {
		this.activityMetadata = activityMetadata;
	}

	public ActivityMetadata addActivityMetadata(ActivityMetadata activityMetadata) {
		getActivityMetadata().add(activityMetadata);
		activityMetadata.setActivityObject(this);

		return activityMetadata;
	}

	public ActivityMetadata removeActivityMetadata(ActivityMetadata activityMetadata) {
		getActivityMetadata().remove(activityMetadata);
		activityMetadata.setActivityObject(null);

		return activityMetadata;
	}

	public List<ActivityObjectActivity> getActivityObjectActivities() {
		return this.activityObjectActivities;
	}

	public void setActivityObjectActivities(List<ActivityObjectActivity> activityObjectActivities) {
		this.activityObjectActivities = activityObjectActivities;
	}

	public ActivityObjectActivity addActivityObjectActivity(ActivityObjectActivity activityObjectActivity) {
		getActivityObjectActivities().add(activityObjectActivity);
		activityObjectActivity.setActivityObject(this);

		return activityObjectActivity;
	}

	public ActivityObjectActivity removeActivityObjectActivity(ActivityObjectActivity activityObjectActivity) {
		getActivityObjectActivities().remove(activityObjectActivity);
		activityObjectActivity.setActivityObject(null);

		return activityObjectActivity;
	}

	public List<ActivityObjectAudience> getActivityObjectAudiences() {
		return this.activityObjectAudiences;
	}

	public void setActivityObjectAudiences(List<ActivityObjectAudience> activityObjectAudiences) {
		this.activityObjectAudiences = activityObjectAudiences;
	}

	public ActivityObjectAudience addActivityObjectAudience(ActivityObjectAudience activityObjectAudience) {
		getActivityObjectAudiences().add(activityObjectAudience);
		activityObjectAudience.setActivityObject(this);

		return activityObjectAudience;
	}

	public ActivityObjectAudience removeActivityObjectAudience(ActivityObjectAudience activityObjectAudience) {
		getActivityObjectAudiences().remove(activityObjectAudience);
		activityObjectAudience.setActivityObject(null);

		return activityObjectAudience;
	}

	public List<ActivityObjectProperty> getActivityObjectProperties1() {
		return this.activityObjectProperties1;
	}

	public void setActivityObjectProperties1(List<ActivityObjectProperty> activityObjectProperties1) {
		this.activityObjectProperties1 = activityObjectProperties1;
	}

	public ActivityObjectProperty addActivityObjectProperties1(ActivityObjectProperty activityObjectProperties1) {
		getActivityObjectProperties1().add(activityObjectProperties1);
		activityObjectProperties1.setActivityObject1(this);

		return activityObjectProperties1;
	}

	public ActivityObjectProperty removeActivityObjectProperties1(ActivityObjectProperty activityObjectProperties1) {
		getActivityObjectProperties1().remove(activityObjectProperties1);
		activityObjectProperties1.setActivityObject1(null);

		return activityObjectProperties1;
	}

	public List<ActivityObjectProperty> getActivityObjectProperties2() {
		return this.activityObjectProperties2;
	}

	public void setActivityObjectProperties2(List<ActivityObjectProperty> activityObjectProperties2) {
		this.activityObjectProperties2 = activityObjectProperties2;
	}

	public ActivityObjectProperty addActivityObjectProperties2(ActivityObjectProperty activityObjectProperties2) {
		getActivityObjectProperties2().add(activityObjectProperties2);
		activityObjectProperties2.setActivityObject2(this);

		return activityObjectProperties2;
	}

	public ActivityObjectProperty removeActivityObjectProperties2(ActivityObjectProperty activityObjectProperties2) {
		getActivityObjectProperties2().remove(activityObjectProperties2);
		activityObjectProperties2.setActivityObject2(null);

		return activityObjectProperties2;
	}

	public List<Actor> getActors() {
		return this.actors;
	}

	public void setActors(List<Actor> actors) {
		this.actors = actors;
	}

	public Actor addActor(Actor actor) {
		getActors().add(actor);
		actor.setActivityObject(this);

		return actor;
	}

	public Actor removeActor(Actor actor) {
		getActors().remove(actor);
		actor.setActivityObject(null);

		return actor;
	}

	public List<Comment> getComments() {
		return this.comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public Comment addComment(Comment comment) {
		getComments().add(comment);
		comment.setActivityObject(this);

		return comment;
	}

	public Comment removeComment(Comment comment) {
		getComments().remove(comment);
		comment.setActivityObject(null);

		return comment;
	}

	public List<Document> getDocuments() {
		return this.documents;
	}

	public void setDocuments(List<Document> documents) {
		this.documents = documents;
	}

	public Document addDocument(Document document) {
		getDocuments().add(document);
		document.setActivityObject(this);

		return document;
	}

	public Document removeDocument(Document document) {
		getDocuments().remove(document);
		document.setActivityObject(null);

		return document;
	}

	public List<Event> getEvents() {
		return this.events;
	}

	public void setEvents(List<Event> events) {
		this.events = events;
	}

	public Event addEvent(Event event) {
		getEvents().add(event);
		event.setActivityObject(this);

		return event;
	}

	public Event removeEvent(Event event) {
		getEvents().remove(event);
		event.setActivityObject(null);

		return event;
	}

	public List<Link> getLinks() {
		return this.links;
	}

	public void setLinks(List<Link> links) {
		this.links = links;
	}

	public Link addLink(Link link) {
		getLinks().add(link);
		link.setActivityObject(this);

		return link;
	}

	public Link removeLink(Link link) {
		getLinks().remove(link);
		link.setActivityObject(null);

		return link;
	}

	public List<Post> getPosts() {
		return this.posts;
	}

	public void setPosts(List<Post> posts) {
		this.posts = posts;
	}

	public Post addPost(Post post) {
		getPosts().add(post);
		post.setActivityObject(this);

		return post;
	}

	public Post removePost(Post post) {
		getPosts().remove(post);
		post.setActivityObject(null);

		return post;
	}

}