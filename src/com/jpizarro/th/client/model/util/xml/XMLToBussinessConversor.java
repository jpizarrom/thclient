package com.jpizarro.th.client.model.util.xml;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import com.jpizarro.th.entity.User;

import es.sonxurxo.gpsgame.client.util.exception.ServerException;

public class XMLToBussinessConversor {
	private static Document parseHttpEntity(HttpEntity entity) throws Exception {
		InputStream in = null;
		try {
			in = entity.getContent();
		} catch (Exception e) {
			throw e;
		}
		
		Document document = null;
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			document = db.parse(in);
		} catch (IOException ioe) {
			throw ioe;
		} catch (ParserConfigurationException pce) {
			throw new Exception(pce);
		} catch (SAXException se) {
			throw new Exception(se);
		}
		return document;
	}
	
	public static User toUser(HttpEntity entity) throws Exception{
		//		throw new Exception("Not Impl");
		User user = new User();
		Document document = parseHttpEntity(entity);
		if (document.getElementsByTagName("user").getLength() == 1) {
			
			Element element = (Element)document.getElementsByTagName(
			"username").item(0);
			String username = element.getChildNodes().item(0).getNodeValue();
			user.setUserName(username.replace("+", " "));
			
			
		}else {
			Element codeElement = (Element)document.getElementsByTagName(
			"code").item(0);
			int code = Integer.parseInt(codeElement.getChildNodes().item(0).
					getNodeValue());
			Element messageElement = (Element)document.getElementsByTagName(
			"message").item(0);
			String message = messageElement.getChildNodes().item(0).getNodeValue();

			throw new ServerException(code, message);
		}
		
		return user;
	}

}
