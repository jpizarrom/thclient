package com.jpizarro.th.client.model.service.user;

import com.jpizarro.th.client.model.util.http.HttpHelper;
import com.jpizarro.th.entity.User;

public class HttpUserServiceImpl implements UserService{

	@Override
	public User login(String login, String clearPassword)
			throws Exception {
		return HttpHelper.getInstance().login(login, clearPassword);
	}

	@Override
	public void logout(String username) throws Exception {
		HttpHelper.getInstance().logout(username);
	}

	@Override
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
