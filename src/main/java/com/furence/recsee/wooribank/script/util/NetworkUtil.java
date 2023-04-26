package com.furence.recsee.wooribank.script.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final public class NetworkUtil {
	private static final Logger logger = LoggerFactory.getLogger(NetworkUtil.class);
	
	private NetworkUtil() {}
	
	public static String getMacAddress() {
		
		String macAddr = null;
		
		try {
			InetAddress addr = InetAddress.getLocalHost();
			NetworkInterface ni = NetworkInterface.getByInetAddress(addr);
			byte[] mac = ni.getHardwareAddress();
			
			macAddr = Arrays.stream(ArrayUtils.toObject(mac))
					.map(b->String.format("%02x",b.byteValue()))
					.reduce("", String::concat)
					.toString();
		} catch (Exception e) {
			logger.error("error", e);
		}
		
		return macAddr;
	}
	
	public static String getIpAddress() {
		
		String ipAddr = null;
		
		try {
			InetAddress addr = InetAddress.getLocalHost();
			ipAddr = addr.getHostAddress();
		} catch (Exception e) {
			logger.error("error", e);
		}
		
		return ipAddr;
	}
}
