package com.furence.recsee.admin.model;

public class QueueInfo {
	String rQueueNum;
	String rQueueName;
	
	public String getrQueueNum() {
		return rQueueNum;
	}
	public void setrQueueNum(String rQueueNum) {
		this.rQueueNum = rQueueNum;
	}
	public String getrQueueName() {
		return rQueueName;
	}
	public void setrQueueName(String rQueueName) {
		this.rQueueName = rQueueName;
	}
	@Override
	public String toString() {
		return "QueueInfo [rQueueNum=" + rQueueNum + ", rQueueName="
				+ rQueueName + "]";
	}
}
