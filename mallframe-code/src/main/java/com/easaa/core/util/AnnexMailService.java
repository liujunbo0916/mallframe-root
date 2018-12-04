package com.easaa.core.util;

import java.io.File;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.lang3.StringUtils;

public class AnnexMailService {
     
	private static String contentTpl = "<!DOCTYPE html><html><head><meta charset='UTF-8'><title>国士风社企认证资料</title><style type='text/css'>.container{font-family: 'Microsoft YaHei';width: 600px;margin: 0 auto;padding: 8px;border: 3px dashed #db303f;border-radius: 6px;} .title{text-align: center;color: #db303f;}.content{ text-align: justify;color: #717273;font-weight: 600;}footer{text-align: right;color: #db303f;font-weight: 600;font-size: 18px;}</style></head><body><div class='container'><h2 class='title'>2018年度社会企业认证资料</h2><p class='content'>请在下载附件中的社企认证资料，请填写《2018年度中国慈展会社会企业认证申请表》，以图片或者扫面件的形式上传到国士风app。</p><footer>联系国士风平台：13631705905</footer></div></body></html>";
	
	
	
	 public static boolean sendMail(String subject, String toMail,  
	            String content, String... files) {  
	        boolean isFlag = false;  
	        try {  
	            String smtpFromMail = "shirley.sun@iguoshifeng.com";  //账号  
	            String pwd = "HuangDaoWeiYi123"; //密码  
	            int port = 465; //端口  
	            String host = "smtp.exmail.qq.com"; //端口  
	            Properties props = new Properties();  
	            props.put("mail.smtp.host", host);  
	            props.put("mail.smtp.auth", "true");  
	            props.put("mail.smtp.ssl.enable", "true");  
	            Session session = Session.getDefaultInstance(props);  
	            session.setDebug(false);  
	            MimeMessage message = new MimeMessage(session);  
	            try {  
	                message.setFrom(new InternetAddress(smtpFromMail, "2018年度社会企业认证资料"));  
	                message.addRecipient(Message.RecipientType.TO,  
	                        new InternetAddress(toMail));  
	                message.setSubject(subject);  
	                message.addHeader("charset", "UTF-8");  
	                  
	                /*添加正文内容*/  
	                Multipart multipart = new MimeMultipart();  
	                BodyPart contentPart = new MimeBodyPart();  
	                if(StringUtils.isNotEmpty(content)){
	                	contentPart.setText(content);  
	                }else{
	                	contentPart.setText(contentTpl);
	                }
	                contentPart.setHeader("Content-Type", "text/html; charset=UTF-8");  
	                multipart.addBodyPart(contentPart);  
	                /*添加附件*/  
	                for (String file : files) {  
	                    File usFile = new File(file);  
	                    MimeBodyPart fileBody = new MimeBodyPart();  
	                    DataSource source = new FileDataSource(file);  
	                    fileBody.setDataHandler(new DataHandler(source));  
	                    sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder();  
	                    fileBody.setFileName("=?UTF-8?B?"  
	                            + enc.encode(usFile.getName().getBytes()) + "?=");  
	                    multipart.addBodyPart(fileBody);  
	                }  
	                message.setContent(multipart);  
	                message.setSentDate(new Date());  
	                message.saveChanges();  
	                Transport transport = session.getTransport("smtp");  
	                transport.connect(host,port,smtpFromMail, pwd);  
	                transport.sendMessage(message, message.getAllRecipients());  
	                transport.close();  
	                isFlag = true;  
	            } catch (Exception e) {  
	            	e.printStackTrace();
	                isFlag = false;  
	            }  
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        }  
	        return isFlag;  
	    }  
	  
	    public static void main(String[] args) {  
	        AnnexMailService.sendMail("你好", "1452884372@qq.com", "nimabi" ,   
	                "D:/2013-01.jpg");  
	    }  
}
