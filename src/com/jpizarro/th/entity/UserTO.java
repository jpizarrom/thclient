package com.jpizarro.th.entity;

public class UserTO extends Ubication{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7534948413604544467L;
	private long userId;
	private String username;
	private String password;
//	private PersonalInformation personalInformation;
	private long gameId;
	
	private String role;
	
	public UserTO() {
		super();
	}

	public UserTO(String name, String description) {
		super(name, description);
		// TODO Auto-generated constructor stub
	}

//	public PersonalInformation getPersonalInformation() {
//		return personalInformation;
//	}
//
//	public void setPersonalInformation(PersonalInformation personalInformation) {
//		this.personalInformation = personalInformation;
//	}

	public String getUsername() {
		return username;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setGameId(long gameId) {
		this.gameId = gameId;
	}

	public long getGameId() {
		return gameId;
	}

	@Override
	public String toString() {
		return "User [userName=" + username + ", password=" + password
				+ ", gameId=" + gameId + "]";
	}
	

}
