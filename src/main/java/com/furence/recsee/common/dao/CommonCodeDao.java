
package com.furence.recsee.common.dao;

import java.util.List;
import com.furence.recsee.common.model.CommonCodeVO;


public interface CommonCodeDao {

	List<CommonCodeVO> selectCommonCode(CommonCodeVO commonCode);
	CommonCodeVO selectCommonName(CommonCodeVO commonCode);
	
	Integer updateCommonCode(CommonCodeVO commonCode);
}
