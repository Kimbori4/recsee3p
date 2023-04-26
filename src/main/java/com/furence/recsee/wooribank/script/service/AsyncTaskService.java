package com.furence.recsee.wooribank.script.service;

import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

@EnableAsync
@Service("asyncTaskService")
public class AsyncTaskService {

	@FunctionalInterface
	public interface AsyncTask {		
		void run() throws Exception;
	}
	
	@FunctionalInterface
	public interface AsyncFutureTask<T> {		
		abstract T run() throws Exception;
	}
	
	/**
	 * 리턴이 있는 비동기 작업 
	 * @param <T>
	 * @param task
	 * @return
	 */
	@Async
	public <T> CompletableFuture<T> execute( AsyncFutureTask<T> task ) throws Exception {
		return  CompletableFuture.completedFuture( task.run());
	}
	
	/**
	 * 리턴 없는 비동기 작업 실행
	 * @param task
	 */
	@Async
	public void execute( AsyncTask task ) throws Exception {		
		task.run();
	}
}
