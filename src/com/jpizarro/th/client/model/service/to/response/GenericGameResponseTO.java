package com.jpizarro.th.client.model.service.to.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.jpizarro.th.entity.HintTO;
import com.jpizarro.th.entity.MessageTO;

public class GenericGameResponseTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7187541164428347769L;
	
	private List<InGameUserInfoTO> inGameUserInfoTOs = new ArrayList<InGameUserInfoTO>();
//	private Users inGameUserInfoTOs;
	private List<HintTO> hints = new ArrayList<HintTO>();
	private List<HintTO> hideHints = new ArrayList<HintTO>();
	private List<HintTO> userSeeHintTOList = new ArrayList<HintTO>();
	private List<HintTO> teamSeeHintTOList = new ArrayList<HintTO>();
	private HintTO goal;
	private List<MessageTO> myMessages = new ArrayList<MessageTO>();
	private boolean hasFinished = false;	

	public GenericGameResponseTO() {
		super();
		// TODO Auto-generated constructor stub
	}

//	public List<InGameUserInfoTO> getInGameUserInfoTOs() {
//		return inGameUserInfoTOs;
//	}
//
//	public void setInGameUserInfoTOs(List<InGameUserInfoTO> inGameUserInfoTOs) {
//		this.inGameUserInfoTOs = inGameUserInfoTOs;
//	}
//	
//	public InGameUserInfoTO getInGamePlayerInfoTO(String login) {
//		for (InGameUserInfoTO igpiTO:inGameUserInfoTOs) {
//			if (igpiTO.getUsername().equals(login))
//				return igpiTO;
//		}
//		return null;
//	}

	public HintTO getGoal() {
		return goal;
	}

	public List<InGameUserInfoTO> getInGameUserInfoTOs() {
		return inGameUserInfoTOs;
	}

	public void setInGameUserInfoTOs(List<InGameUserInfoTO> inGameUserInfoTOs) {
		this.inGameUserInfoTOs = inGameUserInfoTOs;
	}

	public void setGoal(HintTO goal) {
		this.goal = goal;
	}

	public List<HintTO> getHints() {
		return hints;
	}

	public void setHints(List<HintTO> hints) {
		this.hints = hints;
	}

	public List<HintTO> getHideHints() {
		return hideHints;
	}

	public void setHideHints(List<HintTO> hideHints) {
		this.hideHints = hideHints;
	}

	public List<HintTO> getUserSeeHintTOList() {
		return userSeeHintTOList;
	}

	public void setUserSeeHintTOList(List<HintTO> userSeeHintTOList) {
		this.userSeeHintTOList = userSeeHintTOList;
	}

	public List<HintTO> getTeamSeeHintTOList() {
		return teamSeeHintTOList;
	}

	public void setTeamSeeHintTOList(List<HintTO> teamSeeHintTOList) {
		this.teamSeeHintTOList = teamSeeHintTOList;
	}

	public boolean isHasFinished() {
		return hasFinished;
	}

	public void setHasFinished(boolean hasFinished) {
		this.hasFinished = hasFinished;
	}

	public List<MessageTO> getMyMessages() {
		return myMessages;
	}

	public void setMyMessages(List<MessageTO> myMessages) {
		this.myMessages = myMessages;
	}
}
