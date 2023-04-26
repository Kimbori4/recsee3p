package com.furence.recsee.wooribank.script.param.request.base;

public interface DhtmlxPaging {
	
	public void setCount(int count);
	
	public void setPosStart(int posStart);
	
	public int getCount();
	
	public int getPosStart();
	
	public void setHeader(String heaader);
	
	public String getHeader();
	
}
