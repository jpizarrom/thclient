package com.jpizarro.th.entity.list;

import java.util.List;

import com.jpizarro.th.entity.GameTO;

public class GamesTO {
	private Integer count;
	private Integer start;
	private Integer total;
	private List<GameTO> games;
	private boolean hasMore;
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public Integer getStart() {
		return start;
	}
	public void setStart(Integer start) {
		this.start = start;
	}
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
	public List<GameTO> getGames() {
		return games;
	}
	public void setGames(List<GameTO> games) {
		this.games = games;
	}
	public boolean isHasMore() {
		return hasMore;
	}
	public void setHasMore(boolean hasMore) {
		this.hasMore = hasMore;
	}
	
}
