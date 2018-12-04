package com.easaa.plugin.IM.hx.logic.utils;

import java.io.File;
import java.util.Arrays;

import org.apache.commons.lang.StringUtils;

import com.easaa.core.util.EADate;
import com.easaa.core.util.EAString;
import com.easaa.core.util.FtpUtil;
import com.easaa.entity.PageData;
import com.easaa.plugin.IM.hx.common.Constants;
import com.easaa.plugin.IM.hx.logic.api.EasemobFiles;
import com.easaa.plugin.IM.hx.logic.api.EasemobIMUsers;
import com.easaa.plugin.IM.hx.logic.api.EasemobMessages;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class EasemobIMUtils {
	  private static JsonNodeFactory factory = new JsonNodeFactory(false);
	
	/**
	 * 用户注册
	 * @throws Exception 
	 */
	public static ObjectNode regist(PageData pageData) throws Exception{
	    ObjectNode datanode = JsonNodeFactory.instance.objectNode();
        datanode.put("username",pageData.getAsString("username"));
        datanode.put("password", pageData.getAsString("password"));
        datanode.put("nickname", pageData.getAsString("nickname"));
        ObjectNode createNewIMUserSingleNode = EasemobIMUsers.createNewIMUserSingle(datanode);
        if(!"200".equals(createNewIMUserSingleNode.get("statusCode"))){
        	throw new Exception(createNewIMUserSingleNode.get("error").toString());
        }
        return createNewIMUserSingleNode;
	}
	
	/**
	 * 用户登陆
	 * 
	 * 
	 */
	public static ObjectNode login(PageData pageData) throws Exception{
        ObjectNode createNewIMUserSingleNode = EasemobIMUsers.imUserLogin(pageData.getAsString("username"),pageData.getAsString("password"));
        if(!"200".equals(createNewIMUserSingleNode.get("statusCode"))){
        	throw new Exception(createNewIMUserSingleNode.get("error").toString());
        }
        return createNewIMUserSingleNode;
	}
	/**
	 * 发送消息
	 * @throws Exception 
	 * fromUser：发送人的环信账号
	 * toUsers:发送对象 逗号隔开
	 * messageType:0文本消息  1图片消息  2 语音消息  默认文本消息
	 * targetTypeus：群组聊天或是用户聊天   users或chatgroups
	 * content_msg:发送内容  如果type为0 的时候 对应的是文本  1的时候对应的是图片是     2的时候对应的是语音
	 */
	@SuppressWarnings({ "restriction", "unused" })
	public static PageData sendMessage(PageData pd) throws Exception{
		String messageType  = StringUtils.isNotEmpty(pd.getAsString("messageType"))?pd.getAsString("messageType"):"0";
		String targetTypeus = StringUtils.isNotEmpty(pd.getAsString("targetTypeus"))?pd.getAsString("targetTypeus"):"users";
		String users = pd.getAsString("toUsers");
		sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
		PageData resultNode = new PageData();
        if(StringUtils.isEmpty(users)){
			throw new Exception("用户不能为空");
		}
		String[] usersAry = users.split("\\,");
		
		if(users.length() == 0){
			throw new Exception("用户不能为空");
		}
		//发送消息的用户集合
		ArrayNode targetusers = factory.arrayNode();
		for (int i = 0; i < usersAry.length; i++) {
			targetusers.add(usersAry[i]);
		}
		//发送消息对象
		ObjectNode msgON = factory.objectNode();
		if("0".equalsIgnoreCase(messageType)){
			     //文本消息
			msgON.put("msg", pd.getAsString("content_msg"));
			msgON.put("type","txt");
			resultNode.put("content",pd.getAsString("content_msg"));
			resultNode.put("contentType","TXT");
		}else if("1".equalsIgnoreCase(messageType)){
			//先将图片信息存在本地
			byte[] bt = decoder.decodeBuffer(pd.getAsString("content_msg"));
			//上传图片
			File chartFile = FtpUtil.tofile(bt, "/gchart/imgaes/", EAString.getFourSn()+".jpg");
	        ObjectNode imgDataNode = EasemobFiles.mediaUpload(chartFile);
	        //聊天图片存储到图片服务器
	        String storePath = FtpUtil.upload(chartFile, "gchart", "images");
	        String imgFileUUID = imgDataNode.path("entities").get(0).path("uuid").asText();
	        String shareSecret = imgDataNode.path("entities").get(0).path("share-secret").asText();
	        if (null != imgDataNode) {
	        	throw new Exception("图片发送失败");
	        }
	        msgON.put("type","img");
	        msgON.put("url", HTTPClientUtils.getURL(Constants.APPKEY.replace("#", "/") + "/chatfiles/" + imgFileUUID).toString());
	        msgON.put("filename", "24849.jpg");
	        msgON.put("length", 10);
	        msgON.put("secret", shareSecret);
	        //返回到控制端的对象
	        resultNode.put("content",storePath);
			resultNode.put("contentType","IMG");
		}else if("2".equalsIgnoreCase(messageType)){
			//语音消息
			byte[] bt = decoder.decodeBuffer(pd.getAsString("content_msg"));
			File chartFile = FtpUtil.tofile(bt, "/gchart/imgaes/", EAString.getFourSn()+".MP3");
			ObjectNode imgDataNode = EasemobFiles.mediaUpload(chartFile);
			String storePath = FtpUtil.upload(chartFile, "gchart", "voice");
			resultNode.put("contentType","VOI");
			resultNode.put("content",storePath);
			String audioFileUUID = imgDataNode.path("entities").get(0).path("uuid").asText();
		    String audioFileShareSecret = imgDataNode.path("entities").get(0).path("share-secret").asText();
		    msgON.put("type","audio");
		    msgON.put("url", HTTPClientUtils.getURL(Constants.APPKEY.replace("#", "/") + "/chatfiles/" + audioFileUUID).toString());
		    msgON.put("filename", "music.MP3");
		    msgON.put("length", 10);
		    msgON.put("secret", audioFileShareSecret);
		}
		ObjectNode objectN = EasemobMessages.sendMessages(targetTypeus, targetusers, msgON, pd.getAsString("from_user"), factory.objectNode());		
		if(!"200".equalsIgnoreCase(objectN.get("statusCode").toString())){
			throw new Exception(objectN.get("error").toString());
		}
		return resultNode;
	}
	
	/**
	 * 发送透传消息
	 * @throws Exception 
	 * 
	 * 
	 */
	public static PageData sendPushMessage(PageData pd) throws Exception{
		PageData resultPd = new PageData();
		String users = StringUtils.isNotEmpty(pd.getAsString("toUsers"))?pd.getAsString("toUsers"):"";
		String action = pd.getAsString("action");
		String[] usersAry = users.split(",");
		String msg = pd.getAsString("msg");
		resultPd.put("action", action);
		resultPd.put("toUsers", usersAry);
		resultPd.put("msg", msg);
		resultPd.put("fromUsers", Constants.APP_ADMIN_NAME1);
		ArrayNode targetusers = factory.arrayNode();
		for (int i = 0; i < usersAry.length; i++) {
			targetusers.add(usersAry[i]);
		}
		if(usersAry.length == 0){
			throw new Exception("发送人不能为空");
		}
		if(StringUtils.isEmpty(action)){
			throw new Exception("操作不能为空");
		}
		ObjectNode sendcmdMessageusernode =  EasemobMessages.sendPushMessages(targetusers, msg, action);
		if("200".equalsIgnoreCase(sendcmdMessageusernode.get("statusCode")+"")){
			throw new Exception(sendcmdMessageusernode.get("error").toString());
		}
		return resultPd;
	}
	
	
	
}
