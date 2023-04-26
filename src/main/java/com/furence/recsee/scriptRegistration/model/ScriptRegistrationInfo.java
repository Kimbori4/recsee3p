package com.furence.recsee.scriptRegistration.model;

import java.util.Date;

public class ScriptRegistrationInfo {

	// 트리
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

	// 검색조건
	private String rSearchWord;
	private String rSearchType;

	// 상품녹취 키값
	private String rScriptCallKey; // 수정

	// TTS파일명
	private String rScriptTTSFileName;
	// TTS파일경로
	private String rScriptTTSFilePath;
	private String rScriptTTSHistoryFilePath;

	private String cude; // 통화코드
	private String prdNm; // 상품명
	private String regAM; // 가입금액
	private String advpeNm; // 판매직원명 = 권유자명
	private String prdRiskGd; // 판매사 위험등급
	private String pocpTrtryRt; // POCP_TRTPY_RT = 선취
	private String dfanTrtpyRt; // DFAN_TRTPY_RT = 후취
	private String totRfeRt;// TOT_PFE_RT = 총보수
	private String lattAplTm;// LATT_APL_TM = 환매LATE트레이딩 적용시각
	private String fndbpAplDcnt1;// FNDBP_APL_DCNT_1 = 기준가 적용일수
	private String fndbpAplDcnt2; // LT기준가 적용일수
	private String agnpeNm; // 대리인명
	private String cusNm; // 고객명
	private String scriptType;

	private String rsUseYn;

	// productGroupList
	private String rProductListGroupPk;
	private String rProductListGroupCode;
	private String rProductListType;
	private String rProductListName;
	private String rProductListIFName;

	// productValue
	private int rProductValuePk;
	private String rProductValueName;
	private String rProductValueCode;
	private String rProductValueVal;
	private String rProductValueRealtimeTTS;

	// productReserve
	private int rProductReservePk;
	private String rProductReserveCode;
	private String rProductReserveType;
	private String rProductReserveDate;
	private String rUpdateYn;

	private String rsProductType;
	private String rsProductCode;
	private String rsProductValueName;
	private String rsProductValueCode;
	private String rsProductValueVal;
	private String rsProductValueRealtimeTTS;

	private String ad047;

	private String clearRecYn;

	// product
	private Integer rProductListPk;
	private Integer rProductListFk;
	private String rProductType;
	private String rProductTypeName;
	private String rProductCode;
	private String rProductName;
	private String rUseYn;
	private String rScriptCommon;
	private String rGroupCode;
	private String rProductGroupCode;
	private String rProductGroupYn;
	private String rSysDisType;

	// tree
	private String rScriptStepPk;
	private Integer rScriptStepFk;
	private Integer rScriptStepParent;
	private String rScriptStepName;
	private Integer rScriptStepOrder;
	private String rScriptStepType;
	private String rGroupYn;

	// detail
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
	
	// 스크립트 디테일 가변값 코드 정보
	private String rScriptDetailIfCaseCode;
	private String rScriptDetailIfCaseDetailCode;
	

	// common
	private Integer rsScriptCommonPk;
	private String rsScriptCommonType;
	private String rsScriptCommonName;
	private String rsScriptCommonDesc;
	private String rsScriptCommonText;
	private String rsScriptCommonRealTimeTts;
	private Date rsScriptCommonCreateDate;
	private String rsScriptCommonCreateUser;
	private Date rsScriptCommonUpdateDate;
	private String rsScriptCommonUpdateUser;
	private String rsScriptCommonConfirm;
	private Date rsScriptCommonConfirmdate;
	private String rsScriptCommonConfirmUser;
	private Date rsScriptCommonReservDate;
	private String rsScriptCommonReservUser;

	private String orderBy; // 정렬 컬럼
	private String direction; // 정렬 방향

	private String registeredYN;// 스크립트 등록 여부

	private int limit;
	private int offset;
	private int totalRowCount;
	private int rowNumber;
	
	

	public String getrSysDisType() {
		return rSysDisType;
	}

