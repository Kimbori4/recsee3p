package com.furence.recsee.main.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="sidebar")
@XmlAccessorType(XmlAccessType.NONE)
public class dhtmlXSidebarXml {

	@XmlElement(name="item")
	private List<dhtmlXSidebarItem> itemElement;

	public List<dhtmlXSidebarItem> getItemElement() {
		return itemElement;
	}

	public void setItemElement(List<dhtmlXSidebarItem> itemElement) {
		this.itemElement = itemElement;
	}
}