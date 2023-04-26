package com.furence.recsee.wooribank.script.repository.entity;

import java.util.HashMap;
import java.util.List;

import com.furence.recsee.common.util.Hash;
import com.furence.recsee.wooribank.facerecording.model.ProductListVo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
public class InsuranceAndProductList {
	
	private List<ProductListVo> pList;
	
	private List<HashMap<String, String>> insureList;
	

}
