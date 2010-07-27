package com.jpizarro.th.client.activity;

import com.jpizarro.th.R;
import com.jpizarro.th.client.common.dialogs.CommonDialogs;
import com.jpizarro.th.client.model.service.game.GameService;
import com.jpizarro.th.client.model.service.game.HttpGameServiceImpl;
import com.jpizarro.th.client.model.service.user.HttpUserServiceImpl;
import com.jpizarro.th.client.model.service.user.Axis2UserServiceImpl;
import com.jpizarro.th.client.model.service.user.UserService;
import com.jpizarro.th.entity.Game;
import com.jpizarro.th.entity.User;
import com.jpizarro.th.util.CustomAPP;

import es.sonxurxo.gpsgame.client.util.exception.ServerException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * @author jpizarro
 *
 */
/**
 * @author jpizarro
 *
 */
/**
 * @author jpizarro
 *
 */
public class Login extends Activity {
	
	private static final int PLAYER_INFO_REQUEST_CODE = 0;
	
	private LoginTask loginTask;
	private User user = new User();
	private Game game = new Game();
	
	private TextView loginErrorView, passwordErrorView;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        Button loginButton = (Button)findViewById(R.id.loginButton);
        loginErrorView = (TextView)findViewById(R.id.username_msg);
        passwordErrorView = (TextView)findViewById(R.id.password_msg);
        
        
        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	EditText user = (EditText)findViewById(R.id.username);
            	String s = user.getText().toString().trim();
            	if(s.equals("")){
            		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            		builder.setMessage(R.string.fillUserName);
            		
            		builder.setPositiveButton(R.string.accept, new OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.cancel();
						}
            		});
            		
            		builder.setCancelable(false);
            		AlertDialog alert = builder.create();
            		alert.show();
            	}else{
            		String username = s;
            		String password = ((EditText)findViewById(R.id.password)).getText().toString().trim();
            		login(username,password);
            	}
            }
        });
    }
    
 	public Context getContext(){
		return this.getApplicationContext();
	}
	
	public Activity getActivity(){
		return this;
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		return CommonDialogs.createDialog(id, this);
	}

	/*
	 *  despues login muestra lista con juegos
	 */
	public void login(String username, String password) {
		loginTask = new LoginTask(username, password);
		Thread loginThread = new Thread(null, loginTask, "Login");
		loginThread.start();
		showDialog(CommonDialogs.CONNECTING_TO_SERVER_DIALOG_ID);
	}
	private void doLogin() {
		dismissDialog(CommonDialogs.CONNECTING_TO_SERVER_DIALOG_ID);
		Intent i = new Intent(Login.this, MainMenuActivity.class);
    	i.putExtra("user", user);
    	i.putExtra("game", game);
        startActivityForResult(i, PLAYER_INFO_REQUEST_CODE);
	}
	
	private class LoginTask implements Runnable {
		String userName, password;
		UserService userService;
		GameService gameService;
		
		LoginTask(String userName, String password) {
			this.userName = userName;
			this.password = password;
			userService = CustomAPP.getUserService();
			gameService = CustomAPP.getGameService();
		}

		public void run() {
			LoginHandler handler = new LoginHandler(Looper.getMainLooper());
			Bundle data = new Bundle();
			Message msg = new Message();
			Game game;
			
			try {
				User user = userService.login(userName, password);
//				user.setGameId(1);
				data.putSerializable("user", user);
				if ( user.getGameId() != 0 ){
					game = gameService.findGame(user.getGameId());
					data.putSerializable("game", game);
				}
				data.putSerializable("user", user);
				msg.setData(data);
				handler.sendMessage(msg);
			} catch (Exception e) {
	        	data.putSerializable("Exception", e);
	        	msg.setData(data);
				handler.sendMessage(msg);
	        }
		}
		
	}
	private class LoginHandler extends Handler {

		public LoginHandler(Looper mainLooper) {
			super(mainLooper);
		}
		@Override
		public void handleMessage(Message msg) {
			ServerException sE = 
				(ServerException)msg.getData().getSerializable("ServerException");
			if (sE	!= null) {
				dismissDialog(CommonDialogs.CONNECTING_TO_SERVER_DIALOG_ID);
	        	CommonDialogs.errorMessage = sE.getMessage();
	        	String message = new String();
	        	switch(sE.getCode()) {
	        	case ServerException.DUPLICATE_INSTANCE_CODE:
	        		message = (String)getString(R.string.error_login_exists_message);
	        		loginErrorView.setText(message);
	        		break;
	        	case ServerException.INCORRECT_PASSWORD_CODE:
	        		message = (String)getString(R.string.error_incorrect_password_message);
	        		passwordErrorView.setText(message);
	        		break;
	        	case ServerException.INSTANCE_NOT_FOUND_CODE:
	        		message = (String)getString(R.string.error_instance_not_found_message);
	        		loginErrorView.setText(message);
	        		break;
	        	case ServerException.SERVER_OFFLINE_CODE:
	        		showDialog(CommonDialogs.SERVER_OFFLINE_DIALOG_ID);
	        		break;
	        	case ServerException.EXCEPTION_CODE:
	        		showDialog(CommonDialogs.SERVER_ERROR_DIALOG_ID);
	        		break;
	        	}
	        	return;
	        }
        	Exception e = 
	        	(Exception)msg.getData().getSerializable("Exception");
	        	if (e != null) {
	        		dismissDialog(CommonDialogs.CONNECTING_TO_SERVER_DIALOG_ID);
	        		CommonDialogs.errorMessage = e.getMessage();
		        	showDialog(CommonDialogs.CLIENT_ERROR_DIALOG_ID);
		        	return;
	        	}
			user = (User)msg.getData().getSerializable("user");
			game = (Game)msg.getData().getSerializable("game");
			doLogin();
		}
		
	}

}
