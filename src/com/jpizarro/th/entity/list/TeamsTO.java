package com.jpizarro.th.entity.list;

import java.util.List;

import com.jpizarro.th.entity.TeamTO;

public class TeamsTO {
	private Integer count;
	private Integer start;
	private Integer total;
	private List<TeamTO> teams;
//	private Users users;
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
	public List<TeamTO> getTeams() {
		return teams;
	}
	public void setTeams(List<TeamTO> teams) {
		this.teams = teams;
	}
	public void addTeam(TeamTO t){
		this.addTeam(t);
	}
//	public Users getUsers() {
//		return users;
//	}
//	public void setUsers(Users users) {
//		this.users = users;
//	}
}
