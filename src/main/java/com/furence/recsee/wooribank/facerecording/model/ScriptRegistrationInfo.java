package com.furence.recsee.wooribank.facerecording.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.json.simple.JSONObject;

public class ScriptRegistrationInfo {

	private String taReason;
	//트리
	private Integer rsScriptStepFk;
	private Integer tDepth;
	private Integer tKey;
	private String tName;
	private String tOrder;
	private String rScriptRecState;
	private String rScriptTaState;
	private Integer tParent;
	private String rScriptFilePath;
	private String rScriptTtsServerIp;
	private String rSccriptStepCallKey;
	
	//검색조건 
	private String rSearchWord;
	private String rSearchType;

	//상품녹취 키값 
	private String rScriptCallKey; //수정
	
	//TTS파일명
	private String rScriptTTSFileName;
	//TTS파일경로
	private String rScriptTTSFilePath;
	private String rScriptTTSHistoryFilePath;
	
	
	private String cpClaseeYn; //cp클래스 유무
	private String cude; //통화코드
	private String prdNm; //상품명
	private String regAM; //가입금액
	private String advpeNm; //판매직원명 = 권유자명
	private String prdRiskGd; //판매사 위험등급
	private String pocpTrtryRt; //POCP_TRTPY_RT  = 선취
	private String dfanTrtpyRt;   //DFAN_TRTPY_RT = 후취
	private String totRfeRt;//TOT_PFE_RT = 총보수
	private String lattAplTm;//LATT_APL_TM  = 환매LATE트레이딩 적용시각
	private String fndbpAplDcnt1;//FNDBP_APL_DCNT_1 = 기준가 적용일수
	private String fndbpAplDcnt2; //  LT기준가 적용일수
	private String agnpeNm; //대리인명
	private String cusNm; //고객명
	private String scriptType;
	private boolean rectryFlag;
	
	private String orderBy;		// 정렬 컬럼
	private String direction;	// 정렬 방향
	
	
	private List<String> rScriptDetailTextList = new ArrayList<String>();
	
	
	
	// productValue

		public List<String> getrScriptDetailTextList() {
		return rScriptDetailTextList;
	}
	public void setrScriptDetailTextList(List<String> rScriptDetailTextList) {
		this.rScriptDetailTextList = rScriptDetailTextList;
	}
		public String getScriptType() {
		return scriptType;
	}
	public void setScriptType(String scriptType) {
		this.scriptType = scriptType;
	}
		private String rsProductType;
		private String rsProductCode;
		private String rsProductValueName;
		private String rsProductValueCode;
		private String rsProductValueVal;
		private String rsProductValueRealtimeTTS;
		
		private String ad047;
		
		private String clearRecYn;
		
