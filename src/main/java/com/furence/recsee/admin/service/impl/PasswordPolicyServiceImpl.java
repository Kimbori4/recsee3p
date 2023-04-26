package com.furence.recsee.admin.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.furence.recsee.admin.dao.PasswordPolicyDao;
import com.furence.recsee.admin.model.EnWord;
import com.furence.recsee.admin.model.PasswordPolicyInfo;
import com.furence.recsee.admin.service.PasswordPolicyService;

@Service("rPasswordPolicyService")
public class PasswordPolicyServiceImpl implements PasswordPolicyService {
	@Autowired
	PasswordPolicyDao rPasswordPolicyDao;

	@Override
	public List<PasswordPolicyInfo> selectRPasswordPolicyInfo(PasswordPolicyInfo rPasswordPolicyInfo){
		return rPasswordPolicyDao.selectRPasswordPolicyInfo(rPasswordPolicyInfo);
	}

	@Override
	public Integer updateRPasswordPolicyInfo(PasswordPolicyInfo rPasswordPolicyInfo){
		return rPasswordPolicyDao.updateRPasswordPolicyInfo(rPasswordPolicyInfo);
	}

	@Override
	public Integer selectEnWord (EnWord enWord){
		return rPasswordPolicyDao.selectEnWord(enWord);
	}
}
