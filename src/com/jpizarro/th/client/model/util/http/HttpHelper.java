package com.jpizarro.th.client.model.util.http;

import java.io.IOException;
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
import com.jpizarro.th.lib.game.util.xml.xstream.XStreamFactory;
import com.thoughtworks.xstream.XStream;

import es.sonxurxo.gpsgame.client.util.exception.ServerException;

public class HttpHelper implements THHelper{
	protected static final Logger LOG = Logger.getLogger(HttpHelper.class.getCanonicalName());


	private static final boolean DEBUG = false;


	private String TAG = "HttpHelper";
	//	private final String SERVER_HOST_IP = "cs01.doingit.cl";
	//	private final String SERVER_PORT = "8081";

//	private final String SERVER_HOST_IP = "192.168.42.81";
	private final String SERVER_HOST_IP = "192.168.2.103";
//	private final String SERVER_HOST_IP = "office.doingit.cl";
	//	private final String SERVER_HOST_IP = "10.42.43.1";
	//	private final String SERVER_HOST_IP = "192.168.1.70";
//	private final String SERVER_PORT = "8070";
	private final String SERVER_PORT = "8075";
//	private final String SERVER_PORT = "8043";
	private final String URL_SERVICE = "thserver-game/app/games";
	//	private final String FULL_ADDRESS = "http://" + SERVER_HOST_IP + ":" + 
	//		SERVER_PORT + "/" + URL_SERVICE + "/";


	private final String LOGIN_URL = "login";
	private final String LOGIN_PARAMETER = "login";
	private final String CLEAR_PASSWORD_PARAMETER = "password";

	private final String FIND_CITIES_WITH_GAMES_URL = "CitiesWithGames";
	private final String FIND_GAMES_BY_CITY_URL = "findGamesByCity";
	private final String FIND_GAME_BY_ID_URL = "findGameById";

	private final String CITY_PARAMETER = "city";
	private final String START_INDEX_PARAMETER = "startIndex";
	private final String COUNT_PARAMETER = "count";
	private final String GAME_ID_PARAMETER = "gameId";
	private final String TEAM_ID_PARAMETER = "teamId";

	private final String LOGOUT_URL = "logout";

	private final String START_OR_CONTINUEGAME_URL = "GameState";

	private final String UPDATE_LOCATION_URL = "updateLocation";
	private final String LATITUDE_PARAMETER = "latitude";
	private final String LONGITUDE_PARAMETER = "longitude";

	private final String SEND_MESSAGE_URL = "sendMessage";
	private final String RECEIVER_USER_PARAMETER = "receiverUser";
	private final String BODY_PARAMETER = "body";

	private final String FIND_TEAMS_BY_GAME_URL = "findTeamsByGame";
	private final String FIND_TEAM_BY_ID_URL = "findTeamById";
	private final String JOIN_GAME_URL = "joinGame";

	private final String TAKE_PLACE_URL = "takePlace";
	private final String PLACE_ID_PARAMETER = "placeId";


	private static HttpClient client = new DefaultHttpClient();
	private HttpRequestBase request;
	private HttpResponse response;
	private static HttpHelper instance;
	private XStream xstream;


	private static final String CLIENT_VERSION_HEADER = "User-Agent";
	private String mClientVersion;

	private String mApiBaseUrl = "http://" + SERVER_HOST_IP + ":" + 
	SERVER_PORT + "/" + URL_SERVICE + "/";

	public HttpHelper() {
		super();
		mClientVersion = CLIENT_VERSION_HEADER;
	}
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

	public HttpResponse executeHttpRequest(HttpRequestBase httpRequest) throws IOException {
		if (DEBUG) LOG.log(Level.FINE, "executing HttpRequest for: "
				+ httpRequest.getURI().toString());
		Log.d(TAG, httpRequest.getURI().toString());
		try {
			client.getConnectionManager().closeExpiredConnections();
			return client.execute(httpRequest);
		} catch (IOException e) {
			httpRequest.abort();
			throw e;
		}
	}
	public Object executeHttpRequest(HttpRequestBase httpRequest, XStream xstream)
	throws Exception{

		HttpResponse response = executeHttpRequest(httpRequest);

		int statusCode = response.getStatusLine().getStatusCode();
		switch (statusCode) {
		case 200:
			return xstream.fromXML(response.getEntity().getContent()); 
		}
		throw new ServerException(ServerException.SERVER_OFFLINE_CODE, 
				"executeHttpRequest");
	}

	public HttpGet createHttpGet(String url, NameValuePair... nameValuePairs) {
		if (DEBUG) LOG.log(Level.FINE, "creating HttpGet for: " + url);
		String query = URLEncodedUtils.format(stripNulls(nameValuePairs), HTTP.UTF_8);
		HttpGet httpGet = new HttpGet(url + "?" + query);
		httpGet.addHeader(CLIENT_VERSION_HEADER, mClientVersion);
		if (DEBUG) LOG.log(Level.FINE, "Created: " + httpGet.getURI());
		return httpGet;
	}

