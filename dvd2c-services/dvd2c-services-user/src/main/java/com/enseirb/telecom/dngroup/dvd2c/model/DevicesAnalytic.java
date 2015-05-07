package com.enseirb.telecom.dngroup.dvd2c.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the devices_analytic database table.
 * 
 */
@Entity
@Table(name="devices_analytic")
@NamedQuery(name="DevicesAnalytic.findAll", query="SELECT d FROM DevicesAnalytic d")
public class DevicesAnalytic implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false)
	private int id;

	@Column(name="app_version", length=16)
	private String appVersion;

	@Column(length=255)
	private String manufacturer;

	@Column(length=255)
	private String model;

	@Column(name="os_version", length=16)
	private String osVersion;

	//bi-directional many-to-one association to Device
	@ManyToOne
	@JoinColumn(name="devices_id")
	private Device device;

	public DevicesAnalytic() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAppVersion() {
		return this.appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	public String getManufacturer() {
		return this.manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getModel() {
		return this.model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getOsVersion() {
		return this.osVersion;
	}

	public void setOsVersion(String osVersion) {
		this.osVersion = osVersion;
	}

	public Device getDevice() {
		return this.device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

}