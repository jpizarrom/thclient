package com.jpizarro.th.entity;

import java.util.List;

public class Game {
	
	private int id;
	private String name;
	
	private List<Team> teams;
	private List<Hint> hints;
	private Goal goal;
	
	@Override
	public String toString() {
		return "Game [getId()=" + getId() + ", getName()=" + getName()
				+ ", getGoal()=" + getGoal() + ", getTeams()=" + getTeams()
				+ ", getHints()=" + getHints() + "]";
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
	public List<Team> getTeams() {
		return teams;
	}
	public void setTeams(List<Team> teams) {
		this.teams = teams;
	}
	public List<Hint> getHints() {
		return hints;
	}
	public void setHints(List<Hint> hints) {
		this.hints = hints;
	}
	public Goal getGoal() {
		return goal;
	}
	public void setGoal(Goal goal) {
		this.goal = goal;
	}

}
