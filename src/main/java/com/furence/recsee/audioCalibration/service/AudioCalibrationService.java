package com.furence.recsee.audioCalibration.service;

import java.util.List;

import com.furence.recsee.audioCalibration.model.AudioCalibrationInfo;

public interface AudioCalibrationService {
	List<AudioCalibrationInfo> selectAudioCalibrationInfo(AudioCalibrationInfo audioCalibrationInfo);
}
