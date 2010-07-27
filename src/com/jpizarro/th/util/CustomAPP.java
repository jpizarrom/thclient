package com.jpizarro.th.util;

import com.jpizarro.th.client.model.service.game.Axis2GameServiceImpl;
import com.jpizarro.th.client.model.service.game.GameService;
import com.jpizarro.th.client.model.service.user.Axis2UserServiceImpl;
import com.jpizarro.th.client.model.service.user.UserService;

public class CustomAPP {
	public static UserService getUserService(){
		return new Axis2UserServiceImpl();
	}
	
	public static GameService getGameService(){
		return new Axis2GameServiceImpl();
	}

}
