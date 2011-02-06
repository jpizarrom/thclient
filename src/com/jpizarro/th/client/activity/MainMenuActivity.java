package com.jpizarro.th.client.activity;

import com.jpizarro.th.R;
import com.jpizarro.th.client.common.actions.CommonActions;
import com.jpizarro.th.client.common.dialogs.CommonDialogs;
import com.jpizarro.th.client.util.CustomResultCodes;
import com.jpizarro.th.lib.game.entity.GameTO;
import com.jpizarro.th.lib.game.entity.TeamTO;
import com.jpizarro.th.lib.game.entity.UserTO;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class MainMenuActivity extends Activity {
	private static final int CHANGE_PASSWORD_ID = Menu.FIRST;
	private static final int UPDATE_ID = CHANGE_PASSWORD_ID + 1;
	private static final int VIEW_HISTORY_ID = UPDATE_ID + 1;
	private static final int FIND_GAMES_ID = VIEW_HISTORY_ID + 1;
	private static final int LOGOUT_ID = FIND_GAMES_ID + 1;
	private static final int PLAY_ID = LOGOUT_ID + 1;


	private static final int FIND_GAMES_REQUEST_CODE = 0;
	private static final int PLAY_REQUEST_CODE = FIND_GAMES_REQUEST_CODE + 1;

	private static final int FIND_GAMES_DIALOG_ID = CommonDialogs.FIRST_CUSTOM_DIALOG_ID;

	private TextView usernameView, gamenameView, teamnameView;

	private UserTO user;
	private GameTO game;
	private TeamTO team;


	private String city = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_info_page);

		usernameView = (TextView)findViewById(R.id.username);
		gamenameView = (TextView)findViewById(R.id.gamename);
		teamnameView = (TextView)findViewById(R.id.mm_teamname);
	}
	@Override
	protected void onResume() {
		showDialog(CommonDialogs.CONNECTING_TO_SERVER_DIALOG_ID);
		super.onResume();

		user = (UserTO)getIntent().getExtras().getSerializable("user");
		fillPersonalInfo();

		if ( user.getTeamId() > 0 )
			team = (TeamTO)getIntent().getExtras().getSerializable("team");
		if (team != null)
			fillTeamInfo();

		if ( user.getGameId() > 0 )
			game = (GameTO)getIntent().getExtras().getSerializable("game");

		if (game != null)
			fillGameInfo();

		dismissDialog(CommonDialogs.CONNECTING_TO_SERVER_DIALOG_ID);
	}

	private void fillGameInfo() {
		// TODO Auto-generated method stub
		gamenameView.setText(game.getName());
	}
	private void fillTeamInfo() {
		// TODO Auto-generated method stub
		teamnameView.setText(team.getName());
	}
	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog d = CommonDialogs.createDialog(id, this);
		if (d != null)
			return d;

		switch(id) {
		case FIND_GAMES_DIALOG_ID:
			CharSequence [] items;
			if (city != null)
				items = new CharSequence[]{
					getResources().getString(R.string.by_city)
					,getResources().getString(R.string.by_distance)
					,getResources().getString(R.string.here_in) + " " + city
			};
			else 
				items = new CharSequence[]{getResources().getString(R.string.by_city)};

			return new AlertDialog.Builder(this)
			//            .setTitle(R.string.pi_find_games_choose)
			.setItems(items, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {

					int method = FindGamesActivity.SEARCH_METHOD_CITY;
					if (which == 0)
						method = FindGamesActivity.SEARCH_METHOD_CITY;
					else if (which == 1)
						method = FindGamesActivity.SEARCH_METHOD_LOCATION;
					else if (which == 2)
						method = FindGamesActivity.SEARCH_METHOD_THIS_CITY;
					doFindGames(method);
				}
			})
			.create();
		}

		return null;
	}

	private void fillPersonalInfo() {
		usernameView.setText(user.getUsername());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu);

		menu.add(0, LOGOUT_ID, 0, R.string.logout)
		//    	.setIcon(R.drawable.logout)
		;
		menu.add(0, FIND_GAMES_ID, 0, R.string.find_games)
		//    	.setIcon(R.drawable.find)
		;
		menu.add(0, PLAY_ID, 0, R.string.play_game)
		//    	.setIcon(R.drawable.find)
		;
		return true;
	}
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch(item.getItemId()) {
		case LOGOUT_ID:
			doLogout();
			break;
			//        case UPDATE_ID:
			//        	doUpdateInfo();
			//        	break;
			//        case CHANGE_PASSWORD_ID:
			//        	doChangePassword();
			//        	break;
			//        case VIEW_HISTORY_ID:
			//	    	doViewHistory();
			//	    	break;
		case FIND_GAMES_ID:
			doFindGames();
			break;
		case PLAY_ID:
			doPlayGame();
			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == CustomResultCodes.LOGOUT_RESULT_CODE) {
			setResult(CustomResultCodes.LOGOUT_RESULT_CODE);
			finish();
			return;
		}
		switch (requestCode) {
		case FIND_GAMES_REQUEST_CODE:
			break;
		}
	}
	private void doLogout() {
		CommonActions.launchLogoutThread(user.getUsername(), this);		
	}

	public void doFindGames() {
		showDialog(FIND_GAMES_DIALOG_ID);
	}
	private void doFindGames(int method) {
		Intent i = new Intent(this, FindGamesActivity.class);
		i.putExtra("user", user);

		switch (method) {
		case FindGamesActivity.SEARCH_METHOD_CITY:
			i.putExtra("method", FindGamesActivity.SEARCH_METHOD_CITY);
			break;
		}

		startActivityForResult(i, FIND_GAMES_REQUEST_CODE);

	}
	private void doPlayGame() {
		Intent i = new Intent(this, MapActivity.class);
		i.putExtra("user", user);
		i.putExtra("game", game);
		startActivityForResult(i, PLAY_REQUEST_CODE);

	}
	private void showToast(int messageId, int length) {
		Toast.makeText(this, messageId,
				length).show();
	}

	private void showToast(String message, int length) {
		Toast.makeText(this, message,
				length).show();
	}


}
