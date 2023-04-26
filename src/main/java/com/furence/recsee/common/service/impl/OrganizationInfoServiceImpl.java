package com.furence.recsee.common.service.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.furence.recsee.common.dao.OrganizationInfoDao;
import com.furence.recsee.common.model.OrganizationInfo;
import com.furence.recsee.common.service.OrganizationInfoService;

@Service("organizationInfoService")
public class OrganizationInfoServiceImpl implements OrganizationInfoService {
	
	@Autowired
	OrganizationInfoDao organizatinInfoMapper;

	@Override
	public List<OrganizationInfo> selectOrganizationBgInfo(
			OrganizationInfo organizationInfo) {

		return organizatinInfoMapper.selectOrganizationBgInfo(organizationInfo);
	}

	@Override
	public List<OrganizationInfo> selectOrganizationMgInfo(
			OrganizationInfo organizationInfo) {
		
		return organizatinInfoMapper.selectOrganizationMgInfo(organizationInfo);
	}

	@Override
	public List<OrganizationInfo> selectOrganizationSgInfo(
			OrganizationInfo organizationInfo) {
		
		return organizatinInfoMapper.selectOrganizationSgInfo(organizationInfo);
	}

	@Override
	public HashMap<String, String> selectOrganizationLastInfo(OrganizationInfo organizationInfo) {
		
		return organizatinInfoMapper.selectOrganizationLastInfo(organizationInfo);
	}

	@Override
	public Integer insertOrganizationInfo(OrganizationInfo organizationInfo) {
		
		return organizatinInfoMapper.insertOrganizationInfo(organizationInfo);
	}

	@Override
	public Integer updateOrganizationInfo(OrganizationInfo organizationInfo) {
		
		return organizatinInfoMapper.updateOrganizationInfo(organizationInfo);
	}

	@Override
	public Integer deleteOrganizationInfo(OrganizationInfo organizationInfo) {
		
		return organizatinInfoMapper.deleteOrganizationInfo(organizationInfo);
	}

	@Override
	public List<OrganizationInfo> sameNameOrganizationInfo(OrganizationInfo organizationInfo) {
		
		return organizatinInfoMapper.sameNameOrganizationInfo(organizationInfo);
	}

	@Override
	public Integer updateGroupCodeInfo(OrganizationInfo organizationInfo) {
		
		return organizatinInfoMapper.updateGroupCodeInfo(organizationInfo);
	}

	@Override
	public List<OrganizationInfo> selectOraganiztionAll(OrganizationInfo organizationInfo) {
		return organizatinInfoMapper.selectOraganiztionAll(organizationInfo);
	}

	@Override
	public Integer updateUseBg(OrganizationInfo bgcode) {
		return organizatinInfoMapper.updateUseBg(bgcode);
	}

	@Override
	public List<OrganizationInfo> selectCallCenterMgInfo(OrganizationInfo organizationInfo) {
		return organizatinInfoMapper.selectCallCenterMgInfo(organizationInfo);
	}
	
	@Override
	public List<OrganizationInfo> selectOrganizationCBgInfo(OrganizationInfo organizationInfo) {
		return organizatinInfoMapper.selectOrganizationCBgInfo(organizationInfo);
	}
	
	@Override
	public List<OrganizationInfo> selectOrganizationCMgInfo(OrganizationInfo organizationInfo) {
		return organizatinInfoMapper.selectOrganizationCMgInfo(organizationInfo);
	}
	
	@Override
	public List<OrganizationInfo> selectOrganizationCSgInfo(OrganizationInfo organizationInfo) {
		return organizatinInfoMapper.selectOrganizationCSgInfo(organizationInfo);
	}
}
