package com.jpizarro.th.client.model.util.xml;

import com.jpizarro.th.client.model.service.to.response.GenericGameResponseTO;
import com.jpizarro.th.client.model.util.xml.xstream.JoinedConverter;
import com.jpizarro.th.entity.Game;
import com.jpizarro.th.entity.Goal;
import com.jpizarro.th.entity.Hint;
import com.jpizarro.th.entity.Team;
import com.jpizarro.th.entity.User;
import com.jpizarro.th.entity.list.Cities;
import com.jpizarro.th.entity.list.Games;
import com.jpizarro.th.entity.list.Teams;
import com.jpizarro.th.entity.list.Users;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.basic.BooleanConverter;
import com.thoughtworks.xstream.converters.basic.NullConverter;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XppDriver;

public class XStreamFactory
{
	public static XStream createXStream()
	{
//		XStream xstream = new XStream(new DomDriver());
		XStream xstream = new XStream(new XppDriver());
		
		xstream.registerConverter(new CalendarConverter(), XStream.PRIORITY_VERY_HIGH);
//		xstream.registerConverter(new JoinedConverter(), XStream.PRIORITY_NORMAL);

		
		xstream.addImplicitCollection(Games.class, "games", Game.class);
		xstream.addImplicitCollection(Teams.class, "teams", Team.class);
		xstream.addImplicitCollection(Users.class, "users", User.class);
		
		xstream.addImplicitCollection(Cities.class, "cities");
		
		xstream.alias("user", User.class);
		xstream.alias("game", Game.class);
		xstream.alias("team", Team.class);
		xstream.alias("goal", Goal.class);
		xstream.alias("hint", Hint.class);
		
		xstream.alias("joined", Boolean.class);
		xstream.alias("logout", Boolean.class);
		xstream.alias("messageSent", Boolean.class);
		
		xstream.alias("city", String.class);
		
		xstream.alias("games", Games.class);
		xstream.alias("teams", Teams.class);
		xstream.alias("users", Users.class);
		xstream.alias("cities", Cities.class);
		
		xstream.alias("genericGameResponse", GenericGameResponseTO.class);
		xstream.alias("updateLocationReponse", GenericGameResponseTO.class);
		
//		xstream.aliasField("users", GenericGameResponseTO.class, "inGameUserInfoTOs");
//		xstream.aliasField("user", InGameUserInfoTO.class, "inGameUserInfoTOs");
		
		return xstream;
	}

}
