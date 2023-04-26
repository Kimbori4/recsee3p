package com.furence.recsee.admin.dao;

import com.furence.recsee.admin.model.PwHistory;

public interface PwHistoryDao {
	Integer selectPwHistory(PwHistory pwHistory);
	Integer insertPwHistory(PwHistory pwHistory);
}
