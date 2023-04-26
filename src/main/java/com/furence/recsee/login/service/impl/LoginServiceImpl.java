package com.furence.recsee.login.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.furence.recsee.common.model.LoginVO;
import com.furence.recsee.login.dao.LoginDao;
import com.furence.recsee.login.service.LoginService;


@Service("loginService")
public class LoginServiceImpl implements LoginService {


	@Autowired
	LoginDao loginDao;

	@Override
	public LoginVO selectUserInfo(LoginVO bean) {

		return loginDao.selectUserInfo(bean);
	}
	
	@Override
	public LoginVO selectUser(LoginVO bean) {
		return loginDao.selectUser(bean);
	}
	
	@Override
	public Integer updateLastLoginDate(LoginVO bean) {
		return loginDao.updateLastLoginDate(bean);
	}
	
	@Override
	public Integer updateTryCount(LoginVO bean) {
		return loginDao.updateTryCount(bean);
	}
	
	@Override
	public Integer updateUserLock(LoginVO bean) {
		return loginDao.updateUserLock(bean);
	}

	@Override
	public LoginVO selectAuserInfo(LoginVO bean) {
		return loginDao.selectAuserInfo(bean);
	}

	@Override
	public Integer updateLastLoginDateAuser(LoginVO bean) {
		return loginDao.updateLastLoginDateAuser(bean);
	}
}
