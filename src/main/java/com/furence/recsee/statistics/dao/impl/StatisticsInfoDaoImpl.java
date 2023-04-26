package com.furence.recsee.statistics.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;

import com.furence.recsee.statistics.dao.StatisticsInfoDao;
import com.furence.recsee.statistics.model.StatisticsInfo;

public class StatisticsInfoDaoImpl implements StatisticsInfoDao {
	@Autowired
	private SqlSession sqlSession;

	public void setSqlSessionTemplate(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}

	@Override
	public List<StatisticsInfo> selectCallsStatistics(StatisticsInfo statisticsInfo) {
		StatisticsInfoDao statisticsInfoMapper = sqlSession.getMapper(StatisticsInfoDao.class);
		return statisticsInfoMapper.selectCallsStatistics(statisticsInfo);
	}
	
	@Override
	public List<StatisticsInfo> selectDayTimeUserStatistics(StatisticsInfo statisticsInfo){
		StatisticsInfoDao statisticsInfoMapper = sqlSession.getMapper(StatisticsInfoDao.class);
		return statisticsInfoMapper.selectDayTimeUserStatistics(statisticsInfo);
	}

	@Override
	public List<StatisticsInfo> dashboard7DayStatistics(StatisticsInfo statisticsInfo) {
		StatisticsInfoDao statisticsInfoMapper = sqlSession.getMapper(StatisticsInfoDao.class);
		return statisticsInfoMapper.dashboard7DayStatistics(statisticsInfo);
	}
	;
	@Override
	public List<StatisticsInfo> dashboardDaily(StatisticsInfo statisticsInfo) {
		StatisticsInfoDao statisticsInfoMapper = sqlSession.getMapper(StatisticsInfoDao.class);
		return statisticsInfoMapper.dashboardDaily(statisticsInfo);
	}

	@Override
	public List<StatisticsInfo> dashboardSystem(StatisticsInfo statisticsInfo) {
		StatisticsInfoDao statisticsInfoMapper = sqlSession.getMapper(StatisticsInfoDao.class);
		return statisticsInfoMapper.dashboardSystem(statisticsInfo);
	}

	@Override
	public List<StatisticsInfo> selectUserStatistics(StatisticsInfo statisticsInfo) {
		StatisticsInfoDao statisticsInfoMapper = sqlSession.getMapper(StatisticsInfoDao.class);
		return statisticsInfoMapper.selectUserStatistics(statisticsInfo);
	}

	@Override
	public StatisticsInfo callSummary(StatisticsInfo statisticsInfo) {
		StatisticsInfoDao statisticsInfoMapper = sqlSession.getMapper(StatisticsInfoDao.class);
		return statisticsInfoMapper.callSummary(statisticsInfo);
	}

	@Override
	public StatisticsInfo callCountbyType(StatisticsInfo statisticsInfo) {
		StatisticsInfoDao statisticsInfoMapper = sqlSession.getMapper(StatisticsInfoDao.class);
		return statisticsInfoMapper.callCountbyType(statisticsInfo);
	}

	@Override
	public StatisticsInfo callCountbyTime(StatisticsInfo statisticsInfo) {
		StatisticsInfoDao statisticsInfoMapper = sqlSession.getMapper(StatisticsInfoDao.class);
		return statisticsInfoMapper.callCountbyTime(statisticsInfo);
	}

	@Override
	public List<StatisticsInfo> callCountbyDay(StatisticsInfo statisticsInfo) {
		StatisticsInfoDao statisticsInfoMapper = sqlSession.getMapper(StatisticsInfoDao.class);
		return statisticsInfoMapper.callCountbyDay(statisticsInfo);
	}

	@Override
	public List<StatisticsInfo> callCountByCallTimeUser(StatisticsInfo statisticsInfo) {
		StatisticsInfoDao statisticsInfoMapper = sqlSession.getMapper(StatisticsInfoDao.class);
		return statisticsInfoMapper.callCountByCallTimeUser(statisticsInfo);
	}

