package com.jpizarro.th.entity;

public class Competitor extends User {

	public Competitor(String name, String description) {
		super(name, description);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "Competitor [getId()=" + getId() + ", getUserName()="
				+ getUserName() + ", getPassword()=" + getPassword()
				+ ", getName()=" + getName() + ", getDescription()="
				+ getDescription() + ", getLat()=" + getLat() + ", getLon()="
				+ getLon() + "]";
	}


}
