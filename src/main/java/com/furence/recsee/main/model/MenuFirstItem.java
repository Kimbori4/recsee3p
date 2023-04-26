package com.furence.recsee.main.model;

import java.util.*;

import javax.xml.bind.annotation.*;

@XmlRootElement(name="item")
@XmlAccessorType(XmlAccessType.NONE)
public class MenuFirstItem {
	@XmlAttribute(name="id")
	private String attrFirstId;

	@XmlAttribute(name="text")
	private String attrFirstText;

	@XmlAttribute(name="img")
	private String attrFirstImg;

	@XmlAttribute(name="type")
	private String attrFirstType;

	@XmlElement(name="item")
	private Collection<MenuSecondItem> menuSecondItem = new ArrayList<MenuSecondItem>();

	public String getAttrFirstId() {
		return attrFirstId;
	}
	public void setAttrFirstId(String attrFirstId) {
		this.attrFirstId = attrFirstId;
	}

	public String getAttrFirstText() {
		return attrFirstText;
	}
	public void setAttrFirstText(String attrFirstText) {
		this.attrFirstText = attrFirstText;
	}

	public String getAttrFirstImg() {
		return attrFirstImg;
	}
	public void setAttrFirstImg(String attrFirstImg) {
		this.attrFirstImg = attrFirstImg;
	}

	public String getAttrFirstType() {
		return attrFirstType;
	}
	public void setAttrFirstType(String attrFirstType) {
		this.attrFirstType = attrFirstType;
	}

	public Collection<MenuSecondItem> getMenuSecondItem() {
		return menuSecondItem;
	}
	public void setMenuSecondItem(Collection<MenuSecondItem> menuSecondItem) {
		this.menuSecondItem = menuSecondItem;
	}

}
