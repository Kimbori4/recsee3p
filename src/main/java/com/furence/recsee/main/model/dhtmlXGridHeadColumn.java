package com.furence.recsee.main.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("column")
@XmlRootElement(name="column")
@XmlAccessorType(XmlAccessType.FIELD)
public class dhtmlXGridHeadColumn {

	@XmlAttribute(name="align")
	private String align;

	@XmlAttribute(name="color")
	private String color;

	@XmlAttribute(name="format")
	private String format;

	@XmlAttribute(name="id")
	private String id;

	@XmlAttribute(name="sort")
	private String sort;

	@XmlAttribute(name="type")
	private String type;

	@XmlAttribute(name="width")
	private String width;
	
	@XmlAttribute(name="columnMinWidth")
	private String columnMinWidth;

	@XmlAttribute(name="hidden")
	private String hidden;

	@XmlAttribute(name="xmlcontent")
	private String xmlContent;

	@XmlAttribute(name="editable")
	private String editable;

	@XmlAttribute(name="source")
	private String source;

	@XmlAttribute(name="auto")
	private String auto;

	@XmlAttribute(name="cache")
	private String cache;

	@XmlAttribute(name="filtering")
	private String filtering;

	@XmlValue
	private String value;

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

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getHidden() {
		return hidden;
	}

	public void setHidden(String hidden) {
		this.hidden = hidden;
	}

	public String getXmlContent() {
		return xmlContent;
	}

	public void setXmlContent(String xmlContent) {
		this.xmlContent = xmlContent;
	}

	public String getEditable() {
		return editable;
	}

	public void setEditable(String editable) {
		this.editable = editable;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getAuto() {
		return auto;
	}

	public void setAuto(String auto) {
		this.auto = auto;
	}

	public String getCache() {
		return cache;
	}

	public void setCache(String cache) {
		this.cache = cache;
	}

	public String getFiltering() {
		return filtering;
	}

	public void setFiltering(String filtering) {
		this.filtering = filtering;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getColumnMinWidth() {
		return columnMinWidth;
	}

	public void setColumnMinWidth(String columnMinWidth) {
		this.columnMinWidth = columnMinWidth;
	}
}
