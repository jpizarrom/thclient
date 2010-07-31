package com.jpizarro.th.entity.list;

import java.util.List;

import com.jpizarro.th.entity.GameTO;

public class CitiesTO {
	private Integer count;
	private Integer start;
	private Integer total;
	private List<String> cities;
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
	public List<String> getCities() {
		return cities;
	}
	public void setCities(List<String> cities) {
		this.cities = cities;
	}
	private void addCity(String c){
		this.cities.add(c);
	}
}
