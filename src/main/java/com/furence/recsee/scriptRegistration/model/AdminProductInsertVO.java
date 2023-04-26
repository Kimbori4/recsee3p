package com.furence.recsee.scriptRegistration.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AdminProductInsertVO {
	private String productType;
	
	private String productCode;
	
	private String productName;
	
	private String useYn;
	
	private String groupYn;
	
	private String groupCode;
	
	private String sysdisType;
	
	private String productAttr;
	
}
