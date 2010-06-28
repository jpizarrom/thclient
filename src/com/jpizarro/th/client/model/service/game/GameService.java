package com.jpizarro.th.client.model.service.game;

import java.util.List;

import com.jpizarro.th.client.model.service.to.GameCTO;
import com.jpizarro.th.client.model.service.to.response.GenericGameResponseTO;
import com.jpizarro.th.entity.Game;

public interface GameService {
	public List<String> findCitiesWithGames() throws Exception;
	
	public Game findGame(long gameId) 
	throws Exception;
	
	public boolean joinGame(long gameId) 
	throws Exception;
	
	public boolean abandonGame(long gameId) 
	throws Exception;
	
	public GenericGameResponseTO updateLocation(int latitude, int longitude) 
	throws Exception;
	
	public GameCTO findGamesByCity(String city, int startIndex, int count) 
	throws Exception ;

}
