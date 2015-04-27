package com.enseirb.telecom.dngroup.dvd2c.model.generated;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the actors database table.
 * 
 */
@Entity
@Table(name="actors")
@NamedQuery(name="Actor.findAll", query="SELECT a FROM Actor a")
public class Actor implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="created_at")
	private Date createdAt;

	private String email;

	@Column(name="logo_content_type")
	private String logoContentType;

	@Column(name="logo_file_name")
	private String logoFileName;

	@Column(name="logo_file_size")
	private int logoFileSize;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="logo_updated_at")
	private Date logoUpdatedAt;

	private String name;

	@Column(name="notification_settings")
	private String notificationSettings;

	@Column(name="notify_by_email")
	private byte notifyByEmail;

	private String slug;

	@Column(name="subject_type")
	private String subjectType;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="updated_at")
	private Date updatedAt;

	//bi-directional many-to-one association to Activity
	@OneToMany(mappedBy="actor1")
	private List<Activity> activities1;

	//bi-directional many-to-one association to Activity
	@OneToMany(mappedBy="actor2")
	private List<Activity> activities2;

	//bi-directional many-to-one association to Activity
	@OneToMany(mappedBy="actor3")
	private List<Activity> activities3;

	//bi-directional many-to-one association to ActivityAction
	@OneToMany(mappedBy="actor")
	private List<ActivityAction> activityActions;

	//bi-directional many-to-one association to ActorKey
	@OneToMany(mappedBy="actor")
	private List<ActorKey> actorKeys;

	//bi-directional many-to-one association to ActivityObject
	@ManyToOne
	@JoinColumn(name="activity_object_id")
	private ActivityObject activityObject;

	//bi-directional many-to-one association to Contact
	@OneToMany(mappedBy="actor1")
	private List<Contact> contacts1;

	//bi-directional many-to-one association to Contact
	@OneToMany(mappedBy="actor2")
	private List<Contact> contacts2;

	//bi-directional many-to-one association to Group
	@OneToMany(mappedBy="actor")
	private List<Group> groups;

	//bi-directional many-to-one association to Profile
	@OneToMany(mappedBy="actor")
	private List<Profile> profiles;

	//bi-directional many-to-one association to Relation
	@OneToMany(mappedBy="actor")
	private List<Relation> relations;

	//bi-directional many-to-one association to RemoteSubject
	@OneToMany(mappedBy="actor")
	private List<RemoteSubject> remoteSubjects;

	//bi-directional many-to-one association to Site
	@OneToMany(mappedBy="actor")
	private List<Site> sites;

	//bi-directional many-to-one association to User
	@OneToMany(mappedBy="actor")
	private List<User> users;

	public Actor() {
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

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLogoContentType() {
		return this.logoContentType;
	}

	public void setLogoContentType(String logoContentType) {
		this.logoContentType = logoContentType;
	}

	public String getLogoFileName() {
		return this.logoFileName;
	}

	public void setLogoFileName(String logoFileName) {
		this.logoFileName = logoFileName;
	}

	public int getLogoFileSize() {
		return this.logoFileSize;
	}

	public void setLogoFileSize(int logoFileSize) {
		this.logoFileSize = logoFileSize;
	}

	public Date getLogoUpdatedAt() {
		return this.logoUpdatedAt;
	}

	public void setLogoUpdatedAt(Date logoUpdatedAt) {
		this.logoUpdatedAt = logoUpdatedAt;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNotificationSettings() {
		return this.notificationSettings;
	}

	public void setNotificationSettings(String notificationSettings) {
		this.notificationSettings = notificationSettings;
	}

	public byte getNotifyByEmail() {
		return this.notifyByEmail;
	}

	public void setNotifyByEmail(byte notifyByEmail) {
		this.notifyByEmail = notifyByEmail;
	}

	public String getSlug() {
		return this.slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public String getSubjectType() {
		return this.subjectType;
	}

	public void setSubjectType(String subjectType) {
		this.subjectType = subjectType;
	}

	public Date getUpdatedAt() {
		return this.updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public List<Activity> getActivities1() {
		return this.activities1;
	}

	public void setActivities1(List<Activity> activities1) {
		this.activities1 = activities1;
	}

	public Activity addActivities1(Activity activities1) {
		getActivities1().add(activities1);
		activities1.setActor1(this);

		return activities1;
	}

	public Activity removeActivities1(Activity activities1) {
		getActivities1().remove(activities1);
		activities1.setActor1(null);

		return activities1;
	}

	public List<Activity> getActivities2() {
		return this.activities2;
	}

	public void setActivities2(List<Activity> activities2) {
		this.activities2 = activities2;
	}

	public Activity addActivities2(Activity activities2) {
		getActivities2().add(activities2);
		activities2.setActor2(this);

		return activities2;
	}

	public Activity removeActivities2(Activity activities2) {
		getActivities2().remove(activities2);
		activities2.setActor2(null);

		return activities2;
	}

	public List<Activity> getActivities3() {
		return this.activities3;
	}

	public void setActivities3(List<Activity> activities3) {
		this.activities3 = activities3;
	}

	public Activity addActivities3(Activity activities3) {
		getActivities3().add(activities3);
		activities3.setActor3(this);

		return activities3;
	}

	public Activity removeActivities3(Activity activities3) {
		getActivities3().remove(activities3);
		activities3.setActor3(null);

		return activities3;
	}

	public List<ActivityAction> getActivityActions() {
		return this.activityActions;
	}

	public void setActivityActions(List<ActivityAction> activityActions) {
		this.activityActions = activityActions;
	}

	public ActivityAction addActivityAction(ActivityAction activityAction) {
		getActivityActions().add(activityAction);
		activityAction.setActor(this);

		return activityAction;
	}

	public ActivityAction removeActivityAction(ActivityAction activityAction) {
		getActivityActions().remove(activityAction);
		activityAction.setActor(null);

		return activityAction;
	}

	public List<ActorKey> getActorKeys() {
		return this.actorKeys;
	}

	public void setActorKeys(List<ActorKey> actorKeys) {
		this.actorKeys = actorKeys;
	}

	public ActorKey addActorKey(ActorKey actorKey) {
		getActorKeys().add(actorKey);
		actorKey.setActor(this);

		return actorKey;
	}

	public ActorKey removeActorKey(ActorKey actorKey) {
		getActorKeys().remove(actorKey);
		actorKey.setActor(null);

		return actorKey;
	}

	public ActivityObject getActivityObject() {
		return this.activityObject;
	}

	public void setActivityObject(ActivityObject activityObject) {
		this.activityObject = activityObject;
	}

	public List<Contact> getContacts1() {
		return this.contacts1;
	}

	public void setContacts1(List<Contact> contacts1) {
		this.contacts1 = contacts1;
	}

	public Contact addContacts1(Contact contacts1) {
		getContacts1().add(contacts1);
		contacts1.setActor1(this);

		return contacts1;
	}

	public Contact removeContacts1(Contact contacts1) {
		getContacts1().remove(contacts1);
		contacts1.setActor1(null);

		return contacts1;
	}

	public List<Contact> getContacts2() {
		return this.contacts2;
	}

	public void setContacts2(List<Contact> contacts2) {
		this.contacts2 = contacts2;
	}

	public Contact addContacts2(Contact contacts2) {
		getContacts2().add(contacts2);
		contacts2.setActor2(this);

		return contacts2;
	}

	public Contact removeContacts2(Contact contacts2) {
		getContacts2().remove(contacts2);
		contacts2.setActor2(null);

		return contacts2;
	}

	public List<Group> getGroups() {
		return this.groups;
	}

	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}

	public Group addGroup(Group group) {
		getGroups().add(group);
		group.setActor(this);

		return group;
	}

	public Group removeGroup(Group group) {
		getGroups().remove(group);
		group.setActor(null);

		return group;
	}

	public List<Profile> getProfiles() {
		return this.profiles;
	}

	public void setProfiles(List<Profile> profiles) {
		this.profiles = profiles;
	}

	public Profile addProfile(Profile profile) {
		getProfiles().add(profile);
		profile.setActor(this);

		return profile;
	}

	public Profile removeProfile(Profile profile) {
		getProfiles().remove(profile);
		profile.setActor(null);

		return profile;
	}

	public List<Relation> getRelations() {
		return this.relations;
	}

	public void setRelations(List<Relation> relations) {
		this.relations = relations;
	}

	public Relation addRelation(Relation relation) {
		getRelations().add(relation);
		relation.setActor(this);

		return relation;
	}

	public Relation removeRelation(Relation relation) {
		getRelations().remove(relation);
		relation.setActor(null);

		return relation;
	}

	public List<RemoteSubject> getRemoteSubjects() {
		return this.remoteSubjects;
	}

	public void setRemoteSubjects(List<RemoteSubject> remoteSubjects) {
		this.remoteSubjects = remoteSubjects;
	}

	public RemoteSubject addRemoteSubject(RemoteSubject remoteSubject) {
		getRemoteSubjects().add(remoteSubject);
		remoteSubject.setActor(this);

		return remoteSubject;
	}

	public RemoteSubject removeRemoteSubject(RemoteSubject remoteSubject) {
		getRemoteSubjects().remove(remoteSubject);
		remoteSubject.setActor(null);

		return remoteSubject;
	}

	public List<Site> getSites() {
		return this.sites;
	}

	public void setSites(List<Site> sites) {
		this.sites = sites;
	}

	public Site addSite(Site site) {
		getSites().add(site);
		site.setActor(this);

		return site;
	}

	public Site removeSite(Site site) {
		getSites().remove(site);
		site.setActor(null);

		return site;
	}

	public List<User> getUsers() {
		return this.users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public User addUser(User user) {
		getUsers().add(user);
		user.setActor(this);

		return user;
	}

	public User removeUser(User user) {
		getUsers().remove(user);
		user.setActor(null);

		return user;
	}

}