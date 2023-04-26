package com.furence.recsee.main.model;

import java.util.Collection;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("row")
@XmlRootElement(name="row")
@XmlAccessorType(XmlAccessType.FIELD)
public class dhtmlXGridRow {

	@XmlAttribute(name="bgColor")
	private String bgColor;
	
	@XmlAttribute(name="xmlkids")
	private String xmlkids;
	
	
	
	public String getXmlkids() {
		return xmlkids;
	}

	public void setXmlkids(String xmlkids) {
		this.xmlkids = xmlkids;
	}

	@XmlAttribute(name="class")
	private String rowClass;
	
	@XmlAttribute(name="id")
	private String id;

	@XmlAttribute(name="locked")
	private String locked;
	
	@XmlAttribute(name="selected")
	private String selected;
	
	@XmlAttribute(name="style")
	private String style;
	
	@JsonProperty("call")
	@XmlElement(name="call")
	private Collection<dhtmlXGridHeadCall> callElements;
	
	@JsonProperty("cell")
	@XmlElement(name="cell")
	private Collection<dhtmlXGridRowCell> cellElements;
	
	@JsonProperty("userdata")
	@XmlElement(name="userdata")
	private Collection<dhtmlXGridRowUserdata> userdataElements;
	
	@JsonProperty("row")
	@XmlElement(name="row")
	private Collection<dhtmlXGridRow> rowElements;

	public Collection<dhtmlXGridRow> getRowElements() {
		return rowElements;
	}

	public void setRowElements(Collection<dhtmlXGridRow> rowElements) {
		this.rowElements = rowElements;
	}

	public String getBgColor() {
		return bgColor;
	}

	public void setBgColor(String bgColor) {
		this.bgColor = bgColor;
	}

	public String getRowClass() {
		return rowClass;
	}

	public void setRowClass(String rowClass) {
		this.rowClass = rowClass;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLocked() {
		return locked;
	}

	public void setLocked(String locked) {
		this.locked = locked;
	}

	public String getSelected() {
		return selected;
	}

	public void setSelected(String selected) {
		this.selected = selected;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public Collection<dhtmlXGridHeadCall> getCallElements() {
		return callElements;
	}

	public void setCallElements(Collection<dhtmlXGridHeadCall> callElements) {
		this.callElements = callElements;
	}

	public Collection<dhtmlXGridRowCell> getCellElements() {
		return cellElements;
	}
	
	public void setCellElements(Collection<dhtmlXGridRowCell> cellElements) {
		this.cellElements = cellElements;
	}

	public Collection<dhtmlXGridRowUserdata> getUserdataElements() {
		return userdataElements;
	}

	public void setUserdataElements(Collection<dhtmlXGridRowUserdata> userdataElements) {
		this.userdataElements = userdataElements;
	}

	
	
}
