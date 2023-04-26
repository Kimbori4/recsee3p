
package com.furence.recsee.common.service;

import java.util.List;

import com.furence.recsee.common.model.ChannInfo;
import com.furence.recsee.common.model.CodeInfoVO;
import com.furence.recsee.common.model.CustomerInfoVO;


public interface ChannInfoService {

	List<ChannInfo> selectChennelInfo(String sysCode);
	Integer checkChInfo(ChannInfo channelInfo);
	Integer updateChInfo(ChannInfo channelInfo);
	Integer deleteChInfo(ChannInfo channelInfo);
	Integer insertChInfo(ChannInfo channelInfo);
	List<ChannInfo> selectSysCode();
	Integer selectCompSysCode(String channelInfo);
	List<ChannInfo> selectChSysCode();
}
