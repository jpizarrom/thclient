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

package com.jpizarro.th.client.common.dialogs;

import com.jpizarro.th.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;

/**
 * 
 * @author "Xurxo Mendez Perez"
 *
 */
public class CommonDialogs {

    public static final int SERVER_ERROR_DIALOG_ID = 0;
    public static final int CLIENT_ERROR_DIALOG_ID = 1;
	public static final int SERVER_OFFLINE_DIALOG_ID = 2;
	public static final int SQLITE_ERROR_DIALOG_ID = 3;
	public static final int LOADING_DIALOG_ID = 4;
	public static final int CONNECTING_TO_SERVER_DIALOG_ID = 5;
	public static final int CONNECTION_LOST_DIALOG_ID = 6;
	public static final int FIRST_CUSTOM_DIALOG_ID = CONNECTION_LOST_DIALOG_ID + 1;

	public static String errorMessage;
	
	public static Dialog createDialog(int id, Activity activity) {
    	switch(id) {
    	case SERVER_ERROR_DIALOG_ID:
    		return new AlertDialog.Builder(activity)
//            .setIcon(R.drawable.alert_dialog_icon)
            .setTitle(activity.getResources().getString(R.string.error_server_error_message) + 
            		" : " + errorMessage)
            .setPositiveButton(R.string.ok, null)
           .create();
        case CLIENT_ERROR_DIALOG_ID:
        	return new AlertDialog.Builder(activity)
//            .setIcon(R.drawable.alert_dialog_icon)
            .setTitle(activity.getResources().getString(R.string.error_client_exception) + 
            		" : " + errorMessage)
            .setPositiveButton(R.string.ok, null)
           .create();
        case SQLITE_ERROR_DIALOG_ID:
        	return new AlertDialog.Builder(activity)
//            .setIcon(R.drawable.alert_dialog_icon)
            .setTitle(activity.getResources().getString(R.string.sqlite_error) + 
            		" : " + errorMessage)
            .setPositiveButton(R.string.ok, null)
           .create();
        case SERVER_OFFLINE_DIALOG_ID:
        	return new AlertDialog.Builder(activity)
//            .setIcon(R.drawable.alert_dialog_icon)
            .setTitle(activity.getResources().getString(R.string.server_offline) + 
            		" : " + errorMessage)
            .setPositiveButton(R.string.ok, null)
           .create();
        case CONNECTION_LOST_DIALOG_ID:
        	return new AlertDialog.Builder(activity)
//            .setIcon(R.drawable.alert_dialog_icon)
            .setTitle(activity.getResources().getString(R.string.connection_lost) + 
            		" : " + errorMessage)
            .setPositiveButton(R.string.ok, null)
           .create();
        case CONNECTING_TO_SERVER_DIALOG_ID:
            ProgressDialog dialog = new ProgressDialog(activity);
            dialog.setTitle("Please wait");
            dialog.setMessage("Connecting to server...");
            dialog.setIndeterminate(true);
            dialog.setCancelable(true);
            return dialog;
        case LOADING_DIALOG_ID:
            dialog = new ProgressDialog(activity);
            dialog.setTitle("Please wait");
            dialog.setMessage("Saving...");
            dialog.setIndeterminate(true);
            dialog.setCancelable(true);
            return dialog;
		}
        return null;
    }
}
