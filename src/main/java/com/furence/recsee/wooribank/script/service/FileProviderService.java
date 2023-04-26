package com.furence.recsee.wooribank.script.service;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.furence.recsee.wooribank.script.param.request.base.FileParam;
import com.furence.recsee.wooribank.script.service.file.types.FileService;
import com.furence.recsee.wooribank.script.service.file.types.FileService.FileServiceType;
import com.furence.recsee.wooribank.script.service.file.types.FileService.ProviderParam;


@Service
public class FileProviderService {	
	
	@SuppressWarnings("rawtypes")
	private final Map<FileServiceType, FileService> container = new HashMap<>();
	
	@Autowired
	@SuppressWarnings("rawtypes")
	public FileProviderService( List<FileService> list ){
		for(FileService service : list) {
			this.container.put(service.getType(), service);
		}
		
	}
	
	/**
	 * 공통 함수 호출
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public File createFile(ProviderParam<? extends FileParam> param ) throws Exception {
		
		Objects.requireNonNull(param);
		Objects.requireNonNull(param.getType());
		
		return this.container.get(param.getType()).createFile(param.getParamter());
			
	}
	
}
