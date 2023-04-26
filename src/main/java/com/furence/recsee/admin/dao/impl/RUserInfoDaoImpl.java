package com.furence.recsee.admin.dao.impl;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.furence.recsee.admin.dao.RUserInfoDao;
import com.furence.recsee.admin.model.MultiPartInfo;
import com.furence.recsee.admin.model.RUserInfo;
import com.furence.recsee.common.service.EtcConfigInfoService;
import com.furence.recsee.common.service.SynchronizationService;
import com.furence.recsee.common.util.SynchronizationUtil;

@Repository("RUserInfoDao")
public class RUserInfoDaoImpl implements RUserInfoDao {
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
	public List<RUserInfo> selectRUserInfo(RUserInfo ruserInfo) {
		RUserInfoDao ruserInfoMapper = sqlSession.getMapper(RUserInfoDao.class);

		return ruserInfoMapper.selectRUserInfo(ruserInfo);
	}
	
	@Override
	public List<RUserInfo> selectTreeViewRUserInfo(RUserInfo ruserInfo) {
		RUserInfoDao ruserInfoMapper = sqlSession.getMapper(RUserInfoDao.class);

		return ruserInfoMapper.selectTreeViewRUserInfo(ruserInfo);
	}

	@Override
	public Integer insertRUserInfo(RUserInfo ruserInfo) {
		RUserInfoDao ruserInfoMapper = sqlSession.getMapper(RUserInfoDao.class);

		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("insertRUserInfo").getBoundSql(ruserInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return ruserInfoMapper.insertRUserInfo(ruserInfo);
	}

	@Override
	public Integer updateRUserInfo(RUserInfo ruserInfo) {
		RUserInfoDao ruserInfoMapper = sqlSession.getMapper(RUserInfoDao.class);

		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updateRUserInfo").getBoundSql(ruserInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return ruserInfoMapper.updateRUserInfo(ruserInfo);
	}

	@Override
	public Integer deleteRUserInfo(RUserInfo ruserInfo) {
		RUserInfoDao ruserInfoMapper = sqlSession.getMapper(RUserInfoDao.class);

		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("deleteRUserInfo").getBoundSql(ruserInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return ruserInfoMapper.deleteRUserInfo(ruserInfo);
	}

	@Override
	public Integer userLevelEmpty(RUserInfo ruserInfo) {
		RUserInfoDao ruserInfoMapper = sqlSession.getMapper(RUserInfoDao.class);

		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("userLevelEmpty").getBoundSql(ruserInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return ruserInfoMapper.userLevelEmpty(ruserInfo);
	}

	@Override
	public List<MultiPartInfo> selectMultiPartInfo(MultiPartInfo multiPartInfo) {
		RUserInfoDao ruserInfoMapper = sqlSession.getMapper(RUserInfoDao.class);

		return ruserInfoMapper.selectMultiPartInfo(multiPartInfo);
	}

	@Override
	public List<HashMap<String, String>> countMultiPartInfo(MultiPartInfo multiPartInfo) {
		RUserInfoDao ruserInfoMapper = sqlSession.getMapper(RUserInfoDao.class);

		return ruserInfoMapper.countMultiPartInfo(multiPartInfo);
	}

	@Override
	public Integer insertMultiPartInfo(MultiPartInfo multiPartInfo) {
		RUserInfoDao ruserInfoMapper = sqlSession.getMapper(RUserInfoDao.class);

		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("insertMultiPartInfo").getBoundSql(multiPartInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return ruserInfoMapper.insertMultiPartInfo(multiPartInfo);
	}

	@Override
	public Integer deleteMultiPartInfo(MultiPartInfo multiPartInfo) {
		RUserInfoDao ruserInfoMapper = sqlSession.getMapper(RUserInfoDao.class);

		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("deleteMultiPartInfo").getBoundSql(multiPartInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return ruserInfoMapper.deleteMultiPartInfo(multiPartInfo);
	}

	@Override
	public List<RUserInfo> selectPeople(RUserInfo ruserInfo) {
		RUserInfoDao ruserInfoMapper = sqlSession.getMapper(RUserInfoDao.class);

		return ruserInfoMapper.selectPeople(ruserInfo);
	}

	@Override
	public List<RUserInfo> adminUserManageSelect(RUserInfo ruserInfo) {
		RUserInfoDao ruserInfoMapper = sqlSession.getMapper(RUserInfoDao.class);

		return ruserInfoMapper.adminUserManageSelect(ruserInfo);
	}

	@Override
	public Integer CountadminUserManageSelect(RUserInfo ruserInfo) {
		RUserInfoDao ruserInfoMapper = sqlSession.getMapper(RUserInfoDao.class);

		return ruserInfoMapper.CountadminUserManageSelect(ruserInfo);
	}

	@Override
	public Integer CountadminAUserManageSelect(RUserInfo ruserInfo) {
		RUserInfoDao ruserInfoMapper = sqlSession.getMapper(RUserInfoDao.class);

		return ruserInfoMapper.CountadminAUserManageSelect(ruserInfo);
	}

	@Override
	public List<RUserInfo> adminAUserManageSelect(RUserInfo ruserInfo) {
		RUserInfoDao ruserInfoMapper = sqlSession.getMapper(RUserInfoDao.class);

		return ruserInfoMapper.adminAUserManageSelect(ruserInfo);
	}

	@Override
	public Integer checkId(RUserInfo ruserInfo) {
		RUserInfoDao ruserInfoMapper = sqlSession.getMapper(RUserInfoDao.class);

		return ruserInfoMapper.checkId(ruserInfo);
	}

	@Override
	public List<RUserInfo> adminAUserManageSelectTree(RUserInfo rUserInfo) {
		RUserInfoDao ruserInfoMapper = sqlSession.getMapper(RUserInfoDao.class);

		return ruserInfoMapper.adminAUserManageSelectTree(rUserInfo);
	}

	@Override
	public List<RUserInfo> adminUserManageSelectExcel(RUserInfo rUserInfo) {
		RUserInfoDao ruserInfoMapper = sqlSession.getMapper(RUserInfoDao.class);

		return ruserInfoMapper.adminUserManageSelectExcel(rUserInfo);
	}
	
	@Override
	public Integer multiGroupModify(RUserInfo ruserInfo) {
		RUserInfoDao ruserInfoMapper = sqlSession.getMapper(RUserInfoDao.class);

		return ruserInfoMapper.multiGroupModify(ruserInfo);
	}
	
	@Override
	public Integer deleteMgRUserInfo(RUserInfo ruserInfo) {
		RUserInfoDao ruserInfoMapper = sqlSession.getMapper(RUserInfoDao.class);

		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("deleteMgRUserInfo").getBoundSql(ruserInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return ruserInfoMapper.deleteMgRUserInfo(ruserInfo);
	}
	
	@Override
	public Integer upsertRUserInfo(RUserInfo ruserInfo) {
		RUserInfoDao ruserInfoMapper = sqlSession.getMapper(RUserInfoDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("upsertRUserInfo").getBoundSql(ruserInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return ruserInfoMapper.upsertRUserInfo(ruserInfo);
	}
	
	@Override
	public Integer checkPhone(String phoneNumber) {
		RUserInfoDao ruserInfoMapper = sqlSession.getMapper(RUserInfoDao.class);

		return ruserInfoMapper.checkPhone(phoneNumber);
	}
	
	@Override
	public Integer mobileUsage() {
		RUserInfoDao ruserInfoMapper = sqlSession.getMapper(RUserInfoDao.class);

		return ruserInfoMapper.mobileUsage();
	}

	@Override
	public Integer checkIP(RUserInfo ruserInfo) {
		RUserInfoDao ruserInfoMapper = sqlSession.getMapper(RUserInfoDao.class);

		return ruserInfoMapper.checkIP(ruserInfo);
	}

	@Override
	public Integer unlockUser(RUserInfo ruserInfo) {
		RUserInfoDao ruserInfoMapper = sqlSession.getMapper(RUserInfoDao.class);

		return ruserInfoMapper.unlockUser(ruserInfo);
	}
	
	@Override
	public List<RUserInfo> mobileManageSelect(RUserInfo rUserInfo) {
		RUserInfoDao ruserInfoMapper = sqlSession.getMapper(RUserInfoDao.class);
		
		return ruserInfoMapper.mobileManageSelect(rUserInfo);
	}

	@Override
	public List<RUserInfo> adminAUserManageRealTimeSelect(RUserInfo ruserInfo) {
		RUserInfoDao ruserInfoMapper = sqlSession.getMapper(RUserInfoDao.class);

		return ruserInfoMapper.adminAUserManageRealTimeSelect(ruserInfo);
	}
}
