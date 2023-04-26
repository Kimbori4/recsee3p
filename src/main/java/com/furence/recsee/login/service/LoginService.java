package com.furence.recsee.login.service;

import com.furence.recsee.common.model.LoginVO;

public interface LoginService {

	LoginVO selectUserInfo(LoginVO bean);
	LoginVO selectUser(LoginVO bean);
	Integer updateLastLoginDate(LoginVO bean);
	Integer updateTryCount(LoginVO bean);
	Integer updateUserLock(LoginVO bean);
	LoginVO selectAuserInfo(LoginVO bean);
	Integer updateLastLoginDateAuser(LoginVO bean);

}