		public String getClearRecYn() {
			return clearRecYn;
		}
		public void setClearRecYn(String clearRecYn) {
			this.clearRecYn = clearRecYn;
		}
		public String getAd047() {
			return ad047;
		}
		public void setAd047(String ad047) {
			this.ad047 = ad047;
		}
		private String rsProductValuePk;
		public String getRsProductValuePk() {
			return rsProductValuePk;
		}
		public void setRsProductValuePk(String rsProductValuePk) {
			this.rsProductValuePk = rsProductValuePk;
		}
		public String getRsProductType() {
			return rsProductType;
		}
		public void setRsProductType(String rsProductType) {
			this.rsProductType = rsProductType;
		}
		public String getRsProductCode() {
			return rsProductCode;
		}
		public void setRsProductCode(String rsProductCode) {
			this.rsProductCode = rsProductCode;
		}
		public String getRsProductValueName() {
			return rsProductValueName;
		}
		public void setRsProductValueName(String rsProductValueName) {
			this.rsProductValueName = rsProductValueName;
		}
		public String getRsProductValueCode() {
			return rsProductValueCode;
		}
		public void setRsProductValueCode(String rsProductValueCode) {
			this.rsProductValueCode = rsProductValueCode;
		}
		public String getRsProductValueVal() {
			return rsProductValueVal;
		}
		public void setRsProductValueVal(String rsProductValueVal) {
			this.rsProductValueVal = rsProductValueVal;
		}
		public String getRsProductValueRealtimeTTS() {
			return rsProductValueRealtimeTTS;
		}
		public void setRsProductValueRealtimeTTS(String rsProductValueRealtimeTTS) {
			this.rsProductValueRealtimeTTS = rsProductValueRealtimeTTS;
		}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public String getPrdNm() {
		return prdNm;
	}
	public void setPrdNm(String prdNm) {
		this.prdNm = prdNm;
	}
	public String getAgnpeNm() {
		return agnpeNm;
	}
	public void setAgnpeNm(String agnpeNm) {
		this.agnpeNm = agnpeNm;
	}
	public String getCusNm() {
		return cusNm;
	}
	public void setCusNm(String cusNm) {
		this.cusNm = cusNm;
	}
	public String getCude() {
		return cude;
	}
	public void setCude(String cude) {
		this.cude = cude;
	}
	public String getRegAM() {
		return regAM;
	}
	public void setRegAM(String regAM) {
		this.regAM = regAM;
	}
	public String getAdvpeNm() {
		return advpeNm;
	}
	public void setAdvpeNm(String advpeNm) {
		this.advpeNm = advpeNm;
	}
	public String getPrdRiskGd() {
		return prdRiskGd;
	}
	public void setPrdRiskGd(String prdRiskGd) {
		this.prdRiskGd = prdRiskGd;
	}
	public String getPocpTrtryRt() {
		return pocpTrtryRt;
	}
	public void setPocpTrtryRt(String pocpTrtryRt) {
		this.pocpTrtryRt = pocpTrtryRt;
	}
	public String getDfanTrtpyRt() {
		return dfanTrtpyRt;
	}
	public void setDfanTrtpyRt(String dfanTrtpyRt) {
		this.dfanTrtpyRt = dfanTrtpyRt;
	}
	public String getTotRfeRt() {
		return totRfeRt;
	}
	public void setTotRfeRt(String totRfeRt) {
		this.totRfeRt = totRfeRt;
	}
	public String getLattAplTm() {
		return lattAplTm;
	}
	public void setLattAplTm(String lattAplTm) {
		this.lattAplTm = lattAplTm;
	}
	public String getFndbpAplDcnt1() {
		return fndbpAplDcnt1;
	}
	public void setFndbpAplDcnt1(String fndbpAplDcnt1) {
		this.fndbpAplDcnt1 = fndbpAplDcnt1;
	}
	public String getFndbpAplDcnt2() {
		return fndbpAplDcnt2;
	}
	public void setFndbpAplDcnt2(String fndbpAplDcnt2) {
		this.fndbpAplDcnt2 = fndbpAplDcnt2;
	}
	public String getrScriptTTSHistoryFilePath() {
		return rScriptTTSHistoryFilePath;
	}
	public void setrScriptTTSHistoryFilePath(String rScriptTTSHistoryFilePath) {
		this.rScriptTTSHistoryFilePath = rScriptTTSHistoryFilePath;
	}
	public String getrSccriptStepCallKey() {
		return rSccriptStepCallKey;
	}
	public void setrSccriptStepCallKey(String rSccriptStepCallKey) {
		this.rSccriptStepCallKey = rSccriptStepCallKey;
	}
	public String getrScriptFilePath() {
		return rScriptFilePath;
	}
	public void setrScriptFilePath(String rScriptFilePath) {
		this.rScriptFilePath = rScriptFilePath;
	}
	public String getrScriptTtsServerIp() {
		return rScriptTtsServerIp;
	}
	public void setrScriptTtsServerIp(String rScriptTtsServerIp) {
		this.rScriptTtsServerIp = rScriptTtsServerIp;
	}
	public String getrScriptTTSFileName() {
		return rScriptTTSFileName;
	}
	public void setrScriptTTSFileName(String rScriptTTSFileName) {
		this.rScriptTTSFileName = rScriptTTSFileName;
	}
	public String getrScriptTTSFilePath() {
		return rScriptTTSFilePath;
	}
	public void setrScriptTTSFilePath(String rScriptTTSFilePath) {
		this.rScriptTTSFilePath = rScriptTTSFilePath;
	}
	public String getrScriptRecState() {
		return rScriptRecState;
	}
	public void setrScriptRecState(String rScriptRecState) {
		this.rScriptRecState = rScriptRecState;
	}
	public String getrScriptTaState() {
		return rScriptTaState;
	}
	public void setrScriptTaState(String rScriptTaState) {
		this.rScriptTaState = rScriptTaState;
	}
	public String getrScriptCallKey() {
		return rScriptCallKey;
	}
	public void setrScriptCallKey(String rScriptCallKey) {
		this.rScriptCallKey = rScriptCallKey;
	}
	public String getrSearchWord() {
		return rSearchWord;
	}
	public void setrSearchWord(String rSearchWord) {
		this.rSearchWord = rSearchWord;
	}
	public String getrSearchType() {
		return rSearchType;
	}
	public void setrSearchType(String rSearchType) {
		this.rSearchType = rSearchType;
	}
	public String gettOrder() {
		return tOrder;
	}
	public void settOrder(String tOrder) {
		this.tOrder = tOrder;
	}
	//product
	private Integer rProductListPk;
	private Integer rProductListFk;
	private String rProductType;
	private String rProductCode;
	private String rProductName;
	private String	rUseYn;
	private String	rScriptCommon;
	private String	rGroupCode;
	private String rProductGroupCode;
	private String rProductGroupYn;
	private String rSysDisType;
	
