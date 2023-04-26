package com.furence.recsee.statistics.service;

import java.util.List;

import com.furence.recsee.statistics.model.StatisticsInfo;

public interface StatisticsInfoService {
	List<StatisticsInfo> selectCallsStatistics(StatisticsInfo statisticsInfo);
	List<StatisticsInfo> selectDayTimeUserStatistics(StatisticsInfo statisticsInfo);
	List<StatisticsInfo> dashboard7DayStatistics(StatisticsInfo statisticsInfo);
	List<StatisticsInfo> dashboardDaily(StatisticsInfo statisticsInfo);
	List<StatisticsInfo> dashboardSystem(StatisticsInfo statisticsInfo);
	List<StatisticsInfo> selectUserStatistics(StatisticsInfo statisticsInfo);
	StatisticsInfo callSummary(StatisticsInfo statisticsInfo);
	StatisticsInfo callCountbyType(StatisticsInfo statisticsInfo);
	StatisticsInfo callCountbyTime(StatisticsInfo statisticsInfo);
	List<StatisticsInfo> callCountbyDay(StatisticsInfo statisticsInfo);
	List<StatisticsInfo> callCountByCallTimeUser(StatisticsInfo statisticsInfo);
	List<StatisticsInfo> temp();
	Integer temp2(StatisticsInfo a);
	Integer totalCallCountByCallTimeUser(StatisticsInfo statisticsInfo);
	Integer totalSelectUserStatistics(StatisticsInfo statisticsInfo);
	List<StatisticsInfo> selectUserTotalStatistics(StatisticsInfo statisticsItem);
	
	//모바일 녹취
	List<StatisticsInfo> selectCallsMobileStatistics(StatisticsInfo statisticsInfo);
	List<StatisticsInfo> selectUsersCallsMobileStatistics(StatisticsInfo statisticsInfo);
	List<StatisticsInfo> selectDetailsCallsMobileStatistics(StatisticsInfo statisticsInfo);
	List<StatisticsInfo> totalUsersCallsMobileStatistics(StatisticsInfo statisticsInfo);
	Integer totalDetailsCallsMobileStatistics(StatisticsInfo statisticsInfo);	
	
	//키워드
	List<StatisticsInfo> keywordRank(StatisticsInfo statisticsInfo);
	List<StatisticsInfo> selectKeyword(StatisticsInfo statisticsInfo);
	List<StatisticsInfo> selectCategory(StatisticsInfo statisticsInfo);
}
