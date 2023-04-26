package com.furence.recsee.monitoring.model;

import org.hyperic.sigar.*;

public class CpuChecker {

	private String cpuResult = null;

    public CpuChecker() throws SigarException {
        Sigar sigar = new Sigar();
        CpuPerc cpu = sigar.getCpuPerc();
 
        this.cpuResult = CpuPerc.format(cpu.getCombined()).toString();

//        System.out.println("Total cpu : User : " + CpuPerc.format(cpu.getUser()) + ", Sys : " + CpuPerc.format(cpu.getSys()) + ", Idle : " + CpuPerc.format(cpu.getIdle()));

        sigar.close();
    }
    public String getCpuResult() {
    	return this.cpuResult;
    }
}
