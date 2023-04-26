package com.furence.recsee.wooribank.script.param.response;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ScriptSnapshot {
	
	/**
	 * 수정자
	 */
	@JsonProperty("rsUpdateUser")
	private String updateUser;
	
	/**
	 * 수정일
	 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	@JsonProperty("rsUpdateDate")
	private Date updateDate;
	
	/**
	 * 결재자
	 */
	@JsonProperty("rsApproveUser")
	private String approveUser;
	
	/**
	 * 결재일
	 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	@JsonProperty("rsApproveDate")
	private Date approveDate;
	
	/**
	 * 스냅샷 기준 버전
	 */
	@JsonProperty("rsVersion")
	private int version;
	
	@JsonIgnoreProperties(ignoreUnknown = true)
	private ScriptInfo scriptInfo;
}
