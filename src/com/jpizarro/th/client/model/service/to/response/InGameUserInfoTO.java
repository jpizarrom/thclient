package com.jpizarro.th.client.model.service.to.response;

import java.io.Serializable;

public class InGameUserInfoTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4241092982460666213L;
	
	private String username;
	private int latitude;
	private int longitude;
	
	public InGameUserInfoTO() {
		super();
	}
	public InGameUserInfoTO(String username, int latitude, int longitude) {
		super();
		this.username = username;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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
	

}