	//tree
	private String	rScriptStepPk;
	private Integer	rScriptStepFk;
	private Integer	rScriptStepParent;
	private String	rScriptStepName;
	private Integer	rScriptStepOrder;
	private String	rScriptStepType;
	private String	rGroupYn;


	
	//detail
	private Integer rScriptDetailPk;
	private String rScriptDetailType;
	private String rScriptDetailCreateUser;
	private String rScriptDetailComKind;
	private String rScriptDetailText;
	private String rScriptDetailIfCase;
	private String rScriptDetailIfCaseDetail;
	private String rScriptDetailRealtimeTTS;
	private String rScriptDetailCreateDate;
	private String rScriptDetailUpdateUser;
	private String rScriptDetailConfirm;
	private String rScriptDetailConfirmDate;
	private String rScriptDetailConfirmUser;
	private String rScriptDetailConfirmOrder;
	private String rScriptDetailConfirmComFk;
	private String rScriptDetailUdateDate;
	private String rScriptDetailReservDate;
	private String rScriptCommonConfirm;
	private String rScriptCommonType;
	private String rScriptCommonPk;
	private Integer common;
	private Integer rScriptDetailEltCase;
	private String rProductAttributesExt;
	private String rProductAttributes;
	private String rScriptStepDetailCaseAttributes;
	
	
	
	//common
	private Integer rsScriptCommonPk;
	private String rsScriptCommonType;
	private String rsScriptCommonName;
	private String rsScriptCommonDesc;
	private String rsScriptCommonText;
	private String rsScriptCommonRealTimeTts;
	private String rsScriptCommonCreateDate;
	private String rsScriptCommonCreateUser;
	private String rsScriptCommonUpdateDate;
	private String rsScriptCommonUpdateUser;
	private String rsScriptCommonConfirm;
	private String rsScriptCommonConfirmdate;
	private String rsScriptCommonConfirmUser;
	private String rsScriptCommonReservDate;
	private String rsScriptCommonReservUser;
	public String getRsScriptCommonReservUser() {
		return rsScriptCommonReservUser;
	}
	public void setRsScriptCommonReservUser(String rsScriptCommonReservUser) {
		this.rsScriptCommonReservUser = rsScriptCommonReservUser;
	}
	private String rsUseYn;
	
