package com.furence.recsee.monitoring.model;

public class DashboardInfo {
	private int day1;
	private int day2;
	private int day3;
	private int day4;
	private int day5;
	private int day6;
	private int day7;
	private String startDate;
	private String endDate;
	private String rExtNum;
	private String rRecRtime;
	private String rChNum;
	private String rRecDate;
	private String rRecHh24;
	private String rRecMi;
	private String rRecSs;
	private Integer rRecCnt;
	private String rCallEtime;
	private String rUserId;
	private String rUserName;


	public String getrUserId() {
		return rUserId;
	}
	public void setrUserId(String rUserId) {
		this.rUserId = rUserId;
	}
	public String getrUserName() {
		return rUserName;
	}
	public void setrUserName(String rUserName) {
		this.rUserName = rUserName;
	}
	public String getrRecSs() {
		return rRecSs;
	}
	public void setrRecSs(String rRecSs) {
		this.rRecSs = rRecSs;
	}
	public String getrCallEtime() {
		return rCallEtime;
	}
	public void setrCallEtime(String rCallEtime) {
		this.rCallEtime = rCallEtime;
	}
	public String getrRecDate() {
		return rRecDate;
	}
	public void setrRecDate(String rRecDate) {
		this.rRecDate = rRecDate;
	}
	public String getrRecHh24() {
		return rRecHh24;
	}
	public void setrRecHh24(String rRecHh24) {
		this.rRecHh24 = rRecHh24;
	}
	public String getrRecMi() {
		return rRecMi;
	}
	public void setrRecMi(String rRecMi) {
		this.rRecMi = rRecMi;
	}
	public Integer getrRecCnt() {
		return rRecCnt;
	}
	public void setrRecCnt(Integer rRecCnt) {
		this.rRecCnt = rRecCnt;
	}
	public String getrRecRtime() {
		return rRecRtime;
	}
	public void setrRecRtime(String rRecRtime) {
		this.rRecRtime = rRecRtime;
	}
	public String getrChNum() {
		return rChNum;
	}
	public void setrChNum(String rChNum) {
		this.rChNum = rChNum;
	}
	public int getDay1() {
		return day1;
	}
	public void setDay1(int day1) {
		this.day1 = day1;
	}
	public int getDay2() {
		return day2;
	}
	public void setDay2(int day2) {
		this.day2 = day2;
	}
	public int getDay3() {
		return day3;
	}
	public void setDay3(int day3) {
		this.day3 = day3;
	}
	public int getDay4() {
		return day4;
	}
	public void setDay4(int day4) {
		this.day4 = day4;
	}
	public int getDay5() {
		return day5;
	}
	public void setDay5(int day5) {
		this.day5 = day5;
	}
	public int getDay6() {
		return day6;
	}
	public void setDay6(int day6) {
		this.day6 = day6;
	}
	public int getDay7() {
		return day7;
	}
	public void setDay7(int day7) {
		this.day7 = day7;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getrExtNum() {
		return rExtNum;
	}
	public void setrExtNum(String rExtNum) {
		this.rExtNum = rExtNum;
	}

	@Override
	public String toString() {
		return "DashboardInfo [day1=" + day1 + ", day2=" + day2 + ", day3="
				+ day3 + ", day4=" + day4 + ", day5=" + day5 + ", day6=" + day6
				+ ", day7=" + day7 + ", startDate=" + startDate + ", endDate="
				+ endDate + ", rExtNum=" + rExtNum + ", rRecRtime=" + rRecRtime
				+ ", rChNum=" + rChNum + ", rRecDate=" + rRecDate
				+ ", rRecHh24=" + rRecHh24 + ", rRecMi=" + rRecMi + ", rRecSs="
				+ rRecSs + ", rRecCnt=" + rRecCnt + ", rCallEtime="
				+ rCallEtime + ", rUserId=" + rUserId + ", rUserName="
				+ rUserName + "]";
	}

}
