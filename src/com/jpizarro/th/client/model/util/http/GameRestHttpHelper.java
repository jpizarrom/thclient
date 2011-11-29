package com.jpizarro.th.client.model.util.http;

import com.jpizarro.th.lib.game.util.GameRestURL;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import android.util.Log;

import com.jpizarro.th.lib.game.entity.GameTO;
import com.jpizarro.th.lib.game.entity.TeamTO;
import com.jpizarro.th.lib.game.entity.UserTO;
import com.jpizarro.th.lib.game.entity.list.CitiesTO;
import com.jpizarro.th.lib.game.entity.list.GamesTO;
import com.jpizarro.th.lib.game.entity.list.TeamsTO;
import com.jpizarro.th.lib.game.entity.response.GenericGameResponseTO;
import com.jpizarro.th.lib.game.util.GameRestURL;
import com.jpizarro.th.lib.game.util.xml.xstream.XStreamFactory;
import com.jpizarro.th.lib.message.entity.MessageTO;
import com.jpizarro.th.lib.message.util.MessageRestURL;
import com.jpizarro.th.lib.team.util.TeamRestURL;
import com.jpizarro.th.lib.user.util.UserRestURL;
import com.thoughtworks.xstream.XStream;

import es.sonxurxo.gpsgame.client.util.exception.ServerException;

public class GameRestHttpHelper implements THHelper{
	protected static final Logger LOG = Logger.getLogger(GameRestHttpHelper.class.getCanonicalName());

	private static final boolean DEBUG = false;

	private String TAG = "HttpHelper";
	
//	public static final String SERVER_HOST_IP = "192.168.16.135";
//	public static final String SERVER_HOST_IP = "192.168.1.71";
//	public String SERVER_HOST_IP = "192.168.1.70";
//	public String SERVER_PORT = "8070";
//	public String SERVICE = "thserver-game/app";
	
	public String SERVER_HOST_IP = "";
	public String SERVER_PORT = "80";
	public String SERVICE = "app";
	
	public String URL_BASE = "http://" + SERVER_HOST_IP + ":" + 
	SERVER_PORT + "/" + SERVICE;
	public String URL = "http://" + SERVER_HOST_IP + ":" + 
	SERVER_PORT + "/" + SERVICE + "/" + GameRestURL.ENTITY;

	private static GameRestHttpHelper instance;

	// Create a new RestTemplate instance
	RestTemplate restTemplate ;

	public GameRestHttpHelper() {
		super();
		// The HttpComponentsClientHttpRequestFactory uses the
		// org.apache.http package to make network requests
		restTemplate = new RestTemplate();
		restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
	}
	static {
		instance = new GameRestHttpHelper();

	}
	public static GameRestHttpHelper getInstance() {
		return instance;
	}
	
	@Override
	public void setServer(String server){
		SERVER_HOST_IP = server;
		URL_BASE = "http://" + SERVER_HOST_IP + ":" + 
		SERVER_PORT + "/" + SERVICE;
		URL = "http://" + SERVER_HOST_IP + ":" + 
		SERVER_PORT + "/" + SERVICE + "/" + GameRestURL.ENTITY;
	}

