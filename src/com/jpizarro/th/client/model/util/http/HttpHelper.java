package com.jpizarro.th.client.model.util.http;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

import com.jpizarro.th.model.service.to.response.GenericGameResponseTO;
import com.jpizarro.th.entity.GameTO;
import com.jpizarro.th.entity.TeamTO;
import com.jpizarro.th.entity.UserTO;
import com.jpizarro.th.entity.list.CitiesTO;
import com.jpizarro.th.entity.list.GamesTO;
import com.jpizarro.th.entity.list.TeamsTO;
import com.jpizarro.th.util.xml.xstream.XStreamFactory;
import com.thoughtworks.xstream.XStream;

import es.sonxurxo.gpsgame.client.util.exception.ServerException;

public class HttpHelper implements THHelper{
	
	private String TAG = "HttpHelper";
	private final String SERVER_HOST_IP = "cs01.doingit.cl";
	private final String SERVER_PORT = "8081";
	
//	private final String SERVER_HOST_IP = "192.168.42.100";
//	private final String SERVER_HOST_IP = "10.42.43.1";
//	private final String SERVER_HOST_IP = "192.168.1.70";
//	private final String SERVER_PORT = "8070";
	private final String GAME_URL = "thserver";
	private final String FULL_ADDRESS = "http://" + SERVER_HOST_IP + ":" + 
		SERVER_PORT + "/" + GAME_URL + "/";
	
	private final String LOGIN_URL = "ws/login";
	private final String LOGIN_PARAMETER = "login";
	private final String CLEAR_PASSWORD_PARAMETER = "password";
	
	private final String FIND_CITIES_WITH_GAMES_URL = "ws/findCitiesWithGames";
	private final String FIND_GAMES_BY_CITY_URL = "ws/findGamesByCity";
	private final String FIND_GAME_BY_ID_URL = "ws/findGameById";
	
	private final String CITY_PARAMETER = "city";
	private final String START_INDEX_PARAMETER = "startIndex";
	private final String COUNT_PARAMETER = "count";
	private final String GAME_ID_PARAMETER = "gameId";
	private final String TEAM_ID_PARAMETER = "teamId";
	
	private final String LOGOUT_URL = "ws/logout";
	
	private final String START_OR_CONTINUEGAME_URL = "ws/GameState";
	
	private final String UPDATE_LOCATION_URL = "ws/updateLocation";
	private final String LATITUDE_PARAMETER = "latitude";
	private final String LONGITUDE_PARAMETER = "longitude";
	
	private final String SEND_MESSAGE_URL = "ws/sendMessage";
	private final String RECEIVER_USER_PARAMETER = "receiverUser";
	private final String BODY_PARAMETER = "body";
	
	private final String FIND_TEAMS_BY_GAME_URL = "ws/findTeamsByGame";
	private final String JOIN_GAME_URL = "ws/joinGame";
	
	private final String TAKE_PLACE_URL = "ws/takePlace";
	private final String PLACE_ID_PARAMETER = "placeId";

	
	private static HttpClient client = new DefaultHttpClient();
	private HttpUriRequest request;
	private HttpResponse response;
	private static HttpHelper instance;
	private XStream xstream;
	
	static {
		instance = new HttpHelper();
		
	}
	public static HttpHelper getInstance() {
		return instance;
	}
	protected XStream getXStream()
	{
		
		if (xstream == null)
		{
			return XStreamFactory.createXStream();
		}
		
		return xstream;
	}
	public UserTO login(String userName, String password) 
	throws Exception {
		String encodedLogin = URLEncoder.encode(userName.replace("%2B", "+"), "UTF-8");
		String encodedPassword = URLEncoder.encode(password.replace("%2B", "+"), "UTF-8");
        request = new HttpGet(FULL_ADDRESS + 
        		LOGIN_URL + "?" + 
        		LOGIN_PARAMETER + "=" + encodedLogin + "&" + 
        		CLEAR_PASSWORD_PARAMETER + "=" + encodedPassword);
        Log.d(TAG, request.getURI().toString());

        try {        	
        	response = client.execute(request);
        	HttpEntity entity = response.getEntity();

        	return (UserTO)this.getXStream().fromXML(entity.getContent());    	
//        	return XMLToBussinessConversor.toUser(entity);
//        } catch (ServerException e) {
//        	throw e;
        } catch(IOException e) {
        	throw new ServerException(ServerException.SERVER_OFFLINE_CODE, 
        			e.getMessage());
        } catch (Exception e) {
        	e.printStackTrace();
        	throw e;
        }
		
	}
	
	public void logout(String login) 
	throws Exception {
		String encodedLogin = URLEncoder.encode(login.replace("%2B", "+"), "UTF-8");
        request = new HttpGet(FULL_ADDRESS + 
        		LOGOUT_URL + "?" + 
        		LOGIN_PARAMETER + "=" + encodedLogin);
        Log.d(TAG, request.getURI().toString());
        try {
        	response = client.execute(request);
        } catch(IOException e) {
        	throw new ServerException(ServerException.SERVER_OFFLINE_CODE, 
			e.getMessage());
        } catch (Exception e) {
        	throw e;
        }
	}
	
