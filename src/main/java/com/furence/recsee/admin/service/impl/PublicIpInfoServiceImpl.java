package com.furence.recsee.admin.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.furence.recsee.admin.dao.PublicIpInfoDao;
import com.furence.recsee.admin.model.PublicIpInfo;
import com.furence.recsee.admin.service.PublicIpInfoService;

@Service("publicIpInfoService")
public class PublicIpInfoServiceImpl implements PublicIpInfoService {
	
	@Autowired
	PublicIpInfoDao publicIpInfoMapper;

	@Override
	public List<PublicIpInfo> selectPublicIpInfo(PublicIpInfo publicIpInfo) {
		return publicIpInfoMapper.selectPublicIpInfo(publicIpInfo);
	}

	@Override
	public PublicIpInfo selectOnePublicIpInfo(PublicIpInfo publicIpInfo) {
		return publicIpInfoMapper.selectOnePublicIpInfo(publicIpInfo);
	}

	@Override
	public Integer insertPublicIpInfo(PublicIpInfo publicIpInfo) {
		return publicIpInfoMapper.insertPublicIpInfo(publicIpInfo);
	}

	@Override
	public Integer updatePublicIpInfo(PublicIpInfo publicIpInfo) {
		return publicIpInfoMapper.updatePublicIpInfo(publicIpInfo);
	}

	@Override
	public Integer deletePublicIpInfo(PublicIpInfo publicIpInfo) {
		return publicIpInfoMapper.deletePublicIpInfo(publicIpInfo);
	}
	
}
