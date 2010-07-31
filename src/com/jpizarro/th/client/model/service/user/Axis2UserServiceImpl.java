package com.jpizarro.th.client.model.service.user;

import com.jpizarro.th.client.model.util.http.HttpHelper;
import com.jpizarro.th.client.model.util.http.SoapHelper;
import com.jpizarro.th.entity.UserTO;

public class Axis2UserServiceImpl implements UserService{

	public UserTO login(String login, String clearPassword)
			throws Exception {
		UserTO user =  SoapHelper.getInstance().login(login, clearPassword);
		return findUserById(user.getPlaceId());
	}
	
	
	private UserTO findUserById(long gameId)  throws Exception {
		return SoapHelper.getInstance().findUserById(gameId);
	}

	public void logout(String username) throws Exception {
		HttpHelper.getInstance().logout(username);
	}

	public boolean changePassword(String oldPassword, String newPassword)
			throws Exception {
		return HttpHelper.getInstance().changePassword(oldPassword, newPassword);
	}
	
	public boolean registerUser() throws Exception {
		return HttpHelper.getInstance().registerUser();
	}

	public boolean updateUser()
			throws Exception {
		return HttpHelper.getInstance().updateUser();
	}

}
