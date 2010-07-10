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

import com.jpizarro.th.client.model.service.to.GameCTO;
import com.jpizarro.th.client.model.service.to.response.GenericGameResponseTO;
import com.jpizarro.th.client.model.util.xml.XMLToBussinessConversor;
import com.jpizarro.th.entity.Game;
import com.jpizarro.th.entity.User;

import es.sonxurxo.gpsgame.client.util.exception.ServerException;

public class HttpHelper {
	private String TAG = "HttpHelper";
	
	private final String SERVER_HOST_IP = "10.42.43.1";
	private final String SERVER_PORT = "8070";
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
	
	private final String LOGOUT_URL = "ws/logout";
	
	private final String UPDATE_LOCATION_URL = "ws/updateLocation";
	private final String LATITUDE_PARAMETER = "latitude";
	private final String LONGITUDE_PARAMETER = "longitude";
	
	private static HttpClient client = new DefaultHttpClient();
	private HttpUriRequest request;
	private HttpResponse response;
	private static HttpHelper instance;
	
	static {
		instance = new HttpHelper();
	}
	public static HttpHelper getInstance() {
		return instance;
	}
	
	public User login(String userName, String password) 
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

        	return XMLToBussinessConversor.toUser(entity);
        } catch (ServerException e) {
        	throw e;
        } catch(IOException e) {
        	throw new ServerException(ServerException.SERVER_OFFLINE_CODE, 
        			e.getMessage());
        } catch (Exception e) {
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
			
			return XMLToBussinessConversor.toCityList(entity);
		} catch (ServerException e) {
        	throw e;
        } catch(IOException e) {
        	throw new ServerException(ServerException.SERVER_OFFLINE_CODE, 
			e.getMessage());
        } catch (Exception e) {
        	throw e;
        }
		
	}
	
	public Game findGame(long gameId)  throws Exception {
		request = new HttpGet(FULL_ADDRESS + 
				FIND_GAME_BY_ID_URL + "?" + 
				GAME_ID_PARAMETER + "=" + gameId);

        try {        	
        	response = client.execute(request);
        	HttpEntity entity = response.getEntity();

        	return XMLToBussinessConversor.toGame(entity);

        } catch (ServerException e) {
        	throw e;
        } catch(IOException e) {
        	throw new ServerException(ServerException.SERVER_OFFLINE_CODE, 
			e.getMessage());
        } catch (Exception e) {
        	throw e;
        }
	}
	public boolean joinGame(long gameId) 
	throws Exception {
		throw new ServerException(ServerException.NOT_IMPL, "Not Impl: "+TAG+" joinGame");
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
        	
        	return XMLToBussinessConversor.toGenericGameResponseTO(entity);
        	
	 } catch (ServerException e) {
     	throw e;
     } catch(IOException e) {
     	throw new ServerException(ServerException.SERVER_OFFLINE_CODE, 
			e.getMessage());
     } catch (Exception e) {
     	throw e;
     }
	}
	
	public boolean sendMessage(String receiverLogin, String body) 
	throws Exception {
		throw new ServerException(ServerException.NOT_IMPL, "Not Impl: "+TAG+" sendMessage");
	}
	
	public GameCTO findGamesByCity(String city, int startIndex, int count) 
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

        	GameCTO gameCTO = XMLToBussinessConversor.toGameList(entity);
        	return gameCTO;
        } catch (ServerException e) {
        	throw e;
        } catch(IOException e) {
        	throw new ServerException(ServerException.SERVER_OFFLINE_CODE, 
			e.getMessage());
        } catch (Exception e) {
        	throw e;
        }
	}

}
