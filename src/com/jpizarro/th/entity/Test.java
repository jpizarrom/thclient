package com.jpizarro.th.entity;

public class Test {
	public static String test(){
		Hint hint = new Hint("Hint1","Hint esta en casa pe");
		
		Goal goal = new Goal("Goal1","Goal esta en casa pe");
		
		Competitor c = new Competitor("name1","desc1");
		c.setUsername("username1");
		c.setPassword("pass1");
		
		Team t = new Team("Team1");
//		t.getCompetitors().add(c);
		
		Game g = new Game();
		
		return hint +"\n" +
			goal +"\n" +
			c +"\n" +
			t +"\n";
	}

}
