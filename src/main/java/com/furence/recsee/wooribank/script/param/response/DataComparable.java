package com.furence.recsee.wooribank.script.param.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@AllArgsConstructor
@Builder
public class DataComparable<T> {
	
	public enum Side {
		Before ,
		After;
	}
	
	private T before;
	
	private T after;

	@FunctionalInterface
	public interface DataProvider<T> {
		T get();
	}
	
	public DataComparable(DataProvider<T> beforeProvider , DataProvider<T> afterProvider) {
		before = beforeProvider.get() ;
		after = afterProvider.get();
	}
}

