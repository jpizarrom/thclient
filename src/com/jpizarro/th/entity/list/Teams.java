package com.jpizarro.th.entity.list;

import java.util.List;

import com.jpizarro.th.entity.Team;

public class Teams {
	private Integer count;
	private Integer start;
	private Integer total;
	private List<Team> teams;
	private Users users;
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public Integer getStart() {
		return start;
	}
	public void setStart(Integer start) {
		this.start = start;
	}
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
	public List<Team> getTeams() {
		return teams;
	}
	public void setTeams(List<Team> teams) {
		this.teams = teams;
	}
	public void addTeam(Team t){
		this.addTeam(t);
	}
	public Users getUsers() {
		return users;
	}
	public void setUsers(Users users) {
		this.users = users;
	}
	
}
