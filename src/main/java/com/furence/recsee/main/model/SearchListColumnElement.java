package com.furence.recsee.main.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement(name="column")
@XmlAccessorType(XmlAccessType.FIELD)
public class SearchListColumnElement {

	@XmlAttribute(name="width")
	private String width;

	@XmlAttribute(name="type")
	private String type;

	@XmlAttribute(name="align")
	private String align;

	@XmlAttribute(name="color")
	private String color;

	@XmlAttribute(name="sort")
	private String sort;

	@XmlAttribute(name="id")
	private String id;

	@XmlAttribute(name="hidden")
	private String hidden;
	
	@XmlAttribute(name="source")
	private String source;

	@XmlAttribute(name="editable")
	private String editable;

	@XmlValue
	private String value;

	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = width;
	}

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	public String getAlign() {
		return align;
	}
	public void setAlign(String align) {
		this.align = align;
	}

	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}

	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public String getHidden() {
		return hidden;
	}
	public void setHidden(String hidden) {
		this.hidden = hidden;
	}
	
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}

	public String getEditable() {
		return editable;
	}
	public void setEditable(String editable) {
		this.editable = editable;
	}

	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
