package com.jpizarro.th.activity;

import com.jpizarro.th.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        Button loginButton = (Button)findViewById(R.id.loginButton);
        
        
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
	
	/*
	 *  despues login muestra lista con juegos
	 */
	public void login(String username, String password) {
//		this.startActivity(new Intent(Intent.ACTION_VIEW));
	}

}
