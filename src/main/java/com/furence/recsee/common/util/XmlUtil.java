package com.furence.recsee.common.util;

import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XmlUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(XmlUtil.class);


	/**
	 * Document의 Root엘리먼트를 반환한다.
	 * @param xmlStr 
	 * @return Root엘리먼트
	 */
//	public static Element getDocumentElements(String xmlStr) {
//		
//		DocumentBuilderFactory factory = null;
//		DocumentBuilder loader = null;
//		Document document = null;
//		Element root = null;
//		try {
//			factory = DocumentBuilderFactory.newInstance();
//			loader = factory.newDocumentBuilder();
//			
//			document = loader.parse(new InputSource(new StringReader(xmlStr)));
//			root = document.getDocumentElement();
//
//			return root;
//			
//		} catch (IOException ex) {
//			logger.info("", ex);
//		} catch (SAXException ex) {
//			logger.info("", ex);
//		} catch (ParserConfigurationException ex) {
//			logger.info("", ex);
//		} catch (FactoryConfigurationError ex) {
//			logger.info("", ex);
//		} catch (NullPointerException ex) {
//			logger.info("", ex);
//		}
//		
//		return null;
//	}

	/**
	 * Document의 Root엘리먼트를 반환한다.
	 * @param xmlStr 
	 * @return Root엘리먼트
	 */
//	public static Element getDocumentElements(URL xmlUrl) {
//
//		DocumentBuilderFactory factory = null;
//		DocumentBuilder loader = null;
//		Document document = null;
//		Element root = null;
//		try {
//			factory = DocumentBuilderFactory.newInstance();
//			loader = factory.newDocumentBuilder();
//			
//			document = loader.parse(xmlUrl.openStream());
//			root = document.getDocumentElement();
//			return root;
//			
//		} catch (IOException ex) {
//			logger.info("", ex);
//		} catch (SAXException ex) {
//			ex.printStackTrace();
//			logger.info("", ex);
//		} catch (ParserConfigurationException ex) {
//			ex.printStackTrace();
//			logger.info("", ex);
//		} catch (FactoryConfigurationError ex) {
//			ex.printStackTrace();
//			logger.info("", ex);
//		} catch (NullPointerException ex) {
//			ex.printStackTrace();
//			logger.info("", ex);
//		}
//		
//		return null;
//	}
	
	/**
	 * XML로부터 주어진 노드의 값을 반환한다.
	 * @param strXml XML
	 * @param unqKey 지정노드 이름
	 * @return 지정노드의 값 (지정 노드가 복수의 경우 첫번째 노드의 값)
	 */
//	public static String getValueFromUnqItem(String strXml, String unqKey) {
//		Element root = getDocumentElements(strXml);
//		return getValueFromUnqItem(root, unqKey);
//	}

	/**
	 * Root엘리먼트로부터 주어진 노드의 값을 반환한다.
	 * @param el 검색을 시작할 최상위 엘리먼트
	 * @param unqKey 지정노드 이름
	 * @return 지정노드의 값 (지정 노드가 복수의 경우 첫번째 노드의 값)
	 */
	public static String getValueFromUnqItem(Element el, String unqKey) {
		if (el != null) {
			NodeList items = el.getElementsByTagName(unqKey);
			if (items.getLength() > 0) {
				return items.item(0).getFirstChild().getNodeValue();
			}
		} 

		return null;
	}
	
	public static Map<String, String> getXmlToHashMap(Element el, String rootName) {
		Map<String, String> resultMap = new HashMap<String, String>();
		
		if(el != null) {
			NodeList nodeList = el.getElementsByTagName(rootName);
			for(int i = 0; i < nodeList.getLength(); i++) {
				Node currentNode = nodeList.item(i);
				if(currentNode.hasAttributes()) {
					Node item = currentNode.getAttributes().item(0);
					resultMap.put(item.getTextContent(), currentNode.getTextContent());
				}
			}
		}
		
		return resultMap;
	}
}
