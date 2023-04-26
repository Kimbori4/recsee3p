package com.furence.recsee.wooribank.script.param.response;

import com.furence.recsee.wooribank.script.util.Pair;

public enum ResultCode {

	SUCCESS( new Pair<Integer, String>(0, "Success") ),
	NO_LGOING_USER( new Pair<Integer, String>(-1001, "로그인 필요") ),
	NO_PERMITTION( new Pair<Integer, String>(-1002, "권한없음") ),
	
	MISSED_REQUIRED_PARAMETER( new Pair<Integer, String>(-2001, "필수 파라미터 누락") ),
	INVALID_PARAMETER( new Pair<Integer, String>(-2002, "파라미터 유효성 검사 실패") ) ,
	
	ALREADY_REQUESTED( new Pair<Integer, String>(-3001, "결재/적용 예정 데이터가 존재함") ) ,
	ALREADY_COMPLETED( new Pair<Integer, String>(-3002, "이미 완료된 트랜잭션") ) ,
	NOT_FOUND_TRANSACTION( new Pair<Integer, String>(-3003, "존재하지 않는 트랜잭션") ) ,
	NO_AFFECTED( new Pair<Integer, String>(-3004, "영향받은 행이 없음") ) ,
	NO_EDITED_SCRIPT( new Pair<Integer, String>(-3005, "적용할 스크립트 수정건이 없음") ) ,
	USING_IN_PRODUCT_SCRIPT( new Pair<Integer, String>(-3006, "스크립트디테일에 사용중인 공용문구") ),
	NOT_REJECTED (new Pair<Integer, String>(-3007, "결재대기 또는 결재완료 상태로 재상신 불가")),
	
	CONNECT_FAIL_TO_TTS_SERVER( new Pair<Integer, String>(-4001, "TTS 서버 접속 실패") ),
	
	SYSTEM_ERROR( new Pair<Integer, String>(-9999, "시스템 에러") ),
	UNDEFINED_ERROR( new Pair<Integer, String>(-1, "정의되지 않은 에러") );
	
	private Pair<Integer, String> resultSet;
	
	private ResultCode(Pair<Integer, String> resultSet) {
		this.resultSet = resultSet;
	}
	
	public int getCode() {
		return this.resultSet.first();
	}
	
	public String getMessage() {
		return this.resultSet.second();
	}
	
	public void setMessage(String msg) {
		this.resultSet.second(msg);
	}
	
	public static ResultCode create(int code) {
		for(ResultCode result: ResultCode.values()) {
			if(result.getCode() == code ) {
				return result;
			}
		}
		return UNDEFINED_ERROR;
	}
	
	public static ResultCode of(Throwable th) {
		ResultCode result = SYSTEM_ERROR;
		result.setMessage(th.getLocalizedMessage());
		return result;
	}
}
