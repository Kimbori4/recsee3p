package com.furence.recsee.audioCalibration.dao;

import java.util.List;

import com.furence.recsee.audioCalibration.model.AudioCalibrationInfo;

public interface AudioCalibrationDao {
	List<AudioCalibrationInfo> selectAudioCalibrationInfo(AudioCalibrationInfo audioCalibrationInfo);
}