	//productGroupList
	private String rProductListGroupPk;
	private String rProductListGroupCode;
	private String rProductListType;
	private String rProductListName;
	private String rProductListIFName;

	//productValue
	private int rProductValuePk;
	private String rProductValueName;
	private String rProductValueCode;
	private String rProductValueVal;
	private String rProductValueRealtimeTTS;
	
	//productReserve
	private int rProductReservePk;
	private String rProductReserveCode;
	private String rProductReserveType;
	private String rProductReserveDate;
	private String rUpdateYn;
	
	
	
	private Integer moreProductType;
	
	
	
	
	
	public Integer getMoreProductType() {
		return moreProductType;
	}
	public void setMoreProductType(Integer moreProductType) {
		this.moreProductType = moreProductType;
	}
	public String getrSysDisType() {
		return rSysDisType;
	}
	public void setrSysDisType(String rSysDisType) {
		this.rSysDisType = rSysDisType;
	}
	public int getrProductReservePk() {
		return rProductReservePk;
	}
	public void setrProductReservePk(int rProductReservePk) {
		this.rProductReservePk = rProductReservePk;
	}
	public String getrProductReserveCode() {
		return rProductReserveCode;
	}
	public void setrProductReserveCode(String rProductReserveCode) {
		this.rProductReserveCode = rProductReserveCode;
	}
	public String getrProductReserveType() {
		return rProductReserveType;
	}
	public void setrProductReserveType(String rProductReserveType) {
		this.rProductReserveType = rProductReserveType;
	}
	public String getrProductReserveDate() {
		return rProductReserveDate;
	}
	public void setrProductReserveDate(String rProductReserveDate) {
		this.rProductReserveDate = rProductReserveDate;
	}
	public String getRUpdateYn() {
		return rUpdateYn;
	}
	public void setRUpdateYn(String rsUpdateYn) {
		this.rUpdateYn = rsUpdateYn;
	}
	public int getrProductValuePk() {
		return rProductValuePk;
	}
	public void setrProductValuePk(int rProductValuePk) {
		this.rProductValuePk = rProductValuePk;
	}
	public String getrProductValueName() {
		return rProductValueName;
	}
	public void setrProductValueName(String rProductValueName) {
		this.rProductValueName = rProductValueName;
	}
	public String getrProductValueCode() {
		return rProductValueCode;
	}
	public void setrProductValueCode(String rProductValueCode) {
		this.rProductValueCode = rProductValueCode;
	}
	public String getrProductValueVal() {
		return rProductValueVal;
	}
	public void setrProductValueVal(String rProductValueVal) {
		this.rProductValueVal = rProductValueVal;
	}
	public String getrProductValueRealtimeTTS() {
		return rProductValueRealtimeTTS;
	}
	public void setrProductValueRealtimeTTS(String rProductValueRealtimeTTS) {
		this.rProductValueRealtimeTTS = rProductValueRealtimeTTS;
	}
	//product
	public Integer getrProductListPk() {
		return rProductListPk;
	}
	public void setrProductListPk(Integer rProductListPk) {
		this.rProductListPk = rProductListPk;
	}
	public String getrProductType() {
		return rProductType;
	}
	public void setrProductType(String rProductType) {
		this.rProductType = rProductType;
	}
	public String getrProductCode() {
		return rProductCode;
	}
	public void setrProductCode(String rProductCode) {
		this.rProductCode = rProductCode;
	}
	public String getrProductName() {
		return rProductName;
	}
	public void setrProductName(String rProductName) {
		this.rProductName = rProductName;
	}
	public String getrProductGroupCode() {
		return rProductGroupCode;
	}
	public void setrProductGroupCode(String rProductGroupCode) {
		this.rProductGroupCode = rProductGroupCode;
	}
	public String getrProductGroupYn() {
		return rProductGroupYn;
	}
	public void setrProductGroupYn(String rProductGroupYn) {
		this.rProductGroupYn = rProductGroupYn;
	}
	//tree
	public String getrScriptStepPk() {
		return rScriptStepPk;
	}
	public void setrScriptStepPk(String rScriptStepPk) {
		this.rScriptStepPk = rScriptStepPk;
	}
	public Integer getrScriptStepFk() {
		return rScriptStepFk;
	}
	public void setrScriptStepFk(Integer rScriptStepFk) {
		this.rScriptStepFk = rScriptStepFk;
	}
	public Integer getrScriptStepParent() {
		return rScriptStepParent;
	}
	public void setrScriptStepParent(Integer rScriptStepParent) {
		this.rScriptStepParent = rScriptStepParent;
	}
	public String getrScriptStepName() {
		return rScriptStepName;
	}
	public void setrScriptStepName(String rScriptStepName) {
		this.rScriptStepName = rScriptStepName;
	}
	public Integer getrScriptStepOrder() {
		return rScriptStepOrder;
	}
	public void setrScriptStepOrder(Integer rScriptStepOrder) {
		this.rScriptStepOrder = rScriptStepOrder;
	}
	public String getrScriptStepType() {
		return rScriptStepType;
	}
	public void setrScriptStepType(String rScriptStepType) {
		this.rScriptStepType = rScriptStepType;
	}
	public String getrUseYn() {
		return rUseYn;
	}
	public void setrUseYn(String rUseYn) {
		this.rUseYn = rUseYn;
	}
	
	
	public Integer getRsScriptStepFk() {
		return rsScriptStepFk;
	}
	public void setRsScriptStepFk(Integer rsScriptStepFk) {
		this.rsScriptStepFk = rsScriptStepFk;
	}
	public Integer gettDepth() {
		return tDepth;
	}
	public void settDepth(Integer tDepth) {
		this.tDepth = tDepth;
	}
	public Integer gettKey() {
		return tKey;
	}
	public void settKey(Integer tKey) {
		this.tKey = tKey;
	}
	public Integer gettParent() {
		return tParent;
	}
	public void tParent(Integer tParent) {
		this.tParent = tParent;
	}
	public String gettName() {
		return tName;
	}
	public void settName(String tName) {
		this.tName = tName;
	}
	
	
	//detail 
	
