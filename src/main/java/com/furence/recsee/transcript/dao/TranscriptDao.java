package com.furence.recsee.transcript.dao;

import java.util.List;

import com.furence.recsee.transcript.model.SttEnginState;
import com.furence.recsee.transcript.model.SttModel;
import com.furence.recsee.transcript.model.SttResult;
import com.furence.recsee.transcript.model.TranscriptListInfo;
import com.furence.recsee.transcript.model.TranscriptMap;
import com.furence.recsee.transcript.model.SttDataset;

public interface TranscriptDao {
	// 분배 및 전사 관리 테이블
	Integer insertTranscriptList(TranscriptListInfo transcriptListInfo);
	Integer updateTranscriptList(TranscriptListInfo transcriptListInfo);
	Integer deleteTranscriptList(TranscriptListInfo transcriptListInfo);
	
	// 로컬파일 insert
	Integer insertTranscriptListLocal(TranscriptListInfo transcriptListInfo);

	// 분배 후 상담원 할당
	Integer updateTranscriber(TranscriptListInfo transcriptListInfo);

	// 녹취파일 listenUrl 조회
	List<TranscriptListInfo> selectFileListenUrl (TranscriptListInfo transcriptListInfo);
	// 로컬파일 있는지 조회(feat.txt)
	List<TranscriptListInfo> selectLocalFile (TranscriptListInfo transcriptListInfo);
	
	// 전사 내용 관리 테이블
	Integer insertTranscriptInfo(TranscriptListInfo transcriptListInfo);
	Integer updateTranscriptInfo(TranscriptListInfo transcriptListInfo);
	Integer deleteTranscriptInfo(TranscriptListInfo transcriptListInfo);

	List<TranscriptListInfo> selectTranscriptLearningInfo(TranscriptListInfo transcriptListInfo);
	List<TranscriptListInfo> selectTranscriptInfo(TranscriptListInfo transcriptListInfo);
	List <TranscriptListInfo> selectTranscriptList(TranscriptListInfo transcriptListInfo);
	List <TranscriptListInfo> selectTranscriptListPop(TranscriptListInfo transcriptListInfo);
	List<TranscriptListInfo> selectTranscriptListDup(TranscriptListInfo transcriptListInfo);
	
	// 데이터셋 테이블
	Integer insertSttDataset(SttDataset sttDataset);
	Integer updateSttDataset(SttDataset sttDataset);
	Integer deleteSttDataset(SttDataset sttDataset);
	List<SttDataset> selectSttDataset(SttDataset sttDataset);
	List<SttDataset> selectAllSttDataset(SttDataset sttDataset);
	List<SttDataset> selectSttDatasetTest(SttDataset sttDataset);
	List<SttDataset> selectSttDatasetOverlap(SttDataset sttDataset);
	
	// 맵핑 테이블
	Integer insertTranscriptMap(TranscriptMap transcriptMap);
	Integer updateTranscriptMap(TranscriptMap transcriptMap);
	Integer deleteTranscriptMap(TranscriptMap transcriptMap);
	List<TranscriptMap> selectTranscriptMap(TranscriptMap transcriptMap);
	List<TranscriptMap> selectTranscriptMapSerial(TranscriptMap transcriptMap);
	
	// STT 모델 테이블
	Integer insertSttModel(SttModel sttModel);
	Integer updateApplySttModel(SttModel sttModel);
	Integer updateApplyNSttModel(SttModel sttModel);
	Integer deleteSttModel(SttModel sttModel);
	List<SttModel> selectSttModel(SttModel sttModel);
	List<SttModel> selectAllSttModel(SttModel sttModel);
	List<SttModel> selectSttBaseModel(SttModel sttModel);
	List<SttModel> selectSttBaseModelPath(SttModel sttModel);
	List<SttModel> selectSttModelOverlap(SttModel sttModel);
	
	// 적용 모델 정보
	List<SttEnginState> selectSttEnginState(SttEnginState sttEnginState);
	Integer upsertSttEnginState(SttEnginState sttEnginState);

	// STT 결과 조회
	List<SttResult> selectSttResult(SttResult sttResult);
}
