package com.furence.recsee.main.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement(name="item")
@XmlAccessorType(XmlAccessType.NONE)
public class dhtmlXTreeUserData {

	@XmlAttribute(name="name")
	private String name;
	
	@XmlValue
	private String userDataValue;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUserDataValue() {
		return userDataValue;
	}

	public void setUserDataValue(String userDataValue) {
		this.userDataValue = userDataValue;
	}
	
}
