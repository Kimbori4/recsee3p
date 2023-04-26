package com.furence.recsee.common.service;

import java.util.List;

import com.furence.recsee.common.model.CommonCodeVO;

public interface CommonCodeService {
	List<CommonCodeVO> selectCommonCode(CommonCodeVO commonCode);
	CommonCodeVO selectCommonName(CommonCodeVO commonCode);

	Integer updateCommonCode(CommonCodeVO commonCode);
}
