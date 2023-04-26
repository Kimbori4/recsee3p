package com.furence.recsee.wooribank.script.repository.handler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import com.furence.recsee.wooribank.script.constants.Const;
import com.furence.recsee.wooribank.script.constants.Const.Approve;

public class ApproveStautsTypeHandler extends BaseTypeHandler<Const.Approve>{

	@Override
	public Approve getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		return Const.Approve.create(cs.getString(columnIndex));
	}
	
	@Override
	public Approve getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		return Const.Approve.create(rs.getString(columnIndex));
	}
	
	@Override
	public Approve getNullableResult(ResultSet rs, String columnName) throws SQLException {
		return Const.Approve.create(rs.getString(columnName));
	}
	
	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, Approve parameter, JdbcType jdbcType)
			throws SQLException {
		ps.setString(i, Optional.ofNullable(parameter).orElse(null).getValue() );
	}
}
