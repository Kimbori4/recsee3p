package com.furence.recsee.common.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.furence.recsee.common.dao.CommonCodeDao;
import com.furence.recsee.common.model.CommonCodeVO;
import com.furence.recsee.common.service.CommonCodeService;


@Service("commonCodeService")
public class CommonCodeServiceImpl implements CommonCodeService {
	@Autowired
	CommonCodeDao commonCodeMapper;

	@Override
	public List<CommonCodeVO> selectCommonCode(CommonCodeVO commonCode){
		return commonCodeMapper.selectCommonCode(commonCode);
	}
	
	@Override
	public CommonCodeVO selectCommonName(CommonCodeVO commonCode) {
		return commonCodeMapper.selectCommonName(commonCode);
	}

	@Override
	public Integer updateCommonCode(CommonCodeVO commonCode) {
		return commonCodeMapper.updateCommonCode(commonCode);
	}
}
