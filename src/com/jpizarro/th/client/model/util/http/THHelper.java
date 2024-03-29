package com.jpizarro.th.client.model.util.http;

import java.util.List;

import com.jpizarro.th.model.service.to.response.GenericGameResponseTO;
import com.jpizarro.th.entity.GameTO;
import com.jpizarro.th.entity.TeamTO;
import com.jpizarro.th.entity.UserTO;
import com.jpizarro.th.entity.list.GamesTO;

public interface THHelper {
	public UserTO login(String userName, String password) 
	throws Exception;
	
	public void logout(String login) 
	throws Exception;
	
	public boolean registerUser() 
	throws Exception;
	
	public boolean updateUser() 
	throws Exception;
	
	public boolean changePassword(String oldPassword, String newPassword) 
	throws Exception;
	
	public List<String> findCitiesWithGames() throws Exception;
	
	public GameTO findGame(long gameId)  throws Exception;
	
	public boolean joinGame(long gameId, long teamId) 
	throws Exception;
	
	public GenericGameResponseTO updateLocation(int latitude, int longitude) 
	throws Exception;
	
	public boolean sendMessage(String receiverLogin, String body) 
	throws Exception;
	
	public GamesTO findGamesByCity(String city, int startIndex, int count) 
	throws Exception;
	
	public GenericGameResponseTO startOrContinueGame(String login) 
	throws Exception;
	
	public List<TeamTO> findTeamsByGame(long gameId, int startIndex, int count) 
	throws Exception;
	
	public GenericGameResponseTO takePlace(long id, int latitude, int longitude) 
	throws Exception;
}
