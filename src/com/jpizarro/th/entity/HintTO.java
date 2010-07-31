package com.jpizarro.th.entity;

public class HintTO extends Ubication{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2597865271165158366L;

	public HintTO(String name, String description) {
		super(name, description);
		// TODO Auto-generated constructor stub
	}

	public HintTO(long id, int latitude, int longitude, String name,
			String description) {
		super(id, latitude, longitude, name, description);
		// TODO Auto-generated constructor stub
	}

}
