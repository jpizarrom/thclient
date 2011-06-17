package com.jpizarro.th.client.model.service.game;

import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.jpizarro.th.client.model.util.http.GameRestHttpHelper;
import com.jpizarro.th.client.model.util.http.THHelper;
import com.jpizarro.th.lib.game.entity.GameTO;
import com.jpizarro.th.lib.game.entity.TeamTO;
import com.jpizarro.th.lib.game.entity.list.GamesTO;
import com.jpizarro.th.lib.game.entity.response.GenericGameResponseTO;

import es.sonxurxo.gpsgame.client.util.exception.ServerException;

public class HttpGameServiceImpl implements GameService{
	
	private static THHelper instance;
	static {
		instance = GameRestHttpHelper.getInstance();
	}

	public HttpGameServiceImpl() {
		super();
		// TODO Auto-generated constructor stub
	}

//	public HttpGameServiceImpl(Context applicationContext) {
//		// TODO Auto-generated constructor stub
//		SharedPreferences settings = PreferenceManager
//		.getDefaultSharedPreferences(applicationContext);
//		
//        String server = settings.getString("game_server", "192.168.1.70");
//        instance.setServer(server);
//	}
	@Override
	public void setServer(String server){
		instance.setServer(server);
	}

	@Override
	public List<String> findCitiesWithGames() throws Exception {
		return instance.findCitiesWithGames();
	}

	@Override
	public boolean abandonGame(long userId, long gameId) throws Exception {
		// TODO Auto-generated method stub
		throw new ServerException(ServerException.NOT_IMPL, "Not Impl "+"abandonGame");
	}

	@Override
	public GameTO findGame(long gameId) throws Exception {
		return instance.findGame(gameId);
	}

	@Override
	public boolean joinGame(long gameId, long teamId, long userId) throws Exception {
		return instance.joinGame(gameId, teamId, userId);
	}

	@Override
	public GenericGameResponseTO updateLocation(long userId, int latitude, int longitude)
	throws Exception {
		return instance.updateLocation(userId, latitude, longitude);
	}

	@Override
	public GamesTO findGamesByCity(String city, int startIndex, int count)
	throws Exception {
		// TODO Auto-generated method stub
		return instance.findGamesByCity(city, 
				startIndex, count);
	}

	@Override
	public GenericGameResponseTO startOrContinueGame(String username)
	throws Exception {
		return instance.startOrContinueGame(username);
	}
	
	@Override
	public GenericGameResponseTO startOrContinueGame(long gameId, long userId, long teamId) 
	throws Exception {
		return instance.startOrContinueGame(gameId, userId, teamId) ;
	}
	
	@Override
	public boolean sendMessage(long userId, String receiverUser, String body)
	throws Exception {
		return instance.sendMessage(userId, receiverUser, body);
	}

	@Override
	public List<TeamTO> findTeamsByGame(long gameId, int startIndex, int count)
	throws Exception {
		// TODO Auto-generated method stub
		return instance.findTeamsByGame(gameId, 
				startIndex, count);
	}

	@Override
	public GenericGameResponseTO takePlace(long userId, long placeId, int latitude,
			int longitude, long gameId, long teamId
			) throws Exception {
		// TODO Auto-generated method stub
		return instance.takePlace(userId, placeId, latitude, longitude, gameId, teamId);
//		return null;
	}

	@Override
	public TeamTO findTeam(long teamId) throws Exception {
		return instance.findTeam(teamId);
	}

	@Override
	public boolean joinGame(long gameId, long teamId) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

}
