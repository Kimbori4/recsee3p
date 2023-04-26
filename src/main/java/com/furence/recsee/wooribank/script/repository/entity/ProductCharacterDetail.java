package com.furence.recsee.wooribank.script.repository.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Builder
public class ProductCharacterDetail {
	
	private String rsProductCode;
	
	private String rsProductType;
	
	private String rsProductName;
	
	private String rsProductDetailText;
	
	private String rsProductSubCode;

}
