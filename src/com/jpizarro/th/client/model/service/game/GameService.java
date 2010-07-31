package com.jpizarro.th.client.model.service.game;

import java.util.List;

import com.jpizarro.th.model.service.to.response.GenericGameResponseTO;
import com.jpizarro.th.entity.GameTO;
import com.jpizarro.th.entity.TeamTO;
import com.jpizarro.th.entity.list.GamesTO;

public interface GameService {
	public List<String> findCitiesWithGames() throws Exception;
	
	public GameTO findGame(long gameId) 
	throws Exception;
	
	public boolean joinGame(long gameId, long teamId) 
	throws Exception;
	
	public boolean abandonGame(long gameId) 
	throws Exception;
	
	public GenericGameResponseTO updateLocation(int latitude, int longitude) 
	throws Exception;
	
	public GenericGameResponseTO takePlace(long placeId, int latitude, int longitude) 
	throws Exception;
	
	public GamesTO findGamesByCity(String city, int startIndex, int count) 
	throws Exception ;
	
	public List<TeamTO> findTeamsByGame(long gameId, int startIndex, int count) 
	throws Exception ;
	
	public GenericGameResponseTO startOrContinueGame(String login) 
	throws Exception;
	
	public boolean sendMessage(String receiverUser, String body)
	throws Exception;
}
