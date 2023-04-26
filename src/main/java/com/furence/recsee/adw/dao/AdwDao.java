package com.furence.recsee.adw.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.furence.recsee.adw.model.CsvInfo;


@Mapper
public interface AdwDao {
	
	
	List<CsvInfo> selectAdwData(HashMap<String, String> data);

}
