package com.furence.recsee.wooribank.script.util.file.pdf;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Objects;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

public class HtmlDocument {
	
	@Getter
	@Setter(AccessLevel.PRIVATE)
	private Document doc;
	
	@Getter
	private Element root;
	
	@Getter
	private Element head;
	
	@Getter
	private Element body;
	
	DocumentBuilder builder;
	
	public HtmlDocument() throws ParserConfigurationException, DOMException {		
		this.builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		this.doc = this.builder.newDocument();
		createHtmlTemplate();
	}
	
	/**
	 * html 기본 태그생성(html-head-body)
	 * @throws DOMException
	 */
	public void createHtmlTemplate() throws DOMException{
		this.root = addElement("html");
		this.doc.appendChild(this.root);
		this.root.setAttribute("xmlns", "http://www.w3.org/1999/xhtml");
		
		this.head = this.doc.createElement("head");		
		this.body = this.doc.createElement("body");
		
		this.root.appendChild(this.head);
		this.root.appendChild(this.body);
		
	}
	
	/**
	 * 새로운 노드 생성
	 * @param tag
	 * @return
	 */
	private Element addElement(String tag) {
		return this.doc.createElement(tag);
	}
	
	/**
	 * 하위 태그 추가
	 * @param parent
	 * @param tag
	 * @return
	 */
	public Element addChildElement(Element parent,String tag) {
		Element child = addElement(tag);
		parent.appendChild(child);
		return child;
	} 
	
	/**
	 * body 태그 내 새로운 태그 입력
	 * @param html
	 * @return
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws UnsupportedEncodingException 
	 * @throws Exception
	 */
	public Element appendChildBody(String html) throws  SAXException, IOException  {		
		Objects.requireNonNull(html);
		Document newDoc = this.builder.parse( new ByteArrayInputStream(html.getBytes("utf-8")) );
		Node newNode = this.doc.importNode(newDoc.getDocumentElement(), true);
		return (Element) this.body.appendChild( newNode);		
	}
	
	
	public void addStyleSheet(String css) throws SAXException, IOException {
		Element style = addChildElement(this.head, "style");
		style.setTextContent(css);
	}
}
