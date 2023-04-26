package com.furence.recsee.common.dao;

import java.util.List;

import com.furence.recsee.common.model.CodeInfoVO;
import com.furence.recsee.common.model.CustomerInfoVO;
import com.furence.recsee.common.model.MeritUser;


public interface CustomerInfoDao {

	List<CodeInfoVO> 		selectsBgCodeList();
	//List<CodeInfoVO>	 	MgCodeList();

	List<CodeInfoVO> 		selectsMgCodeList();
	List<CodeInfoVO> 		selectsSgCodeList();
	List<CustomerInfoVO>	selectsCustomerList();

	Integer					insertMgInfo(CodeInfoVO codeInfoVO);
	Integer					insertSgInfo(CodeInfoVO codeInfoVO);
	Integer					insertCustomerInfo(CustomerInfoVO customerInfoVO);

	Integer					updateMgCode(CodeInfoVO codeInfoVO);
	Integer					updateSgCode(CodeInfoVO codeInfoVO);
	Integer					updateCustomerInfo(CustomerInfoVO customerInfoVO);

	Integer					checkMgCode(CodeInfoVO codeInfoVO);
	Integer					checkSgCode(CodeInfoVO codeInfoVO);
	Integer					checkCustomerInfo(CustomerInfoVO customerInfoVO);

	Integer					deleteMgCode(CodeInfoVO codeInfoVO);
	Integer					deleteSgCode(CodeInfoVO codeInfoVO);
	Integer					deleteCustomerInfo(CustomerInfoVO customerInfoVO);

	Integer insertBgInfo(CodeInfoVO codeinfo);
	
	Integer upsertMgInfo(CodeInfoVO codeinfo);
	Integer deleteMgInfo(CodeInfoVO codeinfo);
	Integer upsertSgInfo(CodeInfoVO codeinfo);
	Integer deleteSgInfo(CodeInfoVO codeinfo);
	
	List<MeritUser> meritzUserMapping(MeritUser meritUser);

	Integer meritzUserInsert(MeritUser meritUser);
	Integer meritzUserDelete(MeritUser meritUser);
	Integer meritzRUserInsert();
}
