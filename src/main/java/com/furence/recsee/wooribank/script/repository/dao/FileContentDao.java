package com.furence.recsee.wooribank.script.repository.dao;

import org.springframework.stereotype.Repository;

import com.furence.recsee.wooribank.script.param.request.FileDownloadParam;
import com.furence.recsee.wooribank.script.param.request.FileDownloadParam.ScriptCallInfo;
import com.furence.recsee.wooribank.script.param.response.ScriptSnapshot;

@Repository
public interface FileContentDao {

	/**
	 * 상품스크립트 해당 버전의 스냅샷 조회
	 * @param productHistoryDto
	 * @return
	 */
	public ScriptSnapshot selectScriptVersionSnapshot(FileDownloadParam.VersionSnapshot param );
	
	
	/**
	 * 상품의  현재 스크립트 조회
	 * @param ScriptInfo
	 * @return
	 */
	public String selectProductScriptInfo(FileDownloadParam.ScriptCurrentInfo scriptInfo );
	
	
	/**
	 * 녹취 당시 상품 스크립트 조회
	 * @param ScriptCall
	 * @return
	 */
	public String selectCallScriptInfo(FileDownloadParam.ScriptCallInfo scriptCall );


	/**
	 * 녹취시작시 상품 스크립트 조회
	 * @param ScriptCall
	 * @return
	 */
	public String selectCallScriptInfoRec(ScriptCallInfo param);
}