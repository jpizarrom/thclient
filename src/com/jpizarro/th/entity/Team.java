package com.jpizarro.th.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Team implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 731485816032540739L;
	private long id;
	private String name;
	private String description;
//	private List<Competitor> competitors;
//	private List<Hint> hints;
	
	public Team(String name) {
		super();
		this.name = name;
//		competitors = new ArrayList<Competitor>();
	}

	public Team() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		String result ="";
//		result += "Team [getId()=" + getId() + ", getName()=" + getName()
//				+ ", getCompetitors()=";
//		for (Competitor c : getCompetitors())
//			result += c; 
		result +=		"]";
		
		return result;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

//	public List<Competitor> getCompetitors() {
//		return competitors;
//	}
//
//	public void setCompetitors(List<Competitor> competitors) {
//		this.competitors = competitors;
//	}

}
