package com.jpizarro.th.client.model.service.game;

import java.util.List;

import com.jpizarro.th.client.model.service.to.GameCTO;
import com.jpizarro.th.client.model.service.to.response.GenericGameResponseTO;
import com.jpizarro.th.client.model.util.http.HttpHelper;
import com.jpizarro.th.entity.Game;
import com.jpizarro.th.entity.Team;

import es.sonxurxo.gpsgame.client.util.exception.ServerException;

public class HttpGameServiceImpl implements GameService{

	@Override
	public List<String> findCitiesWithGames() throws Exception {
		return HttpHelper.getInstance().findCitiesWithGames();
	}

	@Override
	public boolean abandonGame(long gameId) throws Exception {
		// TODO Auto-generated method stub
		throw new ServerException(ServerException.NOT_IMPL, "Not Impl "+"abandonGame");
	}

	@Override
	public Game findGame(long gameId) throws Exception {
		return HttpHelper.getInstance().findGame(gameId);
	}

	@Override
	public boolean joinGame(long gameId, long teamId) throws Exception {
		return HttpHelper.getInstance().joinGame(gameId, teamId);
	}

	@Override
	public GenericGameResponseTO updateLocation(int latitude, int longitude)
			throws Exception {
		return HttpHelper.getInstance().updateLocation(latitude, longitude);
	}

	@Override
	public GameCTO findGamesByCity(String city, int startIndex, int count)
			throws Exception {
		// TODO Auto-generated method stub
		return HttpHelper.getInstance().findGamesByCity(city, 
				startIndex, count);
	}

	@Override
	public GenericGameResponseTO startOrContinueGame(String username)
			throws Exception {
		return HttpHelper.getInstance().startOrContinueGame(username);
	}

	@Override
	public boolean sendMessage(String receiverUser, String body)
			throws Exception {
		return HttpHelper.getInstance().sendMessage(receiverUser, body);
	}

	@Override
	public List<Team> findTeamsByGame(long gameId, int startIndex, int count)
			throws Exception {
		// TODO Auto-generated method stub
		return HttpHelper.getInstance().findTeamsByGame(gameId, 
				startIndex, count);
	}

}
