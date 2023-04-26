package com.furence.recsee.common.util;

import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;

public class CryptoDataSource extends BasicDataSource{

	@Override
	public void setUsername(String userName) {
		// TODO Auto-generated method stub
		super.setUsername(userName);
	}
	
	@Override
	public void setPassword(String password) {
		// TODO Auto-generated method stub
		AesUtil aes = new AesUtil();
		//System.out.println(aes.encrypt(password)); 
		super.setPassword(aes.decrypt(password));
	}
}
