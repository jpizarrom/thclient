package com.jpizarro.th.entity;

public class Goal extends Ubication {

	private int id;
	
	public Goal(String name, String description) {
		super(name, description);
		// TODO Auto-generated constructor stub
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
