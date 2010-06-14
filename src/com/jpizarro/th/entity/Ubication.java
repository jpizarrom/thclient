package com.jpizarro.th.entity;

import java.io.Serializable;

public class Ubication implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7491962873324125012L;
	private double lat;
	private double lon;

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

	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLon() {
		return lon;
	}
	public void setLon(double lon) {
		this.lon = lon;
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
