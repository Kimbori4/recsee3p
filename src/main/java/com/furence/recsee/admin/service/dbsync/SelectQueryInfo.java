package com.furence.recsee.admin.service.dbsync;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SelectQueryInfo {

	private HashMap<String, String> putResult = new HashMap<>();
	
	public void setMap(String col, String val) {
		putResult.put(col, val);
	}
	public HashMap<String, String> getMap() {
		return putResult;
	}
	
}
