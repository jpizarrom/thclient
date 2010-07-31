package com.jpizarro.th.entity.list;

import java.util.List;

import com.jpizarro.th.entity.UserTO;

public class UsersTO {
	private Integer count;
	private Integer start;
	private Integer total;
	private List<UserTO> users;
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
	public List<UserTO> getUsers() {
		return users;
	}
	public void setUsers(List<UserTO> users) {
		this.users = users;
	}
	public void addUser(UserTO u){
		this.users.add(u);
	}	
}
