package com.furence.recsee.main.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="row")
@XmlAccessorType(XmlAccessType.FIELD)
public class dhtmlXSidebarItem {

	@XmlAttribute(name="id")
	private String id;

	@XmlAttribute(name="text")
	private String text;

	@XmlAttribute(name="icon")
	private String icon;

	@XmlAttribute(name="level")
	private String level;

	@XmlAttribute(name="selected")
	private String selected;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getSelected() {
		return selected;
	}

	public void setSelected(String selected) {
		this.selected = selected;
	}
}
