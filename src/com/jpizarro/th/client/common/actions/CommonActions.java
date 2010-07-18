package com.jpizarro.th.client.common.actions;

import com.jpizarro.th.client.common.dialogs.CommonDialogs;
import com.jpizarro.th.client.model.service.user.HttpUserServiceImpl;
import com.jpizarro.th.util.CustomResultCodes;

import es.sonxurxo.gpsgame.client.util.exception.ServerException;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;


public class CommonActions {

	private static LogoutTask logoutTask;
	
	public static void launchLogoutThread(String username, Activity from) {
		CommonActions cA = new CommonActions();
		logoutTask = cA.new LogoutTask(username, from);
		Thread logoutThread = new Thread(null, logoutTask, "Login");
		logoutThread.start();
		from.showDialog(CommonDialogs.CONNECTING_TO_SERVER_DIALOG_ID);
	}
	private void doLogout(Activity from) {
		from.setResult(CustomResultCodes.LOGOUT_RESULT_CODE);
		from.finish();
	}
	
	private class LogoutHandler extends Handler {

		private Activity from;
		
		public LogoutHandler(Looper looper, Activity from) {
			super(looper);
			this.from = from;
		}
		
		@Override
		public void handleMessage(Message msg) {
			ServerException sE = 
				(ServerException)msg.getData().getSerializable("ServerException");
			if (sE	!= null) {
				try {
					from.dismissDialog(CommonDialogs.CONNECTING_TO_SERVER_DIALOG_ID);
				} catch (Exception e) {
					
				}
				CommonDialogs.errorMessage = sE.getMessage();
				from.showDialog(CommonDialogs.SERVER_ERROR_DIALOG_ID);
	        	return;
	        }
        	Exception e = 
	        	(ServerException)msg.getData().getSerializable("Exception");
	        	if (e != null) {
	        		try {
						from.dismissDialog(CommonDialogs.CONNECTING_TO_SERVER_DIALOG_ID);
					} catch (Exception ee) {
						
					}
					CommonDialogs.errorMessage = e.getLocalizedMessage();
	        		from.showDialog(CommonDialogs.CLIENT_ERROR_DIALOG_ID);
		        	return;
	        	}
			if (msg.getData().getBoolean("result")) {
				doLogout(from);
			}
		}
	}
	private class LogoutTask implements Runnable {

		String login;
		Activity from;
		
		LogoutTask(String login, Activity from) {
			this.login = login;
			this.from = from;
		}
		
		public void run() {
			
			LogoutHandler handler = new LogoutHandler(Looper.getMainLooper(), from);
			Bundle data = new Bundle();
			Message msg = new Message();
			try {
				HttpUserServiceImpl userService = new HttpUserServiceImpl();
				userService.logout(login);
				data.putBoolean("result", true);
				msg.setData(data);
				handler.sendMessage(msg);
				
			} catch (ServerException e) {
	        	data.putSerializable("ServerException", e);
	        	msg.setData(data);
				handler.sendMessage(msg);
	        } catch (Exception e) {
	        	data.putSerializable("Exception", e);
	        	msg.setData(data);
				handler.sendMessage(msg);
	        }
		}
	}

}
