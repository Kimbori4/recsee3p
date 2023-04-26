package com.furence.recsee.main.model;

import java.util.Collection;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="menu")
@XmlAccessorType(XmlAccessType.NONE)
public class dhtmlXMenu {

	@XmlAttribute(name="parentId")
	private String parentId;
	
	@XmlElement(name="item")
	private Collection<dhtmlXMenuItem> itemElement;

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public Collection<dhtmlXMenuItem> getItemElement() {
		return itemElement;
	}

	public void setItemElement(Collection<dhtmlXMenuItem> itemElement) {
		this.itemElement = itemElement;
	}
	
}
