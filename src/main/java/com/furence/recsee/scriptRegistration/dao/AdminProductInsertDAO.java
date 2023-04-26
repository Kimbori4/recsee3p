package com.furence.recsee.scriptRegistration.dao;

public interface AdminProductInsertDAO {
	void doInsertProduct(String productType, String productCode, String productName, String useYn, String groupYn, String groupCode, String sysdisType, String productAttr);
	public int checkProductCode(String productType, String productCode);
	
	void executeProcdure();
}
