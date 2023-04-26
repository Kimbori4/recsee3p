package com.furence.recsee.wooribank.script.param.request;

import java.io.Serializable;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;
import lombok.ToString;


@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MakeTTSParam implements Serializable{
	private static final Logger logger = LoggerFactory.getLogger(MakeTTSParam.class);
	private static final long serialVersionUID = 5766228197861773629L;

	@JsonProperty("requestUser")
	private String userId;
	
	@Singular("productList")
	@JsonProperty("productList")
	private List<String> productPkList;
	
	@JsonGetter("userId")
	public String getRequestUser() {
		return this.userId;
	}
	
	@JsonGetter("productPkList")
	public List<String> getProductList() {
		return this.productPkList;
	}
	
	
	public String toJsonString() {
		
		String ret = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			ret = mapper.writeValueAsString(this);
		} catch(Exception e) {
			logger.error("error", e);
		}
		
		return ret;
	}
}
