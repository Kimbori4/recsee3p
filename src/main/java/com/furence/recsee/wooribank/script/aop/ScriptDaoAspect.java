package com.furence.recsee.wooribank.script.aop;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import com.furence.recsee.wooribank.script.annotation.Translate;

@Aspect
@Component
public class ScriptDaoAspect {

	@Pointcut("execution(* com.furence.recsee.wooribank.script.service.Script*.*(..))" )
	public void servicePointcut() {};
	
	@Around("servicePointcut()")
	public Object argumentsHtmlEscpase(ProceedingJoinPoint pjp) throws Throwable {
		
		Arrays.asList(pjp.getArgs()).stream()
			.forEach(arg-> {
				if(arg == null) return;
				Arrays.asList(arg.getClass().getDeclaredFields()).stream()
					.filter(f-> f.getAnnotation(Translate.class) != null )
					.forEach(f -> translateFieldValue(arg, f));
			});
		
		return pjp.proceed();
	}
		
	private void translateFieldValue(Object obj, Field field ){	
		
		try {
			Translate trans = (Translate) field.getAnnotation(Translate.class);
			
			field.setAccessible(true);  
			String value = (String)field.get(obj);
			
			if(value != null) {				
				for(Translate.TranslateTypes transType : Arrays.asList(trans.value()) ) {
					value = transType.translate(String.valueOf(value));
				}								
				field.set(obj, value);
			}
		} catch (Exception e) {
			e.getMessage();
		}	
	}
	
}
