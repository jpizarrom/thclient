package com.jpizarro.th.client.model.util.xml;

import java.util.ArrayList;
import java.util.List;

import com.jpizarro.th.entity.Game;
import com.jpizarro.th.entity.Team;


public class XStreamResponse {
	private List<Team> teams = new ArrayList<Team>();
	private List<Game> games = new ArrayList<Game>();


	
	public List<Team> getTeams() {
		return teams;
	}

	public void setTeams(List<Team> teams) {
		this.teams = teams;
	}

	public void addTeam(Team t){
		this.teams.add(t);
	}

	public List<Game> getGames() {
		return games;
	}

	public void setGames(List<Game> games) {
		this.games = games;
	}
	public void addGame(Game g){
		this.games.add(g);
	}

}
