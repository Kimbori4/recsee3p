package com.furence.recsee.main.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="complete")
@XmlAccessorType(XmlAccessType.FIELD)
public class dhtmlXGridCombo {

	@XmlElement(name="option")
	private List<dhtmlXGridComboOption> valueAttr;

	public List<dhtmlXGridComboOption> getValueAttr() {
		return valueAttr;
	}

	public void setValueAttr(List<dhtmlXGridComboOption> valueAttr) {
		this.valueAttr = valueAttr;
	}

}

