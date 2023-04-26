package com.furence.recsee.main.model;

import java.util.Collection;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="item")
@XmlAccessorType(XmlAccessType.NONE)
public class dhtmlXTreeItem {

	@XmlAttribute(name="id")
	private String id;
	
	@XmlAttribute(name="text")
	private String text;
	
	@XmlAttribute(name="tooltip")
	private String toolTip;
	
	@XmlAttribute(name="im0")
	private String im0;
	
	@XmlAttribute(name="im1")
	private String im1;
	
	@XmlAttribute(name="im2")
	private String im2;
	
	@XmlAttribute(name="aCol")
	private String aColor;
	
	@XmlAttribute(name="sCol")
	private String sColor;
	
	@XmlAttribute(name="select")
	private String select;
	
	@XmlAttribute(name="style")
	private String style;
	
	@XmlAttribute(name="open")
	private String open;

	@XmlAttribute(name="call")
	private String call;
	
	@XmlAttribute(name="checked")
	private String checked;
	
	@XmlAttribute(name="nocheckbox")
	private String noCheckbox;
	
	@XmlAttribute(name="child")
	private String child;
	
	@XmlAttribute(name="imheight")
	private String imgHeight;
	
	@XmlAttribute(name="imwidth")
	private String imgWidth;
	
	@XmlAttribute(name="topoffset")
	private String topOffset;
	
	@XmlAttribute(name="radio")
	private String radio;

	@XmlElement(name="item")
	private Collection<dhtmlXTreeItem> itemElements;
	
	@XmlElement(name="userdata")
	private Collection<dhtmlXTreeUserData> userItemElement;
	
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

	public String getToolTip() {
		return toolTip;
	}

	public void setToolTip(String toolTip) {
		this.toolTip = toolTip;
	}

	public String getIm0() {
		return im0;
	}

	public void setIm0(String im0) {
		this.im0 = im0;
	}

	public String getIm1() {
		return im1;
	}

	public void setIm1(String im1) {
		this.im1 = im1;
	}

	public String getIm2() {
		return im2;
	}

	public void setIm2(String im2) {
		this.im2 = im2;
	}

	public String getaColor() {
		return aColor;
	}

	public void setaColor(String aColor) {
		this.aColor = aColor;
	}

	public String getsColor() {
		return sColor;
	}

	public void setsColor(String sColor) {
		this.sColor = sColor;
	}

	public String getSelect() {
		return select;
	}

	public void setSelect(String select) {
		this.select = select;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getOpen() {
		return open;
	}

	public void setOpen(String open) {
		this.open = open;
	}

	public String getCall() {
		return call;
	}

	public void setCall(String call) {
		this.call = call;
	}

	public String getChecked() {
		return checked;
	}

	public void setChecked(String checked) {
		this.checked = checked;
	}

	public String getNoCheckbox() {
		return noCheckbox;
	}

	public void setNoCheckbox(String noCheckbox) {
		this.noCheckbox = noCheckbox;
	}

	public String getChild() {
		return child;
	}

	public void setChild(String child) {
		this.child = child;
	}

	public String getImgHeight() {
		return imgHeight;
	}

	public void setImgHeight(String imgHeight) {
		this.imgHeight = imgHeight;
	}

	public String getImgWidth() {
		return imgWidth;
	}

	public void setImgWidth(String imgWidth) {
		this.imgWidth = imgWidth;
	}

	public String getTopOffset() {
		return topOffset;
	}

	public void setTopOffset(String topOffset) {
		this.topOffset = topOffset;
	}

	public String getRadio() {
		return radio;
	}

	public void setRadio(String radio) {
		this.radio = radio;
	}

	public Collection<dhtmlXTreeItem> getItemElements() {
		return itemElements;
	}

	public void setItemElements(Collection<dhtmlXTreeItem> itemElements) {
		this.itemElements = itemElements;
	}

	public Collection<dhtmlXTreeUserData> getUserItemElement() {
		return userItemElement;
	}

	public void setUserItemElement(Collection<dhtmlXTreeUserData> userItemElement) {
		this.userItemElement = userItemElement;
	}
	
}
