package com.jpizarro.th.entity;

public class PersonalInformationTO {
	private String direction;
	
	@Override
	public String toString() {
		return "PersonalInformation [getDirection()=" + getDirection() + "]";
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}
}