	public boolean registerUser() 
	throws Exception {
		throw new ServerException(ServerException.NOT_IMPL, "Not Impl: "+TAG+" registerUser");
	}
	public boolean updateUser() 
	throws Exception {
		throw new ServerException(ServerException.NOT_IMPL, "Not Impl: "+TAG+" updateUser");
	}
	
	public boolean changePassword(String oldPassword, String newPassword) 
	throws Exception {
		throw new ServerException(ServerException.NOT_IMPL, "Not Impl: "+TAG+" changePassword");
	}

	public List<String> findCitiesWithGames() throws Exception {
		request = new HttpGet(FULL_ADDRESS + 
				FIND_CITIES_WITH_GAMES_URL);
		 Log.d(TAG, request.getURI().toString());
		
		try {
			response = client.execute(request);
			HttpEntity entity = response.getEntity();
			
			CitiesTO cities = (CitiesTO)this.getXStream().fromXML(entity.getContent());
			return cities.getCities();
//			return XMLToBussinessConversor.toCityList(entity);
//		} catch (ServerException e) {
//        	throw e;
        } catch(IOException e) {
        	throw new ServerException(ServerException.SERVER_OFFLINE_CODE, 
			e.getMessage());
        } catch (Exception e) {
        	e.printStackTrace();
        	throw e;
        }
		
	}
	
	public GameTO findGame(long gameId)  throws Exception {
		request = new HttpGet(FULL_ADDRESS + 
				FIND_GAME_BY_ID_URL + "?" + 
				GAME_ID_PARAMETER + "=" + gameId);

        try {        	
        	response = client.execute(request);
        	HttpEntity entity = response.getEntity();

        	return (GameTO)this.getXStream().fromXML(entity.getContent());
//        	return XMLToBussinessConversor.toGame(entity);
//        } catch (ServerException e) {
//        	throw e;
        } catch(IOException e) {
        	throw new ServerException(ServerException.SERVER_OFFLINE_CODE, 
			e.getMessage());
        } catch (Exception e) {
        	e.printStackTrace();
        	throw e;
        }
	}
	public boolean joinGame(long gameId, long teamId) 
throws Exception {
		
		String encodedGameId = URLEncoder.encode(
				String.valueOf(gameId).replace("%2B", "+"), "UTF-8");
		
		String encodedTeamId = URLEncoder.encode(
				String.valueOf(teamId).replace("%2B", "+"), "UTF-8");
		
		request = new HttpPost(FULL_ADDRESS + 
				JOIN_GAME_URL + "?" + 
        		GAME_ID_PARAMETER + "=" + encodedGameId +"&" +
        		TEAM_ID_PARAMETER + "=" + encodedTeamId
				);
		Log.d("TESTSSSSS", request.getURI().toString());
		
        try {        	
        	response = client.execute(request);
        	HttpEntity entity = response.getEntity();

        	return (Boolean)this.getXStream().fromXML(entity.getContent());
//        	return XMLToBussinessConversor.toBooleanOrExceptionJoin(entity);
//        } catch (ServerException e) {
//        	throw e;
        } catch(IOException e) {
        	throw new ServerException(ServerException.SERVER_OFFLINE_CODE, 
			e.getMessage());
        } catch (Exception e) {
        	e.printStackTrace();
        	throw e;
        }
	}
	
	
	public GenericGameResponseTO updateLocation(int latitude, int longitude) 
	throws Exception {
		request = new HttpPost(FULL_ADDRESS + 
				UPDATE_LOCATION_URL + "?" + 
        		LATITUDE_PARAMETER + "=" + String.valueOf(latitude) + "&" +  
        		LONGITUDE_PARAMETER + "=" + String.valueOf(longitude));
		try{
			response = client.execute(request);
        	HttpEntity entity = response.getEntity();
        	
        	return (GenericGameResponseTO)this.getXStream().fromXML(entity.getContent());
//        	return XMLToBussinessConversor.toGenericGameResponseTO(entity);
//	 } catch (ServerException e) {
//     	throw e;
     } catch(IOException e) {
     	throw new ServerException(ServerException.SERVER_OFFLINE_CODE, 
			e.getMessage());
     } catch (Exception e) {
     	throw e;
     }
	}
	
	public boolean sendMessage(String receiverLogin, String body) 
	throws Exception {
		String encodedBody = 
			URLEncoder.encode(body.replace("%2B", "+"), "UTF-8");
		
		if (receiverLogin != null) {
			String encodedReceiverLogin = 
				URLEncoder.encode(receiverLogin.replace("%2B", "+"), "UTF-8");	

			request = new HttpPost(FULL_ADDRESS + 
	        		SEND_MESSAGE_URL + "?" + 
	        		RECEIVER_USER_PARAMETER + "=" + encodedReceiverLogin + "&" +  
	        		BODY_PARAMETER + "=" + encodedBody);
		}
		else {
			request = new HttpGet(FULL_ADDRESS + 
	        		SEND_MESSAGE_URL + "?" + 
	        		BODY_PARAMETER + "=" + encodedBody);
		}

        try {        	
        	response = client.execute(request);
        	HttpEntity entity = response.getEntity();

        	return (Boolean)this.getXStream().fromXML(entity.getContent());
//        	return XMLToBussinessConversor.toBooleanOrExceptionSend(entity);
//        } catch (ServerException e) {
//        	throw e;
        } catch(IOException e) {
        	throw new ServerException(ServerException.SERVER_OFFLINE_CODE, 
			e.getMessage());
        } catch (Exception e) {
        	throw e;
        }
	}
	
