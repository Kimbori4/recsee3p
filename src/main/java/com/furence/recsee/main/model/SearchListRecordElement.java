package com.furence.recsee.main.model;

import java.util.*;

import javax.xml.bind.annotation.*;


@XmlRootElement(name="row")
@XmlAccessorType(XmlAccessType.FIELD)
public class SearchListRecordElement {
	@XmlAttribute(name="id")
	private String id;
	
	@XmlAttribute(name="phone1")
	private String phone1;
	
	@XmlAttribute(name="phone2")
	private String phone2;
	
	@XmlAttribute(name="phone3")
	private String phone3;
	
	@XmlAttribute(name="custName")
	private String custName;
	
	@XmlAttribute(name="userName")
	private String userName;
	

	@XmlElement(name="cell")
	private List<String> searchListRecordElementCell;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPhone1() {
		return phone1;
	}

	public void setPhone1(String phone1) {
		this.phone1 = phone1;
	}

	public String getPhone2() {
		return phone2;
	}

	public void setPhone2(String phone2) {
		this.phone2 = phone2;
	}

	public String getPhone3() {
		return phone3;
	}

	public void setPhone3(String phone3) {
		this.phone3 = phone3;
	}

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public SearchListRecordElement() {
		searchListRecordElementCell = new ArrayList<String>();
	}

	public List<String> getSearchListRecordElementCell() {
		return searchListRecordElementCell;
	}

	public void setSearchListRecordElementCell(List<String> searchListRecordElementCell) {
		this.searchListRecordElementCell = searchListRecordElementCell;
	}

}