	public Integer getrScriptDetailPk() {
		return rScriptDetailPk;
	}
	public void setrScriptDetailPk(Integer rScriptDetailPk) {
		this.rScriptDetailPk = rScriptDetailPk;
	}
	public String getrScriptDetailType() {
		return rScriptDetailType;
	}
	public void setrScriptDetailType(String rScriptDetailType) {
		this.rScriptDetailType = rScriptDetailType;
	}
	public String getrScriptDetailComKind() {
		return rScriptDetailComKind;
	}
	public void setrScriptDetailComKind(String rScriptDetailComKind) {
		this.rScriptDetailComKind = rScriptDetailComKind;
	}
	public String getrScriptDetailText() {
		return rScriptDetailText;
	}
	public void setrScriptDetailText(String rScriptDetailText) {
		this.rScriptDetailText = rScriptDetailText;
	}
	public String getrScriptDetailIfCase() {
		return rScriptDetailIfCase;
	}
	public void setrScriptDetailIfCase(String rScriptDetailIfCase) {
		this.rScriptDetailIfCase = rScriptDetailIfCase;
	}
	public String getrScriptDetailIfCaseDetail() {
		return rScriptDetailIfCaseDetail;
	}
	public void setrScriptDetailIfCaseDetail(String rScriptDetailIfCaseDetail) {
		this.rScriptDetailIfCaseDetail = rScriptDetailIfCaseDetail;
	}
	public String getrScriptDetailRealtimeTTS() {
		return rScriptDetailRealtimeTTS;
	}
	public void setrScriptDetailRealtimeTTS(String rScriptDetailRealtimeTTS) {
		this.rScriptDetailRealtimeTTS = rScriptDetailRealtimeTTS;
	}
	public String getrScriptDetailCreateDate() {
		return rScriptDetailCreateDate;
	}
	public void setrScriptDetailCreateDate(String rScriptDetailCreateDate) {
		this.rScriptDetailCreateDate = rScriptDetailCreateDate;
	}
	public String getrScriptDetailUpdateUser() {
		return rScriptDetailUpdateUser;
	}
	public void setrScriptDetailUpdateUser(String rScriptDetailUpdateUser) {
		this.rScriptDetailUpdateUser = rScriptDetailUpdateUser;
	}
	public String getrScriptDetailConfirm() {
		return rScriptDetailConfirm;
	}
	public void setrScriptDetailConfirm(String rScriptDetailConfirm) {
		this.rScriptDetailConfirm = rScriptDetailConfirm;
	}
	public String getrScriptDetailConfirmDate() {
		return rScriptDetailConfirmDate;
	}
	public void setrScriptDetailConfirmDate(String rScriptDetailConfirmDate) {
		this.rScriptDetailConfirmDate = rScriptDetailConfirmDate;
	}
	public String getrScriptDetailConfirmUser() {
		return rScriptDetailConfirmUser;
	}
	public void setrScriptDetailConfirmUser(String rScriptDetailConfirmUser) {
		this.rScriptDetailConfirmUser = rScriptDetailConfirmUser;
	}
	public String getrScriptDetailConfirmOrder() {
		return rScriptDetailConfirmOrder;
	}
	public void setrScriptDetailConfirmOrder(String rScriptDetailConfirmOrder) {
		this.rScriptDetailConfirmOrder = rScriptDetailConfirmOrder;
	}
	public String getrScriptDetailConfirmComFk() {
		return rScriptDetailConfirmComFk;
	}
	public void setrScriptDetailConfirmComFk(String rScriptDetailConfirmComFk) {
		this.rScriptDetailConfirmComFk = rScriptDetailConfirmComFk;
	}
	public String getrScriptDetailCreateUser() {
		return rScriptDetailCreateUser;
	}
	public void setrScriptDetailCreateUser(String rScriptDetailCreateUser) {
		this.rScriptDetailCreateUser = rScriptDetailCreateUser;
	}
	public String getrScriptDetailUdateDate() {
		return rScriptDetailUdateDate;
	}
	public void setrScriptDetailUdateDate(String rScriptDetailUdateDate) {
		this.rScriptDetailUdateDate = rScriptDetailUdateDate;
	}
	public String getrScriptDetailReservDate() {
		return rScriptDetailReservDate;
	}
	public void setrScriptDetailReservDate(String rScriptDetailReservDate) {
		this.rScriptDetailReservDate = rScriptDetailReservDate;
	}
	public String getRsScriptCommonType() {
		return rsScriptCommonType;
	}
	public Integer getRsScriptCommonPk() {
		return rsScriptCommonPk;
	}
	public void setRsScriptCommonPk(Integer rsScriptCommonPk) {
		this.rsScriptCommonPk = rsScriptCommonPk;
	}
	public void setRsScriptCommonType(String rsScriptCommonType) {
		this.rsScriptCommonType = rsScriptCommonType;
	}
	public String getRsScriptCommonName() {
		return rsScriptCommonName;
	}
	public void setRsScriptCommonName(String rsScriptCommonName) {
		this.rsScriptCommonName = rsScriptCommonName;
	}
	public String getRsScriptCommonDesc() {
		return rsScriptCommonDesc;
	}
	public void setRsScriptCommonDesc(String rsScriptCommonDesc) {
		this.rsScriptCommonDesc = rsScriptCommonDesc;
	}
	public String getRsScriptCommonText() {
		return rsScriptCommonText;
	}
	public void setRsScriptCommonText(String rsScriptCommonText) {
		this.rsScriptCommonText = rsScriptCommonText;
	}
	public String getRsScriptCommonRealTimeTts() {
		return rsScriptCommonRealTimeTts;
	}
	public void setRsScriptCommonRealTimeTts(String rsScriptCommonRealTimeTts) {
		this.rsScriptCommonRealTimeTts = rsScriptCommonRealTimeTts;
	}
	public String getRsScriptCommonCreateDate() {
		return rsScriptCommonCreateDate;
	}
	public void setRsScriptCommonCreateDate(String rsScriptCommonCreateDate) {
		this.rsScriptCommonCreateDate = rsScriptCommonCreateDate;
	}
	public String getRsScriptCommonCreateUser() {
		return rsScriptCommonCreateUser;
	}
	public void setRsScriptCommonCreateUser(String rsScriptCommonCreateUser) {
		this.rsScriptCommonCreateUser = rsScriptCommonCreateUser;
	}
	public String getRsScriptCommonUpdateDate() {
		return rsScriptCommonUpdateDate;
	}
	public void setRsScriptCommonUpdateDate(String rsScriptCommonUpdateDate) {
		this.rsScriptCommonUpdateDate = rsScriptCommonUpdateDate;
	}
	public String getRsScriptCommonUpdateUser() {
		return rsScriptCommonUpdateUser;
	}
	public void setRsScriptCommonUpdateUser(String rsScriptCommonUpdateUser) {
		this.rsScriptCommonUpdateUser = rsScriptCommonUpdateUser;
	}
	public String getRsScriptCommonConfirm() {
		return rsScriptCommonConfirm;
	}
	public void setRsScriptCommonConfirm(String rsScriptCommonConfirm) {
		this.rsScriptCommonConfirm = rsScriptCommonConfirm;
	}
	public String getRsScriptCommonConfirmUser() {
		return rsScriptCommonConfirmUser;
	}
	public void setRsScriptCommonConfirmUser(String rsScriptCommonConfirmUser) {
		this.rsScriptCommonConfirmUser = rsScriptCommonConfirmUser;
	}

