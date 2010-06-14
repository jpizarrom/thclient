package com.jpizarro.th.client.model.service.user;

import com.jpizarro.th.client.model.util.http.HttpHelper;
import com.jpizarro.th.entity.User;

public class HttpUserServiceImpl implements UserService{

	@Override
	public User login(String login, String clearPassword)
			throws Exception {
		return HttpHelper.getInstance().login(login, clearPassword);
	}


}
