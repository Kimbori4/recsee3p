package com.furence.recsee.wooribank.script.batch;

import org.springframework.stereotype.Repository;

@Repository
public interface BatchTaskDao {

	/**
	 * 일일 스크립트 예약건 적용 프로시저
	 * @return
	 */
	int executeScriptAppplyDialy();
	
	
}
