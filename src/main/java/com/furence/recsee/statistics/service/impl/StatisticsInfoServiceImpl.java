package com.furence.recsee.statistics.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.furence.recsee.statistics.dao.StatisticsInfoDao;
import com.furence.recsee.statistics.model.StatisticsInfo;
import com.furence.recsee.statistics.service.StatisticsInfoService;

@Service("statisticsInfoService")
public class StatisticsInfoServiceImpl implements StatisticsInfoService {
	@Autowired
	StatisticsInfoDao statisticsInfoMapper;

	@Override
	public List<StatisticsInfo> selectCallsStatistics(StatisticsInfo statisticsInfo) {
		return statisticsInfoMapper.selectCallsStatistics(statisticsInfo);
	}
	
	@Override
	public List<StatisticsInfo> selectDayTimeUserStatistics(StatisticsInfo statisticsInfo){
		return statisticsInfoMapper.selectDayTimeUserStatistics(statisticsInfo);
	}

	@Override
	public List<StatisticsInfo> dashboard7DayStatistics(StatisticsInfo statisticsInfo) {
		return statisticsInfoMapper.dashboard7DayStatistics(statisticsInfo);
	}

	@Override
	public List<StatisticsInfo> dashboardDaily(StatisticsInfo statisticsInfo) {
		return statisticsInfoMapper.dashboardDaily(statisticsInfo);
	}

	@Override
	public List<StatisticsInfo> dashboardSystem(StatisticsInfo statisticsInfo) {
		return statisticsInfoMapper.dashboardSystem(statisticsInfo);
	}

	@Override
	public List<StatisticsInfo> selectUserStatistics(StatisticsInfo statisticsInfo) {
		return statisticsInfoMapper.selectUserStatistics(statisticsInfo);
	}

	@Override
	public StatisticsInfo callSummary(StatisticsInfo statisticsInfo) {
		return statisticsInfoMapper.callSummary(statisticsInfo);
	}

	@Override
	public StatisticsInfo callCountbyType(StatisticsInfo statisticsInfo) {
		return statisticsInfoMapper.callCountbyType(statisticsInfo);
	}

	@Override
	public StatisticsInfo callCountbyTime(StatisticsInfo statisticsInfo) {
		return statisticsInfoMapper.callCountbyTime(statisticsInfo);
	}

	@Override
	public List<StatisticsInfo> callCountbyDay(StatisticsInfo statisticsInfo) {
		return statisticsInfoMapper.callCountbyDay(statisticsInfo);
	}

	@Override
	public List<StatisticsInfo> callCountByCallTimeUser(StatisticsInfo statisticsInfo) {
		return statisticsInfoMapper.callCountByCallTimeUser(statisticsInfo);
	}

	@Override
	public List<StatisticsInfo> temp() {
		return statisticsInfoMapper.temp();
	}

	@Override
	public Integer temp2(StatisticsInfo a) {
		return statisticsInfoMapper.temp2(a);
	}

	@Override
	public Integer totalCallCountByCallTimeUser(StatisticsInfo statisticsInfo) {
		return statisticsInfoMapper.totalCallCountByCallTimeUser(statisticsInfo);
	}

	@Override
	public Integer totalSelectUserStatistics(StatisticsInfo statisticsInfo) {
		return statisticsInfoMapper.totalSelectUserStatistics(statisticsInfo);
	}

	@Override
	public List<StatisticsInfo> selectUserTotalStatistics(StatisticsInfo statisticsItem) {
		return statisticsInfoMapper.selectUserTotalStatistics(statisticsItem);
	}
	
	//모바일 녹취
	@Override
	public List<StatisticsInfo> selectCallsMobileStatistics(StatisticsInfo statisticsItem) {
		return statisticsInfoMapper.selectCallsMobileStatistics(statisticsItem);
	}
	
	@Override
	public List<StatisticsInfo> selectUsersCallsMobileStatistics(StatisticsInfo statisticsItem) {
		return statisticsInfoMapper.selectUsersCallsMobileStatistics(statisticsItem);
	}
	
	@Override
	public List<StatisticsInfo> selectDetailsCallsMobileStatistics(StatisticsInfo statisticsItem) {
		return statisticsInfoMapper.selectDetailsCallsMobileStatistics(statisticsItem);
	}
	
	@Override
	public List<StatisticsInfo>  totalUsersCallsMobileStatistics(StatisticsInfo statisticsItem) {
		return statisticsInfoMapper.totalUsersCallsMobileStatistics(statisticsItem);
	}
	
	@Override
	public Integer totalDetailsCallsMobileStatistics(StatisticsInfo statisticsItem) {
		return statisticsInfoMapper.totalDetailsCallsMobileStatistics(statisticsItem);
	}
	
	//키워드
	@Override
	public List<StatisticsInfo> keywordRank(StatisticsInfo statisticsInfo){
		return statisticsInfoMapper.keywordRank(statisticsInfo);
	}
	@Override
	public List<StatisticsInfo> selectKeyword(StatisticsInfo statisticsInfo){
		return statisticsInfoMapper.selectKeyword(statisticsInfo);
	}
	@Override
	public List<StatisticsInfo> selectCategory(StatisticsInfo statisticsInfo){
		return statisticsInfoMapper.selectCategory(statisticsInfo);
	}
}
