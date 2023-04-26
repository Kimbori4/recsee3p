package com.furence.recsee.monitoring.model;

import java.util.Arrays;
import java.util.List;

import org.hyperic.sigar.SigarException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.furence.recsee.common.model.SystemInfo;

public class SystemMonitoringProc {

	@SuppressWarnings("unchecked")
	public JSONObject systemMonitoringProc(List<SystemInfo> systemInfoList) throws SigarException {
		JSONObject realTimeResult = new JSONObject();
		//String realTimeResult = "";

		SystemInfo systemInfoItem = systemInfoList.get(0);

        String cpuResult = null;
        Object[] memoryResult = null;
        String[] diskList = null;
        String[][] diskResult = new String[][]{};

		if(systemInfoItem.getStoragePath() != null){
			diskList = systemInfoItem.getStoragePath().split(",");
		}

		// cpu load
	    /*try {
	        CpuChecker cpu = new CpuChecker();
	        cpuResult = cpu.getCpuResult();

	        System.out.println("CPU Result : " + cpuResult);
	    } catch (SigarException ex) {
	        ex.printStackTrace();
	    }*/

	    if(cpuResult != null) {
	    	realTimeResult.put("cpu", cpuResult.substring(0, cpuResult.length()-1));
	    } else {
	    	realTimeResult.put("cpu", 0);
	    }

		// 메모리 사용률
		/*try {
			MemoryChecker mc = new MemoryChecker();
			mc.output(null);
			memoryResult = mc.memoryResult;

	        System.out.println("Memory Result : " + Arrays.toString(memoryResult));
		} catch (Exception e) {
			e.printStackTrace();
		}*/

		JSONObject jsonMemory = new JSONObject();
		if(memoryResult != null) {
			jsonMemory.put("total", Integer.parseInt(memoryResult[0].toString()));
			jsonMemory.put("use", Integer.parseInt(memoryResult[1].toString()));
			jsonMemory.put("use_percent", Double.parseDouble(memoryResult[2].toString()));
		} else {
			jsonMemory.put("total", 0);
			jsonMemory.put("use", 0);
			jsonMemory.put("use_percent", 0);
		}
		realTimeResult.put("memory",  jsonMemory);

		JSONArray jsonHddList = new JSONArray();
		if(diskList!=null && diskList.length > 0) {
//		    try {
//			    DiskChecker curDf = new DiskChecker();
//			    curDf.output(diskList);
//			    diskResult = curDf.diskResult;
//
//			    for(int i=0;i<diskResult.length;i++) {
//			    	System.out.println("disk Result display : " + Arrays.toString(diskResult[i]));
//			    }
//		    } catch (Exception e) {
//		    	e.printStackTrace();
//		    }
		    /*
		    try {
				Iostat ioStat = new Iostat();

			    for(int i=0;i<diskResult.length;i++) {
			    	System.out.println("disk display : " + diskList[i]);
			    	ioStat.outputDisk(diskList[i].toUpperCase());
			    	if(ioStat.ioResult[0] != null) diskResult[i][5] = ioStat.ioResult[0];

			    }
		    } catch(SigarException se) {
		    	se.printStackTrace();
		    }
			*/

		    for(int i=0;i<diskResult.length;i++) {
		    	JSONObject jsonHdd = new JSONObject();
		    	if(diskResult[i] != null) {
					jsonHdd.put("path", diskResult[i][0].substring(0,diskResult[i][0].length()-1));
					jsonHdd.put("total", diskResult[i][1]);
					jsonHdd.put("use", diskResult[i][2]);
					jsonHdd.put("use_percent", diskResult[i][4].substring(0, diskResult[i][4].length()-1));
		    	} else {
					jsonHdd.put("path", "");
					jsonHdd.put("total", "0");
					jsonHdd.put("use", "0");
					jsonHdd.put("use_percent", 0.0);
					
		    	}
		    	jsonHddList.add(jsonHdd);
		    }
		}
		realTimeResult.put("hdd", jsonHddList);

		return realTimeResult;
	}
}
