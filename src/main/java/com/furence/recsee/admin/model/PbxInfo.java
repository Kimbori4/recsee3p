package com.furence.recsee.admin.model;

public class PbxInfo {
	String rPbxId;
	String rPbxName;
	String rPbxIp;
	String rPbxLicense;
	String rPbxSipId;
	String rPbxSipPassword;
	
	public String getrPbxId() {
		return rPbxId;
	}
	public void setrPbxId(String rPbxId) {
		this.rPbxId = rPbxId;
	}
	public String getrPbxName() {
		return rPbxName;
	}
	public void setrPbxName(String rPbxName) {
		this.rPbxName = rPbxName;
	}
	public String getrPbxIp() {
		return rPbxIp;
	}
	public void setrPbxIp(String rPbxIp) {
		this.rPbxIp = rPbxIp;
	}
	public String getrPbxLicense() {
		return rPbxLicense;
	}
	public void setrPbxLicense(String rPbxLicense) {
		this.rPbxLicense = rPbxLicense;
	}
	public String getrPbxSipId() {
		return rPbxSipId;
	}
	public void setrPbxSipId(String rPbxSipId) {
		this.rPbxSipId = rPbxSipId;
	}
	public String getrPbxSipPassword() {
		return rPbxSipPassword;
	}
	public void setrPbxSipPassword(String rPbxSipPassword) {
		this.rPbxSipPassword = rPbxSipPassword;
	}
	@Override
	public String toString() {
		return "PbxInfo [rPbxId=" + rPbxId + ", rPbxName=" + rPbxName
				+ ", rPbxIp=" + rPbxIp + ", rPbxLicense=" + rPbxLicense
				+ ", rPbxSipId=" + rPbxSipId + ", rPbxSipPassword="
				+ rPbxSipPassword + "]";
	}
}