	private List<NameValuePair> stripNulls(NameValuePair... nameValuePairs) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		for (int i = 0; i < nameValuePairs.length; i++) {
			NameValuePair param = nameValuePairs[i];
			if (param.getValue() != null) {
				if (DEBUG) LOG.log(Level.FINE, "Param: " + param);
				params.add(param);
			}
		}
		return params;
	}
	private String fullUrl(String url) {
		return URL + url;
	}

	public UserTO login(String userName, String password) 
	throws Exception {
		UserTO r = null;
		com.jpizarro.th.lib.user.entity.UserTO t = null;
		t = restTemplate.getForObject(
				this.URL_BASE+"/test"+"/"+UserRestURL.ENTITY+UserRestURL.LOGIN+"?username={username}&password={password}", com.jpizarro.th.lib.user.entity.UserTO.class,
				userName, password);
		r = restTemplate.getForObject(
				this.URL_BASE+"/"+com.jpizarro.th.lib.game.util.UserRestURL.ENTITY+com.jpizarro.th.lib.game.util.UserRestURL.ENTITY_ID, UserTO.class, t.getUserId());
		r.setUser(t);
		//		r = new UserTO();
//		r.setUserId(t.getUserId());
//		r.setGameId(gameId)
		
		return r;	
	}

	public void logout(String login) 
	throws Exception {
		throw new ServerException(ServerException.NOT_IMPL, "Not Impl: "+TAG+" logout");
	}

	public boolean registerUser() 
	throws Exception {
		throw new ServerException(ServerException.NOT_IMPL, "Not Impl: "+TAG+" registerUser");
	}
	public boolean updateUser() 
	throws Exception {
		throw new ServerException(ServerException.NOT_IMPL, "Not Impl: "+TAG+" updateUser");
	}

	public boolean changePassword(long userId, String oldPassword, String newPassword) 
	throws Exception {
		throw new ServerException(ServerException.NOT_IMPL, "Not Impl: "+TAG+" changePassword");
	}

	public List<String> findCitiesWithGames() throws Exception {
		List<String> r = null;
		r = restTemplate.getForObject(fullUrl(GameRestURL.FIND_CITIES_WITH_GAMES), CitiesTO.class).getCities();
		return r;		
	}

	public GameTO findGame(long gameId)  throws Exception {
		GameTO r = null;
		r = restTemplate.getForObject(fullUrl(GameRestURL.ENTITY_ID), GameTO.class, gameId);
		return r;
	}

	public boolean joinGame(long gameId, long teamId, long userId) 
	throws Exception {
		boolean r = false;
		com.jpizarro.th.lib.team.entity.TeamTO t = restTemplate.getForObject(this.URL_BASE+"/test"+"/"+TeamRestURL.ENTITY+TeamRestURL.ADD_USER_TO_TEAM, com.jpizarro.th.lib.team.entity.TeamTO.class, teamId, userId);
		if (t!=null)
			r = true;
		return r;
	}


	public GenericGameResponseTO updateLocation(long userId, int latitude, int longitude) 
	throws Exception {
		try{
		boolean t = restTemplate.getForObject(
				this.URL_BASE+"/test"+"/"+UserRestURL.ENTITY+UserRestURL.ENTITY_ID+UserRestURL.UPDATE_LOCATION+"?latitude={latitude}&longitude={longitude}", 
				Boolean.class, userId, latitude, longitude);
		}catch(Exception e){
			
		}
//		throw new ServerException(ServerException.NOT_IMPL, "Not Impl: "+TAG+" updateLocation");
		return null;
	}

	public boolean sendMessage(long userId, String receiverLogin, String body) 
	throws Exception {
		MessageTO to = new MessageTO();
		to.setMessageBody(body);
		to.setSender(userId);
//		Serializer serializer = new Persister();
//		OutputStream b = new OutputStream()
//	    {
//	        private StringBuilder string = new StringBuilder();
//	        @Override
//	        public void write(int b) throws IOException {
//	            this.string.append((char) b );
//	        }
//
//	        //Netbeans IDE automatically overrides this toString()
//	        public String toString(){
//	            return this.string.toString();
//	        }
//	    };
//		try {
//			serializer.write(to, b);
//			System.out.println(b.toString());
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		com.jpizarro.th.lib.user.entity.UserTO r = restTemplate.getForObject(
				this.URL_BASE+"/test"+"/"+UserRestURL.ENTITY+UserRestURL.FIND_USER_BY_USERNAME, 
				com.jpizarro.th.lib.user.entity.UserTO.class, receiverLogin);
		com.jpizarro.th.lib.message.entity.UserTO mu = new com.jpizarro.th.lib.message.entity.UserTO();
		mu.setUserId(r.getUserId());
		to.getReceivers().add(mu);
		
		try{
		restTemplate.postForEntity(
				this.URL_BASE+"/test"+"/"+MessageRestURL.ENTITY, 
				to, MessageTO.class);
		return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
//		throw new ServerException(ServerException.NOT_IMPL, "Not Impl: "+TAG+" sendMessage");
	}

	public GamesTO findGamesByCity(String city, int startIndex, int count) 
	throws Exception {
		GamesTO r = null;
		r = restTemplate.getForObject(fullUrl(GameRestURL.FIND_GAMES_BY_CITY), GamesTO.class, city);
		return r;	
	}

	public GenericGameResponseTO startOrContinueGame(String login) 
	throws Exception {
		throw new ServerException(ServerException.NOT_IMPL, "Not Impl: "+TAG+" startOrContinueGame");
	}
	public GenericGameResponseTO startOrContinueGame(long gameId, long userId, long teamId) 
	throws Exception {
		GenericGameResponseTO r = null;
		r = restTemplate.getForObject(fullUrl(GameRestURL.START_OR_CONTINUEGAME_URL+"?userId={userId}&teamId={teamId}"), 
				GenericGameResponseTO.class, gameId, userId, teamId);
		return r;	
	}

	public List<TeamTO> findTeamsByGame(long gameId, int startIndex, int count) 
	throws Exception {
		List<TeamTO> r = null;
		try{
			r = restTemplate.getForObject(fullUrl(GameRestURL.FIND_TEAMS_BY_GAME), TeamsTO.class, gameId).getTeams();
		}catch (Exception e){
			e.printStackTrace();
		}
		return r;	
	}

	@Override
	public GenericGameResponseTO takePlace(long userId, long placeId, int latitude, int longitude, long gameId, long teamId) 
	throws Exception{
		GenericGameResponseTO r = null;
			r = restTemplate.getForObject(
					fullUrl(GameRestURL.ENTITY_ID+"/takePlace/{placeId}"+"?userId={userId}&teamId={teamId}"), 
					GenericGameResponseTO.class, gameId, placeId, userId, teamId);
//			throw new ServerException(ServerException.NOT_IMPL, "Not Impl: "+TAG+" updateLocation");
			return r;
	}

	@Override
	public TeamTO findTeam(long teamId) throws Exception {
		throw new ServerException(ServerException.NOT_IMPL, "Not Impl: "+TAG+" findTeam");
	}

	@Override
	public boolean joinGame(long gameId, long teamId) throws Exception {
		throw new ServerException(ServerException.NOT_IMPL, "Not Impl: "+TAG+" joinGame(long gameId, long teamId)");
	}

}
