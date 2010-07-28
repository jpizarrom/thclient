package com.jpizarro.th.client.model.service.to.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.jpizarro.th.entity.Goal;
import com.jpizarro.th.entity.Hint;
import com.jpizarro.th.entity.list.Users;

public class GenericGameResponseTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7187541164428347769L;
	
//	private List<InGameUserInfoTO> inGameUserInfoTOs = new ArrayList<InGameUserInfoTO>();
	private Users users;
	private List<Hint> hints = new ArrayList<Hint>();
	private List<Hint> hideHints = new ArrayList<Hint>();
	private List<Hint> userSeeHintTOList = new ArrayList<Hint>();
	private List<Hint> teamSeeHintTOList = new ArrayList<Hint>();
	private Hint goal;
//	private List<Message> messages = new ArrayList<Message>();
//	private boolean hasFinished = false;	

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

	public Users getUsers() {
		return users;
	}

	public void setUsers(Users users) {
		this.users = users;
	}

	public Hint getGoal() {
		return goal;
	}

	public void setGoal(Hint goal) {
		this.goal = goal;
	}

	public List<Hint> getHints() {
		return hints;
	}

	public void setHints(List<Hint> hints) {
		this.hints = hints;
	}

	public List<Hint> getHideHints() {
		return hideHints;
	}

	public void setHideHints(List<Hint> hideHints) {
		this.hideHints = hideHints;
	}

	public List<Hint> getUserSeeHintTOList() {
		return userSeeHintTOList;
	}

	public void setUserSeeHintTOList(List<Hint> userSeeHintTOList) {
		this.userSeeHintTOList = userSeeHintTOList;
	}

	public List<Hint> getTeamSeeHintTOList() {
		return teamSeeHintTOList;
	}

	public void setTeamSeeHintTOList(List<Hint> teamSeeHintTOList) {
		this.teamSeeHintTOList = teamSeeHintTOList;
	}
	
	

}
