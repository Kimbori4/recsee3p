package com.furence.recsee.common.service.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.furence.recsee.common.dao.OraGetDbDao;
import com.furence.recsee.common.model.ChannInfo;
import com.furence.recsee.common.model.CodeInfoVO;
import com.furence.recsee.common.model.CustomerInfoVO;
import com.furence.recsee.common.service.OraGetDbService;


@Service("oraGetDbService")
public class OraGetDbServiceImpl implements OraGetDbService {

	@Autowired
	OraGetDbDao oraGetMapper;

	public List<CustomerInfoVO> selectsCustomerList(){
		return oraGetMapper.selectsCustomerList();
	}

	public List<CodeInfoVO> selectsMgCodeList(){
		return oraGetMapper.selectsMgCodeList();
	}

	public List<CodeInfoVO> selectsSgCodeList(){
		return oraGetMapper.selectsSgCodeList();
	}

	public void selectCustInfo(HashMap<String, Object> map){
		oraGetMapper.selectCustInfo(map);
	}

	@Override
	public 	List<ChannInfo> selectChannelInfo(){
		return oraGetMapper.selectChannelInfo();
	}

	@Override
	public 	Integer selectMChannCnt(ChannInfo channelInfo){
		return oraGetMapper.selectMChannCnt(channelInfo);
	}
	@Override
	public void encodingCHAR(){
		oraGetMapper.encodingCHAR();
	}
}
