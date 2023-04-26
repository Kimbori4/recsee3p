package com.furence.recsee.wooribank.script.annotation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.junit.Test;

import com.furence.recsee.wooribank.script.param.request.ProductParam;
import com.furence.recsee.wooribank.script.util.Pair;
import com.google.common.collect.Maps;

import junit.framework.TestCase;

public class ColumnMapUtilTest extends TestCase {

	@Test
	public void testGetColumnName() {
		
		
		String column =ColumnMapUtil
				.getColumnNameByJsonProperty(ProductParam.Search.class, "rsScriptName");
		System.out.println(column);
		assertEquals("rs_product_name", column);
	}
	
	
	@Test
	public void testStringArray() {
		
		String script = "가나다{라마}바사{아자}차카타파하";
		String expected = "가나다{AB}바사{12}차카타파하";
		Map<String, String> map = Maps.newHashMap();
		map.put("라마", "AB");
		map.put("아자", "12");
		
		String actual = translateText(script,"{" , "}" , map);
		assertEquals(actual, expected, actual);
	}
	
	public String translateText(String script, String findPrefix, String findSuffix, Map<String, String> map ) {
		
		Iterator<Entry<String, String>> iter = map.entrySet().iterator();
		while(iter.hasNext()) {
			Entry<String, String> kv = iter.next();
			script = script.replace(findPrefix+kv.getKey()+findSuffix, 
					findPrefix+kv.getValue()+findSuffix);
		}
		return script;
	}
	
	@Test
	public void testListToMap() {
		List<Pair<String, String>> list = new ArrayList<>();
		list.add(Pair.of("1", "가"));
		list.add(Pair.of("2", "나"));
		list.add(Pair.of("3", "다"));
		
		Map<String, String> map = testStreamMap(list);
		map.forEach((k,v)-> { System.out.println(k+"=>"+v); });
	}
	
	public <T1,T2> Map<T1, T2> testStreamMap(List<Pair<T1, T2>> varList) {
		return varList.stream().collect(
						Collectors.toMap(
								Pair::first, 
								Pair::second,
								(x,y) -> y,
								HashMap::new));
				
	}
	
}
