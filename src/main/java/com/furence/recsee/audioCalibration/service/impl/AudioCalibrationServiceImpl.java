package com.furence.recsee.audioCalibration.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.furence.recsee.audioCalibration.dao.AudioCalibrationDao;
import com.furence.recsee.audioCalibration.model.AudioCalibrationInfo;
import com.furence.recsee.audioCalibration.service.AudioCalibrationService;

@Service("audioCalibrationService")
public class AudioCalibrationServiceImpl implements AudioCalibrationService {
	@Autowired
	AudioCalibrationDao audioCalibrationMapper;

	@Override
	public List<AudioCalibrationInfo> selectAudioCalibrationInfo(AudioCalibrationInfo audioCalibrationInfo) {
		return audioCalibrationMapper.selectAudioCalibrationInfo(audioCalibrationInfo);
	}
}
