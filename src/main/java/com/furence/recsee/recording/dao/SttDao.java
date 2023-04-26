package com.furence.recsee.recording.dao;

public interface SttDao {
	void sendTaResult(String callId,String taResult,String reason) throws Exception;
}
