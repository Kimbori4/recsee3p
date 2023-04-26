package com.furence.recsee.wooribank.script.util.webclient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.StreamUtils;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
final public class WebClient <T,R>{
	
	final private Logger logger = LoggerFactory.getLogger(WebClient.class);
	
	final private int REQUEST_TIME_OUT = 3;
	
	final private int RESPONSE_TIME_OUT = 3;
	
	/**
	 * url , method
	 */
	private WebClientData clientData ;
	
	/**
	 * request payload
	 */
	private T payload;
	
	/**
	 * header content-type
	 */
	private String contentType;
	
	/**
	 * response class
	 */
	private Class<R> reseponseType;
	
	/**
	 * 성공 callback
	 */
	private SuccessCallback<R> succesCallback;

	/**
	 * 실패 callback
	 */
	private FailCallback failCallback;
	

	@FunctionalInterface
	public interface SuccessCallback<T> {
		void invoke(T t);
	}
	
	@FunctionalInterface
	public interface FailCallback {
		void invoke(HttpStatus status);
	}
	/**
	 * http 요청 sync
	 * @param <T>
	 * @param <R>
	 * @param data
	 * @param resClazz
	 * @return
	 */
	public R reqeust() {
		
		HttpEntity<T> entity = createRequestHeader();		
		return reqeust(this.reseponseType, entity);
	}

	
	/**
	 * http 요청 async
	 * @param <T>
	 * @param <R>
	 * @param data
	 * @param resClazz
	 * @return
	 */
	public void reqeustAsync() throws Exception {
		
		HttpEntity<T> entity = createRequestHeader();			
		reqeustAsync(this.reseponseType, entity);
	}
	
	/**
	 * file download
	 * @return
	 * @throws Exception
	 */
	public File download() throws Exception {
		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
		factory.setConnectTimeout( REQUEST_TIME_OUT * 1000 );
		factory.setReadTimeout(RESPONSE_TIME_OUT * 1000 );
		
		RestTemplate client = new RestTemplate( factory );		
		client.setInterceptors(Collections.singletonList(new WebClientLogger()) );
		
		return client.execute(
				clientData.getUrl(), 
				clientData.getMethod(),
				req -> {
					
					req.getHeaders().add("Content-Type", this.contentType);
					req.getHeaders().setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM, MediaType.ALL));
					
					byte[] payload = this.payload.toString().getBytes(StandardCharsets.UTF_8);
					req.getHeaders().setContentLength(payload.length);
					req.getBody().write(payload );
				},
				res -> {
					File f = File.createTempFile("tts", ".wav");
					FileOutputStream fos =  null;
					try {
						fos = new FileOutputStream(f);
						StreamUtils.copy(res.getBody(),fos);
							if(fos!=null) {
								fos.close();
							}
					}catch(IOException e){
						logger.error("error : {}", e.getMessage());
					}finally {
						if(fos != null) {
							try {
								fos.close();
							}catch (Exception e) {
								logger.error("error : {}", e.getMessage());
							}
						}
					}
					return f;
				});
	}
	
	@Builder
	public static <T,R>  WebClientBuilder<T, R> builder(String url, HttpMethod method) {
		
		WebClientBuilder<T,R> builder = new WebClientBuilder<>();
		builder.clientData = WebClientData.of(url, method);
		return builder;
	}
	
	
	/**
	 * Http 요청 헤더 생성
	 * @param <T>
	 * @param data
	 * @return
	 */
	private HttpEntity<T> createRequestHeader() {
		HttpHeaders headers = new HttpHeaders();
		if (this.contentType != null) {
			headers.add("Content-Type", this.contentType);
		}
		HttpEntity<T> entity = new HttpEntity<T>(this.payload, headers);
		return entity;
	}
	
	
	/**
	 * http 요청 sync
	 * @param <T>
	 * @param <R>
	 * @param data
	 * @param resClazz
	 * @return
	 */
	private R reqeust(Class<R> resClazz, HttpEntity<T> entity) {
		
		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
		factory.setConnectTimeout( REQUEST_TIME_OUT * 1000 );
		
		RestTemplate client = new RestTemplate( factory );	
		
		client.setInterceptors(Collections.singletonList(new WebClientLogger()) );
		ResponseEntity<R> res = client.exchange(
				clientData.getUrl(), clientData.getMethod(), entity, resClazz);
		return res.getBody();
	}
	
	/**
	 * http 요청 async
	 * @param <T>
	 * @param <R>
	 * @param data
	 * @param resClazz
	 * @return
	 */	
	private void reqeustAsync(Class<R> resClazz, HttpEntity<T> entity)
			throws InterruptedException, ExecutionException, TimeoutException {
		AsyncRestTemplate asyncClient = new AsyncRestTemplate();
		
		ListenableFuture<ResponseEntity<R>> future = asyncClient.exchange(
				clientData.getUrl(), clientData.getMethod(), entity, resClazz);
		
		ResponseEntity<R> response = future.get(REQUEST_TIME_OUT, TimeUnit.SECONDS);
		
		if( response.getStatusCode() == HttpStatus.OK ) this.succesCallback.invoke(response.getBody());
		else this.failCallback.invoke( response.getStatusCode() );
		
//		future.addCallback((ListenableFutureCallback<? super ResponseEntity<R>>) new ListenableFutureCallback<T>() {
//			@Override
//			public void onSuccess(T result) {
//				logger.info("Success:", result);
//				Optional.ofNullable(succesCallback).ifPresent( f -> { f.invoke(result); });
//			}
//
//			@Override
//			public void onFailure(Throwable t) {
//				logger.info("Fail:", t);
//				Optional.ofNullable(failCallback).ifPresent( f -> { f.invoke(t); });
//			}
//			
//		});
		
	}
}

@Getter
@Setter
@AllArgsConstructor(staticName = "of")
class WebClientData {
	
	private String url;
	
	private HttpMethod method;
}
