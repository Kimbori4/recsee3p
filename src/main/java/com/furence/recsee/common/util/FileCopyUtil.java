package com.furence.recsee.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class FileCopyUtil {
	private static final Logger logger = LoggerFactory.getLogger(FileCopyUtil.class);
	public FileInputStream fis;
	
	public FileOutputStream fos;
	
	public void copy(File sourceA , File targetA) {
		boolean checktargetA = DirectoryUtil.checkDirectory(targetA);
		if(!checktargetA) {
			logger.info(targetA+" 가 존재합니다. 복사를 시작합니다.");
		}
		
		boolean mkdirDirectory = DirectoryUtil.mkdirDirectory(targetA);
		if(mkdirDirectory) {
			logger.info(targetA+" 생성 성공!");
		}
		
		boolean checksourceA  = DirectoryUtil.checkDirectory(sourceA);
		if(!checksourceA) { logger.info(sourceA+" 복사 대상폴더가 존재하지 않습니다."); return;}
		
		File[] listFiles = sourceA.listFiles();

		for (File file : listFiles) {
			File temp = new File(targetA.getAbsoluteFile()+File.separator+file.getName());
			if(file.isDirectory()){
				copy(file,temp);
			}else {
				try {
					fis = new FileInputStream(file);
					fos = new FileOutputStream(temp);
					
					byte[] b = new byte[4096];
					int count = 0 ;
					
					while((count = fis.read(b)) != -1) {
						fos.write(b,0,count);
					}
					logger.info("파일 복제성공!");
				}catch(Exception e) {
					logger.info("복제 실패");
				}finally {
					try {
						fis.close();
						fos.close();
					}catch(Exception e) {
						logger.error("error",e);
					}
				}
			}
		}
	}
	
	

}
