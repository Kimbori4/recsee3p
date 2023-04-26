package com.furence.recsee.admin.service;

import java.util.List;

import com.furence.recsee.admin.model.EnWord;
import com.furence.recsee.admin.model.PasswordPolicyInfo;

public interface PasswordPolicyService {
	List<PasswordPolicyInfo> selectRPasswordPolicyInfo(PasswordPolicyInfo rPasswordPolicyInfo);
	Integer updateRPasswordPolicyInfo(PasswordPolicyInfo rPasswordPolicyInfo);

	Integer selectEnWord (EnWord enWord);
}
