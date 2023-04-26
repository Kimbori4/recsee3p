
package com.furence.recsee.common.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.furence.recsee.common.dao.ChannInfoDao;
import com.furence.recsee.common.dao.CustomerInfoDao;
import com.furence.recsee.common.model.ChannInfo;
import com.furence.recsee.common.model.CodeInfoVO;
import com.furence.recsee.common.model.CustomerInfoVO;
import com.furence.recsee.common.service.ChannInfoService;
import com.furence.recsee.common.service.CustomerInfoService;


@Service("channInfoService")
public class ChannInfoServiceImpl implements ChannInfoService {

	@Autowired
	ChannInfoDao channelInfoMapper;

	@Override
	public List<ChannInfo> selectChennelInfo(String sysCode){
		return channelInfoMapper.selectChennelInfo(sysCode);
	}

	@Override
	public Integer checkChInfo(ChannInfo channelInfo){
		return channelInfoMapper.checkChInfo(channelInfo);
	}

	@Override
	public Integer updateChInfo(ChannInfo channelInfo){
		return channelInfoMapper.updateChInfo(channelInfo);
	}

	@Override
	public Integer deleteChInfo(ChannInfo channelInfo){
		return channelInfoMapper.deleteChInfo(channelInfo);
	}

	@Override
	public Integer insertChInfo(ChannInfo channelInfo){
		return channelInfoMapper.insertChInfo(channelInfo);
	}

	@Override
	public 	List<ChannInfo> selectSysCode(){
		return channelInfoMapper.selectSysCode();
	}

	@Override
	public Integer selectCompSysCode(String channelInfo){
		return channelInfoMapper.selectCompSysCode(channelInfo);
	}

	@Override
	public List<ChannInfo> selectChSysCode(){
		return channelInfoMapper.selectChSysCode();
	}
}