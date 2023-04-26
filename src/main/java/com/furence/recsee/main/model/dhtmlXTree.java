package com.furence.recsee.main.model;

import java.util.Collection;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="tree")
@XmlAccessorType(XmlAccessType.NONE)
public class dhtmlXTree {

	@XmlAttribute(name="id")
	private String id;
	
	@XmlElement(name="item")
	private Collection<dhtmlXTreeItem> itemElements;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Collection<dhtmlXTreeItem> getItemElements() {
		return itemElements;
	}

	public void setItemElements(Collection<dhtmlXTreeItem> itemElements) {
		this.itemElements = itemElements;
	}
}
