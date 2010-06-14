package com.jpizarro.th.client.model.util.http;

import java.io.IOException;
import java.net.URLEncoder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

import com.jpizarro.th.client.model.util.xml.XMLToBussinessConversor;
import com.jpizarro.th.entity.User;

import es.sonxurxo.gpsgame.client.util.exception.ServerException;

public class HttpHelper {
	
	private final String SERVER_HOST_IP = "10.42.43.1";
	private final String SERVER_PORT = "8070";
	private final String GAME_URL = "thserver";
	private final String FULL_ADDRESS = "http://" + SERVER_HOST_IP + ":" + 
		SERVER_PORT + "/" + GAME_URL + "/";
	
	private final String LOGIN_URL = "ws/login";
	private final String LOGIN_PARAMETER = "login";
	private final String CLEAR_PASSWORD_PARAMETER = "password";
	
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
        Log.d("TESTSSSSS", request.getURI().toString());

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

}
