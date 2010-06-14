package com.jpizarro.th.client.model.service.user;

import com.jpizarro.th.entity.User;


public interface UserService {
	public User login(String username, String clearPassword) 
	throws Exception;
	
	public void logout(String username) 
	throws Exception;
	
	public boolean changePassword(String oldPassword, String newPassword) 
	throws Exception;

}
