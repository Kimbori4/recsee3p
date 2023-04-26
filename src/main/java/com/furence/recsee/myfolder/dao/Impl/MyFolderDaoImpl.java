package com.furence.recsee.myfolder.dao.Impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.furence.recsee.common.service.EtcConfigInfoService;
import com.furence.recsee.common.service.SynchronizationService;
import com.furence.recsee.common.util.SynchronizationUtil;
import com.furence.recsee.myfolder.dao.MyFolderDao;
import com.furence.recsee.myfolder.model.MyFolderInfo;
import com.furence.recsee.myfolder.model.MyFolderListinfo;

@Repository("myFolderDao")
public class MyFolderDaoImpl implements MyFolderDao{
	
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
	public List<MyFolderInfo> selectMyFolderInfo(MyFolderInfo myFolderInfo){
		MyFolderDao myFolderInfoMapper = sqlSession.getMapper(MyFolderDao.class);
		
		return myFolderInfoMapper.selectMyFolderInfo(myFolderInfo);
	}

	@Override
	public Integer insertMyFolderItem(MyFolderListinfo myFolderListInfo) {
		MyFolderDao myFolderInfoMapper = sqlSession.getMapper(MyFolderDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("insertMyFolderItem").getSqlSource().getBoundSql(myFolderListInfo);
		synchronizationUtil.SynchronizationInsert(query);		
		
		return myFolderInfoMapper.insertMyFolderItem(myFolderListInfo);
	}

	@Override
	public List<MyFolderListinfo> selectMyfolderListInfo(MyFolderListinfo myfolderListinfo) {
		MyFolderDao myFolderInfoMapper = sqlSession.getMapper(MyFolderDao.class);
		
		return myFolderInfoMapper.selectMyfolderListInfo(myfolderListinfo);
	}

	@Override
	public Integer deleteMyFolder(MyFolderInfo myFolderInfo) {
		MyFolderDao myFolderInfoMapper = sqlSession.getMapper(MyFolderDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("deleteMyFolder").getSqlSource().getBoundSql(myFolderInfo);
		synchronizationUtil.SynchronizationInsert(query);		
		
		return myFolderInfoMapper.deleteMyFolder(myFolderInfo);
	}

	@Override
	public Integer createMyFolder(MyFolderInfo myFolderInfo) {
		MyFolderDao myFolderInfoMapper = sqlSession.getMapper(MyFolderDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		//BoundSql query = sqlSession.getConfiguration().getMappedStatement("createMyFolder").getSqlSource().getBoundSql(myFolderInfo);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("createMyFolder").getSqlSource().getBoundSql(myFolderInfo);
				
		synchronizationUtil.SynchronizationInsert(query);
		
		return myFolderInfoMapper.createMyFolder(myFolderInfo);
	}

	@Override
	public Integer modifyMyFolder(MyFolderInfo myFolderInfo) {
		MyFolderDao myFolderInfoMapper = sqlSession.getMapper(MyFolderDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("modifyMyFolder").getSqlSource().getBoundSql(myFolderInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return myFolderInfoMapper.modifyMyFolder(myFolderInfo);
	}

	@Override
	public Integer deleteMyFolderItem(MyFolderListinfo myFolderListInfo) {
		MyFolderDao myFolderInfoMapper = sqlSession.getMapper(MyFolderDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("deleteMyFolderItem").getSqlSource().getBoundSql(myFolderListInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return myFolderInfoMapper.deleteMyFolderItem(myFolderListInfo);
	}

	@Override
	public Integer moveMyFolderItem(MyFolderListinfo myFolderListInfo) {
		MyFolderDao myFolderInfoMapper = sqlSession.getMapper(MyFolderDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("moveMyFolderItem").getSqlSource().getBoundSql(myFolderListInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return myFolderInfoMapper.moveMyFolderItem(myFolderListInfo);
	}
}
