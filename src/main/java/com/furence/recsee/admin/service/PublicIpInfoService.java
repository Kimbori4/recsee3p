package com.furence.recsee.admin.service;

import java.util.List;

import com.furence.recsee.admin.model.PublicIpInfo;

public interface PublicIpInfoService {
	List<PublicIpInfo> selectPublicIpInfo(PublicIpInfo publicIpInfo);
	PublicIpInfo selectOnePublicIpInfo(PublicIpInfo publicIpInfo);
	Integer insertPublicIpInfo(PublicIpInfo publicIpInfo);
	Integer updatePublicIpInfo(PublicIpInfo publicIpInfo);
	Integer deletePublicIpInfo(PublicIpInfo publicIpInfo);
}
