package com.furence.recsee.common.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ParameterMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.furence.recsee.common.model.EtcConfigInfo;
import com.furence.recsee.common.model.SynchronizationVO;
import com.furence.recsee.common.service.EtcConfigInfoService;
import com.furence.recsee.common.service.SynchronizationService;
import com.initech.beans.BeanInfo;
import com.initech.beans.Introspector;
import com.initech.beans.PropertyDescriptor;

public class SynchronizationUtil {
	private static final Logger logger = LoggerFactory.getLogger(SynchronizationUtil.class);
	private EtcConfigInfoService etcConfigInfoService;
	
	private SynchronizationService synchronizationService;
	
	private String SynchronizationYN;
	private String executeIp;
	
	public SynchronizationUtil(EtcConfigInfoService etcConfig, SynchronizationService synchron) {
		
		etcConfigInfoService = etcConfig;
		synchronizationService = synchron;
		
		EtcConfigInfo etcConfigInfo = new EtcConfigInfo();
		etcConfigInfo.setGroupKey("SYNCHRONIZATION");
		etcConfigInfo.setConfigKey("USE");
		List<EtcConfigInfo> etcConfigResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
		if(etcConfigResult.size() > 0) {
			SynchronizationYN = etcConfigResult.get(0).getConfigValue();
			
			etcConfigInfo = new EtcConfigInfo();
			etcConfigInfo.setGroupKey("SYNCHRONIZATION");
			etcConfigInfo.setConfigKey("IP");
			etcConfigResult = etcConfigInfoService.selectEtcConfigInfo(etcConfigInfo);
			
			executeIp = etcConfigResult.get(0).getConfigValue();
			
		}else {
			SynchronizationYN = "N";
		}
	}
	
	public void SynchronizationInsert(BoundSql query) {	
	
		if("Y".equals(SynchronizationYN)) {
			String sqlQuery = query.getSql();			
			Object param = query.getParameterObject();
			List<ParameterMapping> parameterMapping = query.getParameterMappings();
			Map<String,Object> ObjectMap = null;
			try {
				ObjectMap = domainToMapWithExcept(param,null);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				logger.error("error",e);
			}
			for(ParameterMapping mapping : parameterMapping) {
				String propValue = mapping.getProperty();
				if(ObjectMap!=null && ObjectMap.get(propValue) == null) continue;				
				Object value = ObjectMap.get(propValue);				
				if(value instanceof String) {
					sqlQuery =sqlQuery.replaceFirst("\\?","'"+ value+"'");
				}else {
					sqlQuery =sqlQuery.replaceFirst("\\?",value.toString());
				}
				
				
			}   	
			
			SynchronizationVO synchronizationVO = new SynchronizationVO();
			
			synchronizationVO.setIp(executeIp);
			synchronizationVO.setQuery(sqlQuery);
			synchronizationVO.setReason("");
			synchronizationService.insertSynchronizationInfo(synchronizationVO);			
		}	
	}
	
	public static Map<String, Object> domainToMapWithExcept(Object vo, String[] arrExceptList) throws Exception {
		Map<String, Object> result = new HashMap<>();
		BeanInfo info = Introspector.getBeanInfo(vo.getClass());
		for(PropertyDescriptor pd : info.getPropertyDescriptors()) {
			Method reader = pd.getReadMethod();
			if(reader != null) {
				try {
				if(arrExceptList != null && arrExceptList.length > 0 && isContain(arrExceptList, pd.getName())) continue;
				if(reader.invoke(vo) == null) continue;
				result.put(pd.getName(), reader.invoke(vo));
				}catch (InvocationTargetException e) {
					logger.error("error",e);
				}
			}
		}
		return result;
	}
	
	public static Boolean isContain(String[] arrList, String name) {
		for(String arr : arrList) {
			if(StringUtils.contains(arr, name))
				return true;
		}
		return false;
	}
}
