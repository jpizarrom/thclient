package com.jpizarro.th.client.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.jpizarro.th.client.model.service.game.GameService;
import com.jpizarro.th.client.model.service.game.HttpGameServiceImpl;
import com.jpizarro.th.client.model.service.user.HttpUserServiceImpl;
import com.jpizarro.th.client.model.service.user.UserService;

public class CustomAPP {
	public static UserService getUserService(){
//		return new Axis2UserServiceImpl();
		return new HttpUserServiceImpl();
	}
	
//	public static GameService getGameService(){
////		return new Axis2GameServiceImpl();
//		return new HttpGameServiceImpl();
////		return null;
//	}
	public static GameService getGameService(Context applicationContext){
//		return new Axis2GameServiceImpl();
		SharedPreferences settings = PreferenceManager
		.getDefaultSharedPreferences(applicationContext);
//		
        String server = settings.getString("game_server", "thserver-game.cloudfoundry.com");
//        instance.setServer(server);
        GameService gs = new HttpGameServiceImpl();
        gs.setServer(server);
		return gs;
	}

}
