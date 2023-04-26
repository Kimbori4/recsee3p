package com.furence.recsee.main.model;

import java.util.Collection;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@XmlRootElement(name="rows")
@JsonRootName("rows")
@XmlAccessorType(XmlAccessType.NONE)
public class dhtmlXGridXml {
	
	
	@XmlAttribute(name="total_count")
	private String total_count;
	
	@XmlAttribute(name="count")
	private String count;
	
	@XmlAttribute(name="pos")
	private String pos;
	
	@JsonProperty("head")
	@XmlElement(name="head")
	private dhtmlXGridHead headElement;

	@JsonProperty("row")
	@XmlElement(name="row")
	private Collection<dhtmlXGridRow> rowElements;

	public String getTotal_count() {
		return total_count;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public void setTotal_count(String total_count) {
		this.total_count = total_count;
	}

	public String getPos() {
		return pos;
	}

	public void setPos(String pos) {
		this.pos = pos;
	}

	public dhtmlXGridHead getHeadElement() {
		return headElement;
	}

	public void setHeadElement(dhtmlXGridHead headElement) {
		this.headElement = headElement;
	}

	public Collection<dhtmlXGridRow> getRowElements() {
		return rowElements;
	}

	public void setRowElements(Collection<dhtmlXGridRow> rowElements) {
		this.rowElements = rowElements;
	}


}
