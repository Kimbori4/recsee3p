package com.furence.recsee.wooribank.script.repository.handler;

import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IntegerArrayTypeHandler extends BaseTypeHandler<List<Integer>>{
	private static final Logger logger = LoggerFactory.getLogger(IntegerArrayTypeHandler.class);
	@Override
	public List<Integer> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		return getArrayListFromSqlArray(cs.getArray(columnIndex));
	}
	
	@Override
	public List<Integer> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		return getArrayListFromSqlArray(rs.getArray(columnIndex));
	}
	
	@Override
	public List<Integer> getNullableResult(ResultSet rs, String columnName) throws SQLException {
		return getArrayListFromSqlArray(rs.getArray(columnName));
	}
	
	@Override
	public void setNonNullParameter(PreparedStatement ps, int index, List<Integer> list, JdbcType jdbcType)
			throws SQLException {
		
		ps.setArray(index, null);
		
		Optional<List<Integer>> optList = Optional.ofNullable(list);
		
		optList.ifPresent(o-> {
			try {
				ps.setArray(index, ps.getConnection().createArrayOf("INTEGER", o.toArray(new Integer[o.size()] )));
			} catch (SQLException e) {
				logger.error("error", e);
			}
		} );
	
	}
	
	
	private List<Integer> getArrayListFromSqlArray(Array array) throws SQLException{
		
		if( null == array) return null;
		
		Integer[] intArray = (Integer[]) array.getArray();
		
		return Arrays.asList( intArray );
	}
}
