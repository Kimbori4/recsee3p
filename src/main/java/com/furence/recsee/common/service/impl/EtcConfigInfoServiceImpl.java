package com.furence.recsee.common.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.furence.recsee.common.dao.EtcConfigInfoDao;
import com.furence.recsee.common.model.EtcConfigInfo;
import com.furence.recsee.common.model.TemplateKeyInfo;
import com.furence.recsee.common.service.EtcConfigInfoService;

@Service("etcConfigInfoService")
public class EtcConfigInfoServiceImpl implements EtcConfigInfoService {
	@Autowired
	EtcConfigInfoDao etcConfigInfoMapper;

	@Override
	public List<EtcConfigInfo> selectEtcConfigInfo(EtcConfigInfo etcConfigInfo) {
		return etcConfigInfoMapper.selectEtcConfigInfo(etcConfigInfo);
	}
	@Override
	public List<EtcConfigInfo> selectEtcConfigInfoLike(EtcConfigInfo etcConfigInfo) {
		return etcConfigInfoMapper.selectEtcConfigInfoLike(etcConfigInfo);
	}
	@Override
	public List<EtcConfigInfo> selectEtcConfigKey(EtcConfigInfo etcConfigInfo) {
		return etcConfigInfoMapper.selectEtcConfigKey(etcConfigInfo);
	}
	
	
	@Override
	public Integer insertEtcConfigInfo(EtcConfigInfo etcConfigInfo) {
		return etcConfigInfoMapper.insertEtcConfigInfo(etcConfigInfo);
	}
	@Override
	public Integer updateEtcConfigInfo(EtcConfigInfo etcConfigInfo) {
		return etcConfigInfoMapper.updateEtcConfigInfo(etcConfigInfo);
	}
	
	@Override
	public Integer deleteEtcConfigInfo(EtcConfigInfo etcConfigInfo) {
		return etcConfigInfoMapper.deleteEtcConfigInfo(etcConfigInfo);
	}
	
	@Override
	public Integer setEtcConfigInfo(EtcConfigInfo etcConfigInfo) {

		List<EtcConfigInfo> exists = this.selectEtcConfigInfo(etcConfigInfo);
		
		if(exists.size() > 0) {
			return this.updateEtcConfigInfo(etcConfigInfo);
		} else {
			return this.insertEtcConfigInfo(etcConfigInfo);
		}
	}
	
	@Override
	public List<TemplateKeyInfo> selectTemplateKeyInfo(TemplateKeyInfo templateKeyInfo) {
		return etcConfigInfoMapper.selectTemplateKeyInfo(templateKeyInfo);
	}
	@Override
	public String selectHideConference() {
		return etcConfigInfoMapper.selectHideConference();
	}
	@Override
	public String selectHideTransfer() {
		return etcConfigInfoMapper.selectHideTransfer();
	}
	
	@Override
	public EtcConfigInfo selectOptionInfo(EtcConfigInfo etcConfigInfo) {
		return etcConfigInfoMapper.selectOptionInfo(etcConfigInfo);
	}
	@Override
	public Integer insertPrefixInfo(EtcConfigInfo etcConfigInfo) {
		return etcConfigInfoMapper.insertPrefixInfo(etcConfigInfo);
	}
	@Override
	public Integer updatePrefixInfo(EtcConfigInfo etcConfigInfo) {
		return etcConfigInfoMapper.updatePrefixInfo(etcConfigInfo);
	}
	@Override
	public Integer updateOptionValue(EtcConfigInfo etcConfigInfo) {
		return etcConfigInfoMapper.updateOptionValue(etcConfigInfo);
	}
	
	@Override
	public EtcConfigInfo selectOptionYN(EtcConfigInfo etcConfigInfo) {
		return etcConfigInfoMapper.selectOptionYN(etcConfigInfo);
	}
}
