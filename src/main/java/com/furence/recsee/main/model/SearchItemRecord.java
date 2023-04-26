package com.furence.recsee.main.model;

import java.util.Collection;

import javax.xml.bind.annotation.*;

@XmlRootElement(name="items")
@XmlAccessorType(XmlAccessType.NONE)
public class SearchItemRecord {

	@XmlElement(name="item")
	private Collection<SearchItemRecordElement> itemElements;

	public Collection<SearchItemRecordElement> getItemElements() {
		return itemElements;
	}

	public void setItemElements(Collection<SearchItemRecordElement> itemElements) {
		this.itemElements = itemElements;
	}
	
	
}
