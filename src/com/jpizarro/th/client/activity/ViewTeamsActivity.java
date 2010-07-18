package com.jpizarro.th.client.activity;

import java.util.List;

import com.jpizarro.th.R;
import com.jpizarro.th.client.common.dialogs.CommonDialogs;
import com.jpizarro.th.client.model.service.game.HttpGameServiceImpl;
import com.jpizarro.th.client.model.service.to.GameCTO;
import com.jpizarro.th.entity.Game;
import com.jpizarro.th.entity.Team;
import com.jpizarro.th.entity.User;

import es.sonxurxo.gpsgame.client.util.exception.ServerException;

import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ViewTeamsActivity extends ListActivity {
	
	private static final int FIND_TEAM_DIALOG_ID = CommonDialogs.FIRST_CUSTOM_DIALOG_ID;
	
	private User user;

	private long gameId;
	private Team[] teamArray;
	private FindGamesTask findGamesTask;
	private JoinGameTask joinGameTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		user = (User)getIntent().getExtras().getSerializable("user");
		gameId = getIntent().getExtras().getLong("gameId");
//		if (city != null) {
			launchFindGamesThread(gameId);
//		}
	}
	@Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		return CommonDialogs.createDialog(id, this);
	}
	
	private void launchFindGamesThread(long gameId) {
		findGamesTask = new FindGamesTask(gameId);
		Thread findGamesThread = new Thread(null, findGamesTask, "Login");
		findGamesThread.start();
		showDialog(CommonDialogs.CONNECTING_TO_SERVER_DIALOG_ID);	
		
	}
	private void doViewGames() {
		if (teamArray.length > 0)
			fillGames();		
	}
	
	private void fillGames() {
		// TODO Auto-generated method stub
		TAdapter myAdapter = new TAdapter(this, R.layout.row_game_list, teamArray);
		setListAdapter(myAdapter);
		
	}

	private class FindGamesTask implements Runnable {

		long gameId;
		HttpGameServiceImpl gameService;
		
		FindGamesTask(long gameId) {
			this.gameId = gameId;
			gameService = new HttpGameServiceImpl();
		}
				
		public void run() {
			
			FindGamesHandler handler = new FindGamesHandler(Looper.getMainLooper());
			Bundle data = new Bundle();
			Message msg = new Message();
			try {
				List<Team> gameCTO;
//				if (city != null) 
					gameCTO = gameService.findTeamsByGame(gameId, 
							0, 10);

//				data.putSerializable("gameArray", gameCTO.getGameList().
//						toArray(new Game [0]));
				data.putSerializable("teamArray", gameCTO.toArray( new Team [0] ));
//				data.putBoolean("hasMore", gameCTO.isHasMore());
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
			Team [] gameArray2 = 
				(Team [])msg.getData().getSerializable("teamArray");
			if (gameArray2 != null) {
//				hasMore = msg.getData().getBoolean("hasMore");
				teamArray = gameArray2;
				doViewGames();
			}
		}
	}

	public class TAdapter extends ArrayAdapter<Team>{

		public TAdapter(Context context, int textViewResourceId,
				Team[] objects) {
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
            Team g = this.getItem(position);
            if (g != null) {
            	TextView name = (TextView) v.findViewById(R.id.vg_game_name);
            	name.setText(g.getName());
            	
            	TextView tt = (TextView) v.findViewById(R.id.game_city);
            	tt.setText(g.getDescription());
            }
            
			return v;
			
		}
		
	}
	private void launchFindGamesThread(long gameId, long teamId) {
		joinGameTask = new JoinGameTask(gameId, teamId);
		Thread joinGameThread = new Thread(null, joinGameTask, "Login");
		joinGameThread.start();
		showDialog(CommonDialogs.CONNECTING_TO_SERVER_DIALOG_ID);	
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
//		super.onListItemClick(l, v, position, id);
		Team g = (Team) l.getAdapter().getItem(position);
		Toast.makeText(
                getBaseContext(),
                g.getId()+" "+g.getName()+" "+g.getDescription(),
                Toast.LENGTH_LONG).show();
		launchFindGamesThread(gameId, g.getId());
	
	}
	
	private class JoinGameHandler extends Handler {

		public JoinGameHandler(Looper looper) {
			super(looper);
		}
		
		@Override
		public void handleMessage(Message msg) {
			try {
				dismissDialog(CommonDialogs.CONNECTING_TO_SERVER_DIALOG_ID);
			} catch (IllegalArgumentException e) {
				
			}
			ServerException sE = 
				(ServerException)msg.getData().getSerializable("ServerException");
			if (sE	!= null) {
				CommonDialogs.errorMessage = sE.getLocalizedMessage();
				if (sE.getCode() == ServerException.INSTANCE_NOT_FOUND_CODE)
					showDialog(CommonDialogs.CONNECTION_LOST_DIALOG_ID);
				else
					showDialog(CommonDialogs.SERVER_ERROR_DIALOG_ID);
	        	return;
	        }
        	Exception e = 
	        	(Exception)msg.getData().getSerializable("Exception");
	        	if (e != null) {
	        		CommonDialogs.errorMessage = e.getLocalizedMessage();
		        	showDialog(CommonDialogs.CLIENT_ERROR_DIALOG_ID);
		        	return;
	        	}
			if (msg.getData().getBoolean("result")) {
				long gameId = msg.getData().getLong("gameId");
				long teamId = msg.getData().getLong("teamId");
				
				user.setGameId(gameId);
				Toast.makeText(
		                getBaseContext(),
		                "Joined",
		                Toast.LENGTH_LONG).show();
				Intent mIntent = new Intent();
				Bundle bundle = new Bundle();
				
				bundle.putSerializable("user", user);
				mIntent.putExtras(bundle);
		    	setResult(RESULT_OK, mIntent);
		    	finish();
			}
		}
	}
	
	private class JoinGameTask implements Runnable {

		long gameId, teamId;
		HttpGameServiceImpl gameService;
		
		JoinGameTask(long gameId, long teamId) {
			this.gameId = gameId;
			this.teamId = teamId;
			gameService = new HttpGameServiceImpl();
		}
		
		public void run() {
			
			JoinGameHandler handler = new JoinGameHandler(Looper.getMainLooper());
			Bundle data = new Bundle();
			Message msg = new Message();
			try {
				boolean result = 
					gameService.joinGame(gameId, teamId);
				data.putBoolean("result", result);
				data.putLong("gameId", gameId);
				data.putLong("teamId", teamId);
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
