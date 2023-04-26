package com.furence.recsee.common.dao;

import java.util.List;

import com.furence.recsee.common.model.CustConfigInfo;

public interface CustConfigInfoDao {
	List<CustConfigInfo> selectCustConfigInfo(CustConfigInfo custConfigInfo);
	List<CustConfigInfo> selectMenuYCustConfigInfo(CustConfigInfo custConfigInfo);
	List<CustConfigInfo> selectMenuNCustConfigInfo(CustConfigInfo custConfigInfo);
	Integer	insertCustConfigInfo(CustConfigInfo custConfigInfo);
	Integer updateCustConfigInfo(CustConfigInfo custConfigInfo);
	Integer deleteCustConfigInfo(CustConfigInfo custConfigInfo);
}
