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

package com.jpizarro.th.client.model.service.to;

import java.io.Serializable;
import java.util.List;

import com.jpizarro.th.entity.Game;

/**
 * 
 * @author "Xurxo Mendez Perez"
 *
 */
public class GameCTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3611436455883261106L;
	private List<Game> gameList;
	private boolean hasMore;
	
	public GameCTO() {}
	
	public GameCTO(List<Game> gameList, boolean hasMore) {
		super();
		this.hasMore = hasMore;
		this.gameList = gameList;
	}

	public boolean isHasMore() {
		return hasMore;
	}

	public void setHasMore(boolean hasMore) {
		this.hasMore = hasMore;
	}

	public List<Game> getGameList() {
		return gameList;
	}

	public void setGameList(List<Game> gameList) {
		this.gameList = gameList;
	}

}
