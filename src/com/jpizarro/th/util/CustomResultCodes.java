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

package com.jpizarro.th.util;

import android.app.Activity;

/**
 * 
 * @author "Xurxo Mendez Perez"
 *
 */
public class CustomResultCodes {

	public static final int LOGOUT_RESULT_CODE = Activity.RESULT_FIRST_USER;
//	public static final int VH_NO_GAMES_RESULT_CODE = LOGOUT_RESULT_CODE + 1;
//	public static final int REMOVED_FROM_GAME_RESULT_CODE = LOGOUT_RESULT_CODE + 2;
	public static final int FG_NO_CITIES_RESULT_CODE = LOGOUT_RESULT_CODE + 3;
//	public static final int VG_NO_GAMES_RESULT_CODE = LOGOUT_RESULT_CODE + 4;
//	public static final int GAME_FINISHED_RESULT_CODE = LOGOUT_RESULT_CODE + 5;
//	public static final int ABANDONED_RESULT_CODE = LOGOUT_RESULT_CODE + 6;
	public static final int VIEW_GAMES_REQUEST_CODE = LOGOUT_RESULT_CODE + 7;
//	public static final int JOINED_RESULT_CODE = LOGOUT_RESULT_CODE + 8;
	public static final int VIEW_TEAMS_REQUEST_CODE = LOGOUT_RESULT_CODE + 9;
}
