/*
 * Android Runner is a multiplayer GPS game fully written by Xurxo Mendez Perez
 * 
 * Copyright (C) 2009 Xurxo Mendez Perez
 *   
 * This file is part of Android Runner.
 * 
 * Android Runner is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Android Runner is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Android Runner.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.jpizarro.th.client.activity;

import com.jpizarro.th.R;
import com.jpizarro.th.client.common.dialogs.CommonDialogs;
import com.jpizarro.th.client.model.service.game.HttpGameServiceImpl;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import es.sonxurxo.gpsgame.client.util.exception.ServerException;

/**
 * 
 * @author "Xurxo Mendez Perez"
 *
 */
public class SendMessageActivity extends Activity {

	private EditText bodyText;
	private Button sendButton, cancelButton;

	private String receiverUser;
	
	private SendMessageTask sendMessageTask = new SendMessageTask();
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.write_message_page);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
                WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        receiverUser = getIntent().getExtras().getString("receiverUser");
        
        bodyText = (EditText)findViewById(R.id.wm_body);

        sendButton = (Button)findViewById(R.id.wm_send);
        sendButton.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				launchSendMessageThread();
			}
        	
        });
        cancelButton = (Button)findViewById(R.id.wm_cancel);
        cancelButton.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				doCancel();
			}
        	
        });        
	}
	
	private void exit() {
        setResult(RESULT_OK);
		finish();
	}
	
	private void launchSendMessageThread() {
		String body = bodyText.getText().toString();
		sendMessageTask.setReceiverUser(receiverUser);
		sendMessageTask.setBody(body);
		Thread sendMessageThread = new Thread(null, sendMessageTask, "SendMessageGame");
		sendMessageThread.start();
		showDialog(CommonDialogs.CONNECTING_TO_SERVER_DIALOG_ID);
	}

	private void doCancel() {
        setResult(RESULT_CANCELED);
		finish();
	}
	
	@Override
    protected Dialog onCreateDialog(int id) {
		return CommonDialogs.createDialog(id, this);
	}
	
	private class SendMessageHandler extends Handler {

		public SendMessageHandler(Looper looper) {
			super(looper);
		}
		
		@Override
		public void handleMessage(android.os.Message msg) {
			dismissDialog(CommonDialogs.CONNECTING_TO_SERVER_DIALOG_ID);
			ServerException sE = 
				(ServerException)msg.getData().getSerializable("ServerException");
			if (sE	!= null) {
				CommonDialogs.errorMessage = sE.getMessage();
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
	        	exit();
	        }
		}
		
	}
	
	private class SendMessageTask implements Runnable {

		String receiverUser, body;
		HttpGameServiceImpl gameService;
		
		public String getBody() {
			return body;
		}

		public void setBody(String body) {
			this.body = body;
		}

		public String getReceiverUser() {
			return receiverUser;
		}

		public void setReceiverUser(String receiverUser) {
			this.receiverUser = receiverUser;
		}

		SendMessageTask() {
			gameService = new HttpGameServiceImpl();
		}
		
		public void run() {
			
			SendMessageHandler handler = new SendMessageHandler(Looper.getMainLooper());
			Bundle data = new Bundle();
			android.os.Message msg = new android.os.Message();
			try {
				gameService.sendMessage(receiverUser, body);
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
