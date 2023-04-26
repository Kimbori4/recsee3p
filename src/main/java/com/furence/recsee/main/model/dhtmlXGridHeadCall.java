package com.furence.recsee.main.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("call")
@XmlRootElement(name="call")
@XmlAccessorType(XmlAccessType.FIELD)
public class dhtmlXGridHeadCall {

	@XmlAttribute(name="command")
	private String command;

	@JsonProperty("parma")
	@XmlElement(name="param")
	private List<String> paramElement;

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public List<String> getParamElement() {
		return paramElement;
	}

	public void setParamElement(List<String> paramElement) {
		this.paramElement = paramElement;
	}
}
