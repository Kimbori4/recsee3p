package com.furence.recsee.common.model;

public class ChannInfo {
	private Integer rChNum;
	private String rSysCode;
	private String rExtNum;
	private String rExtKind;
	private String rExtIp;
	private String rExtMac;
	private Double rRxAmp;
	private String rExtKey;
	private String rExtAgtIp;
	private Double rTxAmp;
	private Double rVolume;
	private Double rAgc;
	private String rRecYn;
	private String rLogYn;
	private String rScreenYn;
	private String rChTenant;
	private String rRecType;

	public Integer getrChNum() {
		return rChNum;
	}
	public void setrChNum(Integer rChNum) {
		this.rChNum = rChNum;
	}
	public String getrSysCode() {
		return rSysCode;
	}
	public void setrSysCode(String rSysCode) {
		this.rSysCode = rSysCode;
	}
	public String getrExtNum() {
		return rExtNum;
	}
	public void setrExtNum(String rExtNum) {
		this.rExtNum = rExtNum;
	}
	public String getrExtKind() {
		return rExtKind;
	}
	public void setrExtKind(String rExtKind) {
		this.rExtKind = rExtKind;
	}
	public String getrExtIp() {
		return rExtIp;
	}
	public void setrExtIp(String rExtIp) {
		this.rExtIp = rExtIp;
	}
	public String getrExtMac() {
		return rExtMac;
	}
	public void setrExtMac(String rExtMac) {
		this.rExtMac = rExtMac;
	}
	public Double getrRxAmp() {
		return rRxAmp;
	}
	public void setrRxAmp(Double rRxAmp) {
		this.rRxAmp = rRxAmp;
	}
	public String getrExtKey() {
		return rExtKey;
	}
	public void setrExtKey(String rExtKey) {
		this.rExtKey = rExtKey;
	}
	public String getrExtAgtIp() {
		return rExtAgtIp;
	}
	public void setrExtAgtIp(String rExtAgtIp) {
		this.rExtAgtIp = rExtAgtIp;
	}
	public Double getrTxAmp() {
		return rTxAmp;
	}
	public void setrTxAmp(Double rTxAmp) {
		this.rTxAmp = rTxAmp;
	}
	public Double getrVolume() {
		return rVolume;
	}
	public void setrVolume(Double rVolume) {
		this.rVolume = rVolume;
	}
	public Double getrAgc() {
		return rAgc;
	}
	public void setrAgc(Double rAgc) {
		this.rAgc = rAgc;
	}
	public String getrRecYn() {
		return rRecYn;
	}
	public void setrRecYn(String rRecYn) {
		this.rRecYn = rRecYn;
	}
	public String getrLogYn() {
		return rLogYn;
	}
	public void setrLogYn(String rLogYn) {
		this.rLogYn = rLogYn;
	}
	public String getrScreenYn() {
		return rScreenYn;
	}
	public void setrScreenYn(String rScreenYn) {
		this.rScreenYn = rScreenYn;
	}
	public String getrChTenant() {
		return rChTenant;
	}
	public void setrChTenant(String rChTenant) {
		this.rChTenant = rChTenant;
	}
	public String getrRecType() {
		return rRecType;
	}
	public void setrRecType(String rRecType) {
		this.rRecType = rRecType;
	}
	@Override
	public String toString() {
		return "channelInfo [rChNum=" + rChNum + ", rSysCode=" + rSysCode
				+ ", rExtNum=" + rExtNum + ", rExtKind=" + rExtKind
				+ ", rExtIp=" + rExtIp + ", rExtMac=" + rExtMac + ", rRxAmp="
				+ rRxAmp + ", rExtKey=" + rExtKey + ", rExtAgtIp=" + rExtAgtIp
				+ ", rTxAmp=" + rTxAmp + ", rVolume=" + rVolume + ", rAgc="
				+ rAgc + ", rRecYn=" + rRecYn + ", rLogYn=" + rLogYn
				+ ", rScreenYn=" + rScreenYn + ", rChTenant=" + rChTenant
				+ ", rRecType=" + rRecType + "]";
	}


}
