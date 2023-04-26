package com.furence.recsee.main.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="beforeInit")
@XmlAccessorType(XmlAccessType.FIELD)
public class dhtmlXGridHeadBeforeInit {
	@XmlElement(name="call")
	private List<dhtmlXGridHeadCall> callElement;

	public List<dhtmlXGridHeadCall> getCallElement() {
		return callElement;
	}

	public void setCallElement(List<dhtmlXGridHeadCall> callElement) {
		this.callElement = callElement;
	}
}
