package com.furence.recsee.adw.service;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.furence.recsee.adw.dao.AdwDao;
import com.furence.recsee.adw.model.CsvInfo;
import com.furence.recsee.adw.util.CsvFactory;


@Service
public class AdwServiceImpl implements AdwService {
	


	@Override
	public File makeAdwCsvFile(HashMap<String, String> dateHash) throws Exception {

		return null;
	}
	
	
}
