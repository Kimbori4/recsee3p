package com.furence.recsee.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.initech.safedb.SimpleSafeDB;
import com.initech.safedb.sdk.exception.SafeDBSDKException;

public class SafeDBUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(SafeDBUtil.class);
	private static  SafeDBUtil safedb = null;
	private static  SimpleSafeDB simpleSafedb = null;
	
	private SafeDBUtil() {};
	
    /**
     * singleton으로 생성하게 하여 계속 객체를 유지한다.
     * 
     * @return
     */
	
	public static SafeDBUtil getInstance() {
		if(safedb==null){
			synchronized(SafeDBUtil.class){
				if(safedb==null){
					safedb=new SafeDBUtil();
				}
			}
		}
		return safedb;
	}
	
    /**
     * 
     * SafeDB 로그인 상태를 유지하고 로그인 되어있지 않으면 로그인 호출
     * 
     * @param property
     * @return
     * @throws SafeDBSDKException 
     */
	
	public static boolean init() throws SafeDBSDKException {
	     boolean ret = false;
	     
	       simpleSafedb = SimpleSafeDB.getInstance();
	    	if ( simpleSafedb.login() )
	    		return true;
			else
			{
				ret = simpleSafedb.login();
			}
	    			
	        return ret;
	}
	

    /**
     * 암호화를 한다
     * @param userName 
     * @param tableName 
     * @param columnName 
     * @param str 
     * @return 
     */
    public byte[] encrypt(String userName, String tableName, String columnName, byte str[]) throws SafeDBSDKException 
    {
        byte ret[] = null;
        try
        {
        	if ( isSecurity(str) ) 
        	{
            	if (simpleSafedb == null)
            		simpleSafedb = SimpleSafeDB.getInstance();
            	
	            ret = simpleSafedb.encrypt(userName, tableName, columnName, str);
        	} else
        		return str;
        }
        catch(SafeDBSDKException e)
        {
            logger.error("error",e);
            throw e;
        }
        
        return ret;
    }
    
    /**
     * 복호화를 한다.
     * @param userName String
     * @param tableName String
     * @param columnName String
     * @param str byte
     * @return
     */
    public String decrypt(String userName, String tableName, String columnName, String str) throws SafeDBSDKException
    {
        byte ret[] = null;
        byte strArr[] = str.getBytes();
    	if ( isSecurity(strArr) ) 
    	{
        	if (simpleSafedb == null)
        		simpleSafedb = SimpleSafeDB.getInstance();       
            ret = simpleSafedb.decrypt(userName, tableName, columnName, strArr);
    	}
    	else
    		return new String(strArr);

        return new String(ret);
    }
    
	
	
    /**
     * 암/복호화 대상 데이터가 존재하는지 확인하여, 보안적용할지 여부를 판단한다.
     * 
     * @param str 
     * @return
     */
    private boolean isSecurity(byte str[]) 
    {
    	if ( str != null )
    	{
    		try
    		{
	    		if ( "".equals(new String(str).trim()))
					return false;
				else
					return true;
    		}
    		catch (NullPointerException e) 
    		{
    			return false;
    		}
    		catch (Exception e) 
    		{
    			return false;
    		}
    	}
    	else
    		return false;
    }

}
