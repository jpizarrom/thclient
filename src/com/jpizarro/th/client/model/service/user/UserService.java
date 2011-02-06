package com.jpizarro.th.client.model.service.user;

import com.jpizarro.th.lib.game.entity.UserTO;


public interface UserService {
	public UserTO login(String username, String clearPassword) 
	throws Exception;
	
	public void logout(String username) 
	throws Exception;
	
	public boolean changePassword(String oldPassword, String newPassword) 
	throws Exception;

}
