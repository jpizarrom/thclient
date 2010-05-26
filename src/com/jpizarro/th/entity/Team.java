package com.jpizarro.th.entity;

import java.util.ArrayList;
import java.util.List;

public class Team {
	private int id;
	private String name;
	private List<Competitor> competitors;
	private List<Hint> hints;
	
	public Team(String name) {
		super();
		this.name = name;
		competitors = new ArrayList<Competitor>();
	}

	@Override
	public String toString() {
		String result ="";
		result += "Team [getId()=" + getId() + ", getName()=" + getName()
				+ ", getCompetitors()=";
		for (Competitor c : getCompetitors())
			result += c; 
		result +=		"]";
		
		return result;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Competitor> getCompetitors() {
		return competitors;
	}

	public void setCompetitors(List<Competitor> competitors) {
		this.competitors = competitors;
	}

}
