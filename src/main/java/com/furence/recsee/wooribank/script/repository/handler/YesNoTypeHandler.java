package com.furence.recsee.wooribank.script.repository.handler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import com.furence.recsee.wooribank.script.constants.Const;
import com.furence.recsee.wooribank.script.constants.Const.Bool;

public class YesNoTypeHandler extends BaseTypeHandler<Const.Bool> {

	@Override
	public Bool getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		return Const.Bool.create( cs.getString(columnIndex));
	}
	
	@Override
	public Bool getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		return Const.Bool.create( rs.getString(columnIndex));
	}
	
	@Override
	public Bool getNullableResult(ResultSet rs, String columnName) throws SQLException {
		return Const.Bool.create( rs.getString(columnName));
	}
	
	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, Bool parameter, JdbcType jdbcType)
			throws SQLException {
		ps.setString(i, Optional.ofNullable(parameter).orElse(null).getValue() );
	}
	
}
