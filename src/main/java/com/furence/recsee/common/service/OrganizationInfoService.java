package com.furence.recsee.common.service;

import java.util.HashMap;
import java.util.List;

import com.furence.recsee.common.model.OrganizationInfo;

public interface OrganizationInfoService {
	List<OrganizationInfo> selectOrganizationBgInfo(OrganizationInfo organizationInfo);
	List<OrganizationInfo> selectOrganizationMgInfo(OrganizationInfo organizationInfo);
	List<OrganizationInfo> selectOrganizationSgInfo(OrganizationInfo organizationInfo);
	HashMap<String, String> selectOrganizationLastInfo(OrganizationInfo organizationInfo);
	Integer	insertOrganizationInfo(OrganizationInfo organizationInfo);
	Integer	updateOrganizationInfo(OrganizationInfo organizationInfo);
	Integer	deleteOrganizationInfo(OrganizationInfo organizationInfo);
	List<OrganizationInfo> sameNameOrganizationInfo(OrganizationInfo organizationInfo);
	Integer updateGroupCodeInfo(OrganizationInfo organizationInfo);
	List<OrganizationInfo> selectOraganiztionAll(OrganizationInfo organizationInfo);
	Integer updateUseBg(OrganizationInfo bgcode);
	List<OrganizationInfo> selectCallCenterMgInfo(OrganizationInfo organizationInfo);
	
	List<OrganizationInfo> selectOrganizationCBgInfo(OrganizationInfo organizationInfo);
	List<OrganizationInfo> selectOrganizationCMgInfo(OrganizationInfo organizationInfo);
	List<OrganizationInfo> selectOrganizationCSgInfo(OrganizationInfo organizationInfo);
	
}
