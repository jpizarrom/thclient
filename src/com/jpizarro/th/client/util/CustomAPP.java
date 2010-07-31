package com.jpizarro.th.client.util;

import com.jpizarro.th.client.model.service.game.GameService;
import com.jpizarro.th.client.model.service.game.HttpGameServiceImpl;
import com.jpizarro.th.client.model.service.user.HttpUserServiceImpl;
import com.jpizarro.th.client.model.service.user.UserService;

public class CustomAPP {
	public static UserService getUserService(){
//		return new Axis2UserServiceImpl();
		return new HttpUserServiceImpl();
	}
	
	public static GameService getGameService(){
//		return new Axis2GameServiceImpl();
		return new HttpGameServiceImpl();
	}

}
