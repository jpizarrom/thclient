package com.jpizarro.th.entity;

import java.io.Serializable;

public class Ubication implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7491962873324125012L;
	private int latitude;
	private int longitude;

	private String name;
	private String description;

	public Ubication() {
		super();
	}

	public Ubication(String name, String description) {
		super();
		this.name = name;
		this.description = description;
	}

	public int getLatitude() {
		return latitude;
	}

	public void setLatitude(int latitude) {
		this.latitude = latitude;
	}

	public int getLongitude() {
		return longitude;
	}

	public void setLongitude(int longitude) {
		this.longitude = longitude;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

}
