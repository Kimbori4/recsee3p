package com.furence.recsee.common.util;

import java.security.MessageDigest;
import java.security.PrivateKey;

import javax.crypto.Cipher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.furence.recsee.login.controller.LoginController;

public class EncryptUtil {

	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

	public String decryptRsa(PrivateKey privateKey, String securedValue) {
		 String decryptedValue = "";
		 try{
			Cipher cipher = Cipher.getInstance("RSA");
		   /**
			* 암호화 된 값은 byte 배열이다.
			* 이를 문자열 폼으로 전송하기 위해 16진 문자열(hex)로 변경한다.
			* 서버측에서도 값을 받을 때 hex 문자열을 받아서 이를 다시 byte 배열로 바꾼 뒤에 복호화 과정을 수행한다.
			*/
			byte[] encryptedBytes = hexToByteArray(securedValue);
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
			decryptedValue = new String(decryptedBytes, "utf-8"); // 문자 인코딩 주의.
		 }catch(NullPointerException e)
		 {
			 logger.info("decryptRsa Exception Error : "+e.getMessage());
		 }catch(Exception e)
		 {
			 logger.info("decryptRsa Exception Error : "+e.getMessage());
		 }
			return decryptedValue;
	}
	/**
	 * 16진 문자열을 byte 배열로 변환한다.
	 */
	 public static byte[] hexToByteArray(String hex) {
		if (hex == null || hex.length() % 2 != 0) {
			return new byte[]{};
		}

		byte[] bytes = new byte[hex.length() / 2];
		for (int i = 0; i < hex.length(); i += 2) {
			byte value = (byte)Integer.parseInt(hex.substring(i, i + 2), 16);
			bytes[(int) Math.floor(i / 2)] = value;
		}
		return bytes;
	}
	 
	 
	 /**
	  * @param password 암호화할 문자열
	  * @param salt 추가 문자
	  * @return 암호화된 문자열
	  */
	// 정책 만들기 전 salt는 임시로 fcpass604!를 사용한다.
	public static String salt = "fcpass604!"; 
	public static String encryptSHA512(String password, String salt) {
		String generatedPassword = null;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			md.update(salt.getBytes("UTF-8"));
			byte[] bytes = md.digest(password.getBytes("UTF-8"));
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < bytes.length; i++) {
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}
			generatedPassword = sb.toString();
			return generatedPassword;
		} catch (NullPointerException e) {
			 logger.error("decryptRsa Exception Error : "+e.getMessage());
			return "##fail##";
		}catch (Exception e) {
			 logger.error("decryptRsa Exception Error : "+e.getMessage());
			return "##fail##";
		}
		
	}
}
