package com.jpizarro.th.client.model.util.xml;

import com.jpizarro.th.client.model.service.to.response.GenericGameResponseTO;
import com.jpizarro.th.entity.Game;
import com.jpizarro.th.entity.Games;
import com.jpizarro.th.entity.Goal;
import com.jpizarro.th.entity.Hint;
import com.jpizarro.th.entity.Team;
import com.jpizarro.th.entity.Teams;
import com.jpizarro.th.entity.User;
import com.jpizarro.th.entity.Users;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class XStreamFactory
{
	public static XStream createXStream()
	{
		XStream xstream = new XStream(new DomDriver());
		xstream.registerConverter(new CalendarConverter(), XStream.PRIORITY_VERY_HIGH);
		
		xstream.addImplicitCollection(Games.class, "games", Game.class);
		xstream.addImplicitCollection(Teams.class, "teams", Team.class);
		xstream.addImplicitCollection(Users.class, "users", User.class);
		
		xstream.alias("user", User.class);
		xstream.alias("game", Game.class);
		xstream.alias("team", Team.class);
		xstream.alias("goal", Goal.class);
		xstream.alias("hint", Hint.class);
		
		xstream.alias("games", Games.class);
		xstream.alias("teams", Teams.class);
		xstream.alias("users", Users.class);
		
		xstream.alias("genericGameResponse", GenericGameResponseTO.class);
		
//		xstream.aliasField("users", GenericGameResponseTO.class, "inGameUserInfoTOs");
//		xstream.aliasField("user", InGameUserInfoTO.class, "inGameUserInfoTOs");
		
		return xstream;
	}

}
