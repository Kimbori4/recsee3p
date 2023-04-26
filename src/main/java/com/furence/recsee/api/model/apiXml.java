package com.furence.recsee.api.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Root")
@XmlAccessorType(XmlAccessType.FIELD)
public class apiXml {

	@XmlAttribute(name="xmlns")
	private String xmlns;
	
	private String NormalRec;
	
	private String FilePathMain;
	
	private String Ret;
	
	private String ErrorMessage;

	public String getNormalRec() {
		return NormalRec;
	}

	public void setNormalRec(String normalRec) {
		this.NormalRec = normalRec;
	}

	public String getFilePathMain() {
		return FilePathMain;
	}

	public void setFilePathMain(String filePathMain) {
		this.FilePathMain = filePathMain;
	}

	public String getRet() {
		return Ret;
	}

	public void setRet(String ret) {
		this.Ret = ret;
	}

	public String getErrorMessage() {
		return ErrorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.ErrorMessage = errorMessage;
	}

	public String getXmlns() {
		return xmlns;
	}

	public void setXmlns(String xmlns) {
		this.xmlns = xmlns;
	}
	


	
	
}

