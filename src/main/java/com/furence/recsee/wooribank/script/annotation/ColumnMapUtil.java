package com.furence.recsee.wooribank.script.annotation;

import java.lang.reflect.Field;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ColumnMapUtil {
	
	
	/**
	 * filed에 선언된 @ColumnMap value 리턴
	 * @param clazz
	 * @param fieldName
	 * @return  @ColumnMap value or fieldName
	 */
	public static <T> String getColumnName(Class<T> clazz, String fieldName) {
		try {
			Field field = clazz.getDeclaredField(fieldName);
			if( field.isAnnotationPresent(ColumnMap.class)) {				
				return field.getAnnotation(ColumnMap.class).value();
			}
		} catch(Exception e) {
			throw new RuntimeException("["+fieldName+"] 에 해당하는 @ColumnMap value 가 없음. \n원인:"+e.getMessage());
		}
		
		return fieldName;
	}
	
	/**
	 * filed에 선언된 @ColumnMap value 리턴
	 * @param clazz
	 * @param fieldName
	 * @return  @ColumnMap value or fieldName
	 */
	public static <T> String getColumnNameByJsonProperty(Class<T> clazz, String jsonPoperty) {
			
		return Stream.of( clazz.getDeclaredFields())
				.filter(f->f.isAnnotationPresent(JsonProperty.class) && f.isAnnotationPresent(ColumnMap.class) )
				.filter(f->f.getAnnotation(JsonProperty.class).value().equals(jsonPoperty))
				.map(f->f.getAnnotation(ColumnMap.class).value())
				.findAny()
				.orElse(jsonPoperty);
	}
}
