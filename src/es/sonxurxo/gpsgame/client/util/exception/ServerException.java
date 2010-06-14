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

package es.sonxurxo.gpsgame.client.util.exception;

/**
 * 
 * @author "Xurxo Mendez Perez"
 *
 */
@SuppressWarnings("serial")
public class ServerException extends Exception {

	public static final int INSTANCE_NOT_FOUND_CODE = 1;
	public static final int DUPLICATE_INSTANCE_CODE = 2;
	public static final int INCORRECT_PASSWORD_CODE = 3;
	public static final int MAX_USERS_REACHED_CODE = 4;
	public static final int TIME_OUT_CODE = 5;
	public static final int EXCEPTION_CODE = 6;
	public static final int SERVER_OFFLINE_CODE = 7;
	public static final int NOT_IMPL = 8;

	private int code;
	private String message;
	
	public ServerException(int code, String message) {
		super(message);
		this.code = code;
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