	public HttpPost createHttpPost(String url, NameValuePair... nameValuePairs) {
		if (DEBUG) LOG.log(Level.FINE, "creating HttpPost for: " + url);
		HttpPost httpPost = new HttpPost(url);
		httpPost.addHeader(CLIENT_VERSION_HEADER, mClientVersion);
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(stripNulls(nameValuePairs), HTTP.UTF_8));
		} catch (UnsupportedEncodingException e1) {
			throw new IllegalArgumentException("Unable to encode http parameters.");
		}
		if (DEBUG) LOG.log(Level.FINE, "Created: " + httpPost);
		return httpPost;
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
		return mApiBaseUrl   + url;
	}

	public UserTO login(String userName, String password) 
	throws Exception {
		request = createHttpGet(fullUrl(LOGIN_URL) //
				,new BasicNameValuePair(LOGIN_PARAMETER, userName) //
		,new BasicNameValuePair(CLEAR_PASSWORD_PARAMETER, password) //
		);
		return (UserTO) executeHttpRequest(request, this.getXStream() );
	}

	public void logout(String login) 
	throws Exception {
		request = createHttpGet(fullUrl(LOGOUT_URL) //
				,new BasicNameValuePair(LOGIN_PARAMETER, login) //
		);
		executeHttpRequest(request, this.getXStream());
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
		request = createHttpGet(fullUrl(FIND_CITIES_WITH_GAMES_URL) //
		);
		List<String> r = null;
		try{
//		r = ((CitiesTO) executeHttpRequest(request, this.getXStream() )).getCities();
		// Create a new RestTemplate instance
		RestTemplate restTemplate = new RestTemplate();

		// The HttpComponentsClientHttpRequestFactory uses the
		// org.apache.http package to make network requests
		restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
		r = restTemplate.getForObject(fullUrl(FIND_CITIES_WITH_GAMES_URL), CitiesTO.class).getCities();
		}catch (Exception e){
			e.printStackTrace();
		}
		return r;		
	}

	public GameTO findGame(long gameId)  throws Exception {
		request = createHttpGet(fullUrl(FIND_GAME_BY_ID_URL) //
				,new BasicNameValuePair(GAME_ID_PARAMETER, String.valueOf(gameId)) //
		);
		return (GameTO) executeHttpRequest(request, this.getXStream());
	}

	public boolean joinGame(long gameId, long teamId) 
	throws Exception {
		request = createHttpGet(fullUrl(JOIN_GAME_URL) //
				,new BasicNameValuePair(GAME_ID_PARAMETER, String.valueOf(gameId)) //
		,new BasicNameValuePair(TEAM_ID_PARAMETER, String.valueOf(teamId)) //
		);
		return (Boolean) executeHttpRequest(request, this.getXStream());
	}


	public GenericGameResponseTO updateLocation(int latitude, int longitude) 
	throws Exception {
		request = createHttpGet(fullUrl(UPDATE_LOCATION_URL) //
				,new BasicNameValuePair(LATITUDE_PARAMETER, String.valueOf(latitude)) //
		,new BasicNameValuePair(LONGITUDE_PARAMETER, String.valueOf(longitude)) //
		);
		return (GenericGameResponseTO) executeHttpRequest(request, this.getXStream());
	}

	public boolean sendMessage(String receiverLogin, String body) 
	throws Exception {
		if (receiverLogin != null) 
			request = createHttpGet(fullUrl(SEND_MESSAGE_URL) //
					,new BasicNameValuePair(RECEIVER_USER_PARAMETER, receiverLogin) //
			,new BasicNameValuePair(BODY_PARAMETER, body) //
			);
		else
			request = createHttpGet(fullUrl(SEND_MESSAGE_URL) //
					,new BasicNameValuePair(BODY_PARAMETER, body) //
			);

		return (Boolean) executeHttpRequest(request, this.getXStream());
	}

	public GamesTO findGamesByCity(String city, int startIndex, int count) 
	throws Exception {
		request = createHttpGet(fullUrl(FIND_GAMES_BY_CITY_URL) //
				,new BasicNameValuePair(CITY_PARAMETER, city) //
		,new BasicNameValuePair(START_INDEX_PARAMETER, String.valueOf(startIndex)) //
		,new BasicNameValuePair(COUNT_PARAMETER, String.valueOf(count)) //
		);
		return (GamesTO) executeHttpRequest(request, this.getXStream());
	}

	public GenericGameResponseTO startOrContinueGame(String login) 
	throws Exception {
		request = createHttpGet(fullUrl(START_OR_CONTINUEGAME_URL) //
		);
		return (GenericGameResponseTO) executeHttpRequest(request, this.getXStream());
	}

	public List<TeamTO> findTeamsByGame(long gameId, int startIndex, int count) 
	throws Exception {
		request = createHttpGet(fullUrl(FIND_TEAMS_BY_GAME_URL) //
				,new BasicNameValuePair(GAME_ID_PARAMETER, String.valueOf(gameId)) //
		,new BasicNameValuePair(START_INDEX_PARAMETER, String.valueOf(startIndex)) //
		,new BasicNameValuePair(COUNT_PARAMETER, String.valueOf(count)) //
		);
		return ((TeamsTO) executeHttpRequest(request, this.getXStream())).getTeams();
	}

	public GenericGameResponseTO takePlace(long id, int latitude, int longitude) 
	throws Exception{
		request = createHttpGet(fullUrl(TAKE_PLACE_URL) //
				,new BasicNameValuePair(PLACE_ID_PARAMETER, String.valueOf(id)) //
		,new BasicNameValuePair(LATITUDE_PARAMETER, String.valueOf(latitude)) //
		,new BasicNameValuePair(LONGITUDE_PARAMETER, String.valueOf(longitude)) //
		);
		return (GenericGameResponseTO) executeHttpRequest(request, this.getXStream());
	}

	@Override
	public TeamTO findTeam(long teamId) throws Exception {
		request = createHttpGet(fullUrl(FIND_TEAM_BY_ID_URL) //
				,new BasicNameValuePair(TEAM_ID_PARAMETER, String.valueOf(teamId)) //
		);
		return (TeamTO) executeHttpRequest(request, this.getXStream());
	}

}