	public void setrSysDisType(String rSysDisType) {
		this.rSysDisType = rSysDisType;
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

	public String gettName() {
		return tName;
	}

	public void settName(String tName) {
		this.tName = tName;
	}

	public String gettOrder() {
		return tOrder;
	}

	public void settOrder(String tOrder) {
		this.tOrder = tOrder;
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

	public Integer gettParent() {
		return tParent;
	}

	public void settParent(Integer tParent) {
		this.tParent = tParent;
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

	public String getrSccriptStepCallKey() {
		return rSccriptStepCallKey;
	}

	public void setrSccriptStepCallKey(String rSccriptStepCallKey) {
		this.rSccriptStepCallKey = rSccriptStepCallKey;
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

	public String getrScriptCallKey() {
		return rScriptCallKey;
	}

	public void setrScriptCallKey(String rScriptCallKey) {
		this.rScriptCallKey = rScriptCallKey;
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

	public String getrScriptTTSHistoryFilePath() {
		return rScriptTTSHistoryFilePath;
	}

	public void setrScriptTTSHistoryFilePath(String rScriptTTSHistoryFilePath) {
		this.rScriptTTSHistoryFilePath = rScriptTTSHistoryFilePath;
	}

	public String getCude() {
		return cude;
	}

	public void setCude(String cude) {
		this.cude = cude;
	}

	public String getPrdNm() {
		return prdNm;
	}

	public void setPrdNm(String prdNm) {
		this.prdNm = prdNm;
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

	public String getScriptType() {
		return scriptType;
	}

	public void setScriptType(String scriptType) {
		this.scriptType = scriptType;
	}

	public String getRsUseYn() {
		return rsUseYn;
	}

	public void setRsUseYn(String rsUseYn) {
		this.rsUseYn = rsUseYn;
	}

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

	public String getrUpdateYn() {
		return rUpdateYn;
	}

	public void setrUpdateYn(String rUpdateYn) {
		this.rUpdateYn = rUpdateYn;
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

	public String getAd047() {
		return ad047;
	}

	public void setAd047(String ad047) {
		this.ad047 = ad047;
	}

	public String getClearRecYn() {
		return clearRecYn;
	}

	public void setClearRecYn(String clearRecYn) {
		this.clearRecYn = clearRecYn;
	}

	public Integer getrProductListPk() {
		return rProductListPk;
	}

	public void setrProductListPk(Integer rProductListPk) {
		this.rProductListPk = rProductListPk;
	}

	public Integer getrProductListFk() {
		return rProductListFk;
	}

	public void setrProductListFk(Integer rProductListFk) {
		this.rProductListFk = rProductListFk;
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

	public String getrUseYn() {
		return rUseYn;
	}

	public void setrUseYn(String rUseYn) {
		this.rUseYn = rUseYn;
	}

	public String getrScriptCommon() {
		return rScriptCommon;
	}

	public void setrScriptCommon(String rScriptCommon) {
		this.rScriptCommon = rScriptCommon;
	}

	public String getrGroupCode() {
		return rGroupCode;
	}

	public void setrGroupCode(String rGroupCode) {
		this.rGroupCode = rGroupCode;
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

	public String getrGroupYn() {
		return rGroupYn;
	}

	public void setrGroupYn(String rGroupYn) {
		this.rGroupYn = rGroupYn;
	}

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

	public String getrScriptDetailCreateUser() {
		return rScriptDetailCreateUser;
	}

	public void setrScriptDetailCreateUser(String rScriptDetailCreateUser) {
		this.rScriptDetailCreateUser = rScriptDetailCreateUser;
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

	public Integer getRsScriptCommonPk() {
		return rsScriptCommonPk;
	}

	public void setRsScriptCommonPk(Integer rsScriptCommonPk) {
		this.rsScriptCommonPk = rsScriptCommonPk;
	}

	public String getRsScriptCommonType() {
		return rsScriptCommonType;
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

	public Date getRsScriptCommonCreateDate() {
		return rsScriptCommonCreateDate;
	}

	public void setRsScriptCommonCreateDate(Date rsScriptCommonCreateDate) {
		this.rsScriptCommonCreateDate = rsScriptCommonCreateDate;
	}

	public String getRsScriptCommonCreateUser() {
		return rsScriptCommonCreateUser;
	}

	public void setRsScriptCommonCreateUser(String rsScriptCommonCreateUser) {
		this.rsScriptCommonCreateUser = rsScriptCommonCreateUser;
	}

	public Date getRsScriptCommonUpdateDate() {
		return rsScriptCommonUpdateDate;
	}

	public void setRsScriptCommonUpdateDate(Date rsScriptCommonUpdateDate) {
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

	public Date getRsScriptCommonConfirmdate() {
		return rsScriptCommonConfirmdate;
	}

	public void setRsScriptCommonConfirmdate(Date rsScriptCommonConfirmdate) {
		this.rsScriptCommonConfirmdate = rsScriptCommonConfirmdate;
	}

	public String getRsScriptCommonConfirmUser() {
		return rsScriptCommonConfirmUser;
	}

	public void setRsScriptCommonConfirmUser(String rsScriptCommonConfirmUser) {
		this.rsScriptCommonConfirmUser = rsScriptCommonConfirmUser;
	}

	public Date getRsScriptCommonReservDate() {
		return rsScriptCommonReservDate;
	}

	public void setRsScriptCommonReservDate(Date rsScriptCommonReservDate) {
		this.rsScriptCommonReservDate = rsScriptCommonReservDate;
	}

	public String getRsScriptCommonReservUser() {
		return rsScriptCommonReservUser;
	}

	public void setRsScriptCommonReservUser(String rsScriptCommonReservUser) {
		this.rsScriptCommonReservUser = rsScriptCommonReservUser;
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

	public String getRegisteredYN() {
		return registeredYN;
	}

	public void setRegisteredYN(String registeredYN) {
		this.registeredYN = registeredYN;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getTotalRowCount() {
		return totalRowCount;
	}

	public void setTotalRowCount(int totalRowCount) {
		this.totalRowCount = totalRowCount;
	}

	public int getRowNumber() {
		return rowNumber;
	}

	public void setRowNumber(int rowNumber) {
		this.rowNumber = rowNumber;
	}

	public String getrScriptDetailIfCaseCode() {
		return rScriptDetailIfCaseCode;
	}

	public void setrScriptDetailIfCaseCode(String rScriptDetailIfCaseCode) {
		this.rScriptDetailIfCaseCode = rScriptDetailIfCaseCode;
	}

	public String getrScriptDetailIfCaseDetailCode() {
		return rScriptDetailIfCaseDetailCode;
	}

	public void setrScriptDetailIfCaseDetailCode(String rScriptDetailIfCaseDetailCode) {
		this.rScriptDetailIfCaseDetailCode = rScriptDetailIfCaseDetailCode;
	}

	public String getrProductTypeName() {
		return rProductTypeName;
	}

	public void setrProductTypeName(String rProductTypeName) {
		this.rProductTypeName = rProductTypeName;
	}
	
	
}
