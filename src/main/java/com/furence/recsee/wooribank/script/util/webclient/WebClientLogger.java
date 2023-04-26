package com.furence.recsee.wooribank.script.util.webclient;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;

import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import com.initech.inisafenet.iniplugin.log.Logger;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WebClientLogger implements ClientHttpRequestInterceptor{
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(WebClientLogger.class);
	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
			throws IOException {
		URI uri = request.getURI();
		traceRequest(request, body);
		
		ClientHttpResponse response = execution.execute(request, body);
		traceResponse(response, uri);
		
		return response;
	}
	
	private void traceRequest(HttpRequest request, byte[] body)  {
		StringBuilder sb = new StringBuilder();
		sb.append("[Request]")
			.append("\nUri:").append(request.getURI())
			.append("\nMethod:").append(request.getMethod())
			.append("\nRequest Body:").append(new String(body, StandardCharsets.UTF_8) );			
			
		log.info(sb.toString());
	}
	
	private void traceResponse(ClientHttpResponse response, URI uri) {
		
		try {
			
			StringBuilder sb = new StringBuilder();
			sb.append("[Response]")
				.append("\nUri:").append(uri)
				.append("\nStatus:").append(response.getStatusCode());
			log.info(sb.toString());
		} catch (Exception e) {
			logger.info("error: "+ e.getMessage());
		}
	}
}
