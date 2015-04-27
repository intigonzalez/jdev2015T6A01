package com.enseirb.telecom.dngroup.dvd2c.model.generated;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the notifications database table.
 * 
 */
@Entity
@Table(name="notifications")
@NamedQuery(name="Notification.findAll", query="SELECT n FROM Notification n")
public class Notification implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	private String attachment;

	@Lob
	private String body;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="created_at")
	private Date createdAt;

	private byte draft;

	@Temporal(TemporalType.TIMESTAMP)
	private Date expires;

	private byte global;

	@Column(name="notification_code")
	private String notificationCode;

	@Column(name="notified_object_id")
	private int notifiedObjectId;

	@Column(name="notified_object_type")
	private String notifiedObjectType;

	@Column(name="sender_id")
	private int senderId;

	@Column(name="sender_type")
	private String senderType;

	private String subject;

	private String type;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="updated_at")
	private Date updatedAt;

	//bi-directional many-to-one association to Conversation
	@ManyToOne
	private Conversation conversation;

	//bi-directional many-to-one association to Receipt
	@OneToMany(mappedBy="notification")
	private List<Receipt> receipts;

	public Notification() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAttachment() {
		return this.attachment;
	}

	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}

	public String getBody() {
		return this.body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Date getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public byte getDraft() {
		return this.draft;
	}

	public void setDraft(byte draft) {
		this.draft = draft;
	}

	public Date getExpires() {
		return this.expires;
	}

	public void setExpires(Date expires) {
		this.expires = expires;
	}

	public byte getGlobal() {
		return this.global;
	}

	public void setGlobal(byte global) {
		this.global = global;
	}

	public String getNotificationCode() {
		return this.notificationCode;
	}

	public void setNotificationCode(String notificationCode) {
		this.notificationCode = notificationCode;
	}

	public int getNotifiedObjectId() {
		return this.notifiedObjectId;
	}

	public void setNotifiedObjectId(int notifiedObjectId) {
		this.notifiedObjectId = notifiedObjectId;
	}

	public String getNotifiedObjectType() {
		return this.notifiedObjectType;
	}

	public void setNotifiedObjectType(String notifiedObjectType) {
		this.notifiedObjectType = notifiedObjectType;
	}

	public int getSenderId() {
		return this.senderId;
	}

	public void setSenderId(int senderId) {
		this.senderId = senderId;
	}

	public String getSenderType() {
		return this.senderType;
	}

	public void setSenderType(String senderType) {
		this.senderType = senderType;
	}

	public String getSubject() {
		return this.subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
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

	public Conversation getConversation() {
		return this.conversation;
	}

	public void setConversation(Conversation conversation) {
		this.conversation = conversation;
	}

	public List<Receipt> getReceipts() {
		return this.receipts;
	}

	public void setReceipts(List<Receipt> receipts) {
		this.receipts = receipts;
	}

	public Receipt addReceipt(Receipt receipt) {
		getReceipts().add(receipt);
		receipt.setNotification(this);

		return receipt;
	}

	public Receipt removeReceipt(Receipt receipt) {
		getReceipts().remove(receipt);
		receipt.setNotification(null);

		return receipt;
	}

}