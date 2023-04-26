package com.furence.recsee.adw.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.furence.recsee.adw.model.CsvInfo;

public class CsvFactory {
	
	public static final String extension = ".csv";
	
	public static final String path = "C:/adw/";
	
	
	public static File writeCSV(List<CsvInfo> info,String fileName,String filePath) throws Exception {
		File folder = new File(path);
		if(!folder.exists()) {
			folder.mkdirs();
		}
		if(info == null) {
			throw new Exception("");
		}
		File file = new File(filePath+fileName+extension);
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(file, true));
			for (CsvInfo i : info) {
				i.npeCheckData();
				String csvTextExport = i.csvTextExport();
				bw.write(csvTextExport);
				bw.newLine();
			}
		}catch (Exception e) {
			// TODO: handle exception
		}finally {
			try {
				if(bw!=null) {
					bw.flush();
					bw.close();
				}
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return file;
	}
	

}