	public GamesTO findGamesByCity(String city, int startIndex, int count) 
	throws Exception {
		String encodedCity = URLEncoder.encode(city.replace("%2B", "+"), "UTF-8");
		request = new HttpGet(FULL_ADDRESS + 
				FIND_GAMES_BY_CITY_URL + "?" + 
        		CITY_PARAMETER +  "=" + encodedCity + "&" + 
        		START_INDEX_PARAMETER +  "=" + startIndex + "&" + 
        		COUNT_PARAMETER +  "=" + count);
		Log.d("TESTSSSSS", request.getURI().toString());

        try {        	
        	response = client.execute(request);
        	HttpEntity entity = response.getEntity();

        	GamesTO response = (GamesTO)this.getXStream().fromXML(entity.getContent());
        	return response;
//        	return new GameCTO(response.getGames(),false);
//        	GameCTO gameCTO = XMLToBussinessConversor.toGameList(entity);
//        	return gameCTO;
//        } catch (ServerException e) {
//        	throw e;
        } catch(IOException e) {
        	throw new ServerException(ServerException.SERVER_OFFLINE_CODE, 
			e.getMessage());
        } catch (Exception e) {
        	e.printStackTrace();
        	throw e;
        }
	}
	
	public GenericGameResponseTO startOrContinueGame(String login) 
	throws Exception {
		request = new HttpGet(FULL_ADDRESS + 
				START_OR_CONTINUEGAME_URL );
		
		Log.d(this.getClass().getName(), request.getURI().toString());
        try {        	
        	response = client.execute(request);
        	HttpEntity entity = response.getEntity();

        	return (GenericGameResponseTO)this.getXStream().fromXML(entity.getContent());
//        	return XMLToBussinessConversor.toGenericGameResponseTO(entity);
//        } catch (ServerException e) {
//        	throw e;
        } catch(IOException e) {
        	throw new ServerException(ServerException.SERVER_OFFLINE_CODE, 
			e.getMessage());
        } catch (Exception e) {
        	e.printStackTrace();
        	throw e;
        }
	}

	public List<TeamTO> findTeamsByGame(long gameId, int startIndex, int count) 
	throws Exception {
		// TODO Auto-generated method stub
//		String encodedCity = URLEncoder.encode(city.replace("%2B", "+"), "UTF-8");
		request = new HttpGet(FULL_ADDRESS + 
				FIND_TEAMS_BY_GAME_URL + "?" + 
				GAME_ID_PARAMETER +  "=" + gameId + "&" + 
        		START_INDEX_PARAMETER +  "=" + startIndex + "&" + 
        		COUNT_PARAMETER +  "=" + count);
		Log.d("TESTSSSSS", request.getURI().toString());

        try {        	
        	response = client.execute(request);
        	HttpEntity entity = response.getEntity();
        	
        	TeamsTO response = (TeamsTO)this.getXStream().fromXML(entity.getContent());
        	return response.getTeams();
//        	List<Team> teams = XMLToBussinessConversor.toTeamList(entity);
//        	return teams;
//        } catch (ServerException e) {
//        	throw e;
        } catch(IOException e) {
        	throw new ServerException(ServerException.SERVER_OFFLINE_CODE, 
			e.getMessage());
        } catch (Exception e) {
        	e.printStackTrace();
        	throw e;
        }
	}

	public GenericGameResponseTO takePlace(long id, int latitude, int longitude) 
	throws Exception{
			request = new HttpPost(FULL_ADDRESS + 
					TAKE_PLACE_URL + "?" + 
					PLACE_ID_PARAMETER + "=" + String.valueOf(id) + "&" +  
	        		LATITUDE_PARAMETER + "=" + String.valueOf(latitude) + "&" +  
	        		LONGITUDE_PARAMETER + "=" + String.valueOf(longitude));
			Log.d("TESTSSSSS", request.getURI().toString());
			
			try{
				response = client.execute(request);
	        	HttpEntity entity = response.getEntity();
	        	
	        	return (GenericGameResponseTO)this.getXStream().fromXML(entity.getContent());
//	        	return XMLToBussinessConversor.toGenericGameResponseTO(entity);
	        	
//		 } catch (ServerException e) {
//	     	throw e;
	     } catch(IOException e) {
	     	throw new ServerException(ServerException.SERVER_OFFLINE_CODE, 
				e.getMessage());
	     } catch (Exception e) {
	    	 e.printStackTrace();
	     	throw e;
	     }
	}

}
