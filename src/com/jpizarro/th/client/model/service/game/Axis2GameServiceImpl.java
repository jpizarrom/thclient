package com.jpizarro.th.client.model.service.game;

import java.util.List;

import com.jpizarro.th.lib.game.entity.GameTO;
import com.jpizarro.th.lib.game.entity.TeamTO;
import com.jpizarro.th.lib.game.entity.list.GamesTO;
import com.jpizarro.th.lib.game.entity.response.GenericGameResponseTO;
import com.jpizarro.th.client.model.util.http.GameRestHttpHelper;


import es.sonxurxo.gpsgame.client.util.exception.ServerException;

public class Axis2GameServiceImpl implements GameService{

	@Override
	public List<String> findCitiesWithGames() throws Exception {
		return GameRestHttpHelper.getInstance().findCitiesWithGames();
	}

	@Override
	public boolean abandonGame(long userId, long gameId) throws Exception {
		// TODO Auto-generated method stub
		throw new ServerException(ServerException.NOT_IMPL, "Not Impl "+"abandonGame");
	}

	@Override
	public GameTO findGame(long gameId) throws Exception {
		return GameRestHttpHelper.getInstance().findGame(gameId);
	}

	@Override
	public boolean joinGame(long gameId, long teamId) throws Exception {
		return GameRestHttpHelper.getInstance().joinGame(gameId, teamId);
	}

	@Override
	public GenericGameResponseTO updateLocation(long userId, int latitude, int longitude)
			throws Exception {
		return GameRestHttpHelper.getInstance().updateLocation(userId, latitude, longitude);
	}

	@Override
	public GamesTO findGamesByCity(String city, int startIndex, int count)
			throws Exception {
		// TODO Auto-generated method stub
		return GameRestHttpHelper.getInstance().findGamesByCity(city, 
				startIndex, count);
	}

	@Override
	public GenericGameResponseTO startOrContinueGame(String username)
			throws Exception {
		return GameRestHttpHelper.getInstance().startOrContinueGame(username);
	}

	@Override
	public boolean sendMessage(long userId, String receiverUser, String body)
			throws Exception {
		return GameRestHttpHelper.getInstance().sendMessage(userId, receiverUser, body);
	}

	@Override
	public List<TeamTO> findTeamsByGame(long gameId, int startIndex, int count)
			throws Exception {
		// TODO Auto-generated method stub
		return GameRestHttpHelper.getInstance().findTeamsByGame(gameId, 
				startIndex, count);
	}

	@Override
	public GenericGameResponseTO takePlace(long userId, long placeId, int latitude,
			int longitude, long gameId, long teamId) throws Exception {
		// TODO Auto-generated method stub
//		return GameRestHttpHelper.getInstance().takePlace(userId, placeId, latitude, longitude);
		return null;
	}

	@Override
	public TeamTO findTeam(long teamId) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean joinGame(long gameId, long teamId, long userId)
			throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

}
