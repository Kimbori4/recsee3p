package com.furence.recsee.admin.service.dbsync;

public class PostgresqlQuery {
	
	public String select(String column_list, String table_name) {
		String column = column_list.replace("\"", "").replace("[", "").replace("]", "");
		String sql = "select " + column + " from " + table_name;
		return sql;
	}
	
	public String upsert(String column_key, String column_normal, String table_name) {
		String[] column_list_key = column_key.replace("\"", "").replace("[", "").replace("]", "").split(",");
		String[] column_list_normal = column_normal.replace("\"", "").replace("[", "").replace("]", "").split(",");
		String sql = "with upsert as (update "+ table_name + " set ";
		String keyCaV="";
		String normalCaV="";
		String keyAsNormal = "";
		String col = "";
		String val = "";
		for (int i = 0; i < column_list_key.length; i++) {
			if(i==column_list_key.length-1) {
				keyCaV += column_list_key[i] + "=?";
				keyAsNormal += column_list_key[i] + "=?";
			}else {
				keyCaV += column_list_key[i] + "=? and ";
				keyAsNormal += column_list_key[i] + "=?,";
			}
		}
		for (int i = 0; i < column_list_key.length; i++) {
			if(i==column_list_key.length-1) {
				col += column_list_key[i];
				val += "?";
			}else {
				col += column_list_key[i]+",";
				val += "?,";
			}
		}
		if(!("[]".equals(column_normal))) {
			col += ",";
			val += ",";
			for (int i = 0; i < column_list_normal.length; i++) {
				if(i==column_list_normal.length-1) {
					normalCaV += column_list_normal[i] + "=?";
				}else {
					normalCaV += column_list_normal[i] + "=?,";
				}
			}
			for (int i = 0; i < column_list_normal.length; i++) {
				if(i==column_list_normal.length-1) {
					col += column_list_normal[i];
					val += "?";
				}else {
					col += column_list_normal[i]+",";
					val += "?,";
				}
			}
		}
		
		// 키가 있는 컬럼만 선택한 경우
		if("[]".equals(column_normal)) {
			sql += keyAsNormal + " where " + keyCaV + " returning*) insert into " + table_name + " (" + col + ") select " + val + " where not exists (select "+ col +" from upsert)";
		// 둘다 선택한 경우
		}else {
			sql += normalCaV + " where " + keyCaV + " returning*) insert into " + table_name + " (" + col + ") select " + val + " where not exists (select "+ col +" from upsert)";
		}
		return sql;
	}
	
}
