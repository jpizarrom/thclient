package com.jpizarro.th.client.model.service.to.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GenericGameResponseTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7187541164428347769L;
	
	private List<InGameUserInfoTO> inGameUserInfoTOs = new ArrayList<InGameUserInfoTO>();

	public GenericGameResponseTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public List<InGameUserInfoTO> getInGameUserInfoTOs() {
		return inGameUserInfoTOs;
	}

	public void setInGameUserInfoTOs(List<InGameUserInfoTO> inGameUserInfoTOs) {
		this.inGameUserInfoTOs = inGameUserInfoTOs;
	}
	
	

}
