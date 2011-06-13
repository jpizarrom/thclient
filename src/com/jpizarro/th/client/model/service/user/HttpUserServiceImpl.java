package com.jpizarro.th.client.model.service.user;

import com.jpizarro.th.client.model.util.http.GameRestHttpHelper;
import com.jpizarro.th.lib.game.entity.UserTO;

public class HttpUserServiceImpl implements UserService{

	@Override
	public UserTO login(String login, String clearPassword)
			throws Exception {
		return GameRestHttpHelper.getInstance().login(login, clearPassword);
	}

	@Override
	public void logout(String username) throws Exception {
		GameRestHttpHelper.getInstance().logout(username);
	}

	@Override
	public boolean changePassword(long userId, String oldPassword, String newPassword)
			throws Exception {
		return GameRestHttpHelper.getInstance().changePassword(userId, oldPassword, newPassword);
	}
	
//	@Override
	public boolean registerUser() throws Exception {
		return GameRestHttpHelper.getInstance().registerUser();
	}

//	@Override
	public boolean updateUser()
			throws Exception {
		return GameRestHttpHelper.getInstance().updateUser();
	}

}
