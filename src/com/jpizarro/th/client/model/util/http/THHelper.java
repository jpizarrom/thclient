package com.jpizarro.th.client.model.util.http;

import java.util.List;

import com.jpizarro.th.lib.game.entity.GameTO;
import com.jpizarro.th.lib.game.entity.TeamTO;
import com.jpizarro.th.lib.game.entity.UserTO;
import com.jpizarro.th.lib.game.entity.list.GamesTO;
import com.jpizarro.th.lib.game.entity.response.GenericGameResponseTO;

public interface THHelper {
	public UserTO login(String userName, String password) 
	throws Exception;
	
	public void logout(String login) 
	throws Exception;
	
	public boolean registerUser() 
	throws Exception;
	
	public boolean updateUser() 
	throws Exception;
	
	public boolean changePassword(long userId, String oldPassword, String newPassword) 
	throws Exception;
	
	public List<String> findCitiesWithGames() throws Exception;
	
	public GameTO findGame(long gameId)  throws Exception;
	
	public boolean joinGame(long gameId, long teamId) 
	throws Exception;
	
	public boolean joinGame(long gameId, long teamId, long userId) 
	throws Exception;
	
	public GenericGameResponseTO updateLocation(long userId, int latitude, int longitude) 
	throws Exception;
	
	public boolean sendMessage(long userId, String receiverLogin, String body) 
	throws Exception;
	
	public GamesTO findGamesByCity(String city, int startIndex, int count) 
	throws Exception;
	
	public GenericGameResponseTO startOrContinueGame(String login) 
	throws Exception;
	
	public GenericGameResponseTO startOrContinueGame(long gameId, long userId, long teamId)
	throws Exception;
	
	public List<TeamTO> findTeamsByGame(long gameId, int startIndex, int count) 
	throws Exception;
	
	public TeamTO findTeam(long teamId)  throws Exception;
	
	public GenericGameResponseTO takePlace(long userId, long placeId, int latitude, int longitude, long gameId, long teamId) 
	throws Exception;

	void setServer(String server);
}
