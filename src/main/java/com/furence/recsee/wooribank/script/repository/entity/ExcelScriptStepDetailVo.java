package com.furence.recsee.wooribank.script.repository.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class ExcelScriptStepDetailVo implements Cloneable {
	

	private int rScriptDetailPk;
	private String rScriptDetailType;
	private String rScriptDetailComKind;
	private String rScriptDetailText;
	private String rScriptDetailIfCase;
	private String rScriptStepFk;
	private String rScriptDetailIfCaseDetail;
	private String rScriptDetailRealtimeTTS;
	private String rScriptDetailCreateDate;
	private String rScriptDetailUdateDate;
	private String rScriptDetailUpdateUser;
	private String rScriptDetailConfirm;
	private String rScriptDetailConfirmDate;
	private String rScriptDetailReservDate;
	private String rScriptDetailCreateUser;
	private String rScriptDetailConfirmUser;
	private String rScriptDetailConfirmOrder;
	private String rScriptDetailConfirmComFk;
	private String rScriptDetailIfCaseCode;
	private String rScriptDetailIfCaseDetailCode;
	private String rUseYn;
	private Integer rScriptDetailEltCase;
	private String rProductAttributesExt;
	private String rProductAttributes;
	private String rScriptStepDetailCaseAttributes;

	
}

