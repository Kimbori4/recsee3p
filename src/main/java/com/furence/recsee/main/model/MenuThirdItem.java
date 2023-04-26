package com.furence.recsee.main.model;

import java.util.*;

import javax.xml.bind.annotation.*;

@XmlRootElement(name="item")
@XmlAccessorType(XmlAccessType.NONE)
public class MenuThirdItem {
	@XmlAttribute(name="id")
	private String attrThirdId;

	@XmlAttribute(name="text")
	private String attrThirdText;

	@XmlAttribute(name="img")
	private String attrThirdImg;

	@XmlAttribute(name="type")
	private String attrThirdType;

	@XmlElement(name="href")
	private List<String> menuThirdItemHref = new ArrayList<String>();

	public String getAttrThirdId() {
		return attrThirdId;
	}
	public void setAttrThirdId(String attrThirdId) {
		this.attrThirdId = attrThirdId;
	}

	public String getAttrThirdText() {
		return attrThirdText;
	}
	public void setAttrThirdText(String attrThirdText) {
		this.attrThirdText = attrThirdText;
	}

	public String getAttrThirdImg() {
		return attrThirdImg;
	}
	public void setAttrThirdImg(String attrThirdImg) {
		this.attrThirdImg = attrThirdImg;
	}

	public String getAttrThirdType() {
		return attrThirdType;
	}
	public void setAttrThirdType(String attrThirdType) {
		this.attrThirdType = attrThirdType;
	}

	public List<String> getMenuThirdItemHref() {
		return menuThirdItemHref;
	}
	public void setMenuThirdItemHref(List<String> menuThirdItemHref) {
		this.menuThirdItemHref = menuThirdItemHref;
	}
}
