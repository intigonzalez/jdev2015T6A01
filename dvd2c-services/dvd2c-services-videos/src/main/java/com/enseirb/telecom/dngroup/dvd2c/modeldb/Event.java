package com.enseirb.telecom.dngroup.dvd2c.modeldb;

import java.io.Serializable;

import javax.persistence.*;

import java.util.Date;


/**
 * The persistent class for the events database table.
 * 
 */
@Entity
@Table(name="events")
@NamedQuery(name="Event.findAll", query="SELECT e FROM Event e")
public class Event extends ActivityObject implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="all_day")
	private byte allDay;

	private int days;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="end_at")
	private Date endAt;

	@Temporal(TemporalType.DATE)
	@Column(name="end_date")
	private Date endDate;

	private int frequency;

	private int interval;

	@Column(name="interval_flag")
	private int intervalFlag;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="start_at")
	private Date startAt;

	@Temporal(TemporalType.DATE)
	@Column(name="start_date")
	private Date startDate;

	//bi-directional many-to-one association to ActivityObject
	@ManyToOne
	@JoinColumn(name="activity_object_id")
	private ActivityObject activityObject;

	public Event() {
	}

	public byte getAllDay() {
		return this.allDay;
	}

	public void setAllDay(byte allDay) {
		this.allDay = allDay;
	}

	public int getDays() {
		return this.days;
	}

	public void setDays(int days) {
		this.days = days;
	}

	public Date getEndAt() {
		return this.endAt;
	}

	public void setEndAt(Date endAt) {
		this.endAt = endAt;
	}

	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public int getFrequency() {
		return this.frequency;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

	public int getInterval() {
		return this.interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	public int getIntervalFlag() {
		return this.intervalFlag;
	}

	public void setIntervalFlag(int intervalFlag) {
		this.intervalFlag = intervalFlag;
	}

	public Date getStartAt() {
		return this.startAt;
	}

	public void setStartAt(Date startAt) {
		this.startAt = startAt;
	}

	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public ActivityObject getActivityObject() {
		return this.activityObject;
	}

	public void setActivityObject(ActivityObject activityObject) {
		this.activityObject = activityObject;
	}

}