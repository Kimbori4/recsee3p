package com.furence.recsee.common.model;

public class SendMailInfo {
	String host;
	Integer port;
	String protocal;
	String userName;
	String password;
	String defaultEncoding = "UTF-8";
	
	String smtpAuth = "false";
	String startTls = "false";
	
	String from;
	String fromName;
	String to;
	String toName;
	
	String subject;
	String text;
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
	public String getProtocal() {
		return protocal;
	}
	public void setProtocal(String protocal) {
		this.protocal = protocal;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getDefaultEncoding() {
		return defaultEncoding;
	}
	public void setDefaultEncoding(String defaultEncoding) {
		this.defaultEncoding = defaultEncoding;
	}
	public String getSmtpAuth() {
		return smtpAuth;
	}
	public void setSmtpAuth(String smtpAuth) {
		this.smtpAuth = smtpAuth;
	}
	public String getStartTls() {
		return startTls;
	}
	public void setStartTls(String startTls) {
		this.startTls = startTls;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getFromName() {
		return fromName;
	}
	public void setFromName(String fromName) {
		this.fromName = fromName;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getToName() {
		return toName;
	}
	public void setToName(String toName) {
		this.toName = toName;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	@Override
	public String toString() {
		return "SendMailInfo [host=" + host + ", port=" + port + ", protocal="
				+ protocal + ", userName=" + userName + ", password="
				+ password + ", defaultEncoding=" + defaultEncoding
				+ ", smtpAuth=" + smtpAuth + ", startTls=" + startTls
				+ ", from=" + from + ", fromName=" + fromName + ", to=" + to
				+ ", toName=" + toName + ", subject=" + subject + ", text="
				+ text + "]";
	}	
}
