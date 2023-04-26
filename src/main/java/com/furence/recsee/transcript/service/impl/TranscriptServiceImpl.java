package com.furence.recsee.transcript.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.furence.recsee.myfolder.model.MyFolderListinfo;
import com.furence.recsee.transcript.dao.TranscriptDao;
import com.furence.recsee.transcript.model.SttDataset;
import com.furence.recsee.transcript.model.SttEnginState;
import com.furence.recsee.transcript.model.SttModel;
import com.furence.recsee.transcript.model.SttResult;
import com.furence.recsee.transcript.model.TranscriptListInfo;
import com.furence.recsee.transcript.model.TranscriptMap;
import com.furence.recsee.transcript.service.TranscriptService;

@Service("transcriptService")
public class TranscriptServiceImpl implements TranscriptService {
	
	@Autowired
	TranscriptDao transcriptInfoSqlMapper;
	
	@Autowired
	TranscriptDao sttDatasetSqlMapper;
	
	@Autowired
	TranscriptDao transcriptMapSqlMapper;
	
	@Autowired
	TranscriptDao sttModelSqlMapper;
	
	@Autowired
	TranscriptDao sttEnginStateSqlMapper;

	@Override
	public Integer insertTranscriptList(TranscriptListInfo transcriptListInfo) {
		return transcriptInfoSqlMapper.insertTranscriptList(transcriptListInfo);
	}

	@Override
	public Integer updateTranscriptList(TranscriptListInfo transcriptListInfo) {
		return transcriptInfoSqlMapper.updateTranscriptList(transcriptListInfo);
	}

	@Override
	public Integer deleteTranscriptList(TranscriptListInfo transcriptListInfo) {
		return transcriptInfoSqlMapper.deleteTranscriptList(transcriptListInfo);
	}
	
	@Override
	public Integer insertTranscriptListLocal(TranscriptListInfo transcriptListInfo) {
		return transcriptInfoSqlMapper.insertTranscriptListLocal(transcriptListInfo);
	}

	@Override
	public Integer updateTranscriber(TranscriptListInfo transcriptListInfo) {
		return transcriptInfoSqlMapper.updateTranscriber(transcriptListInfo);
	}

	@Override
	public List<TranscriptListInfo> selectFileListenUrl(TranscriptListInfo transcriptListInfo) {
		return transcriptInfoSqlMapper.selectFileListenUrl(transcriptListInfo);
	}
	
	@Override
	public List<TranscriptListInfo> selectLocalFile(TranscriptListInfo transcriptListInfo) {
		return transcriptInfoSqlMapper.selectLocalFile(transcriptListInfo);
	}
	
	@Override
	public Integer insertTranscriptInfo(TranscriptListInfo transcriptListInfo) {
		return transcriptInfoSqlMapper.insertTranscriptInfo(transcriptListInfo);
	}

	@Override
	public Integer updateTranscriptInfo(TranscriptListInfo transcriptListInfo) {
		return transcriptInfoSqlMapper.updateTranscriptInfo(transcriptListInfo);
	}

	@Override
	public Integer deleteTranscriptInfo(TranscriptListInfo transcriptListInfo) {
		return transcriptInfoSqlMapper.deleteTranscriptInfo(transcriptListInfo);
	}

	@Override
	public List<TranscriptListInfo> selectTranscriptLearningInfo(TranscriptListInfo transcriptListInfo) {
		return transcriptInfoSqlMapper.selectTranscriptLearningInfo(transcriptListInfo);
	}

	@Override
	public List<TranscriptListInfo> selectTranscriptInfo(TranscriptListInfo transcriptListInfo) {
		return transcriptInfoSqlMapper.selectTranscriptInfo(transcriptListInfo);
	}

	
	@Override
	public List<TranscriptListInfo> selectTranscriptList(TranscriptListInfo transcriptListInfo) {
		return transcriptInfoSqlMapper.selectTranscriptList(transcriptListInfo);
	}
	
	@Override
	public List<TranscriptListInfo> selectTranscriptListPop(TranscriptListInfo transcriptListInfo) {
		return transcriptInfoSqlMapper.selectTranscriptListPop(transcriptListInfo);
	}

