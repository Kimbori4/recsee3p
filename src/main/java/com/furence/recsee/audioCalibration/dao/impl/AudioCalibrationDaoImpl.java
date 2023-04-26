package com.furence.recsee.audioCalibration.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.furence.recsee.audioCalibration.dao.AudioCalibrationDao;
import com.furence.recsee.audioCalibration.model.AudioCalibrationInfo;

@Repository("audioCalibrationDao")
public class AudioCalibrationDaoImpl implements AudioCalibrationDao {

	@Autowired
	private SqlSession sqlSession;

	public void setSqlSessionTemplate(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}

	@Override
	public List<AudioCalibrationInfo> selectAudioCalibrationInfo(AudioCalibrationInfo audioCalibrationInfo) {
		AudioCalibrationDao audioCalibrationMapper = (AudioCalibrationDao)sqlSession.getMapper(AudioCalibrationDao.class);
		return audioCalibrationMapper.selectAudioCalibrationInfo(audioCalibrationInfo);
	}
	
}
