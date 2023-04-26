package com.furence.recsee.common.dao.impl;

import java.util.List;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.furence.recsee.common.dao.CustomerInfoDao;
import com.furence.recsee.common.model.CodeInfoVO;
import com.furence.recsee.common.model.CustomerInfoVO;
import com.furence.recsee.common.model.MeritUser;
import com.furence.recsee.common.service.EtcConfigInfoService;
import com.furence.recsee.common.service.SynchronizationService;
import com.furence.recsee.common.util.SynchronizationUtil;

@Repository("customerInfoDao")
public class CustomerInfoDaoImpl implements CustomerInfoDao {

	@Autowired
	private SqlSession sqlSession;
		
	@Autowired
	private EtcConfigInfoService etcConfigInfoService;
	
	@Autowired
	private SynchronizationService synchronizationService;
	
	public void setSqlSessionTemplate(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}

	@Override
	public List<CodeInfoVO> selectsBgCodeList(){
		//List<CustomerInfoVO> result =  sqlSession.selects("com.furence.nts.mapper.oraGetDb.firstHourCallList",nd);
		List<CodeInfoVO> result =  sqlSession.selectList("com.furence.recsee.common.mapper.CustomerInfoSqlMapper.selectsBgCodeList");
		return result;
	}

/*	@Override
	public List<CodeInfoVO> MgCodeList(){
		//List<CustomerInfoVO> result =  sqlSession.selects("com.furence.nts.mapper.oraGetDb.firstHourCallList",nd);
		PgCustomerInfoGetDbDao pgCustomerInfoMapper = (PgCustomerInfoGetDbDao)sqlSession.getMapper(PgCustomerInfoGetDbDao.class);
		return pgCustomerInfoMapper.MgCodeList();
	//	selectsMgCodeList
		List<CodeInfoVO> result =  sqlSession.selectList("com.furence.recsee.common.mapper.customerInfoSqlMapper.selectsMgCodeList");
		return result;
		List<CodeInfoVO> result =  sqlSession.selectList("com.furence.recsee.common.mapper.customerInfoSqlMapper.selectsMgCodeList");
		return result;

		//CustomerInfoDao pgCustomerInfoMapper = sqlSession.getMapper(CodeInfoVO.class);
		//return pgCustomerInfoMapper.MgCodeList();
	}*/

	@Override
	public List<CodeInfoVO> selectsMgCodeList(){

		List<CodeInfoVO> result =  sqlSession.selectList("com.furence.recsee.common.mapper.customerInfoSqlMapper.selectsMgCodeList");
		return result;
	}

	@Override
	public List<CodeInfoVO> selectsSgCodeList(){

		List<CodeInfoVO> result =  sqlSession.selectList("com.furence.recsee.common.mapper.customerInfoSqlMapper.selectsSgCodeList");
		return result;
	}
	@Override
	public List<CustomerInfoVO> selectsCustomerList(){

		List<CustomerInfoVO> result =  sqlSession.selectList("com.furence.recsee.common.mapper.customerInfoSqlMapper.selectsCustomerList");
		return result;
	}
	@Override
	public Integer insertMgInfo(CodeInfoVO codeInfoVO){

		Integer result = sqlSession.insert("com.furence.recsee.common.mapper.customerInfoSqlMapper.insertMgInfo");
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("insertMgInfo").getBoundSql(codeInfoVO);
		synchronizationUtil.SynchronizationInsert(query);
		
		return result;
	}
	@Override
	public Integer insertSgInfo(CodeInfoVO codeInfoVO){

		Integer result = sqlSession.insert("com.furence.recsee.common.mapper.customerInfoSqlMapper.insertSgInfo");
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("insertSgInfo").getBoundSql(codeInfoVO);
		synchronizationUtil.SynchronizationInsert(query);
		
		return result;
	}

	@Override
	public Integer insertCustomerInfo(CustomerInfoVO customerInfoVO){

		Integer result = sqlSession.insert("com.furence.recsee.common.mapper.customerInfoSqlMapper.insertCustomerInfo");
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("insertCustomerInfo").getBoundSql(customerInfoVO);
		synchronizationUtil.SynchronizationInsert(query);
		
		return result;
	}

	@Override
	public Integer updateMgCode(CodeInfoVO codeInfoVO){

		Integer result = sqlSession.update("com.furence.recsee.common.mapper.customerInfoSqlMapper.updateMgCode");
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updateMgCode").getBoundSql(codeInfoVO);
		synchronizationUtil.SynchronizationInsert(query);
		
		return result;
	}
	@Override
	public Integer updateSgCode(CodeInfoVO codeInfoVO){

		Integer result = sqlSession.update("com.furence.recsee.common.mapper.customerInfoSqlMapper.updateSgCode");
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updateSgCode").getBoundSql(codeInfoVO);
		synchronizationUtil.SynchronizationInsert(query);
		
		return result;
	}

