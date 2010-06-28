package com.jpizarro.th.activity;

import com.jpizarro.th.R;
import com.jpizarro.th.client.common.dialogs.CommonDialogs;
import com.jpizarro.th.client.model.service.game.HttpGameServiceImpl;
import com.jpizarro.th.client.model.service.to.GameCTO;
import com.jpizarro.th.entity.Game;

import es.sonxurxo.gpsgame.client.util.exception.ServerException;

import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class ViewGamesActivity extends ListActivity {

	private String city = null;
	private Game[] gameArray;
	private FindGamesTask findGamesTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		city = getIntent().getExtras().getString("city");
		if (city != null) {
			launchFindGamesThread(city);
		}
	}
	@Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		return CommonDialogs.createDialog(id, this);
	}
	
	private void launchFindGamesThread(String city) {
		findGamesTask = new FindGamesTask(city);
		Thread findGamesThread = new Thread(null, findGamesTask, "Login");
		findGamesThread.start();
		showDialog(CommonDialogs.CONNECTING_TO_SERVER_DIALOG_ID);	
		
	}
	private void doViewGames() {
		if (gameArray.length > 0)
			fillGames();		
	}
	
	private void fillGames() {
		// TODO Auto-generated method stub
		TAdapter myAdapter = new TAdapter(this, R.layout.row_game_list, gameArray);
		setListAdapter(myAdapter);
		
	}

	private class FindGamesTask implements Runnable {

		String city = null;
		HttpGameServiceImpl gameService;
		
		FindGamesTask(String city) {
			this.city = city;
			gameService = new HttpGameServiceImpl();
		}
				
		public void run() {
			
			FindGamesHandler handler = new FindGamesHandler(Looper.getMainLooper());
			Bundle data = new Bundle();
			Message msg = new Message();
			try {
				GameCTO gameCTO;
//				if (city != null) 
					gameCTO = gameService.findGamesByCity(city, 
							0, 10);

				data.putSerializable("gameArray", gameCTO.getGameList().
						toArray(new Game [0]));
				data.putBoolean("hasMore", gameCTO.isHasMore());
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
	private class FindGamesHandler extends Handler {

		public FindGamesHandler(Looper looper) {
			super(looper);
		}
		
		@Override
		public void handleMessage(Message msg) {
			try {
				dismissDialog(CommonDialogs.CONNECTING_TO_SERVER_DIALOG_ID);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
			ServerException sE = 
				(ServerException)msg.getData().getSerializable("ServerException");
			if (sE	!= null) {
				CommonDialogs.errorMessage = sE.getMessage();
	        	showDialog(CommonDialogs.SERVER_ERROR_DIALOG_ID);
	        	return;
	        }
        	Exception e = 
	        	(Exception)msg.getData().getSerializable("Exception");
	        	if (e != null) {
	        		CommonDialogs.errorMessage = e.getMessage();
		        	showDialog(CommonDialogs.CLIENT_ERROR_DIALOG_ID);
		        	return;
	        	}
			Game [] gameArray2 = 
				(Game [])msg.getData().getSerializable("gameArray");
			if (gameArray2 != null) {
//				hasMore = msg.getData().getBoolean("hasMore");
				gameArray = gameArray2;
				doViewGames();
			}
		}
	}

	public class TAdapter extends ArrayAdapter<Game>{

		public TAdapter(Context context, int textViewResourceId,
				Game[] objects) {
			super(context, textViewResourceId, objects);
			// TODO Auto-generated constructor stub
		}

		public TAdapter(Context context, int textViewResourceId) {
			super(context, textViewResourceId);
			// TODO Auto-generated constructor stub

		}
		public View getView(int position, View convertView, ViewGroup
				parent)
		{
			View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.row_game_list, null);
            }
            Game g = this.getItem(position);
            if (g != null) {
            	TextView tt = (TextView) v.findViewById(R.id.game_city);
            	tt.setText(g.getCity());
            }
            
			return v;
			
		}
		
	}
	

}
