package com.jpizarro.th.activity;

import java.util.List;

import com.jpizarro.th.R;
import com.jpizarro.th.client.common.dialogs.CommonDialogs;
import com.jpizarro.th.client.model.service.game.HttpGameServiceImpl;
import com.jpizarro.th.entity.User;

import es.sonxurxo.gpsgame.client.cv.util.constants.CustomResultCodes;
import es.sonxurxo.gpsgame.client.util.exception.ServerException;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class FindGamesActivity extends Activity {
	public static final int SEARCH_METHOD_CITY = 0;
	public static final int SEARCH_METHOD_THIS_CITY = 1;
	public static final int SEARCH_METHOD_LOCATION = 2;
	
	private static final int LOGOUT_ID = Menu.FIRST;
	private static final int FIND_GAMES_ID = Menu.FIRST + 1;
	
	private User user;
	
	private Spinner spinner;
	
	private String [] cities;
	private int method;
	
	private FindCitiesTask findCitiesTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.find_games_page);
		
		spinner = (Spinner)findViewById(R.id.fg_sp_cities);
		
		method = getIntent().getExtras().getInt("method");
		
		user = (User)getIntent().getExtras().getSerializable("user");
		
		if (method == SEARCH_METHOD_CITY) {
			launchFindCitiesThread();
		}
	}
	
	@Override
    protected Dialog onCreateDialog(int id) {
		return CommonDialogs.createDialog(id, this);
    }
	
	private void launchFindCitiesThread() {
		findCitiesTask = new FindCitiesTask();
		Thread thread = new Thread(null, findCitiesTask, "Login");
		thread.start();
		showDialog(CommonDialogs.CONNECTING_TO_SERVER_DIALOG_ID);
	}
	private void fillSpinnerWithCities() {
		if (cities.length == 0) {
			setResult(CustomResultCodes.FG_NO_CITIES_RESULT_CODE);
			finish();
		}
		spinner.setEnabled(true);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
		        android.R.layout.simple_spinner_dropdown_item,
		            cities);

		spinner.setAdapter(adapter);
	}
	
	private class CustomHandler extends Handler {

		public CustomHandler(Looper mainLooper) {
			super(mainLooper);
		}
		@Override
		public void handleMessage(Message msg) {
    		dismissDialog(CommonDialogs.CONNECTING_TO_SERVER_DIALOG_ID);
			ServerException sE = 
				(ServerException)msg.getData().getSerializable("ServerException");
			if (sE	!= null) {
				CommonDialogs.errorMessage = sE.getMessage();
				showDialog(CommonDialogs.SERVER_ERROR_DIALOG_ID);
	        	spinner.setEnabled(false);
	        	return;
	        }
        	Exception e = 
	        	(Exception)msg.getData().getSerializable("Exception");
	        	if (e != null) {
	        		CommonDialogs.errorMessage = e.getLocalizedMessage();
		        	showDialog(CommonDialogs.CLIENT_ERROR_DIALOG_ID);
		        	spinner.setEnabled(false);
		        	return;
	        	}
			String [] cities2 = (String [])msg.getData().getSerializable("cities");
			if (cities2 != null) {
				cities = cities2;
				fillSpinnerWithCities();
			}
		}
		
	}
	private class FindCitiesTask implements Runnable {
		
		public void run() {
			
			CustomHandler handler = new CustomHandler(Looper.getMainLooper());
			Bundle data = new Bundle();
			Message msg = new Message();
			try {
				HttpGameServiceImpl gameService = 
					new HttpGameServiceImpl();
				List<String> citiesList = 
					gameService.findCitiesWithGames();
				data.putSerializable("cities", citiesList.toArray(new String [0]));
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
