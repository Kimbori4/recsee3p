package com.furence.recsee.main.model;

import java.util.*;

import javax.xml.bind.annotation.*;

@XmlRootElement(name="menu")
@XmlAccessorType(XmlAccessType.NONE)
public class MenuCreate {
	@XmlElement(name="item")
	private Collection<MenuFirstItem> menuFirstItem = new ArrayList<MenuFirstItem>();

	public Collection<MenuFirstItem> getMenuFirstItem() {
		return menuFirstItem;
	}
	public void setMenuFirstItem(Collection<MenuFirstItem> menuFirstItem) {
		this.menuFirstItem = menuFirstItem;
	}

}
