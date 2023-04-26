package com.furence.recsee.common.dao;

import java.util.List;

import com.furence.recsee.common.model.Log;

public interface LogDao {
	List<Log> selectLog(Log log);
	List<Log> selectLogName(Log log);
	List<Log> selectLogContents(Log log);
	Integer totalLog(Log log);
	Integer insertLog(Log log);
	Integer deleteLog(Log log);
}
