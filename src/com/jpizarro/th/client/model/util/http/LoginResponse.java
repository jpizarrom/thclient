package com.jpizarro.th.client.model.util.http;

import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import com.jpizarro.th.lib.game.entity.UserTO;

public class LoginResponse implements KvmSerializable{
	private UserTO userTO;

	public Object getProperty(int arg0) {
		// TODO Auto-generated method stub
		return userTO;
	}

	public int getPropertyCount() {
		// TODO Auto-generated method stub
		return 1;
	}

	public void getPropertyInfo(int arg0, Hashtable arg1, PropertyInfo arg2) {
		arg2.name = "return";
		arg2.type = new UserTO().getClass();
		
	}

	public void setProperty(int arg0, Object arg1) {
		userTO = (UserTO)arg1;
		
	}

}
