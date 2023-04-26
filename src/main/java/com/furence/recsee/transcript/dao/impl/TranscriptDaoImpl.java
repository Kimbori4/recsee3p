package com.furence.recsee.transcript.dao.impl;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import com.furence.recsee.common.service.EtcConfigInfoService;
import com.furence.recsee.common.service.SynchronizationService;
import com.furence.recsee.common.util.SynchronizationUtil;
import com.furence.recsee.transcript.dao.TranscriptDao;
import com.furence.recsee.transcript.model.SttDataset;
import com.furence.recsee.transcript.model.SttEnginState;
import com.furence.recsee.transcript.model.SttModel;
import com.furence.recsee.transcript.model.SttResult;
import com.furence.recsee.transcript.model.TranscriptListInfo;
import com.furence.recsee.transcript.model.TranscriptMap;
import com.furence.recsee.transcript.model.SttDataset;

@Repository("transcriptDao")
public class TranscriptDaoImpl implements TranscriptDao{
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
	public Integer insertTranscriptList(TranscriptListInfo transcriptListInfo) {
		TranscriptDao transcriptInfoSqlMapper = sqlSession.getMapper(TranscriptDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("insertTranscriptList").getBoundSql(transcriptListInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return transcriptInfoSqlMapper.insertTranscriptList(transcriptListInfo);
	}
	
	@Override
	public Integer updateTranscriptList(TranscriptListInfo transcriptListInfo) {
		TranscriptDao transcriptInfoSqlMapper = sqlSession.getMapper(TranscriptDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updateTranscriptList").getBoundSql(transcriptListInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return transcriptInfoSqlMapper.updateTranscriptList(transcriptListInfo);
	}
	
	@Override
	public Integer deleteTranscriptList(TranscriptListInfo transcriptListInfo) {
		TranscriptDao transcriptInfoSqlMapper = sqlSession.getMapper(TranscriptDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("deleteTranscriptList").getBoundSql(transcriptListInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return transcriptInfoSqlMapper.deleteTranscriptList(transcriptListInfo);
	}
	
	@Override
	public Integer insertTranscriptListLocal(TranscriptListInfo transcriptListInfo) {
		TranscriptDao transcriptInfoSqlMapper = sqlSession.getMapper(TranscriptDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("insertTranscriptListLocal").getBoundSql(transcriptListInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return transcriptInfoSqlMapper.insertTranscriptListLocal(transcriptListInfo);
	}
	
	@Override
	public Integer insertTranscriptInfo(TranscriptListInfo transcriptListInfo) {
		TranscriptDao transcriptInfoSqlMapper = sqlSession.getMapper(TranscriptDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("insertTranscriptInfo").getBoundSql(transcriptListInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return transcriptInfoSqlMapper.insertTranscriptInfo(transcriptListInfo);
	}
	
	@Override
	public Integer updateTranscriber(TranscriptListInfo transcriptListInfo) {
		TranscriptDao transcriptInfoSqlMapper = sqlSession.getMapper(TranscriptDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updateTranscriber").getBoundSql(transcriptListInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return transcriptInfoSqlMapper.updateTranscriber(transcriptListInfo);
	}
	
	@Override
	public List<TranscriptListInfo> selectFileListenUrl(TranscriptListInfo transcriptListInfo) {
		TranscriptDao transcriptInfoSqlMapper = sqlSession.getMapper(TranscriptDao.class);
		
		return transcriptInfoSqlMapper.selectFileListenUrl(transcriptListInfo);
	}
	
	@Override
	public List<TranscriptListInfo> selectLocalFile(TranscriptListInfo transcriptListInfo) {
		TranscriptDao transcriptInfoSqlMapper = sqlSession.getMapper(TranscriptDao.class);
		
		return transcriptInfoSqlMapper.selectLocalFile(transcriptListInfo);
	}

	@Override
	public Integer updateTranscriptInfo(TranscriptListInfo transcriptListInfo) {
		TranscriptDao transcriptInfoSqlMapper = sqlSession.getMapper(TranscriptDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updateTranscriptInfo").getBoundSql(transcriptListInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return transcriptInfoSqlMapper.updateTranscriptInfo(transcriptListInfo);
	}
	
	@Override
	public Integer deleteTranscriptInfo(TranscriptListInfo transcriptListInfo) {
		TranscriptDao transcriptInfoSqlMapper = sqlSession.getMapper(TranscriptDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("deleteTranscriptInfo").getBoundSql(transcriptListInfo);
		synchronizationUtil.SynchronizationInsert(query);
		
		return transcriptInfoSqlMapper.deleteTranscriptInfo(transcriptListInfo);
	}
	
	@Override
	public List<TranscriptListInfo> selectTranscriptLearningInfo(TranscriptListInfo transcriptListInfo) {
		TranscriptDao transcriptInfoSqlMapper = sqlSession.getMapper(TranscriptDao.class);
		
		return transcriptInfoSqlMapper.selectTranscriptLearningInfo(transcriptListInfo);
	}
	
	@Override
	public List<TranscriptListInfo> selectTranscriptInfo(TranscriptListInfo transcriptListInfo) {
		TranscriptDao transcriptInfoSqlMapper = sqlSession.getMapper(TranscriptDao.class);
		
		return transcriptInfoSqlMapper.selectTranscriptInfo(transcriptListInfo);
	}
	
	@Override
	public List<TranscriptListInfo> selectTranscriptList(TranscriptListInfo transcriptListInfo) {
		TranscriptDao transcriptInfoSqlMapper = sqlSession.getMapper(TranscriptDao.class);
		
		return transcriptInfoSqlMapper.selectTranscriptList(transcriptListInfo);
	}
	
	@Override
	public List<TranscriptListInfo> selectTranscriptListPop(TranscriptListInfo transcriptListInfo) {
		TranscriptDao transcriptInfoSqlMapper = sqlSession.getMapper(TranscriptDao.class);
		
		return transcriptInfoSqlMapper.selectTranscriptListPop(transcriptListInfo);
	}
	
	@Override
	public List<TranscriptListInfo> selectTranscriptListDup(TranscriptListInfo transcriptListInfo) {
		TranscriptDao transcriptInfoSqlMapper = sqlSession.getMapper(TranscriptDao.class);
		
		return transcriptInfoSqlMapper.selectTranscriptListDup(transcriptListInfo);
	}

	@Override
	public Integer insertSttDataset(SttDataset sttDataset) {
		TranscriptDao sttDatasetSqlMapper = sqlSession.getMapper(TranscriptDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("insertSttDataset").getBoundSql(sttDataset);
		synchronizationUtil.SynchronizationInsert(query);
		
		return sttDatasetSqlMapper.insertSttDataset(sttDataset);
	}

	@Override
	public Integer updateSttDataset(SttDataset sttDataset) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer deleteSttDataset(SttDataset sttDataset) {
		TranscriptDao sttDatasetSqlMapper = sqlSession.getMapper(TranscriptDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("deleteSttDataset").getBoundSql(sttDataset);
		synchronizationUtil.SynchronizationInsert(query);
		
		return sttDatasetSqlMapper.deleteSttDataset(sttDataset);
	}

	@Override
	public List<SttDataset> selectSttDataset(SttDataset sttDataset) {
		TranscriptDao sttDatasetSqlMapper = sqlSession.getMapper(TranscriptDao.class);
		
		return sttDatasetSqlMapper.selectSttDataset(sttDataset);
	}
	
	@Override
	public List<SttDataset> selectAllSttDataset(SttDataset sttDataset) {
		TranscriptDao sttDatasetSqlMapper = sqlSession.getMapper(TranscriptDao.class);
		
		return sttDatasetSqlMapper.selectAllSttDataset(sttDataset);
	}
	
	@Override
	public List<SttDataset> selectSttDatasetTest(SttDataset sttDataset) {
		TranscriptDao sttDatasetSqlMapper = sqlSession.getMapper(TranscriptDao.class);
		
		return sttDatasetSqlMapper.selectSttDatasetTest(sttDataset);
	}
	
	@Override
	public List<SttDataset> selectSttDatasetOverlap(SttDataset sttDataset) {
		TranscriptDao sttDatasetSqlMapper = sqlSession.getMapper(TranscriptDao.class);
		
		return sttDatasetSqlMapper.selectSttDatasetOverlap(sttDataset);
	}

	@Override
	public Integer insertTranscriptMap(TranscriptMap transcriptMap) {
		TranscriptDao transcriptMapSqlMapper = sqlSession.getMapper(TranscriptDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("insertTranscriptMap").getBoundSql(transcriptMap);
		synchronizationUtil.SynchronizationInsert(query);
		
		return transcriptMapSqlMapper.insertTranscriptMap(transcriptMap);
	}

	@Override
	public Integer updateTranscriptMap(TranscriptMap transcriptMap) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer deleteTranscriptMap(TranscriptMap transcriptMap) {
		TranscriptDao transcriptMapSqlMapper = sqlSession.getMapper(TranscriptDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("deleteTranscriptMap").getBoundSql(transcriptMap);
		synchronizationUtil.SynchronizationInsert(query);
		
		return transcriptMapSqlMapper.deleteTranscriptMap(transcriptMap);
	}

	@Override
	public List<TranscriptMap> selectTranscriptMap(TranscriptMap transcriptMap) {
		TranscriptDao transcriptMapSqlMapper = sqlSession.getMapper(TranscriptDao.class);
		
		return transcriptMapSqlMapper.selectTranscriptMap(transcriptMap);
	}

	@Override
	public List<TranscriptMap> selectTranscriptMapSerial(TranscriptMap transcriptMap) {
		TranscriptDao transcriptMapSqlMapper = sqlSession.getMapper(TranscriptDao.class);
		
		return transcriptMapSqlMapper.selectTranscriptMapSerial(transcriptMap);
	}

	@Override
	public Integer insertSttModel(SttModel sttModel) {
		TranscriptDao sttModelSqlMapper = sqlSession.getMapper(TranscriptDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("insertSttModel").getBoundSql(sttModel);
		synchronizationUtil.SynchronizationInsert(query);
		
		return sttModelSqlMapper.insertSttModel(sttModel);
	}

	@Override
	public Integer updateApplySttModel(SttModel sttModel) {
		TranscriptDao sttModelSqlMapper = sqlSession.getMapper(TranscriptDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updateApplySttModel").getBoundSql(sttModel);
		synchronizationUtil.SynchronizationInsert(query);
		
		return sttModelSqlMapper.updateApplySttModel(sttModel);
	}
	
	@Override
	public Integer updateApplyNSttModel(SttModel sttModel) {
		TranscriptDao sttModelSqlMapper = sqlSession.getMapper(TranscriptDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("updateApplyNSttModel").getBoundSql(sttModel);
		synchronizationUtil.SynchronizationInsert(query);
		
		return sttModelSqlMapper.updateApplyNSttModel(sttModel);
	}

	@Override
	public Integer deleteSttModel(SttModel sttModel) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SttModel> selectSttModel(SttModel sttModel) {
		TranscriptDao sttModelSqlMapper = sqlSession.getMapper(TranscriptDao.class);
		
		return sttModelSqlMapper.selectSttModel(sttModel);
	}
	
	@Override
	public List<SttModel> selectAllSttModel(SttModel sttModel) {
		TranscriptDao sttModelSqlMapper = sqlSession.getMapper(TranscriptDao.class);
		
		return sttModelSqlMapper.selectAllSttModel(sttModel);
	}
	
	@Override
	public List<SttModel> selectSttBaseModel(SttModel sttModel) {
		TranscriptDao sttModelSqlMapper = sqlSession.getMapper(TranscriptDao.class);
		
		return sttModelSqlMapper.selectSttBaseModel(sttModel);
	}
	
	@Override
	public List<SttModel> selectSttBaseModelPath(SttModel sttModel) {
		TranscriptDao sttModelSqlMapper = sqlSession.getMapper(TranscriptDao.class);
		
		return sttModelSqlMapper.selectSttBaseModelPath(sttModel);
	}
	
	@Override
	public List<SttModel> selectSttModelOverlap(SttModel sttModel) {
		TranscriptDao sttModelSqlMapper = sqlSession.getMapper(TranscriptDao.class);
		
		return sttModelSqlMapper.selectSttModelOverlap(sttModel);
	}

	@Override
	public List<SttEnginState> selectSttEnginState(SttEnginState sttEnginState) {
		TranscriptDao sttEnginStateSqlMapper = sqlSession.getMapper(TranscriptDao.class);
		return sttEnginStateSqlMapper.selectSttEnginState(sttEnginState);
	}
	
	@Override
	public Integer upsertSttEnginState(SttEnginState sttEnginState) {
		TranscriptDao sttEnginStateSqlMapper = sqlSession.getMapper(TranscriptDao.class);
		
		SynchronizationUtil synchronizationUtil = new SynchronizationUtil(etcConfigInfoService,synchronizationService);
		BoundSql query = sqlSession.getConfiguration().getMappedStatement("upsertSttEnginState").getBoundSql(sttEnginState);
		synchronizationUtil.SynchronizationInsert(query);
		
		return sttEnginStateSqlMapper.upsertSttEnginState(sttEnginState);
	}

	@Override
	public List<SttResult> selectSttResult(SttResult sttResult) {
		TranscriptDao sttDatasetSqlMapper = sqlSession.getMapper(TranscriptDao.class);
		
		return sttDatasetSqlMapper.selectSttResult(sttResult);
	}
}
