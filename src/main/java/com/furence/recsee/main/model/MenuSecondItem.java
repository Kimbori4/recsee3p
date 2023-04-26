package com.furence.recsee.main.model;

import java.util.*;

import javax.xml.bind.annotation.*;

@XmlRootElement(name="item")
@XmlAccessorType(XmlAccessType.NONE)
public class MenuSecondItem {
	@XmlAttribute(name="id")
	private String attrSecondId;

	@XmlAttribute(name="text")
	private String attrSecondText;

	@XmlAttribute(name="img")
	private String attrSecondImg;

	@XmlAttribute(name="type")
	private String attrSecondType;

	@XmlElement(name="item")
	private Collection<MenuThirdItem> menuThirdItem = new ArrayList<MenuThirdItem>();

	@XmlElement(name="href")
	private List<String> menuSecondItemHref = new ArrayList<String>();

	public String getAttrSecondId() {
		return attrSecondId;
	}
	public void setAttrSecondId(String attrSecondId) {
		this.attrSecondId = attrSecondId;
	}

	public String getAttrSecondText() {
		return attrSecondText;
	}
	public void setAttrSecondText(String attrSecondText) {
		this.attrSecondText = attrSecondText;
	}

	public String getAttrSecondImg() {
		return attrSecondImg;
	}
	public void setAttrSecondImg(String attrSecondImg) {
		this.attrSecondImg = attrSecondImg;
	}

	public String getAttrSecondType() {
		return attrSecondType;
	}
	public void setAttrSecondType(String attrSecondType) {
		this.attrSecondType = attrSecondType;
	}

	public Collection<MenuThirdItem> getMenuThirdItem() {
		return menuThirdItem;
	}
	public void setMenuThirdItem(Collection<MenuThirdItem> menuThirdItem) {
		this.menuThirdItem = menuThirdItem;
	}

	public List<String> getMenuSecondItemHref() {
		return menuSecondItemHref;
	}
	public void setMenuSecondItemHref(List<String> menuSecondItemHref) {
		this.menuSecondItemHref = menuSecondItemHref;
	}

}
