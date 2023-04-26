package com.furence.recsee.common.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.furence.recsee.common.dao.SubNumberInfoDao;
import com.furence.recsee.common.dao.SystemInfoDao;
import com.furence.recsee.common.model.LogoVO;
import com.furence.recsee.common.model.PacketVO;
import com.furence.recsee.common.model.SubNumberInfo;
import com.furence.recsee.common.model.SystemInfo;
import com.furence.recsee.common.service.SubNumberInfoService;
import com.furence.recsee.common.service.SystemInfoService;

@Service("subNumberInfoService")
public class SubNumberInfoServiceImpl implements SubNumberInfoService {
	@Autowired
	SubNumberInfoDao subNumberInfoMapper;

	@Override
	public List<SubNumberInfo> selectSubNumberInfo(SubNumberInfo subNumberInfo) {
		return subNumberInfoMapper.selectSubNumberInfo(subNumberInfo);
	}
	@Override
	public Integer insertSubNumberInfo(SubNumberInfo subNumberInfo) {
		return subNumberInfoMapper.insertSubNumberInfo(subNumberInfo);
	}
	@Override
	public Integer updateSubNumberInfo(SubNumberInfo subNumberInfo) {
		return subNumberInfoMapper.updateSubNumberInfo(subNumberInfo);
	}
	@Override
	public Integer deleteSubNumberInfo(SubNumberInfo subNumberInfo) {
		return subNumberInfoMapper.deleteSubNumberInfo(subNumberInfo);
	}
	
	@Override
	public List<LogoVO> selectLogoInfo(LogoVO logoVO) {
		return subNumberInfoMapper.selectLogoInfo(logoVO);
	}
	
	@Override
	public Integer updateLogoInfo(LogoVO logoVO) {
		return subNumberInfoMapper.updateLogoInfo(logoVO);
	}
	
	@Override
	public List<PacketVO> selectPacketSettingInfo(PacketVO packetVO) {
		return subNumberInfoMapper.selectPacketSettingInfo(packetVO);
	}
	
	@Override
	public List<PacketVO> selectPacketLogInfo(PacketVO packetVO) {
		return subNumberInfoMapper.selectPacketLogInfo(packetVO);
	}
	
	@Override
	public Integer selectTotalPacketLog(PacketVO packetVO) {
		return subNumberInfoMapper.selectTotalPacketLog(packetVO);
	}
	
	@Override
	public Integer insertPacketSettingInfo(PacketVO packetVO) {
		return subNumberInfoMapper.insertPacketSettingInfo(packetVO);
	}
	
	@Override
	public Integer updatePacketSettingInfo(PacketVO packetVO) {
		return subNumberInfoMapper.updatePacketSettingInfo(packetVO);
	}
	
	@Override
	public Integer deletePacketSettingInfo(PacketVO packetVO) {
		return subNumberInfoMapper.deletePacketSettingInfo(packetVO);
	}
	@Override
	public Integer deletePacketLogInfo(PacketVO packetVO) {
		return subNumberInfoMapper.deletePacketLogInfo(packetVO);
	}
}
