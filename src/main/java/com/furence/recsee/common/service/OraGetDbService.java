package com.furence.recsee.common.service;

import java.util.HashMap;
import java.util.List;

import com.furence.recsee.common.model.ChannInfo;
import com.furence.recsee.common.model.CodeInfoVO;
import com.furence.recsee.common.model.CustomerInfoVO;


public interface OraGetDbService {

	List<CustomerInfoVO> selectsCustomerList();
	List<CodeInfoVO> selectsMgCodeList();
	List<CodeInfoVO> selectsSgCodeList();
	void selectCustInfo(HashMap<String, Object> map);
	List<ChannInfo> selectChannelInfo();
	Integer selectMChannCnt(ChannInfo channelInfo);
	void encodingCHAR();
}
