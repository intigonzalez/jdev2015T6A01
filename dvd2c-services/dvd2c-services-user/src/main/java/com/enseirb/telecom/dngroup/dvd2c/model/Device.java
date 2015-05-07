package com.enseirb.telecom.dngroup.dvd2c.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the devices database table.
 * 
 */
@Entity
@Table(name="devices")
@NamedQuery(name="Device.findAll", query="SELECT d FROM Device d")
public class Device implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false)
	private int id;

	@Column(name="os_type", length=16)
	private String osType;

	@Column(name="registration_id", nullable=false)
	private byte[] registrationId;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;

	//bi-directional many-to-one association to DevicesAnalytic
	@OneToMany(mappedBy="device")
	private List<DevicesAnalytic> devicesAnalytics;

	public Device() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getOsType() {
		return this.osType;
	}

	public void setOsType(String osType) {
		this.osType = osType;
	}

	public byte[] getRegistrationId() {
		return this.registrationId;
	}

	public void setRegistrationId(byte[] registrationId) {
		this.registrationId = registrationId;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<DevicesAnalytic> getDevicesAnalytics() {
		return this.devicesAnalytics;
	}

	public void setDevicesAnalytics(List<DevicesAnalytic> devicesAnalytics) {
		this.devicesAnalytics = devicesAnalytics;
	}

	public DevicesAnalytic addDevicesAnalytic(DevicesAnalytic devicesAnalytic) {
		getDevicesAnalytics().add(devicesAnalytic);
		devicesAnalytic.setDevice(this);

		return devicesAnalytic;
	}

	public DevicesAnalytic removeDevicesAnalytic(DevicesAnalytic devicesAnalytic) {
		getDevicesAnalytics().remove(devicesAnalytic);
		devicesAnalytic.setDevice(null);

		return devicesAnalytic;
	}

}