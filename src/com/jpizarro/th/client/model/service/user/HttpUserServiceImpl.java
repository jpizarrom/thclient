package com.jpizarro.th.client.model.service.user;

import com.jpizarro.th.client.model.util.http.GameRestHttpHelper;
import com.jpizarro.th.lib.game.entity.UserTO;

public class HttpUserServiceImpl implements UserService{

	public UserTO login(String login, String clearPassword)
			throws Exception {
		return GameRestHttpHelper.getInstance().login(login, clearPassword);
	}

	public void logout(String username) throws Exception {
		GameRestHttpHelper.getInstance().logout(username);
	}

	public boolean changePassword(String oldPassword, String newPassword)
			throws Exception {
		return GameRestHttpHelper.getInstance().changePassword(oldPassword, newPassword);
	}
	
	public boolean registerUser() throws Exception {
		return GameRestHttpHelper.getInstance().registerUser();
	}

	public boolean updateUser()
			throws Exception {
		return GameRestHttpHelper.getInstance().updateUser();
	}

}
