package com.furence.recsee.wooribank.script.util.file.pdf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.StringWriter;
import java.util.Objects;
import java.util.Properties;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Getter;

public class HtmlDocumentWriter {
	
	private static final Logger logger = LoggerFactory.getLogger(HtmlDocumentWriter.class);
	@Getter
	private HtmlDocument htmlDoc;

	private final Properties props;
	
	public HtmlDocumentWriter(HtmlDocument htmlDoc) {
		
		this.htmlDoc = Objects.requireNonNull(htmlDoc);
		this.props = new Properties();
		
		props.setProperty(OutputKeys.DOCTYPE_PUBLIC,"-//W3C//DTD XHTML 1.0 Transitional//EN");
		props.setProperty(OutputKeys.DOCTYPE_SYSTEM,"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd");		
		props.setProperty(OutputKeys.INDENT,"yes");
		props.setProperty(OutputKeys.METHOD,"html");
		props.setProperty(OutputKeys.ENCODING,"UTF-8");
	}
	
	private Transformer getTransfomer() throws TransformerConfigurationException{
		
		TransformerFactory factory = TransformerFactory.newInstance();
		Transformer transfromer = factory.newTransformer();
		transfromer.setOutputProperties(this.props);		
		return transfromer;
	}
	
	/**
	 * DOM 을 String으로 출력함.
	 * @return
	 */
	public String writeAsString(boolean metaTagClosing) {
		
		try( StringWriter strWriter = new StringWriter();) {
			
			DOMSource dom = new DOMSource(this.htmlDoc.getDoc());
			
			Transformer transformer = getTransfomer();
			transformer.transform(dom, new StreamResult(strWriter));	
			String xhtml = strWriter.toString();
			// meta tag 가 안닫혀있으면 닫아준다.
			xhtml = xhtml.replace("<META http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">", 
					"<META http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>");
			return xhtml;			
			
		} catch (Exception e) {
			logger.error("error", e);
		}
		
		return "";
	}
	
	/**
	 * DOM 을 파일로 저장
	 * @return
	 */
	public boolean writeAsFile(String fileName) {
		
		try( FileOutputStream fos = new FileOutputStream(new File(fileName)) ) {
			
			Transformer transformer = getTransfomer();
			DOMSource dom = new DOMSource(this.htmlDoc.getDoc());
			transformer.transform(dom, new StreamResult(fos));
			return true;
			
		} catch (Exception e) {
			logger.error("error", e);
		}
		
		return false;
	}
}
