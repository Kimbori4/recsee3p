package com.furence.recsee.main.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;


@XmlRootElement(name="option")
@XmlAccessorType(XmlAccessType.FIELD)
public class dhtmlXGridComboOption {
	@XmlAttribute(name="value")
	private String value;

	@XmlAttribute(name="selected")
	private String selected;

	@XmlValue
	private String valueElement;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValueElement() {
		return valueElement;
	}

	public void setValueElement(String valueElement) {
		this.valueElement = valueElement;
	}

	public String getSelected() {
		return selected;
	}

	public void setSelected(String selected) {
		this.selected = selected;
	}

}