package com.furence.recsee.main.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.furence.recsee.common.util.StringUtil;

@XmlRootElement(name="cell")
@XmlAccessorType(XmlAccessType.FIELD)
public class dhtmlXGridRowCell {

	@XmlAttribute(name="class")
	private String cellClass;
	
	@XmlAttribute(name="colspan")
	private String colspan;
	
	@XmlAttribute(name="rowspan")
	private String rowspan;
	
	@XmlAttribute(name="style")
	private String style;
	
	@XmlAttribute(name="type")
	private String type;
	
	@JsonIgnore
	@XmlAttribute(name="xmlcontent")
	private String xmlContent;

	@XmlAttribute(name="editable")
	private String editable;

	@XmlAttribute(name="source")
	private String source;

	@XmlAttribute(name="image")
	private String image;
	
	@XmlAttribute(name="name")
	private String name;
	
	@XmlAttribute(name="title")
	private String title;
	
	@XmlValue
	private String value;

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getCellClass() {
		return cellClass;
	}

	public void setCellClass(String cellClass) {
		this.cellClass = cellClass;
	}

	public String getColspan() {
		return colspan;
	}

	public void setColspan(String colspan) {
		this.colspan = colspan;
	}

	public String getRowspan() {
		return rowspan;
	}

	public void setRowspan(String rowspan) {
		this.rowspan = rowspan;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
