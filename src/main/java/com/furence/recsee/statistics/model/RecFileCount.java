package com.furence.recsee.statistics.model;

import java.io.File;

public class RecFileCount {
	static Integer totalFiles = 0;

	public RecFileCount(String[] args) throws Exception {
		if(args == null || args.length != 1){
			totalFiles = 0;
		} else {
			String tempFile = getValidPath(args[0]);
			File dir = new File(tempFile);
			if(!dir.exists() || !dir.isDirectory()){
				totalFiles = 0;
			} else {
				printFileList(dir);
			}
		}
	}

	public static void printFileList(File dir) {
		File[] files = dir.listFiles();
		Integer subDir = 0;
		Integer etcFile = 0;

		for(int i = 0; i < files.length; i++){
			if(files[i].isDirectory()){
				subDir++;
			}
			if(files[i].getName().matches(".*(RX\\.mp3|TX\\.mp3|\\.pcm)$")) etcFile++;
			//files[i].getUsableSpace();
		}
		int fileNum = files.length - subDir - etcFile;

		totalFiles += fileNum;
	}
	public Integer getTotalFiles() {
		return totalFiles;
	}
	public void initTotalFiles() {
		totalFiles = 0;
	}
	
	public static String getValidPath(String path) throws Exception{
		
		if(path == null || "".equals(path)) {
			throw new Exception("path is null");
		}
		
			if(path.indexOf("..") > -1) {
				throw new Exception("path contains ..");
			}
			if(path.indexOf("..") > -1) {
				throw new Exception("path contains // : ");
			}
		
		return path;
		
	}
}
