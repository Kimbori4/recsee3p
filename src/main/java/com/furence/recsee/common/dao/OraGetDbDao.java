package com.furence.recsee.common.dao;

import java.util.HashMap;
import java.util.List;

import com.furence.recsee.common.model.ChannInfo;
import com.furence.recsee.common.model.CodeInfoVO;
import com.furence.recsee.common.model.CustomerInfoVO;


public interface OraGetDbDao {

	List<CustomerInfoVO> selectsCustomerList();
	//List<CustomerInfoVO> BgCodList();
	List<CodeInfoVO> selectsMgCodeList();
	List<CodeInfoVO> selectsSgCodeList();
	void selectCustInfo(HashMap<String, Object> map);
	List<ChannInfo> selectChannelInfo();
	Integer selectMChannCnt(ChannInfo channelInfo);
	void encodingCHAR();
}
