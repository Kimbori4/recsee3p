package com.furence.recsee.main.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="item")
@XmlAccessorType(XmlAccessType.NONE)
public class dhtmlXMenuItem {
	
	@XmlAttribute(name="id")
	private String id;
	
	@XmlAttribute(name="text")
	private String text;
	
	@XmlAttribute(name="img")
	private String img;
	
	@XmlAttribute(name="imgdis")
	private String imgDisabled;
	
	@XmlAttribute(name="enabled")
	private String enabled;
	
	@XmlAttribute(name="complex")
	private String complex;
	
	@XmlElement(name="itemtext")
	private String itemtext;
	
	@XmlElement(name="hotkey")
	private String hotKey;
	
	@XmlElement(name="userdata")
	private dhtmlXMenuItemUserData userData;
	
	@XmlElement(name="href" )
	private dhtmlXMenuItemHref href;
	
	/**
	 * checkbox
	 */
	@XmlAttribute(name="checked")
	private String checked;

	@XmlAttribute(name="type")
	private String type;
	/**
	 * radio button
	 */
	@XmlAttribute(name="group")
	private String group;
	
	@XmlElement(name="item")
	private dhtmlXMenuItem itemElement;

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

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getImgDisabled() {
		return imgDisabled;
	}

	public void setImgDisabled(String imgDisabled) {
		this.imgDisabled = imgDisabled;
	}

	public String getEnabled() {
		return enabled;
	}

	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}

	public String getComplex() {
		return complex;
	}

	public void setComplex(String complex) {
		this.complex = complex;
	}

	public String getItemtext() {
		return itemtext;
	}

	public void setItemtext(String itemtext) {
		this.itemtext = itemtext;
	}

	public String getHotKey() {
		return hotKey;
	}

	public void setHotKey(String hotKey) {
		this.hotKey = hotKey;
	}

	public dhtmlXMenuItemUserData getUserData() {
		return userData;
	}

	public void setUserData(dhtmlXMenuItemUserData userData) {
		this.userData = userData;
	}

	public dhtmlXMenuItemHref getHref() {
		return href;
	}

	public void setHref(dhtmlXMenuItemHref href) {
		this.href = href;
	}

	public String getChecked() {
		return checked;
	}

	public void setChecked(String checked) {
		this.checked = checked;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public dhtmlXMenuItem getItemElement() {
		return itemElement;
	}

	public void setItemElement(dhtmlXMenuItem itemElement) {
		this.itemElement = itemElement;
	}	
}
