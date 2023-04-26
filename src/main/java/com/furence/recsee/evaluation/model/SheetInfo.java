package com.furence.recsee.evaluation.model;

public class SheetInfo {
	public String getEvaluatorCount() {
		return evaluatorCount;
	}
	public void setEvaluatorCount(String evaluatorCount) {
		this.evaluatorCount = evaluatorCount;
	}
	public String getCategorySubIMark() {
		return categorySubIMark;
	}
	public void setCategorySubIMark(String categorySubIMark) {
		this.categorySubIMark = categorySubIMark;
	}
	private String sheetCode;
	private String sheetName;
	private String sheetCreator;
	private Integer sheetDepth;
	private String sheetMdate;
	private String sheetWdate;
	private String sheetUdate;
	private String sheetUsingYn;
	private String bPartCode;
	private String mPartCode;
	private String sPartCode;
	private String evalContent;
	//@mars 추가
	private String sBgcode;
	private String sBgContent;
	private String sBgMark;
	private String sMgcode;
	private String sMgContent;
	private String sMgMark;
	private String sSgcode;
	private String sSgContent;
	private String sSgMark;
	private String sBgMdate;
	private String sBgUdate;
	private String sMgMdate;
	private String sMgUdate;
	private String sSgMdate;
	private String sSgUdate;
	private String sBgName;
	private String sMgName;
	private String sSgName;
	//추가 끝
	private String itemCode;
	private String itemContent;
	private String itemName;
	private String itemMdate;
	private String itemUdate;

	private String categoryBCode; //대분류
	private String oldCategoryBCode;
	private String regSbgOrder;

	private String categoryMCode; //중분류
	private String oldCategoryMCode;
	private String regSmgOrder;

	private String categorySCode; //소분류
	private String oldCategorySCode;
	private String regSsgOrder;

	private String categoryICode; //아이템
	private String oldCategoryICode;
	private String regItemOrder;

	private String categorySubICode; //서브아이템
	private String oldCategorySubICode;
	private String categorySubIMark;
	private String CategorySubIType;	// 수정
	private String CategorySubIAnswerVal;	// 수정
	
	private String rowNum;

	private String bcode;
	private String bcontent;
	private String mcode;
	private String mcontent;
	private String scode;
	private String scontent;
	private String Icode;
	private String ircode;
	private String icontent;
	private String mark;

	private String siteMCode;
	private String siteMContent;
	private String siteMCMemo;

	private String eCampCode;
	private String evalatorFeedback;
	private String agentFeedback;
	private Integer evalTotalMark;

	private String evalatorFeedback2nd;
	private String evalatorId2nd;
	
	private String evalatorId;
	private String evalatorName;
	private String sitemMark;
	private String sitemMarkMax;
	private String evaluatorCount;

	// evalStatistic Add Columns
	private String evalCode;
	private String evalDate;
	private String ecampCode;

	private String recDate;
	private String recTime;
	private String recId;
	private String recName;
	private String recFileName;
	private String custPhone;
	private String screenFileName;
	private String textFileName;
	private String setitem;
	private String evalYn;
	private String evalStatus;

	private String sDate;
	private String eDate;
	private Integer check; //체크
	private String sheetContent; //평가지 설명
	private Integer itemNum; //항목 수
	private Integer evalNum; //평가 횟수
	private Integer totalScore; // 총 점수
	private String searchWord;
	private String searchType;
	private String searchStep;

	private int	changeItemScore;

	private int feedbackCode;
	private String feedback;
	private String feedbackDate;
	private Integer sItemCode;
	private String sItemContent;

	private String groupType;
	
	private String totalScoreString; //엑셀 문구위해 총점
	private String evaluatorOp; //평가자 의견 엑셀 문구위해
	private String counselorOp; //상담원 의견  엑셀 문구위해

	private int indexNum;
	
	private String memoContent; // 메모추가로 인한 추가
    private String memoCode; // 메모추가로인한추가 
	private String memoMark; //메모추가로인한추가
	private String memoLine; // 메모추가로인한 추가
	private String memoSeq; // 메모추가로 인한 추가
    

	private String extNum;
	private String rejectStatus;
	private String rejectCount;
	private String rejectDate;
	private String callingTime;
	private String evalTotalScore;
	private String pivotData;
	private String sectItem;

	private String total;
	private String avg;
	