	@Override
	public List<TranscriptListInfo> selectTranscriptListDup(TranscriptListInfo transcriptListInfo) {
		return transcriptInfoSqlMapper.selectTranscriptListDup(transcriptListInfo);
	}
	
	@Override
	public Integer insertSttDataset(SttDataset sttDataset) {
		return sttDatasetSqlMapper.insertSttDataset(sttDataset);
	}

	@Override
	public Integer updateSttDataset(SttDataset sttDataset) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer deleteSttDataset(SttDataset sttDataset) {
		return sttDatasetSqlMapper.deleteSttDataset(sttDataset);
	}

	@Override
	public List<SttDataset> selectSttDataset(SttDataset sttDataset) {
		return sttDatasetSqlMapper.selectSttDataset(sttDataset);
	}
	
	@Override
	public List<SttDataset> selectAllSttDataset(SttDataset sttDataset) {
		return sttDatasetSqlMapper.selectAllSttDataset(sttDataset);
	}
	
	@Override
	public List<SttDataset> selectSttDatasetTest(SttDataset sttDataset) {
		return sttDatasetSqlMapper.selectSttDatasetTest(sttDataset);
	}
	
	@Override
	public List<SttDataset> selectSttDatasetOverlap(SttDataset sttDataset) {
		return sttDatasetSqlMapper.selectSttDatasetOverlap(sttDataset);
	}

	@Override
	public Integer insertTranscriptMap(TranscriptMap transcriptMap) {
		return transcriptMapSqlMapper.insertTranscriptMap(transcriptMap);
	}

	@Override
	public Integer updateTranscriptMap(TranscriptMap transcriptMap) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer deleteTranscriptMap(TranscriptMap transcriptMap) {
		return transcriptMapSqlMapper.deleteTranscriptMap(transcriptMap);
	}

	@Override
	public List<TranscriptMap> selectTranscriptMap(TranscriptMap transcriptMap) {
		return transcriptMapSqlMapper.selectTranscriptMap(transcriptMap);
	}

	@Override
	public List<TranscriptMap> selectTranscriptMapSerial(TranscriptMap transcriptMap) {
		return transcriptMapSqlMapper.selectTranscriptMapSerial(transcriptMap);
	}

	@Override
	public Integer insertSttModel(SttModel sttModel) {
		return sttModelSqlMapper.insertSttModel(sttModel);
	}

	@Override
	public Integer updateApplySttModel(SttModel sttModel) {
		return sttModelSqlMapper.updateApplySttModel(sttModel);
	}
	
	@Override
	public Integer updateApplyNSttModel(SttModel sttModel) {
		return sttModelSqlMapper.updateApplyNSttModel(sttModel);
	}

	@Override
	public Integer deleteSttModel(SttModel sttModel) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SttModel> selectSttModel(SttModel sttModel) {
		return sttModelSqlMapper.selectSttModel(sttModel);
	}
	
	@Override
	public List<SttModel> selectAllSttModel(SttModel sttModel) {
		return sttModelSqlMapper.selectAllSttModel(sttModel);
	}
	
	@Override
	public List<SttModel> selectSttBaseModel(SttModel sttModel) {
		return sttModelSqlMapper.selectSttBaseModel(sttModel);
	}
	
	@Override
	public List<SttModel> selectSttBaseModelPath(SttModel sttModel) {
		return sttModelSqlMapper.selectSttBaseModelPath(sttModel);
	}
	
	@Override
	public List<SttModel> selectSttModelOverlap(SttModel sttModel) {
		return sttModelSqlMapper.selectSttModelOverlap(sttModel);
	}

	@Override
	public List<SttEnginState> selectSttEnginState(SttEnginState sttEnginState) {
		return sttEnginStateSqlMapper.selectSttEnginState(sttEnginState);
	}
	
	@Override
	public Integer upsertSttEnginState(SttEnginState sttEnginState) {
		return sttEnginStateSqlMapper.upsertSttEnginState(sttEnginState);
	}

	@Override
	public List<SttResult> selectSttResult(SttResult sttResult) {
		return sttDatasetSqlMapper.selectSttResult(sttResult);
	}
}
