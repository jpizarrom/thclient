package com.jpizarro.th.client.model.util.http;

import java.util.List;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.AndroidHttpTransport;
import org.ksoap2.transport.HttpTransportSE;

import com.jpizarro.th.model.service.to.response.GenericGameResponseTO;
import com.jpizarro.th.util.xml.xstream.XStreamFactory;
import com.jpizarro.th.entity.GameTO;
import com.jpizarro.th.entity.TeamTO;
import com.jpizarro.th.entity.UserTO;
import com.jpizarro.th.entity.list.GamesTO;
import com.thoughtworks.xstream.XStream;

import android.util.Log;

public class SoapHelper implements THHelper{
    /** Called when the activity is first created. */
	private static final String SOAP_ACTION = "joinGame";
    private static final String METHOD_NAME = "joinGame";
    private static final String METHOD_LOGIN = "login";
    private static final String FIND_USER_BY_ID = "findUserById";
    
    static final String NAMESPACE = "http://axis.view.server.th.jpizarro.com";
    
//  private static final String NAMESPACE = "http://soap";
    // !!!!! IMPORTANT!!!!! THE URL OF THE CoLDFUSION WEBSERVER NOT LOCALHOST BECAUSE LOCALHOST IS THE ANDROID EMULATOR !!!!!
    
//    private static final String HOST = "10.42.43.1";
//    private static final String HOST = "192.168.1.70";
    private static final String HOST = "192.168.42.100";
    
//    private static final String URL_USER = "http://192.168.1.70:8070/thserver/services/WSUserService?wsdl";
//    private static final String URL_GAME = "http://192.168.1.70:8070/thserver/services/WSGameService?wsdl";
    
    private static final String URL = "http://"+HOST+":8070/thserver/services/";
    private static final String USER_SERVICE = "WSUserService";
    private static final String GAME_SERVICE = "WSGameService";
    
    private XStream xstream;
    private static SoapHelper instance;
    
    private SoapObject rpc;
    private HttpTransportSE service = new HttpTransportSE("");
    SoapSerializationEnvelope envelope;

    static {
		instance = new SoapHelper();
	}
	public static SoapHelper getInstance() {
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
	void prepareService(String url){
		service = new AndroidHttpTransport(url);
	}
	void prepareCall(String serviceName,String soapAction){
//		service = new AndroidHttpTransport(URL+serviceName);
		service.setUrl(URL+serviceName);
//		service.debug  = true;
//		service.setSoapAction(soapAction);
		rpc=new SoapObject(NAMESPACE,soapAction);
		envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(rpc);
	}
	public UserTO login(String userName, String password) 
	throws Exception {
		prepareCall(USER_SERVICE,SoapHelper.METHOD_LOGIN);
//		SoapObject request = new SoapObject(NAMESPACE,SoapHelper.METHOD_LOGIN);
		
		rpc.addProperty("username",userName);
		rpc.addProperty("password",password);
       
//        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
//        envelope.dotNet = true;
//        envelope.setOutputSoapObject(rpc);
//        envelope.addMapping(NAMESPACE, "LoginResultTO",LoginResponse.class);
//        envelope.encodingStyle = SoapSerializationEnvelope.XSI;
        
//        AndroidHttpTransport androidHttpTransport = new AndroidHttpTransport (URL_USER);
        
        try {
//        	androidHttpTransport.call(SoapHelper.METHOD_LOGIN, envelope);
            service.call(null, envelope);
            SoapObject result ;
            result = (SoapObject) envelope.getResponse();
            String res = result.toString();
            
//            Object oresult = (Object) envelope.getResponse();
//            String res = oresult.toString();
//            result = (SoapObject)oresult;
//            LoginResponse lr = (LoginResponse)envelope.getResponse();
            
            UserTO ret = new UserTO();
            
            ret.setUsername(result.getProperty("username").toString());
            ret.setUserId(Long.parseLong(result.getProperty("userId").toString()));
////            
            Log.v(getClass().getName(),"----------------------------------");
//            Log.v(getClass().getName(),result.toString());
//            Log.v(getClass().getName(),"----------------------------------");
//            Log.v(getClass().getName(),oresult.toString());
//            Log.v(getClass().getName(),"----------------------------------");
//            Log.v(getClass().getName(), envelope.bodyIn.toString());
//            Log.v(getClass().getName(),"----------------------------------");
            
	        Log.v(getClass().getName(),res);
	        Log.v(getClass().getName(),"----------------------------------");
//            this.getXStream().fromXML(oresult.toString());

            return ret;
           
          } catch(Exception E) {
        	  E.printStackTrace();
            throw E;
          }
	}
	
	public UserTO findUserById(long gameId)  throws Exception {
		String method = SoapHelper.FIND_USER_BY_ID;
		SoapObject request = new SoapObject(NAMESPACE,method);
		
		request.addProperty("userId",gameId);
       
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        
        AndroidHttpTransport androidHttpTransport = new AndroidHttpTransport (URL+USER_SERVICE);
        
        try {
            androidHttpTransport.call(method, envelope);
            SoapObject result = null;
            result = (SoapObject) envelope.getResponse();
            UserTO ret = new UserTO();
            
            ret.setUserId(Long.parseLong(result.getProperty("userId").toString()));
            ret.setUsername(result.getProperty("username").toString());
            
            ret.setGameId( Long.parseLong(result.getProperty("gameId").toString()));
            ret.setLatitude( Integer.parseInt(result.getProperty("latitude").toString()));
            ret.setLatitude( Integer.parseInt(result.getProperty("longitude").toString()));
            
            Log.v(getClass().getName(),ret.toString());
            Log.v(getClass().getName(),envelope.bodyIn.toString());

            return ret;
           
          } catch(Exception E) {
            throw E;
          }
	}
	
    public boolean joinGame(long gameId, long teamId) 
    throws Exception {
    	boolean r = false;
    	Object result = null;
		SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
		
		request.addProperty("username","as");
		request.addProperty("gameId",gameId);
		request.addProperty("teamId",teamId);
       
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        
//      envelope.addMapping(ClienWS.NAMESPACE, ClienWS.METHOD_NAME, Perfil.class);
        
        AndroidHttpTransport androidHttpTransport = new AndroidHttpTransport (URL +GAME_SERVICE);
        
        try {
            androidHttpTransport.call(SOAP_ACTION, envelope);

//            SoapObject result = (SoapObject)envelope.getResponse();
//            Perfil result = (Perfil)envelope.getResponse();
//            Boolean result = (Boolean)envelope.stringToBoolean((String) envelope.getResponse());
            result = envelope.getResponse();
            r = SoapEnvelope.stringToBoolean(result.toString());
            
          } catch(Exception E) {
//          	E.printStackTrace();
                   
            Log.v(getClass().getName(),"ERROR!!:" + E.getClass().getName() + ": " + E.getMessage());
          }
          return r;
    }

	public void logout(String login) throws Exception {
		// TODO Auto-generated method stub
		
	}

	public boolean registerUser() throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean updateUser() throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean changePassword(String oldPassword, String newPassword)
			throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	public List<String> findCitiesWithGames() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public GameTO findGame(long gameId) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public GenericGameResponseTO updateLocation(int latitude, int longitude)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean sendMessage(String receiverLogin, String body)
			throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	public GamesTO findGamesByCity(String city, int startIndex, int count)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public GenericGameResponseTO startOrContinueGame(String login)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public List<TeamTO> findTeamsByGame(long gameId, int startIndex, int count)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public GenericGameResponseTO takePlace(long id, int latitude, int longitude)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
