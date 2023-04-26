package com.furence.recsee.common.dao.impl;

import java.util.List;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.furence.recsee.common.dao.ChannInfoDao;
import com.furence.recsee.common.model.ChannInfo;
import com.furence.recsee.common.service.EtcConfigInfoService;
import com.furence.recsee.common.service.SynchronizationService;
import com.furence.recsee.common.util.SynchronizationUtil;

@Repository("channInfoDao")
public class ChannInfoDaoImpl implements ChannInfoDao {

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
	public List<ChannInfo> selectChennelInfo(String sysCode){
		List<ChannInfo> result =  sqlSession.selectList("com.furence.recsee.common.mapper.channelInfoSqlMapper.selectChennelInfo",sysCode);
		return result;
	}

	@Override
	public Integer checkChInfo(ChannInfo channelInfo){
		Integer result =  sqlSession.selectOne("com.furence.recsee.common.mapper.channelInfoSqlMapper.checkChInfo",channelInfo);
		return result;
	}

	@Override
	public Integer updateChInfo(ChannInfo channelInfo){
		Integer result =  sqlSession.update("com.furence.recsee.common.mapper.channelInfoSqlMapper.updateChInfo",channelInfo);

		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updateChInfo").getBoundSql(channelInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return result;
	}

	@Override
	public Integer deleteChInfo(ChannInfo channelInfo){
		Integer result =  sqlSession.delete("com.furence.recsee.common.mapper.channelInfoSqlMapper.deleteChInfo",channelInfo);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("deleteChInfo").getBoundSql(channelInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return result;
	}

	@Override
	public Integer insertChInfo(ChannInfo channelInfo){
		Integer result =  sqlSession.insert("com.furence.recsee.common.mapper.channelInfoSqlMapper.insertChInfo",channelInfo);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("insertChInfo").getBoundSql(channelInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return result;
	}

	@Override
	public 	List<ChannInfo> selectSysCode(){
		List<ChannInfo> result=  sqlSession.selectList("com.furence.recsee.common.mapper.channelInfoSqlMapper.selectSysCode");
		return result;
	}

	@Override
	public Integer selectCompSysCode(String channelInfo){
		Integer result=  sqlSession.selectOne("com.furence.recsee.common.mapper.channelInfoSqlMapper.selectCompSysCode",channelInfo);
		return result;
	}

	@Override
	public List<ChannInfo> selectChSysCode(){
		List<ChannInfo> result=  sqlSession.selectList("com.furence.recsee.common.mapper.channelInfoSqlMapper.selectChSysCode");
		return result;
	}
}