	@Override
	public List<StatisticsInfo> temp() {
		StatisticsInfoDao statisticsInfoMapper = sqlSession.getMapper(StatisticsInfoDao.class);
		return statisticsInfoMapper.temp();
	}

	@Override
	public Integer temp2(StatisticsInfo a) {
		StatisticsInfoDao statisticsInfoMapper = sqlSession.getMapper(StatisticsInfoDao.class);
		return statisticsInfoMapper.temp2(a);
	}

	@Override
	public Integer totalCallCountByCallTimeUser(StatisticsInfo statisticsInfo) {
		StatisticsInfoDao statisticsInfoMapper = sqlSession.getMapper(StatisticsInfoDao.class);
		return statisticsInfoMapper.totalCallCountByCallTimeUser(statisticsInfo);
	}

	@Override
	public Integer totalSelectUserStatistics(StatisticsInfo statisticsInfo) {
		StatisticsInfoDao statisticsInfoMapper = sqlSession.getMapper(StatisticsInfoDao.class);
		return statisticsInfoMapper.totalSelectUserStatistics(statisticsInfo);
	}

	@Override
	public List<StatisticsInfo> selectUserTotalStatistics(StatisticsInfo statisticsItem) {
		StatisticsInfoDao statisticsInfoMapper = sqlSession.getMapper(StatisticsInfoDao.class);
		return statisticsInfoMapper.selectUserTotalStatistics(statisticsItem);
	}
	
	//모바일 녹취
	@Override
	public List<StatisticsInfo> selectCallsMobileStatistics(StatisticsInfo statisticsItem) {
		StatisticsInfoDao statisticsInfoMapper = sqlSession.getMapper(StatisticsInfoDao.class);
		return statisticsInfoMapper.selectCallsMobileStatistics(statisticsItem);
	}
	
	@Override
	public List<StatisticsInfo> selectUsersCallsMobileStatistics(StatisticsInfo statisticsItem) {
		StatisticsInfoDao statisticsInfoMapper = sqlSession.getMapper(StatisticsInfoDao.class);
		return statisticsInfoMapper.selectUsersCallsMobileStatistics(statisticsItem);
	}
	
	@Override
	public List<StatisticsInfo> selectDetailsCallsMobileStatistics(StatisticsInfo statisticsItem) {
		StatisticsInfoDao statisticsInfoMapper = sqlSession.getMapper(StatisticsInfoDao.class);
		return statisticsInfoMapper.selectDetailsCallsMobileStatistics(statisticsItem);
	}
	
	@Override
	public List<StatisticsInfo>  totalUsersCallsMobileStatistics(StatisticsInfo statisticsItem) {
		StatisticsInfoDao statisticsInfoMapper = sqlSession.getMapper(StatisticsInfoDao.class);
		return statisticsInfoMapper.totalUsersCallsMobileStatistics(statisticsItem);
	}
	
	@Override
	public Integer totalDetailsCallsMobileStatistics(StatisticsInfo statisticsItem) {
		StatisticsInfoDao statisticsInfoMapper = sqlSession.getMapper(StatisticsInfoDao.class);
		return statisticsInfoMapper.totalDetailsCallsMobileStatistics(statisticsItem);
	}

	//키워드
	@Override
	public List<StatisticsInfo> keywordRank(StatisticsInfo statisticsInfo){
		StatisticsInfoDao statisticsInfoMapper = sqlSession.getMapper(StatisticsInfoDao.class);
		return statisticsInfoMapper.keywordRank(statisticsInfo);
	}
	@Override
	public List<StatisticsInfo> selectKeyword(StatisticsInfo statisticsInfo){
		StatisticsInfoDao statisticsInfoMapper = sqlSession.getMapper(StatisticsInfoDao.class);
		return statisticsInfoMapper.selectKeyword(statisticsInfo);
	}
	@Override
	public List<StatisticsInfo> selectCategory(StatisticsInfo statisticsInfo){
		StatisticsInfoDao statisticsInfoMapper = sqlSession.getMapper(StatisticsInfoDao.class);
		return statisticsInfoMapper.selectCategory(statisticsInfo);
	}
}
