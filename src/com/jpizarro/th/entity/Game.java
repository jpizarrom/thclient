package com.jpizarro.th.entity;

import java.io.Serializable;
import java.util.Calendar;

public class Game implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 9042114067071410684L;
	
	private long gameId;
	private Calendar startDate;
	private Calendar finishDate;
	private String city;

	private int availablePlaces;
	private int availableHints;
	private int availableGoals;
	
	private int currentTeams;
	private int currentUsers;
	private int currentMessages;
	
	private int maxTeams;
	private int maxUserPerTeam;
	
	private int latitude;
	private int longitude;
	
	public Game() {}
	
	public Game(long gameId, Calendar startDate, Calendar finishDate,
			String city, int availablePlaces, int availableHints,
			int availableGoals, int currentTeams, int currentUsers,
			int currentMessages, int maxTeams, int maxUserPerTeam,
			int latitude, int longitude) {
		super();
		this.gameId = gameId;
		this.startDate = startDate;
		this.finishDate = finishDate;
		this.city = city;
		this.availablePlaces = availablePlaces;
		this.availableHints = availableHints;
		this.availableGoals = availableGoals;
		this.currentTeams = currentTeams;
		this.currentUsers = currentUsers;
		this.currentMessages = currentMessages;
		this.maxTeams = maxTeams;
		this.maxUserPerTeam = maxUserPerTeam;
		this.latitude = latitude;
		this.longitude = longitude;
	}
	public long getGameId() {
		return gameId;
	}
	public void setGameId(long gameId) {
		this.gameId = gameId;
	}
	public Calendar getStartDate() {
		return startDate;
	}
	public void setStartDate(Calendar startDate) {
		this.startDate = startDate;
	}
	public Calendar getFinishDate() {
		return finishDate;
	}
	public void setFinishDate(Calendar finishDate) {
		this.finishDate = finishDate;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public int getAvailablePlaces() {
		return availablePlaces;
	}
	public void setAvailablePlaces(int availablePlaces) {
		this.availablePlaces = availablePlaces;
	}
	public int getAvailableHints() {
		return availableHints;
	}
	public void setAvailableHints(int availableHints) {
		this.availableHints = availableHints;
	}
	public int getAvailableGoals() {
		return availableGoals;
	}
	public void setAvailableGoals(int availableGoals) {
		this.availableGoals = availableGoals;
	}
	public int getCurrentTeams() {
		return currentTeams;
	}
	public void setCurrentTeams(int currentTeams) {
		this.currentTeams = currentTeams;
	}
	public int getCurrentUsers() {
		return currentUsers;
	}
	public void setCurrentUsers(int currentUsers) {
		this.currentUsers = currentUsers;
	}
	public int getCurrentMessages() {
		return currentMessages;
	}
	public void setCurrentMessages(int currentMessages) {
		this.currentMessages = currentMessages;
	}
	public int getMaxTeams() {
		return maxTeams;
	}
	public void setMaxTeams(int maxTeams) {
		this.maxTeams = maxTeams;
	}
	public int getMaxUserPerTeam() {
		return maxUserPerTeam;
	}
	public void setMaxUserPerTeam(int maxUserPerTeam) {
		this.maxUserPerTeam = maxUserPerTeam;
	}
	public int getLatitude() {
		return latitude;
	}
	public void setLatitude(int latitude) {
		this.latitude = latitude;
	}
	public int getLongitude() {
		return longitude;
	}
	public void setLongitude(int longitude) {
		this.longitude = longitude;
	}

}