	public String getRsScriptCommonReservDate() {
		return rsScriptCommonReservDate;
	}
	public void setRsScriptCommonReservDate(String rsScriptCommonReservDate) {
		this.rsScriptCommonReservDate = rsScriptCommonReservDate;
	}
	public String getRsUseYn() {
		return rsUseYn;
	}
	public void setRsUseYn(String rsUseYn) {
		this.rsUseYn = rsUseYn;
	}
	public String getRsScriptCommonConfirmdate() {
		return rsScriptCommonConfirmdate;
	}
	public void setRsScriptCommonConfirmdate(String rsScriptCommonConfirmdate) {
		this.rsScriptCommonConfirmdate = rsScriptCommonConfirmdate;
	}
	public String getrScriptCommon() {
		return rScriptCommon;
	}
	public void setrScriptCommon(String rScriptCommon) {
		this.rScriptCommon = rScriptCommon;
	}
	public String getrGroupYn() {
		return rGroupYn;
	}
	public void setrGroupYn(String rGroupYn) {
		this.rGroupYn = rGroupYn;
	}
	public String getrScriptCommonConfirm() {
		return rScriptCommonConfirm;
	}
	public void setrScriptCommonConfirm(String rScriptCommonConfirm) {
		this.rScriptCommonConfirm = rScriptCommonConfirm;
	}
	public String getrScriptCommonType() {
		return rScriptCommonType;
	}
	public void setrScriptCommonType(String rScriptCommonType) {
		this.rScriptCommonType = rScriptCommonType;
	}
	public String getrScriptCommonPk() {
		return rScriptCommonPk;
	}
	public void setrScriptCommonPk(String rScriptCommonPk) {
		this.rScriptCommonPk = rScriptCommonPk;
	}
	public Integer getCommon() {
		return common;
	}
	public void setCommon(Integer common) {
		this.common = common;
	}
	
