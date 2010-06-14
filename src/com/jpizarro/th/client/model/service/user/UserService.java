package com.jpizarro.th.client.model.service.user;

import com.jpizarro.th.entity.User;


public interface UserService {
	public User login(String login, String clearPassword) 
	throws Exception;

}
