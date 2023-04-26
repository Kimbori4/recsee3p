package com.furence.recsee.main.model;

import java.util.*;

import javax.xml.bind.annotation.*;

@XmlRootElement(name="rows")
@XmlAccessorType(XmlAccessType.FIELD)
public class SearchListRecord {

	@XmlAttribute(name="total_count")
	private String total_count;
	
	@XmlAttribute(name="pos")
	private String pos;
	
	@XmlElementWrapper(name="head")
	@XmlElement(name="column")
	private Collection<SearchListColumnElement> columnElements;

	@XmlElement(name="row")
	private Collection<SearchListRecordElement> rowElements;
	
	@XmlElement(name="beforeInit")
	private dhtmlXGridHeadBeforeInit beforeElement;

	@XmlElement(name="afterInit")
	private dhtmlXGridHeadAfterInit afterElement;

	public String getTotal_count() {
		return total_count;
	}

	public void setTotal_count(String total_count) {
		this.total_count = total_count;
	}

	public String getPos() {
		return pos;
	}

	public void setPos(String pos) {
		this.pos = pos;
	}

	public Collection<SearchListColumnElement> getColumnElements() {
		return columnElements;
	}

	public void setColumnElements(Collection<SearchListColumnElement> columnElements) {
		this.columnElements = columnElements;
	}

	public Collection<SearchListRecordElement> getRowElements() {
		return rowElements;
	}

	public void setRowElements(Collection<SearchListRecordElement> rowElements) {
		this.rowElements = rowElements;
	}
	
	public dhtmlXGridHeadBeforeInit getBeforeElement() {
		return beforeElement;
	}

	public void setBeforeElement(dhtmlXGridHeadBeforeInit beforeElement) {
		this.beforeElement = beforeElement;
	}

	public dhtmlXGridHeadAfterInit getAfterElement() {
		return afterElement;
	}

	public void setAfterElement(dhtmlXGridHeadAfterInit afterElement) {
		this.afterElement = afterElement;
	}
}