	private String bgoffset;
	
	
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	public String getAvg() {
		return avg;
	}
	public void setAvg(String avg) {
		this.avg = avg;
	}
	public String getSectItem() {
		return sectItem;
	}
	public void setSectItem(String sectItem) {
		this.sectItem = sectItem;
	}
	public String getTotalScoreString() {
		return totalScoreString;
	}
	public void setTotalScoreString(String totalScoreString) {
		this.totalScoreString = totalScoreString;
	}
	public String getEvaluatorOp() {
		return evaluatorOp;
	}
	public void setEvaluatorOp(String evaluatorOp) {
		this.evaluatorOp = evaluatorOp;
	}
	public String getCounselorOp() {
		return counselorOp;
	}
	public void setCounselorOp(String counselorOp) {
		this.counselorOp = counselorOp;
	}
	public String getEvalCode() {
		return evalCode;
	}
	public void setEvalCode(String evalCode) {
		this.evalCode = evalCode;
	}

	public String getGroupType() {
		return groupType;
	}
	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}

	public Integer getsItemCode() {
		return sItemCode;
	}
	public void setsItemCode(Integer sItemCode) {
		this.sItemCode = sItemCode;
	}
	public String getsItemContent() {
		return sItemContent;
	}
	public void setsItemContent(String sItemContent) {
		this.sItemContent = sItemContent;
	}	
	public String getSiteMCMemo() {
		return siteMCMemo;
	}
	public void setSiteMCMemo(String siteMCMemo) {
		this.siteMCMemo = siteMCMemo;
	}
	
	public String getSitemMarkMax() {
		return sitemMarkMax;
	}
	public void setSitemMarkMax(String sitemMarkMax) {
		this.sitemMarkMax = sitemMarkMax;
	}
	public void setEvalDate(String evalDate) {
		this.evalDate = evalDate;
	}
	public String getEvalDate() {
		return evalDate;
	}
	public void setEcampCode(String ecampCode) {
		this.ecampCode = ecampCode;
	}

	public String getEcampCode() {
		return ecampCode;
	}
	public String getFeedbackDate() {
		return feedbackDate;
	}
	public void setFeedbackDate(String feedbackDate) {
		this.feedbackDate = feedbackDate;
	}
	public int getFeedbackCode() {
		return feedbackCode;
	}
	public void setFeedbackCode(int feedbackCode) {
		this.feedbackCode = feedbackCode;
	}
	public String getFeedback() {
		return feedback;
	}
	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}
	public int getChangeItemScore() {
		return changeItemScore;
	}
	public void setChangeItemScore(int changeItemScore) {
		this.changeItemScore = changeItemScore;
	}
	public int getIndexNum() {
		return indexNum;
	}
	public void setIndexNum(int indexNum) {
		this.indexNum = indexNum;
	}
	public String getSearchWord() {
		return searchWord;
	}
	public void setSearchWord(String searchWord) {
		this.searchWord = searchWord;
	}
	public String getSearchType() {
		return searchType;
	}
	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}
	public String getsBgName() {
		return sBgName;
	}
	public void setsBgName(String sBgName) {
		this.sBgName = sBgName;
	}
	public String getsMgName() {
		return sMgName;
	}
	public void setsMgName(String sMgName) {
		this.sMgName = sMgName;
	}
	public String getsSgName() {
		return sSgName;
	}
	public void setsSgName(String sSgName) {
		this.sSgName = sSgName;
	}
	public String getsBgMdate() {
		return sBgMdate;
	}
	public void setsBgMdate(String sBgMdate) {
		this.sBgMdate = sBgMdate;
	}
	public String getsBgUdate() {
		return sBgUdate;
	}
	public void setsBgUdate(String sBgUdate) {
		this.sBgUdate = sBgUdate;
	}
	public String getsMgMdate() {
		return sMgMdate;
	}
	public void setsMgMdate(String sMgMdate) {
		this.sMgMdate = sMgMdate;
	}
	public String getsMgUdate() {
		return sMgUdate;
	}
	public void setsMgUdate(String sMgUdate) {
		this.sMgUdate = sMgUdate;
	}
	public String getsSgMdate() {
		return sSgMdate;
	}
	public void setsSgMdate(String sSgMdate) {
		this.sSgMdate = sSgMdate;
	}
	public String getsSgUdate() {
		return sSgUdate;
	}
	public void setsSgUdate(String sSgUdate) {
		this.sSgUdate = sSgUdate;
	}
	public Integer getCheck() {
		return check;
	}
	public void setCheck(Integer check) {
		this.check = check;
	}

	public String getSheetContent() {
		return sheetContent;
	}
	public void setSheetContent(String sheetContent) {
		this.sheetContent = sheetContent;
	}
	public Integer getItemNum() {
		return itemNum;
	}
	public void setItemNum(Integer itemNum) {
		this.itemNum = itemNum;
	}
	public Integer getEvalNum() {
		return evalNum;
	}
	public void setEvalNum(Integer evalNum) {
		this.evalNum = evalNum;
	}

	public Integer getTotalScore() {
		return totalScore;
	}
	public void setTotalScore(Integer totalScore) {
		this.totalScore = totalScore;
	}
	public String getEvalStatus() {
		return evalStatus;
	}
	public void setEvalStatus(String evalStatus) {
		this.evalStatus = evalStatus;
	}
	public String getsDate() {
		return sDate;
	}
	public void setsDate(String sDate) {
		this.sDate = sDate;
	}
	public String geteDate() {
		return eDate;
	}
	public void seteDate(String eDate) {
		this.eDate = eDate;
	}
	public String getEvalYn() {
		return evalYn;
	}
	public void setEvalYn(String evalYn) {
		this.evalYn = evalYn;
	}
	public String getRecDate() {
		return recDate;
	}
	public void setRecDate(String recDate) {
		this.recDate = recDate;
	}
	public String getRecTime() {
		return recTime;
	}
	public void setRecTime(String recTime) {
		this.recTime = recTime;
	}
	public String getRecId() {
		return recId;
	}
	public void setRecId(String recId) {
		this.recId = recId;
	}
	public String getRecName() {
		return recName;
	}
	public void setRecName(String recName) {
		this.recName = recName;
	}
	public String getRecFileName() {
		return recFileName;
	}
	public void setRecFileName(String recFileName) {
		this.recFileName = recFileName;
	}
	public String getScreenFileName() {
		return screenFileName;
	}
	public void setScreenFileName(String screenFileName) {
		this.screenFileName = screenFileName;
	}
	public String getTextFileName() {
		return textFileName;
	}
	public void setTextFileName(String textFileName) {
		this.textFileName = textFileName;
	}
	public String getSetitem() {
		return setitem;
	}
	public void setSetitem(String setitem) {
		this.setitem = setitem;
	}
	public String getSitemMark() {
		return sitemMark;
	}
	public void setSitemMark(String sitemMark) {
		this.sitemMark = sitemMark;
	}
	public String getEvalatorId() {
		return evalatorId;
	}
	public void setEvalatorId(String evalatorId) {
		this.evalatorId = evalatorId;
	}
	public String getEvalatorName() {
		return evalatorName;
	}
	public void setEvalatorName(String evalatorName) {
		this.evalatorName = evalatorName;
	}
	public Integer getEvalTotalMark() {
		return evalTotalMark;
	}
	public void setEvalTotalMark(Integer evalTotalMark) {
		this.evalTotalMark = evalTotalMark;
	}
	public String getEvalatorFeedback() {
		return evalatorFeedback;
	}
	public void setEvalatorFeedback(String evalatorFeedback) {
		this.evalatorFeedback = evalatorFeedback;
	}
	public String getAgentFeedback() {
		return agentFeedback;
	}
	public void setAgentFeedback(String agentFeedback) {
		this.agentFeedback = agentFeedback;
	}
	public String geteCampCode() {
		return eCampCode;
	}
	public void seteCampCode(String eCampCode) {
		this.eCampCode = eCampCode;
	}
	public String getMark() {
		return mark;
	}
	public void setMark(String mark) {
		this.mark = mark;
	}
	public String getSiteMCode() {
		return siteMCode;
	}
	public void setSiteMCode(String siteMCode) {
		this.siteMCode = siteMCode;
	}
	public String getSiteMContent() {
		return siteMContent;
	}
	public void setSiteMContent(String siteMContent) {
		this.siteMContent = siteMContent;
	}
	public String getCategorySubICode() {
		return categorySubICode;
	}
	public void setCategorySubICode(String categorySubICode) {
		this.categorySubICode = categorySubICode;
	}
	public String getOldCategorySubICode() {
		return oldCategorySubICode;
	}
	public void setOldCategorySubICode(String oldCategorySubICode) {
		this.oldCategorySubICode = oldCategorySubICode;
	}
	public String getRegSbgOrder() {
		return regSbgOrder;
	}
	public void setRegSbgOrder(String regSbgOrder) {
		this.regSbgOrder = regSbgOrder;
	}
	public String getRegSmgOrder() {
		return regSmgOrder;
	}
	public void setRegSmgOrder(String regSmgOrder) {
		this.regSmgOrder = regSmgOrder;
	}
	public String getRegSsgOrder() {
		return regSsgOrder;
	}
	public void setRegSsgOrder(String regSsgOrder) {
		this.regSsgOrder = regSsgOrder;
	}
	public String getRegItemOrder() {
		return regItemOrder;
	}
	public void setRegItemOrder(String regItemOrder) {
		this.regItemOrder = regItemOrder;
	}
	public String getBcode() {
		return bcode;
	}
	public void setBcode(String bcode) {
		this.bcode = bcode;
	}
	public String getBcontent() {
		return bcontent;
	}
	public void setBcontent(String bcontent) {
		this.bcontent = bcontent;
	}
	public String getMcode() {
		return mcode;
	}
	public void setMcode(String mcode) {
		this.mcode = mcode;
	}
	public String getMcontent() {
		return mcontent;
	}
	public void setMcontent(String mcontent) {
		this.mcontent = mcontent;
	}
	public String getScode() {
		return scode;
	}
	public void setScode(String scode) {
		this.scode = scode;
	}
	public String getScontent() {
		return scontent;
	}
	public void setScontent(String scontent) {
		this.scontent = scontent;
	}
	public String getIcode() {
		return Icode;
	}
	public void setIcode(String Icode) {
		this.Icode = Icode;
	}
	public String getIrcode() {
		return ircode;
	}
	public void setIrcode(String ircode) {
		this.ircode = ircode;
	}
	public String getIcontent() {
		return icontent;
	}
	public void setIcontent(String icontent) {
		this.icontent = icontent;
	}
	public String getCategoryICode() {
		return categoryICode;
	}
	public void setCategoryICode(String categoryICode) {
		this.categoryICode = categoryICode;
	}
	public String getOldCategoryICode() {
		return oldCategoryICode;
	}
	public void setOldCategoryICode(String oldCategoryICode) {
		this.oldCategoryICode = oldCategoryICode;
	}
	public String getCategoryBCode() {
		return categoryBCode;
	}
	public void setCategoryBCode(String categoryBCode) {
		this.categoryBCode = categoryBCode;
	}
	public String getOldCategoryBCode() {
		return oldCategoryBCode;
	}
	public void setOldCategoryBCode(String oldCategoryBCode) {
		this.oldCategoryBCode = oldCategoryBCode;
	}
	public String getCategoryMCode() {
		return categoryMCode;
	}
	public void setCategoryMCode(String categoryMCode) {
		this.categoryMCode = categoryMCode;
	}
	public String getOldCategoryMCode() {
		return oldCategoryMCode;
	}
	public void setOldCategoryMCode(String oldCategoryMCode) {
		this.oldCategoryMCode = oldCategoryMCode;
	}
	public String getCategorySCode() {
		return categorySCode;
	}
	public void setCategorySCode(String categorySCode) {
		this.categorySCode = categorySCode;
	}
	public String getOldCategorySCode() {
		return oldCategorySCode;
	}
	public void setOldCategorySCode(String oldCategorySCode) {
		this.oldCategorySCode = oldCategorySCode;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public String getItemContent() {
		return itemContent;
	}
	public void setItemContent(String itemContent) {
		this.itemContent = itemContent;
	}
	public String getSheetCode() {
		return sheetCode;
	}
	public void setSheetCode(String sheetCode) {
		this.sheetCode = sheetCode;
	}
	public String getSheetName() {
		return sheetName;
	}
	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}
	public String getSheetCreator() {
		return sheetCreator;
	}
	public void setSheetCreator(String sheetCreator) {
		this.sheetCreator = sheetCreator;
	}
	public Integer getSheetDepth() {
		return sheetDepth;
	}
	public void setSheetDepth(Integer sheetDepth) {
		this.sheetDepth = sheetDepth;
	}
	public String getSheetMdate() {
		return sheetMdate;
	}
	public void setSheetMdate(String sheetMdate) {
		this.sheetMdate = sheetMdate;
	}
	public String getSheetWdate() {
		return sheetWdate;
	}
	public void setSheetWdate(String sheetWdate) {
		this.sheetWdate = sheetWdate;
	}
	public String getSheetUsingYn() {
		return sheetUsingYn;
	}
	public void setSheetUsingYn(String sheetUsingYn) {
		this.sheetUsingYn = sheetUsingYn;
	}
	public String getbPartCode() {
		return bPartCode;
	}
	public void setbPartCode(String bPartCode) {
		this.bPartCode = bPartCode;
	}
	public String getmPartCode() {
		return mPartCode;
	}
	public void setmPartCode(String mPartCode) {
		this.mPartCode = mPartCode;
	}
	public String getsPartCode() {
		return sPartCode;
	}
	public void setsPartCode(String sPartCode) {
		this.sPartCode = sPartCode;
	}
	public String getsBgcode() {
		return sBgcode;
	}
	public void setsBgcode(String sBgcode) {
		this.sBgcode = sBgcode;
	}
	public String getsBgContent() {
		return sBgContent;
	}
	public void setsBgContent(String sBgContent) {
		this.sBgContent = sBgContent;
	}
	public String getsBgMark() {
		return sBgMark;
	}
	public void setsBgMark(String sBgMark) {
		this.sBgMark = sBgMark;
	}
	public String getsMgcode() {
		return sMgcode;
	}
	public void setsMgcode(String sMgcode) {
		this.sMgcode = sMgcode;
	}
	public String getsMgContent() {
		return sMgContent;
	}
	public void setsMgContent(String sMgContent) {
		this.sMgContent = sMgContent;
	}
	public String getsMgMark() {
		return sMgMark;
	}
	public void setsMgMark(String sMgMark) {
		this.sMgMark = sMgMark;
	}
	public String getsSgcode() {
		return sSgcode;
	}
	public void setsSgcode(String sSgcode) {
		this.sSgcode = sSgcode;
	}
	public String getsSgContent() {
		return sSgContent;
	}
	public void setsSgContent(String sSgContent) {
		this.sSgContent = sSgContent;
	}
	public String getsSgMark() {
		return sSgMark;
	}
	public void setsSgMark(String sSgMark) {
		this.sSgMark = sSgMark;
	}
	@Override
	public String toString() {
		return "SheetInfo [sheetCode=" + sheetCode + ", sheetName=" + sheetName
				+ ", sheetCreator=" + sheetCreator + ", sheetDepth="
				+ sheetDepth + ", sheetMdate=" + sheetMdate + ", sheetWdate="
				+ sheetWdate + ", sheetUsingYn=" + sheetUsingYn
				+ ", bPartCode=" + bPartCode + ", mPartCode=" + mPartCode
				+ ", sPartCode=" + sPartCode + ", itemCode=" + itemCode
				+ ", itemContent=" + itemContent + ", categoryBCode="
				+ categoryBCode + ", oldCategoryBCode=" + oldCategoryBCode
				+ ", regSbgOrder=" + regSbgOrder + ", categoryMCode="
				+ categoryMCode + ", oldCategoryMCode=" + oldCategoryMCode
				+ ", regSmgOrder=" + regSmgOrder + ", categorySCode="
				+ categorySCode + ", oldCategorySCode=" + oldCategorySCode
				+ ", regSsgOrder=" + regSsgOrder + ", categoryICode="
				+ categoryICode + ", oldCategoryICode=" + oldCategoryICode
				+ ", regItemOrder=" + regItemOrder + ", categorySubICode="
				+ categorySubICode + ", oldCategorySubICode="
				+ oldCategorySubICode + ", bcode=" + bcode + ", bcontent="
				+ bcontent + ", mcode=" + mcode + ", mcontent=" + mcontent
				+ ", scode=" + scode + ", scontent=" + scontent + ", Icode="
				+ Icode + ", ircode=" + ircode + ", icontent=" + icontent
				+ ", mark=" + mark + ", siteMCode=" + siteMCode
				+ ", siteMContent=" + siteMContent + ", eCampCode=" + eCampCode
				+ ", evalatorFeedback=" + evalatorFeedback + ", agentFeedback="
				+ agentFeedback + ", evalTotalMark=" + evalTotalMark
				+ ", evalatorId=" + evalatorId + ", evalatorName="
				+ evalatorName + ", sitemMark=" + sitemMark + ", recDate="
				+ recDate + ", recTime=" + recTime + ", recId=" + recId
				+ ", recName=" + recName + ", recFileName=" + recFileName
				+ ", screenFileName=" + screenFileName + ", textFileName="
				+ textFileName + ", setitem=" + setitem + ", evalYn=" + evalYn
				+ ", sDate=" + sDate + ", eDate=" + eDate + "]";
	}
	public String getSheetUdate() {
		return sheetUdate;
	}
	public void setSheetUdate(String sheetUdate) {
		this.sheetUdate = sheetUdate;
	}
	public String getEvalContent() {
		return evalContent;
	}
	public void setEvalContent(String evalContent) {
		this.evalContent = evalContent;
	}
	public String getSearchStep() {
		return searchStep;
	}
	public void setSearchStep(String searchStep) {
		this.searchStep = searchStep;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getItemMdate() {
		return itemMdate;
	}
	public void setItemMdate(String itemMdate) {
		this.itemMdate = itemMdate;
	}
	public String getItemUdate() {
		return itemUdate;
	}
	public void setItemUdate(String itemUdate) {
		this.itemUdate = itemUdate;
	}
	public String getRowNum() {
		return rowNum;
	}
	public void setRowNum(String rowNum) {
		this.rowNum = rowNum;
	}
	
	// 수정
	public String getCategorySubIType() {
		return CategorySubIType;
	}
	public void setCategorySubIType(String categorySubIType) {
		CategorySubIType = categorySubIType;
	}
	public String getCategorySubIAnswerVal() {
		return CategorySubIAnswerVal;
	}
	public void setCategorySubIAnswerVal(String categorySubIAnswerVal) {
		CategorySubIAnswerVal = categorySubIAnswerVal;
	}
		
	public String getMemoContent() {
		return memoContent;
	}
	public void setMemoContent(String memoContent) {
		this.memoContent = memoContent;
	}
	public String getMemoCode() {
		return memoCode;
	}
	public void setMemoCode(String memoCode) {
		this.memoCode = memoCode;
	}
	public String getMemoMark() {
		return memoMark;
	}
	public void setMemoMark(String memoMark) {
		this.memoMark = memoMark;
	}
	public String getMemoLine() {
		return memoLine;
	}
	public void setMemoLine(String memoLine) {
		this.memoLine = memoLine;
	}
	public String getMemoSeq() {
		return memoSeq;
	}
	public void setMemoSeq(String memoSeq) {
		this.memoSeq = memoSeq;
	}
	public String getCustPhone() {
		return custPhone;
	}
	public void setCustPhone(String custPhone) {
		this.custPhone = custPhone;
	}
	public String getRejectStatus() {
		return rejectStatus;
	}
	public void setRejectStatus(String rejectStatus) {
		this.rejectStatus = rejectStatus;
	}
	public String getRejectCount() {
		return rejectCount;
	}
	public void setRejectCount(String rejectCount) {
		this.rejectCount = rejectCount;
	}
	public String getRejectDate() {
		return rejectDate;
	}
	public void setRejectDate(String rejectDate) {
		this.rejectDate = rejectDate;
	}
	public String getExtNum() {
		return extNum;
	}
	public void setExtNum(String extNum) {
		this.extNum = extNum;
	}
	public String getCallingTime() {
		return callingTime;
	}
	public void setCallingTime(String callingTime) {
		this.callingTime = callingTime;
	}
	public String getEvalatorFeedback2nd() {
		return evalatorFeedback2nd;
	}
	public void setEvalatorFeedback2nd(String evalatorFeedback2nd) {
		this.evalatorFeedback2nd = evalatorFeedback2nd;
	}
	public String getEvalatorId2nd() {
		return evalatorId2nd;
	}
	public void setEvalatorId2nd(String evalatorId2nd) {
		this.evalatorId2nd = evalatorId2nd;
	}
	public String getEvalTotalScore() {
		return evalTotalScore;
	}
	public void setEvalTotalScore(String evalTotalScore) {
		this.evalTotalScore = evalTotalScore;
	}
	public String getPivotData() {
		return pivotData;
	}
	public void setPivotData(String pivotData) {
		this.pivotData = pivotData;
	}
	public String getBgoffset() {
		return bgoffset;
	}
	public void setBgoffset(String bgoffset) {
		this.bgoffset = bgoffset;
	}
	
	
	
}
