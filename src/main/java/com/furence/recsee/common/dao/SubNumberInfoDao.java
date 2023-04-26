package com.furence.recsee.common.dao;

import java.util.List;

import com.furence.recsee.common.model.LogoVO;
import com.furence.recsee.common.model.PacketVO;
import com.furence.recsee.common.model.SubNumberInfo;

public interface SubNumberInfoDao {
	List<SubNumberInfo> selectSubNumberInfo(SubNumberInfo subNumberInfo);
	Integer insertSubNumberInfo(SubNumberInfo subNumberInfo);
	Integer updateSubNumberInfo(SubNumberInfo subNumberInfo);
	Integer deleteSubNumberInfo(SubNumberInfo subNumberInfo);
	
	List<LogoVO> selectLogoInfo(LogoVO logoVO);
	Integer updateLogoInfo(LogoVO logoVO);
	
	List<PacketVO> selectPacketSettingInfo(PacketVO packetVO);
	Integer insertPacketSettingInfo(PacketVO packetVO);
	Integer updatePacketSettingInfo(PacketVO packetVO);
	Integer deletePacketSettingInfo(PacketVO packetVO);
	
	List<PacketVO> selectPacketLogInfo(PacketVO packetVO);
	Integer selectTotalPacketLog(PacketVO packetVO);
	Integer deletePacketLogInfo(PacketVO packetVO);
}
