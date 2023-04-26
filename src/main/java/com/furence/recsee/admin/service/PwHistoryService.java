package com.furence.recsee.admin.service;

import com.furence.recsee.admin.model.PwHistory;

public interface PwHistoryService {
	Integer selectPwHistory(PwHistory pwHistory);
	Integer insertPwHistory(PwHistory pwHistory);
}
