package com.furence.recsee.common.util;

import java.util.Properties;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.furence.recsee.common.model.SendMailInfo;
import com.furence.recsee.common.service.LogInfoService;
import com.initech.shttp.server.Logger;



public class SendMailUtil {
	@Autowired
	private static LogInfoService logInfoService;
	
	public static boolean sendMail(HttpServletRequest request, SendMailInfo sendMailInfo) {
		boolean rst = true;
		
		try {
	        JavaMailSenderImpl sender = new JavaMailSenderImpl();
	        sender.setHost(sendMailInfo.getHost().trim());
	        if(sendMailInfo.getProtocal() != null && !sendMailInfo.getProtocal().trim().isEmpty()) {
		        sender.setProtocol(sendMailInfo.getProtocal().trim());
	        }
	        sender.setPort(sendMailInfo.getPort());
	        if(sendMailInfo.getUserName() != null && !sendMailInfo.getUserName().trim().isEmpty()) {
	        	sender.setUsername(sendMailInfo.getUserName().trim());
	        }
	        if(sendMailInfo.getPassword() != null && !sendMailInfo.getPassword().trim().isEmpty()) {
	        	sender.setPassword(sendMailInfo.getPassword().trim());
	        }
	        sender.setDefaultEncoding(sendMailInfo.getDefaultEncoding().trim());
	        
	        Properties prop = new Properties();
	        prop.setProperty("mail.smtp.auth", sendMailInfo.getSmtpAuth().trim());
	        prop.setProperty("mail.smtp.starttls.enable", sendMailInfo.getStartTls().trim());
	        sender.setJavaMailProperties(prop);
	    
	        MimeMessage message = sender.createMimeMessage();
	        MimeMessageHelper helper = new MimeMessageHelper(message, true, sendMailInfo.getDefaultEncoding().trim());
	        
	        helper.setFrom(sendMailInfo.getFrom());
	        if(sendMailInfo.getFromName() != null && !sendMailInfo.getFromName().trim().isEmpty()) {
	        	helper.setFrom(new InternetAddress(sendMailInfo.getFrom(), sendMailInfo.getFromName().trim(), sendMailInfo.getDefaultEncoding().trim()));
	        }
	        helper.setTo(sendMailInfo.getTo());
	        if(sendMailInfo.getToName() != null && !sendMailInfo.getToName().trim().isEmpty()) {
	        	helper.setTo(new InternetAddress(sendMailInfo.getTo(), sendMailInfo.getToName().trim(), sendMailInfo.getDefaultEncoding().trim()));
	        }
	        helper.setSubject(sendMailInfo.getSubject());
	        helper.setText(sendMailInfo.getText());
	        
	        sender.send(message);
			
		} catch (NullPointerException e) {
			rst = false;
			Logger.error("", "", "", e.toString());
			logInfoService.writeLog(request, "Send Mail Exception");
		}catch (Exception e) {
			rst = false;
			Logger.error("", "", "", e.toString());
			logInfoService.writeLog(request, "Send Mail Exception");
		} finally {
			rst = true;
		}
		return rst;
    }
	
}