	//product list group
	public String getrProductListGroupPk() {
		return rProductListGroupPk;
	}
	public void setrProductListGroupPk(String rProductListGroupPk) {
		this.rProductListGroupPk = rProductListGroupPk;
	}
	public String getrProductListGroupCode() {
		return rProductListGroupCode;
	}
	public void setrProductListGroupCode(String rProductListGroupCode) {
		this.rProductListGroupCode = rProductListGroupCode;
	}
	public String getrProductListType() {
		return rProductListType;
	}
	public void setrProductListType(String rProductListType) {
		this.rProductListType = rProductListType;
	}
	public String getrProductListName() {
		return rProductListName;
	}
	public void setrProductListName(String rProductListName) {
		this.rProductListName = rProductListName;
	}
	public String getrProductListIFName() {
		return rProductListIFName;
	}
	public void setrProductListIFName(String rProductListIFName) {
		this.rProductListIFName = rProductListIFName;
	}
	public void settParent(Integer tParent) {
		this.tParent = tParent;
	}
	public Integer getrProductListFk() {
		return rProductListFk;
	}
	public void setrProductListFk(Integer rProductListFk) {
		this.rProductListFk = rProductListFk;
	}
	public String getrGroupCode() {
		return rGroupCode;
	}
	public void setrGroupCode(String rGroupCode) {
		this.rGroupCode = rGroupCode;
	}
	public String getOrderBy() {
		return orderBy;
	}
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
	public String getDirection() {
		return direction;
	}
	public void setDirection(String direction) {
		this.direction = direction;
	}
	public String getCpClaseeYn() {
		return cpClaseeYn;
	}
	public void setCpClaseeYn(String cpClaseeYn) {
		this.cpClaseeYn = cpClaseeYn;
	}
	public String getrUpdateYn() {
		return rUpdateYn;
	}
	public void setrUpdateYn(String rUpdateYn) {
		this.rUpdateYn = rUpdateYn;
	}
	public boolean isRectryFlag() {
		return rectryFlag;
	}
	public void setRectryFlag(boolean rectryFlag) {
		this.rectryFlag = rectryFlag;
	}
	public Integer getrScriptDetailEltCase() {
		return rScriptDetailEltCase;
	}
	public void setrScriptDetailEltCase(Integer rScriptDetailEltCase) {
		this.rScriptDetailEltCase = rScriptDetailEltCase;
	}
	public String getrProductAttributesExt() {
		return rProductAttributesExt;
	}
	public void setrProductAttributesExt(String rProductAttributesExt) {
		this.rProductAttributesExt = rProductAttributesExt;
	}
	public String getrProductAttributes() {
		return rProductAttributes;
	}
	public void setrProductAttributes(String rProductAttributes) {
		this.rProductAttributes = rProductAttributes;
	}
	public String getrScriptStepDetailCaseAttributes() {
		return rScriptStepDetailCaseAttributes;
	}
	public void setrScriptStepDetailCaseAttributes(String rScriptStepDetailCaseAttributes) {
		this.rScriptStepDetailCaseAttributes = rScriptStepDetailCaseAttributes;
	}

	
	
	public String getTaReason() {
		return taReason;
	}
	public void setTaReason(String taReason) {
		this.taReason = taReason;
	}
	public void createMoreProductScriptDetailOne() {
		this.rScriptDetailPk = 300000+new Random().nextInt(999999);
		this.rScriptDetailType = "S";
		this.rScriptDetailComKind = "N";
		this.rScriptDetailRealtimeTTS = "N";
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("isTextRed", true);
		this.rScriptStepDetailCaseAttributes = jsonObj.toJSONString();
		this.rScriptDetailIfCase = "N";
		this.rScriptDetailText = "★ 직원조작안내 ★ \n개별상품 녹취가 끝났습니다. 직원분은 「다음」 버튼을 눌러 다음상품 녹취를 진행하시기바랍니다.";
	}
	
	
}
