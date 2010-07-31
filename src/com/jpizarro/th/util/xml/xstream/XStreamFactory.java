package com.jpizarro.th.util.xml.xstream;

import com.jpizarro.th.client.model.service.to.response.GenericGameResponseTO;
import com.jpizarro.th.client.model.service.to.response.InGameUserInfoTO;
import com.jpizarro.th.entity.GameTO;
import com.jpizarro.th.entity.GoalTO;
import com.jpizarro.th.entity.HintTO;
import com.jpizarro.th.entity.TeamTO;
import com.jpizarro.th.entity.UserTO;
import com.jpizarro.th.entity.list.CitiesTO;
import com.jpizarro.th.entity.list.GamesTO;
import com.jpizarro.th.entity.list.TeamsTO;
import com.jpizarro.th.entity.list.UsersTO;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XppDriver;

public class XStreamFactory
{
	public static XStream createXStream()
	{
//		XStream xstream = new XStream(new DomDriver());
		XStream xstream = new XStream(new XppDriver());
		
		xstream.registerConverter(new CalendarConverter(), XStream.PRIORITY_VERY_HIGH);
//		xstream.registerConverter(new JoinedConverter(), XStream.PRIORITY_NORMAL);

		
		xstream.addImplicitCollection(GamesTO.class, "games", GameTO.class);
		xstream.addImplicitCollection(TeamsTO.class, "teams", TeamTO.class);
		xstream.addImplicitCollection(UsersTO.class, "users", UserTO.class);
		
		xstream.addImplicitCollection(CitiesTO.class, "cities");
		
		xstream.alias("user", UserTO.class);
		xstream.alias("game", GameTO.class);
		xstream.alias("team", TeamTO.class);
		xstream.alias("goal", GoalTO.class);
		xstream.alias("hint", HintTO.class);
		
		xstream.alias("joined", Boolean.class);
		xstream.alias("logout", Boolean.class);
		xstream.alias("messageSent", Boolean.class);
		
		xstream.alias("city", String.class);
		
		xstream.alias("games", GamesTO.class);
		xstream.alias("teams", TeamsTO.class);
		xstream.alias("users", UsersTO.class);
		xstream.alias("cities", CitiesTO.class);
		
		xstream.alias("genericGameResponse", GenericGameResponseTO.class);
		xstream.alias("updateLocationReponse", GenericGameResponseTO.class);
		
		xstream.alias("inGameUserInfoTO", InGameUserInfoTO.class);
		
//		xstream.aliasField("users", GenericGameResponseTO.class, "inGameUserInfoTOs");
//		xstream.aliasField("user", InGameUserInfoTO.class, "inGameUserInfoTOs");
		
		return xstream;
	}

}
