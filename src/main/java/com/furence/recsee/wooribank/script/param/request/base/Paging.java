package com.furence.recsee.wooribank.script.param.request.base;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Paging implements DhtmlxPaging{
	
	/**
	 * header요청인지 데이터 요청인지 구분
	 */
	private String header;
	
	/**
	 * 현재는 XML만 지원함.
	 */
	private String resourceType;
	
	/**
	 * 정렬 기준 컬럼
	 */
	private String orderBy;

	/**
	 * 정렬 방향 asc , desc
	 */
	private String direction;

	/**
	 * 검색 결과 집합 limit
	 */
	private int limit = 100;

	/**
	 * 검색 결과 집합 offset
	 */
	private int offset = 0;
	
	@Override
	public String getHeader() {
		return this.header;
	}
	
	@Override
	public void setHeader(String header) {
		this.header = header;
	}
	
	@Override
	public int getCount() {
		return this.limit;
	}
	
	@Override
	public int getPosStart() {
		return this.offset;		
	}
	
	@Override
	public void setCount(int count) {
		this.limit = count;
	}
	
	@Override
	public void setPosStart(int posStart) {
		this.offset = posStart;
	}
	
}
