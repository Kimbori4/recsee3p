package com.furence.recsee.myfolder.model;

public class MyFolderInfo {
	private String  rUserId;
	private String rFolderName;
	private String oldrFolderName;
	
	public String getrUserId() {
		return rUserId;
	}
	public void setrUserId(String rUserId) {
		this.rUserId = rUserId;
	}
	public String getrFolderName() {
		return rFolderName;
	}
	public void setrFolderName(String rFolderName) {
		this.rFolderName = rFolderName;
	}
	
	public String getOldrFolderName() {
		return oldrFolderName;
	}
	public void setOldrFolderName(String oldrFolderName) {
		this.oldrFolderName = oldrFolderName;
	}
	
	
}
