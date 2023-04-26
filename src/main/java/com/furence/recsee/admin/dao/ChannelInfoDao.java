package com.furence.recsee.admin.dao;

import java.util.List;

import com.furence.recsee.admin.model.ChannelInfo;

public interface ChannelInfoDao {
	List<ChannelInfo> selectChannelInfo(ChannelInfo channelInfo);
	List<ChannelInfo> groupChannelInfo(ChannelInfo channelInfo);
	Integer insertChannelInfo(ChannelInfo channelInfo);
	Integer updateChannelInfo(ChannelInfo channelInfo);
	Integer deleteChannelInfo(ChannelInfo channelInfo);
	List<ChannelInfo> selectChannelInfoGet();
	List<ChannelInfo> selectChannelMonitoringInfo(ChannelInfo channelInfo);
	List<ChannelInfo> selectChannelMonitoringExtInfo(ChannelInfo channelInfo);
	Integer checkIpOverlap(ChannelInfo channelInfo);
	Integer checkExtOverlap(ChannelInfo channelInfo);
	
	Integer updateSwitchChannelInfo(ChannelInfo channelInfo);
	Integer updateReturningSwitchChannelInfo(ChannelInfo channelInfo);
}
