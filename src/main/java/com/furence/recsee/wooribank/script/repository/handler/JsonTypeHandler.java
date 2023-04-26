package com.furence.recsee.wooribank.script.repository.handler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JsonTypeHandler<T extends Object> extends BaseTypeHandler<T>{

	private static final ObjectMapper mapper = new ObjectMapper();
	
	private Class<T> clazz;
	
	public JsonTypeHandler(Class<T> clazz) {
		if(clazz == null) throw new IllegalArgumentException("type argument can't not be null");
		this.clazz = clazz;
	}
	
	@Override
	public T getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {				
		return cs.getString(columnIndex) == null ? null : this.toObject(cs.getString(columnIndex), clazz);
	}
	
	@Override
	public T getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		return rs.getString(columnIndex) == null ? null : this.toObject(rs.getString(columnIndex), clazz);
	}
	
	@Override
	public T getNullableResult(ResultSet rs, String columnName) throws SQLException {
		
		return  rs.getString(columnName) == null ? null : this.toObject(rs.getString(columnName), clazz);
	}
	
	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException {
		ps.setString(i, parameter == null ? null : this.toJson(parameter));
	}
	
	private String toJson(T object) {
		
		try{
			return mapper.writeValueAsString(object);
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	private T toObject(String str, Class<?> clazz) {
		try{
			return (T) mapper.readValue(str , clazz);
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	static {
		mapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
		mapper.setSerializationInclusion(Include.NON_NULL);
	}
}
