package com.jpizarro.th.entity;

public class User extends Ubication{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7534948413604544467L;
	private int id;
	private String userName;
	private String Password;
	private PersonalInformation personalInformation;
	
	public User() {
		super();
	}

	public User(String name, String description) {
		super(name, description);
		// TODO Auto-generated constructor stub
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return Password;
	}
	public void setPassword(String password) {
		Password = password;
	}

	public PersonalInformation getPersonalInformation() {
		return personalInformation;
	}

	public void setPersonalInformation(PersonalInformation personalInformation) {
		this.personalInformation = personalInformation;
	}
	

}
