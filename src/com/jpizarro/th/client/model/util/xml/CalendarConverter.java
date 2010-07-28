package com.jpizarro.th.client.model.util.xml;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;

public class CalendarConverter extends AbstractSingleValueConverter{

	@Override
	public boolean canConvert(Class c) {
		return Calendar.class.isAssignableFrom(c);
	}

	@Override
	public Object fromString(String s) {
//		if (s.charAt(s.length() - 3) == ':')
//		{
//			s = s.substring(0, s.length() - 3) + s.substring(s.length() - 2, s.length());
//		}
		
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		try
		{
			Date d = fmt.parse(s);
			Calendar c = Calendar.getInstance();
			c.setTime(d);
			
			return c;
		} 
		catch (ParseException e)
		{
			throw new RuntimeException(e);
		}
	}

}
