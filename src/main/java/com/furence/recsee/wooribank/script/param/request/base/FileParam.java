package com.furence.recsee.wooribank.script.param.request.base;

import com.furence.recsee.wooribank.script.service.file.types.FileType;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FileParam {
	/**
	 * 파일 타입
	 */
	private FileType fileType;
}


