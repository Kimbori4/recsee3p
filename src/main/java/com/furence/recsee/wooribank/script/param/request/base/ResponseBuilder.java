package com.furence.recsee.wooribank.script.param.request.base;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;


public class ResponseBuilder<T>{

	private final Supplier<T> instance;
	
	private List<Consumer<T>> setters = new ArrayList<>();
	
	private ResponseBuilder(Supplier<T> instance) {
		this.instance = instance;
	}
	
	public static <T> ResponseBuilder<T> of(Supplier<T> instance) {
		return new ResponseBuilder<T>(instance);
	}
	
	public <U> ResponseBuilder<T> with(BiConsumer<T,U> consumer, U values){
		Consumer<T> c = setter -> consumer.accept(setter, values);
		this.setters.add(c);
		return this;
	}
	
	public T build() {
		T value = this.instance.get();
		this.setters.forEach(iter -> iter.accept(value));
		
		this.setters.clear();
		return value;
	}
}
