package com.furence.recsee.common.util;

import java.util.List;

import com.furence.recsee.common.model.OrganizationInfo;
import com.furence.recsee.common.model.SystemInfo;

public class FindOrganizationUtil {
	
	public String getSysCode(String sysCodeName, List<SystemInfo> systemInfoResult) {
		String result = "";
		
		Integer organizationInfoTotal = systemInfoResult.size();
		
		if(organizationInfoTotal > 0) {
			for(int i = 0; i < organizationInfoTotal; i++) {
				SystemInfo rowItem = systemInfoResult.get(i);
				
				if(sysCodeName != null && rowItem.getSysName() != null
				&& sysCodeName.equals(rowItem.getSysName())) {
					return rowItem.getSysId();
				}
			}
		}
		
		return result; 
	}
	public String getSysFlag(String sysCodeName, List<SystemInfo> systemInfoResult) {
		String result = "";
		
		Integer organizationInfoTotal = systemInfoResult.size();
		
		if(organizationInfoTotal > 0) {
			for(int i = 0; i < organizationInfoTotal; i++) {
				SystemInfo rowItem = systemInfoResult.get(i);
				
				if(sysCodeName != null && rowItem.getSysName() != null
				&& sysCodeName.equals(rowItem.getSysName())) {
					return rowItem.getChannelUpdateFlag();
				}
			}
		}
		
		return result; 
	}
	
	public String getSysCodeName(String sysCode, List<SystemInfo> systemInfoResult) {
		String result = "";
		
		Integer organizationInfoTotal = systemInfoResult.size();
		
		if(organizationInfoTotal > 0) {
			for(int i = 0; i < organizationInfoTotal; i++) {
				SystemInfo rowItem = systemInfoResult.get(i);
				
				if(sysCode != null && rowItem.getSysId() != null
				&& sysCode.equals(rowItem.getSysId())) {
					return rowItem.getSysName();
				}
			}
		}
		
		return result; 
	}
	public String getOrganizationName(String bgCode, List<OrganizationInfo> organizationInfo) {
		String result = "";
		
		Integer organizationInfoTotal = organizationInfo.size();
		
		if(organizationInfoTotal > 0) {
			for(int i = 0; i < organizationInfoTotal; i++) {
				OrganizationInfo rowItem = organizationInfo.get(i);
				
				if(bgCode != null && rowItem.getrBgCode() != null
				&& bgCode.equals(rowItem.getrBgCode())) {
					return rowItem.getrBgName();
				}
			}
		}
		
		return result; 
	}
	public String getOrganizationMName(String mgCode, List<OrganizationInfo> organizationInfo) {
		String result = "";
		
		Integer organizationInfoTotal = organizationInfo.size();
		
		if(organizationInfoTotal > 0) {
			for(int i = 0; i < organizationInfoTotal; i++) {
				OrganizationInfo rowItem = organizationInfo.get(i);
				
				if(mgCode != null && rowItem.getrMgCode() != null
				&& mgCode.equals(rowItem.getrMgCode())) {
					return rowItem.getrMgName();
				}
			}
		}
		
		return result; 
	}
	public String getOrganizationSName(String sgCode, List<OrganizationInfo> organizationInfo) {
		String result = "";
		
		Integer organizationInfoTotal = organizationInfo.size();
		
		if(organizationInfoTotal > 0) {
			for(int i = 0; i < organizationInfoTotal; i++) {
				OrganizationInfo rowItem = organizationInfo.get(i);
				
				if(sgCode != null && rowItem.getrSgCode() != null
				&& sgCode.equals(rowItem.getrSgCode())) {
					return rowItem.getrSgName();
				}
			}
		}
		
		return result; 
	}
	public String getOrganizationName(String bgCode, String mgCode, List<OrganizationInfo> organizationInfo) {
		String result = "";
		
		Integer organizationInfoTotal = organizationInfo.size();
		
		if(organizationInfoTotal > 0) {
			for(int i = 0; i < organizationInfoTotal; i++) {
				OrganizationInfo rowItem = organizationInfo.get(i);
				
				if(bgCode != null && rowItem.getrBgCode() != null
				&& bgCode.equals(rowItem.getrBgCode()) && mgCode == null) {
					return rowItem.getrBgName();
				} else if(bgCode != null && rowItem.getrBgCode() != null
				&& mgCode != null && rowItem.getrMgCode() != null
				&& bgCode.equals(rowItem.getrBgCode()) && mgCode.equals(rowItem.getrMgCode())) {
					return rowItem.getrMgName();
				}
			}
		}
		
		return result; 
	}
	public String getOrganizationName(String bgCode, String mgCode, String sgCode, List<OrganizationInfo> organizationInfo) {
		String result = "";
		
		Integer organizationInfoTotal = organizationInfo.size();
		
		if(organizationInfoTotal > 0) {
			for(int i = 0; i < organizationInfoTotal; i++) {
				OrganizationInfo rowItem = organizationInfo.get(i);
				
				if(bgCode != null && rowItem.getrBgCode() != null
				&& bgCode.equals(rowItem.getrBgCode()) && mgCode == null) {
					return rowItem.getrBgName();
				} else if(bgCode != null && rowItem.getrBgCode() != null
				&& mgCode != null && rowItem.getrMgCode() != null
				&& bgCode.equals(rowItem.getrBgCode()) && mgCode.equals(rowItem.getrMgCode()) && sgCode == null) {
					return rowItem.getrMgName();
				} else if(bgCode != null && rowItem.getrBgCode() != null
				&& mgCode != null && rowItem.getrMgCode() != null
				&& sgCode != null && rowItem.getrSgCode() != null
				&& bgCode.equals(rowItem.getrBgCode()) && mgCode.equals(rowItem.getrMgCode()) && sgCode.equals(rowItem.getrSgCode())) {
					return rowItem.getrSgName();
				}
			}
		}
		
		return result; 
	}
}
