package com.furence.recsee.wooribank.script.util;

public class Pair<T1,T2> {

	private T1 first;
	
	private T2 second;
	
	public Pair( T1 first, T2 second ){
		this.first = first;
		this.second = second;
	}
	
	public static <T1,T2> Pair<T1,T2> of(T1 first, T2 second){
		return new Pair<T1,T2>( first, second );
	}
	
	public T1 first() {
		return this.first;
	}
	
	public T2 second() {
		return this.second;
	}
	
	public void first(T1 t1) {
		this.first = t1;
	}
	
	public void second(T2 t2) {
		this.second = t2;
	}
}
