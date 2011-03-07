package com.jpizarro.th.client.model.service.game;

import java.util.List;

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

	public List<String> findCitiesWithGames() throws Exception {
		return instance.findCitiesWithGames();
	}

	public boolean abandonGame(long gameId) throws Exception {
		// TODO Auto-generated method stub
		throw new ServerException(ServerException.NOT_IMPL, "Not Impl "+"abandonGame");
	}

	public GameTO findGame(long gameId) throws Exception {
		return instance.findGame(gameId);
	}

	public boolean joinGame(long gameId, long teamId) throws Exception {
		return instance.joinGame(gameId, teamId);
	}

	public GenericGameResponseTO updateLocation(int latitude, int longitude)
	throws Exception {
		return instance.updateLocation(latitude, longitude);
	}

	public GamesTO findGamesByCity(String city, int startIndex, int count)
	throws Exception {
		// TODO Auto-generated method stub
		return instance.findGamesByCity(city, 
				startIndex, count);
	}

	public GenericGameResponseTO startOrContinueGame(String username)
	throws Exception {
		return instance.startOrContinueGame(username);
	}

	public boolean sendMessage(String receiverUser, String body)
	throws Exception {
		return instance.sendMessage(receiverUser, body);
	}

	public List<TeamTO> findTeamsByGame(long gameId, int startIndex, int count)
	throws Exception {
		// TODO Auto-generated method stub
		return instance.findTeamsByGame(gameId, 
				startIndex, count);
	}

	public GenericGameResponseTO takePlace(long id, int latitude,
			int longitude) throws Exception {
		// TODO Auto-generated method stub
		return instance.takePlace(id, latitude, longitude);
	}

	@Override
	public TeamTO findTeam(long teamId) throws Exception {
		return instance.findTeam(teamId);
	}

}
