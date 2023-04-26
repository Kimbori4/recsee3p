package com.furence.recsee.wooribank.facerecording.util;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.furence.recsee.common.util.TTSUtil;
import com.furence.recsee.scriptRegistration.model.scriptProductValueInfo;
import com.furence.recsee.wooribank.facerecording.controller.AjaxFaceRecordingController;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class RemunRtTypeChange {
	
	private static final Logger logger = LoggerFactory.getLogger(RemunRtTypeChange.class);
	
	public final String  totPeeRtName = "TOT_PFE_RT";
	public final String  sumPeeRtName = "SUM_PFE_RT";
	
	private String type;
	
	private String value;
	
	private String totPeeRt;
	
	private String sumPeeRt;
	
	
	
	public void createRemnunValue() throws Exception {
		switch (this.type) {
		case "1":
			this.value = "총보수는 "+TTSUtil.parsingValueCheck(totPeeRtName, totPeeRt) +" 입니다";
			logger.info(this.value);
			break;
		case "2":
			this.value = "합성총보수는 "+TTSUtil.parsingValueCheck(sumPeeRtName, sumPeeRt) +" 입니다";
			logger.info(this.value);
			break;
		}
	}
	
	public List<scriptProductValueInfo> replaceRemunRtValue(List<scriptProductValueInfo> valueList){
		for (scriptProductValueInfo valueInfo : valueList) {
			if(valueInfo.getRsProductValueCode().equals("REMUN_RT_TYPE")) {
				valueInfo.setRsProductValueVal(this.value);
				logger.info("REMUN_RT_TYPE value change Complete");
			}
		}
		return valueList;
	}


	
	public void setTotPeeRtAndSumPeeRt(List<scriptProductValueInfo> valueList) throws Exception {
		for (scriptProductValueInfo valueInfo : valueList) {
			if(totPeeRtName.equals(valueInfo.getRsProductValueCode())) {
				this.totPeeRt = valueInfo.getRsProductValueVal();
			}
			
			if(sumPeeRtName.equals(valueInfo.getRsProductValueCode())) {
				this.sumPeeRt = valueInfo.getRsProductValueVal();
			}
		}
	}
	
}
