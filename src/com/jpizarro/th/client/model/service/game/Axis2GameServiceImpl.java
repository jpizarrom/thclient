package com.jpizarro.th.client.model.service.game;

import java.util.List;

import com.jpizarro.th.lib.game.entity.GameTO;
import com.jpizarro.th.lib.game.entity.TeamTO;
import com.jpizarro.th.lib.game.entity.list.GamesTO;
import com.jpizarro.th.lib.game.entity.response.GenericGameResponseTO;
import com.jpizarro.th.client.model.util.http.GameRestHttpHelper;


import es.sonxurxo.gpsgame.client.util.exception.ServerException;

public class Axis2GameServiceImpl implements GameService{

	public List<String> findCitiesWithGames() throws Exception {
		return GameRestHttpHelper.getInstance().findCitiesWithGames();
	}

	public boolean abandonGame(long gameId) throws Exception {
		// TODO Auto-generated method stub
		throw new ServerException(ServerException.NOT_IMPL, "Not Impl "+"abandonGame");
	}

	public GameTO findGame(long gameId) throws Exception {
		return GameRestHttpHelper.getInstance().findGame(gameId);
	}

	public boolean joinGame(long gameId, long teamId) throws Exception {
		return GameRestHttpHelper.getInstance().joinGame(gameId, teamId);
	}

	public GenericGameResponseTO updateLocation(int latitude, int longitude)
			throws Exception {
		return GameRestHttpHelper.getInstance().updateLocation(latitude, longitude);
	}

	public GamesTO findGamesByCity(String city, int startIndex, int count)
			throws Exception {
		// TODO Auto-generated method stub
		return GameRestHttpHelper.getInstance().findGamesByCity(city, 
				startIndex, count);
	}

	public GenericGameResponseTO startOrContinueGame(String username)
			throws Exception {
		return GameRestHttpHelper.getInstance().startOrContinueGame(username);
	}

	public boolean sendMessage(String receiverUser, String body)
			throws Exception {
		return GameRestHttpHelper.getInstance().sendMessage(receiverUser, body);
	}

	public List<TeamTO> findTeamsByGame(long gameId, int startIndex, int count)
			throws Exception {
		// TODO Auto-generated method stub
		return GameRestHttpHelper.getInstance().findTeamsByGame(gameId, 
				startIndex, count);
	}

	public GenericGameResponseTO takePlace(long id, int latitude,
			int longitude) throws Exception {
		// TODO Auto-generated method stub
		return GameRestHttpHelper.getInstance().takePlace(id, latitude, longitude);
	}

	@Override
	public TeamTO findTeam(long teamId) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
