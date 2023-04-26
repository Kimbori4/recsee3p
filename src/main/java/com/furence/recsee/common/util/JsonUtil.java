package com.furence.recsee.common.util;

import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class JsonUtil {

	private JsonUtil() {}
	
	public static void removeNullFrom(JSONObject jsonObject) {
		
		if( null == jsonObject) return;
		
 		@SuppressWarnings("unchecked")
		Iterator<String> iter = jsonObject.keySet().iterator();
		
 		while( iter.hasNext()) {
 			String key = iter.next();
 			
 			Object o = jsonObject.get(key);
 			if( null == o || ( o instanceof String && o.equals("null") )) {
 				iter.remove();
 			} else {
 				removeNullFrom(o);
 			}
 			
 		}
	}
	
	public static void removeNullFrom( JSONArray jsonArray) {
		
		if( null == jsonArray) return;
		
		@SuppressWarnings("unchecked")
		Iterator<Object> iter =  jsonArray.iterator();
		
		while( iter.hasNext()) {
			
			Object o = iter.next();
 			
			if( null == o || ( o instanceof String && o.equals("null") )) {
 				iter.remove();
 			} else {
 				removeNullFrom(o);
 			}
 		}
	}
	
	public static void removeNullFrom( Object object) {
			
			if( null == object) return;
			
	 		if( object instanceof JSONObject ) {
	 			removeNullFrom((JSONObject) object);
	 		} else if ( object instanceof JSONArray) {
	 			removeNullFrom((JSONArray) object);
	 		}
	}
}
