package com.jpizarro.th.client.model.util.xml.xstream;

import java.util.Iterator;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.converters.reflection.AbstractReflectionConverter;
import com.thoughtworks.xstream.converters.reflection.ReflectionProvider;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.mapper.Mapper;

public class JoinedConverter  implements  Converter  {

	public boolean canConvert(Class type) {
		// TODO Auto-generated method stub
		return type == null;
	}

	public void marshal(Object arg0, HierarchicalStreamWriter arg1,
			MarshallingContext arg2) {
		// TODO Auto-generated method stub
		
	}

	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		// TODO Auto-generated method stub
		Iterator it = reader.getAttributeNames();
		while (it.hasNext()) {
			String attrAlias = (String) it.next();
			if(attrAlias.equals("joined"))
				return true;
		}

		return null;
	}

}
