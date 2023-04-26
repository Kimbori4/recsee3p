package com.furence.recsee.common.service;

import java.util.List;

import com.furence.recsee.common.model.EtcConfigInfo;
import com.furence.recsee.common.model.TemplateKeyInfo;

public interface EtcConfigInfoService {
	List<EtcConfigInfo> selectEtcConfigInfo(EtcConfigInfo etcConfigInfo);
	List<EtcConfigInfo> selectEtcConfigInfoLike(EtcConfigInfo etcConfigInfo);
	
	List<EtcConfigInfo> selectEtcConfigKey(EtcConfigInfo etcConfigInfo);

	Integer insertEtcConfigInfo(EtcConfigInfo etcConfigInfo);

	Integer updateEtcConfigInfo(EtcConfigInfo etcConfigInfo);

	Integer setEtcConfigInfo(EtcConfigInfo etcConfigInfo);

	Integer deleteEtcConfigInfo(EtcConfigInfo etcConfigInfo);

	List<TemplateKeyInfo> selectTemplateKeyInfo(TemplateKeyInfo templateKeyInfo);

	String selectHideConference();

	String selectHideTransfer();

	/* 20200120 ��ٺ� ���� */
	Integer updateOptionValue(EtcConfigInfo etcConfigInfo);

	EtcConfigInfo selectOptionYN(EtcConfigInfo etcConfigInfo);

	EtcConfigInfo selectOptionInfo(EtcConfigInfo etcConfigInfo);

	Integer insertPrefixInfo(EtcConfigInfo etcConfigInfo);

	Integer updatePrefixInfo(EtcConfigInfo etcConfigInfo);

}
