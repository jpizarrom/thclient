package com.jpizarro.th.client.model.util.http;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

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
	
	private final String LOGOUT_URL = "ws/logout";
	
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
	
	public Game findGame(String gameId)  throws Exception {
		throw new ServerException(ServerException.NOT_IMPL, "Not Impl: "+TAG+" findGame");
	}
	public boolean joinGame(long gameId) 
	throws Exception {
		throw new ServerException(ServerException.NOT_IMPL, "Not Impl: "+TAG+" joinGame");
	}
	public GenericGameResponseTO updateLocation(int latitude, int longitude) 
	throws Exception {
		throw new ServerException(ServerException.NOT_IMPL, "Not Impl: "+TAG+" updateLocation");
	}
	
	public boolean sendMessage(String receiverLogin, String body) 
	throws Exception {
		throw new ServerException(ServerException.NOT_IMPL, "Not Impl: "+TAG+" sendMessage");
	}
	

}
