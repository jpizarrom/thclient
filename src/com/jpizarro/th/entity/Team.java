package com.jpizarro.th.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Team implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 731485816032540739L;
	private long teamId;
	private String name;
	private String description;
//	private List<Competitor> competitors;
//	private List<Hint> hints;
	private int currentUsers;
	private List<User> users;
	
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

	public long getTeamId() {
		return teamId;
	}

	public void setTeamId(long teamId) {
		this.teamId = teamId;
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

	public int getCurrentUsers() {
		return currentUsers;
	}

	public void setCurrentUsers(int currentUsers) {
		this.currentUsers = currentUsers;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}
	public void addUser(User user) {
		this.users.add(user);
	}
}
