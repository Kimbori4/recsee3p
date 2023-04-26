
package com.furence.recsee.common.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.furence.recsee.common.dao.CustomerInfoDao;
import com.furence.recsee.common.model.CodeInfoVO;
import com.furence.recsee.common.model.CustomerInfoVO;
import com.furence.recsee.common.model.MeritUser;
import com.furence.recsee.common.service.CustomerInfoService;


@Service("customerInfoService")
public class CustomerInfoServiceImpl implements CustomerInfoService {

	@Autowired
	CustomerInfoDao customerInfoMapper;

	public List<CustomerInfoVO> selectsCustomerList(){
		return customerInfoMapper.selectsCustomerList();
	}

	@Override
	public List<CodeInfoVO> selectsBgCodeList() {
		return customerInfoMapper.selectsBgCodeList();
	}

//	public List<CodeInfoVO> MgCodeList(){
//		return customerInfoMapper.MgCodeList();
//	}

	public List<CodeInfoVO> selectsMgCodeList(){
		return customerInfoMapper.selectsMgCodeList();
	}


	public List<CodeInfoVO> selectsSgCodeList(){
		return customerInfoMapper.selectsSgCodeList();
	}

	public Integer insertMgInfo(CodeInfoVO codeInfoVO){
		return customerInfoMapper.insertMgInfo(codeInfoVO);
	}

	public Integer insertSgInfo(CodeInfoVO codeInfoVO){
		return customerInfoMapper.insertSgInfo(codeInfoVO);
	}

	public Integer insertCustomerInfo(CustomerInfoVO customerInfoVO){
		return customerInfoMapper.insertCustomerInfo(customerInfoVO);
	}

	public Integer updateMgInfo(CodeInfoVO codeInfoVO){
		return customerInfoMapper.updateMgCode(codeInfoVO);
	}

	public Integer updateSgInfo(CodeInfoVO codeInfoVO){
		return customerInfoMapper.updateSgCode(codeInfoVO);
	}

	public Integer updateCustomerInfo(CustomerInfoVO customerInfoVO){
		return customerInfoMapper.updateCustomerInfo(customerInfoVO);
	}

	public Integer checkMgCode(CodeInfoVO codeInfoVO){
		return customerInfoMapper.checkMgCode(codeInfoVO);
	}

	public Integer checkSgCode(CodeInfoVO codeInfoVO){
		return customerInfoMapper.checkSgCode(codeInfoVO);
	}

	public Integer checkCustomerInfo(CustomerInfoVO customerInfoVO){
		return customerInfoMapper.checkCustomerInfo(customerInfoVO);
	}

	public Integer deleteMgCode(CodeInfoVO codeInfoVO){
		return customerInfoMapper.deleteMgCode(codeInfoVO);
	}

	public Integer deleteSgCode(CodeInfoVO codeInfoVO){
		return customerInfoMapper.deleteSgCode(codeInfoVO);
	}

	public Integer deleteCustomerInfo(CustomerInfoVO customerInfoVO){
		return customerInfoMapper.deleteCustomerInfo(customerInfoVO);
	}

	@Override
	public Integer insertBgInfo(CodeInfoVO codeinfo) {
		return customerInfoMapper.insertBgInfo(codeinfo);
	}
	
	@Override
	public Integer upsertMgInfo(CodeInfoVO codeinfo) {
		return customerInfoMapper.upsertMgInfo(codeinfo);
	}
	
	@Override
	public Integer deleteMgInfo(CodeInfoVO codeinfo) {
		return customerInfoMapper.deleteMgInfo(codeinfo);
	}
	
	@Override
	public Integer upsertSgInfo(CodeInfoVO codeinfo) {
		return customerInfoMapper.upsertSgInfo(codeinfo);
	}
	
	@Override
	public Integer deleteSgInfo(CodeInfoVO codeinfo) {
		return customerInfoMapper.deleteSgInfo(codeinfo);
	}
	
	@Override
	public List<MeritUser> meritzUserMapping(MeritUser meritUser) {
		return customerInfoMapper.meritzUserMapping(meritUser);
	}

	@Override
	public Integer meritzUserInsert(MeritUser meritUser) {
		return customerInfoMapper.meritzUserInsert(meritUser);
	}
	
	@Override
	public Integer meritzUserDelete(MeritUser meritUser) {
		return customerInfoMapper.meritzUserDelete(meritUser);
	}
	
	@Override
	public Integer meritzRUserInsert() {
		return customerInfoMapper.meritzRUserInsert();
	}
}