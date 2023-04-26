//package com.furence.recsee.common.util;
//
//import java.io.BufferedReader;
//import java.io.BufferedWriter;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.OutputStreamWriter;
//import java.net.InetAddress;
//import java.net.InetSocketAddress;
//import java.net.Socket;
//import java.net.SocketAddress;
//import java.net.SocketTimeoutException;
//
//import com.initech.shttp.server.Logger;
//
//public class TcpClient {
//
//	Socket socket; // socket 연결 resource
//	Integer connectTimeout = 10; // 서버 접속 지연 타임아웃 
//	Integer responseTimeout = 5; // 메세지 응답 지연 타임아웃
//	
//	public String serverConnect(String ip, Integer port) {
//		try {
//			InetAddress inetAddress = InetAddress.getByName(ip);
//			SocketAddress socketAddress = new InetSocketAddress(inetAddress, port);
//			
//			
//			this.socket = new Socket();
//			
//			int timeoutInMs = connectTimeout * 1000;
//			int responseTimeoutInMs = responseTimeout * 1000;
//			
//			this.socket.connect(socketAddress, timeoutInMs);
//			this.socket.setSoTimeout(responseTimeoutInMs);
//			
//			return "success";
//			
//		/*
//		 * Server 접속 타임 아웃 발생 시
//		 */
//		} catch (SocketTimeoutException ste) {
//			Logger.error("", "", "", ste.toString());
//
//			return "error";
//		/*
//		 * 일반 에러 발생 시 
//		 */
//		} catch (Exception e) {
//			Logger.error("", "", "", e.toString());
//			return "error";
//		}
//	}
//	
//	public String serverSendMsg(String msg) {
//		try {
//			/*
//			 * Server 로 메세지 전달
//			 */
//			BufferedWriter bufferWriter = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
//			bufferWriter.write(msg);
//			bufferWriter.flush();
//			
//			/*
//			 * Server의 응답 메시진 수신
//			 */
//			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
//			StringBuilder sb = new StringBuilder();
//			String str;
//			while((str = bufferedReader.readLine()) != null) {
//				sb.append(str);
//			}
//			
//			bufferedReader.close();
//			
//			return sb.toString();
//			
//		/*
//		 * 타임아웃 발생 시
//		 */
//		} catch (SocketTimeoutException ste) {
//			Logger.error("", "", "", ste.toString());;
//
//			return "error";
//		/*
//		 * 일반 I/O 에러 발생 시
//		 */
//		} catch (IOException e) {
//			Logger.error("", "", "", e.toString());
//			
//			return "error";
//		}
//	}
//}