	@Override
	public Integer updateCustomerInfo(CustomerInfoVO customerInfoVO){

		Integer result = sqlSession.update("com.furence.recsee.common.mapper.customerInfoSqlMapper.updateCustomerInfo");
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updateCustomerInfo").getBoundSql(customerInfoVO);
		synchronizationUtil.SynchronizationInsert(query);
		
		return result;
	}

	@Override
	public Integer checkMgCode(CodeInfoVO codeInfoVO){

		Integer result = sqlSession.selectOne("com.furence.recsee.common.mapper.customerInfoSqlMapper.checkMgCode");
		return result;
	}
	@Override
	public Integer checkSgCode(CodeInfoVO codeInfoVO){

		Integer result = sqlSession.selectOne("com.furence.recsee.common.mapper.customerInfoSqlMapper.checkSgCode");
		return result;
	}

	@Override
	public Integer checkCustomerInfo(CustomerInfoVO customerInfoVO){

		Integer result = sqlSession.selectOne("com.furence.recsee.common.mapper.customerInfoSqlMapper.checkCustomerInfo");
		return result;
	}

	@Override
	public Integer deleteMgCode(CodeInfoVO codeInfoVO){

		Integer result = sqlSession.delete("com.furence.recsee.common.mapper.customerInfoSqlMapper.deleteMgCode");
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("deleteMgCode").getBoundSql(codeInfoVO);
		synchronizationUtil.SynchronizationInsert(query);
		
		return result;
	}
	@Override
	public Integer deleteSgCode(CodeInfoVO codeInfoVO){

		Integer result = sqlSession.delete("com.furence.recsee.common.mapper.customerInfoSqlMapper.deleteSgCode");
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("deleteSgCode").getBoundSql(codeInfoVO);
		synchronizationUtil.SynchronizationInsert(query);
		
		return result;
	}

	@Override
	public Integer deleteCustomerInfo(CustomerInfoVO customerInfoVO){

		Integer result = sqlSession.delete("com.furence.recsee.common.mapper.customerInfoSqlMapper.deleteCustomerInfo");
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("deleteCustomerInfo").getBoundSql(customerInfoVO);
		synchronizationUtil.SynchronizationInsert(query);
		
		return result;
	}

	@Override
	public Integer insertBgInfo(CodeInfoVO codeinfo) {
		Integer result = sqlSession.insert("com.furence.recsee.common.mapper.customerInfoSqlMapper.insertBgInfo");
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("insertBgInfo").getBoundSql(codeinfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return result;
	}
	
	@Override
	public Integer upsertMgInfo(CodeInfoVO codeinfo) {
		Integer result = sqlSession.insert("com.furence.recsee.common.mapper.customerInfoSqlMapper.upsertMgInfo");
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("upsertMgInfo").getBoundSql(codeinfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return result;
	}
	
	@Override
	public Integer deleteMgInfo(CodeInfoVO codeinfo) {
		Integer result = sqlSession.delete("com.furence.recsee.common.mapper.customerInfoSqlMapper.deleteMgInfo");
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("deleteMgInfo").getBoundSql(codeinfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return result;
	}
	
	@Override
	public Integer upsertSgInfo(CodeInfoVO codeinfo) {
		Integer result = sqlSession.insert("com.furence.recsee.common.mapper.customerInfoSqlMapper.upsertSgInfo");
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("upsertSgInfo").getBoundSql(codeinfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return result;
	}
	
	@Override
	public Integer deleteSgInfo(CodeInfoVO codeinfo) {
		Integer result = sqlSession.delete("com.furence.recsee.common.mapper.customerInfoSqlMapper.deleteSgInfo");
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("deleteSgInfo").getBoundSql(codeinfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return result;
	}
	
	@Override
	public List<MeritUser> meritzUserMapping(MeritUser meritUser) {
		CustomerInfoDao  customerInfoDao = sqlSession.getMapper(CustomerInfoDao.class);
		return customerInfoDao.meritzUserMapping(meritUser);
	}
	
	@Override
	public Integer meritzUserInsert(MeritUser meritUser) {
		CustomerInfoDao  customerInfoDao = sqlSession.getMapper(CustomerInfoDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("meritzUserInsert").getBoundSql(meritUser);
		synchronizationUtil.SynchronizationInsert(query);
		
		return customerInfoDao.meritzUserInsert(meritUser);
	}
	
	@Override
	public Integer meritzUserDelete(MeritUser meritUser) {
		CustomerInfoDao  customerInfoDao = sqlSession.getMapper(CustomerInfoDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("meritzUserDelete").getBoundSql(meritUser);
		synchronizationUtil.SynchronizationInsert(query);
		
		return customerInfoDao.meritzUserDelete(meritUser);
	}
	
	@Override
	public Integer meritzRUserInsert() {
		CustomerInfoDao  customerInfoDao = sqlSession.getMapper(CustomerInfoDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("meritzRUserInsert").getBoundSql(new Object());
		synchronizationUtil.SynchronizationInsert(query);
		
		return customerInfoDao.meritzRUserInsert();
	}

}
