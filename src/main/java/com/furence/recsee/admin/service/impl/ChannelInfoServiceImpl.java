package com.furence.recsee.admin.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.furence.recsee.admin.dao.ChannelInfoDao;
import com.furence.recsee.admin.model.ChannelInfo;
import com.furence.recsee.admin.service.ChannelInfoService;

@Service("channelInfoService")
public class ChannelInfoServiceImpl implements ChannelInfoService {
	@Autowired
	ChannelInfoDao channelInfoMapper;

	@Override
	public List<ChannelInfo> selectChannelInfo(ChannelInfo channelInfo) {
		// TODO Auto-generated method stub
		return channelInfoMapper.selectChannelInfo(channelInfo);
	}

	@Override
	public List<ChannelInfo> groupChannelInfo(ChannelInfo channelInfo) {
		// TODO Auto-generated method stub
		return channelInfoMapper.groupChannelInfo(channelInfo);
	}

	@Override
	public Integer insertChannelInfo(ChannelInfo channelInfo) {
		// TODO Auto-generated method stub
		return channelInfoMapper.insertChannelInfo(channelInfo);
	}

	@Override
	public Integer updateChannelInfo(ChannelInfo channelInfo) {
		// TODO Auto-generated method stub
		return channelInfoMapper.updateChannelInfo(channelInfo);
	}

	@Override
	public Integer deleteChannelInfo(ChannelInfo channelInfo) {
		// TODO Auto-generated method stub
		return channelInfoMapper.deleteChannelInfo(channelInfo);
	}

	@Override
	public List<ChannelInfo> selectChannelInfoGet() {
		// TODO Auto-generated method stub
		return channelInfoMapper.selectChannelInfoGet();
	}

	@Override
	public List<ChannelInfo> selectChannelMonitoringInfo(ChannelInfo channelInfo) {
		// TODO Auto-generated method stub 
		return channelInfoMapper.selectChannelMonitoringInfo(channelInfo);
	}

	@Override
	public List<ChannelInfo> selectChannelMonitoringExtInfo(ChannelInfo channelInfo) {
		// TODO Auto-generated method stub
		return channelInfoMapper.selectChannelMonitoringExtInfo(channelInfo);
	}

	@Override
	public Integer checkIpOverlap(ChannelInfo channelInfo) {
		// TODO Auto-generated method stub
		return channelInfoMapper.checkIpOverlap(channelInfo);
	}



	@Override
	public Integer checkExtOverlap(ChannelInfo channelInfo) {
		// TODO Auto-generated method stub
		return channelInfoMapper.checkExtOverlap(channelInfo);
	}

	@Override
	public Integer updateSwitchChannelInfo(ChannelInfo channelInfo) {
		// TODO Auto-generated method stub
		return channelInfoMapper.updateSwitchChannelInfo(channelInfo);
	}

	@Override
	public Integer updateReturningSwitchChannelInfo(ChannelInfo channelInfo) {
		// TODO Auto-generated method stub
		return channelInfoMapper.updateReturningSwitchChannelInfo(channelInfo);
	}
}
