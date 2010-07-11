package com.jpizarro.th.client.model.service.to.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.jpizarro.th.entity.Hint;

public class GenericGameResponseTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7187541164428347769L;
	
	private List<InGameUserInfoTO> inGameUserInfoTOs = new ArrayList<InGameUserInfoTO>();
	private List<Hint> hints = new ArrayList<Hint>();
//	private List<Message> messages = new ArrayList<Message>();
//	private boolean hasFinished = false;	

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
	
	public InGameUserInfoTO getInGamePlayerInfoTO(String login) {
		for (InGameUserInfoTO igpiTO:inGameUserInfoTOs) {
			if (igpiTO.getUsername().equals(login))
				return igpiTO;
		}
		return null;
	}

	public List<Hint> getHints() {
		return hints;
	}

	public void setHints(List<Hint> hints) {
		this.hints = hints;
	}
	
	

}
