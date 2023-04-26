package com.furence.recsee.main.model;

import java.util.Collection;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;

@XmlRootElement(name="head")
@XmlAccessorType(XmlAccessType.FIELD)
public class dhtmlXGridHead {

	@JsonProperty("column")
	@XmlElement(name="column")
	private Collection<dhtmlXGridHeadColumn> columnElement;

	@JsonProperty("beforeInit")
	@XmlElement(name="beforeInit")
	private dhtmlXGridHeadBeforeInit beforeElement;

	@JsonProperty("afterInit")
	@XmlElement(name="afterInit")
	private dhtmlXGridHeadAfterInit afterElement;

	public Collection<dhtmlXGridHeadColumn> getColumnElement() {
		return columnElement;
	}

	public void setColumnElement(Collection<dhtmlXGridHeadColumn> columnElement) {
		this.columnElement = columnElement;
	}

	public dhtmlXGridHeadBeforeInit getBeforeElement() {
		return beforeElement;
	}

	public void setBeforeElement(dhtmlXGridHeadBeforeInit beforeElement) {
		this.beforeElement = beforeElement;
	}

	public dhtmlXGridHeadAfterInit getAfterElement() {
		return afterElement;
	}

	public void setAfterElement(dhtmlXGridHeadAfterInit afterElement) {
		this.afterElement = afterElement;
	}
}
