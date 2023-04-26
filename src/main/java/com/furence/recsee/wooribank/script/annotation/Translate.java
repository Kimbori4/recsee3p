package com.furence.recsee.wooribank.script.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.web.util.HtmlUtils;

import lombok.Getter;

@Documented
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Translate {
	
	interface TranslateFunction<T, U>  {
		 U translate (T t);
	}

	enum TranslateTypes implements TranslateFunction<String, String>{
		
		HtmlEsacpe("htmlEscape"){
			@Override
			public String translate(String str) {
				return HtmlUtils.htmlEscape(str);
			}
		},
		HtmlUnesacpe("htmlUnescape"){
			@Override
			public String translate(String tag) {
				return HtmlUtils.htmlUnescape(tag);
			}
		},
		WhiteSpaceRemove("whitespaceRemove"){
			@Override
			public String translate(String str) {
				return str.replaceAll("\\p{Z}"," ");
			}
		};
		
		@Getter
		private String[] values;
		
		TranslateTypes(String[] values){
			this.values = values;
		}
		
		TranslateTypes(String value){
			this.values = new String[1];
			this.values[0] = value;
		}
		
	}
	
	TranslateTypes[] value() default TranslateTypes.HtmlEsacpe;	
}
