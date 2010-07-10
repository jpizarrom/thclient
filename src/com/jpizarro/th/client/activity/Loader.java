package com.jpizarro.th.client.activity;

import java.util.ArrayList;

import com.jpizarro.th.client.common.dialogs.CommonDialogs;
import com.jpizarro.th.client.model.service.game.HttpGameServiceImpl;
import com.jpizarro.th.client.model.service.to.response.GenericGameResponseTO;
import com.jpizarro.th.client.model.service.to.response.InGameUserInfoTO;
import com.jpizarro.th.client.model.service.user.HttpUserServiceImpl;
import com.jpizarro.th.entity.Competitor;
import com.jpizarro.th.entity.Game;
import com.jpizarro.th.entity.Goal;
import com.jpizarro.th.entity.Hint;
import com.jpizarro.th.entity.Team;
import com.jpizarro.th.entity.Test;
import com.jpizarro.th.entity.User;

import android.app.Dialog;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class Loader extends ListActivity {
	
	private TestTask testTask;
	User user;
	GenericGameResponseTO ggrTO;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		final ArrayList<String> list = new ArrayList<String>();
		
		list.add("Test");
		list.add("Login");
		list.add("Game List");
		
		this.setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list));
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		switch(position){
			case 0:
				test();
			break;
			case 1:
				this.startActivity(new Intent(this, Login.class));
				break;
			case 2:
				this.startActivity(new Intent(this, GameList.class));
				break;
		}
	}
	@Override
	protected Dialog onCreateDialog(int id) {
		return CommonDialogs.createDialog(id, this);
	}
	
	private void test(){			
		testTask = new TestTask();
		Thread testThread = new Thread(null, testTask, "Login");
		testThread.start();
	}
	
	private void doTest(){			
//		Toast.makeText(this, Test.test(), Toast.LENGTH_LONG).show();
		Toast.makeText(this, user.getUserName(), Toast.LENGTH_LONG).show();
		for(InGameUserInfoTO in: ggrTO.getInGameUserInfoTOs())
			Toast.makeText(this, in.getUsername(), Toast.LENGTH_LONG).show();
	}
	
	
	private class TestHandler extends Handler {
		public TestHandler(Looper mainLooper) {
			super(mainLooper);
		}
		@Override
		public void handleMessage(Message msg) {
			Exception e = 
	        	(Exception)msg.getData().getSerializable("Exception");
	        	if (e != null) {
//	        		dismissDialog(CommonDialogs.CONNECTING_TO_SERVER_DIALOG_ID);
	        		e.printStackTrace();
	        		CommonDialogs.errorMessage = e.toString();
		        	showDialog(CommonDialogs.CLIENT_ERROR_DIALOG_ID);
		        	return;
	        	}
			
			user = (User)msg.getData().getSerializable("user");
			ggrTO = (GenericGameResponseTO)msg.getData().getSerializable("ggrTO");
			
			doTest();
		}
	}
		
	private class TestTask implements Runnable {
		HttpUserServiceImpl userService;
		HttpGameServiceImpl gameService;
		
		TestTask() {
			userService = new HttpUserServiceImpl();
			gameService = new HttpGameServiceImpl();
		}

		@Override
		public void run() {
			TestHandler handler = new TestHandler(Looper.getMainLooper());
			Bundle data = new Bundle();
			Message msg = new Message();
			
			User user;
			try {
				user = userService.login("j", "j");
				GenericGameResponseTO ggrTO = gameService.updateLocation(10, 10);
				
				data.putSerializable("user", user);
				data.putSerializable("ggrTO", ggrTO);
				msg.setData(data);
				handler.sendMessage(msg);
			} catch (Exception e) {
	        	data.putSerializable("Exception", e);
//	        	e.printStackTrace();
	        	msg.setData(data);
				handler.sendMessage(msg);
	        }
			
			
			
		}
	}

}
