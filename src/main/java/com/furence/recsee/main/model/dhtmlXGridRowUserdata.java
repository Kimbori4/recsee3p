package com.furence.recsee.main.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

import com.furence.recsee.common.util.StringUtil;

@XmlRootElement(name="userdata")
@XmlAccessorType(XmlAccessType.FIELD)
public class dhtmlXGridRowUserdata {

	@XmlAttribute(name="name")
	private String name;
	
	@XmlValue
	private String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		if (StringUtil.isNull(value,true))
			this.value = "";
		else
			this.value = value;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
