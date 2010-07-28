package com.jpizarro.th.entity.list;

import java.util.List;

import com.jpizarro.th.entity.User;

public class Users {
	private Integer count;
	private Integer start;
	private Integer total;
	private List<User> users;
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
	public List<User> getUsers() {
		return users;
	}
	public void setUsers(List<User> users) {
		this.users = users;
	}
	public void addUser(User u){
		this.users.add(u);
	}	
}
