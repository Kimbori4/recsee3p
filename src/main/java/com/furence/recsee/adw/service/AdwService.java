package com.furence.recsee.adw.service;

import java.io.File;
import java.util.HashMap;

public interface AdwService {
	
	File makeAdwCsvFile(HashMap<String, String> dateHash) throws Exception;

}
