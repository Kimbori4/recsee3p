package com.furence.recsee.common.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.furence.recsee.common.dao.CustConfigInfoDao;
import com.furence.recsee.common.model.CustConfigInfo;
import com.furence.recsee.common.service.CustConfigInfoService;

@Service("custConfigInfoService")
public class CustConfigInfoServiceImpl implements CustConfigInfoService {
	@Autowired
	CustConfigInfoDao custConfigInfoMapper;

	@Override
	public List<CustConfigInfo> selectCustConfigInfo(CustConfigInfo custConfigInfo) {
		return custConfigInfoMapper.selectCustConfigInfo(custConfigInfo);
	}

	@Override
	public List<CustConfigInfo> selectMenuYCustConfigInfo(CustConfigInfo custConfigInfo) {
		return custConfigInfoMapper.selectMenuYCustConfigInfo(custConfigInfo);
	}

	@Override
	public List<CustConfigInfo> selectMenuNCustConfigInfo(CustConfigInfo custConfigInfo) {
		return custConfigInfoMapper.selectMenuNCustConfigInfo(custConfigInfo);
	}

	@Override
	public Integer insertCustConfigInfo(CustConfigInfo custConfigInfo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer updateCustConfigInfo(CustConfigInfo custConfigInfo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer deleteCustConfigInfo(CustConfigInfo custConfigInfo) {
		// TODO Auto-generated method stub
		return null;
	}

}
