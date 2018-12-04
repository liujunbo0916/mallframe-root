package com.easaa.plugin.IM.hx.logic.api;

import java.net.URL;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.easaa.plugin.IM.hx.common.Constants;
import com.easaa.plugin.IM.hx.common.HTTPMethod;
import com.easaa.plugin.IM.hx.common.Roles;
import com.easaa.plugin.IM.hx.logic.utils.HTTPClientUtils;
import com.easaa.plugin.IM.hx.logic.vo.ClientSecretCredential;
import com.easaa.plugin.IM.hx.logic.vo.Credential;
import com.easaa.plugin.IM.hx.logic.vo.EndPoints;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * REST API Demo: 发送消息 REST API HttpClient4.3实现
 * 
 * Doc URL: http://www.easemob.com/docs/rest/sendmessage/
 * 
 * @author liujunbo 2014-09-15
 *
 */
public class EasemobMessages {

	private static Logger LOGGER = LoggerFactory.getLogger(EasemobMessages.class);
    private static final String APPKEY = Constants.APPKEY;
    private static JsonNodeFactory factory = new JsonNodeFactory(false);

    // 通过app的client_id和client_secret来获取app管理员token
    private static Credential credential = new ClientSecretCredential(Constants.APP_CLIENT_ID,
            Constants.APP_CLIENT_SECRET, Roles.USER_ROLE_APPADMIN);

    public static void main(String[] args) {
//        //  检测用户是否在线
//        String targetuserPrimaryKey = "daixin001";
//        ObjectNode usernode = getUserStatus(targetuserPrimaryKey);
//        if (null != usernode) {
//            LOGGER.info("检测用户是否在线: " + usernode.toString());
//            //System.out.println(usernode.toString());
//        }
////        // 给用户发一条文本消息
        String from = "";
        String targetTypeus = "users";
        ObjectNode ext = factory.objectNode();
        ArrayNode targetusers = factory.arrayNode();
        targetusers.add("dx_18202811287");
       // targetusers.add("kenshinnuser002");
        ObjectNode txtmsg = factory.objectNode();
        txtmsg.put("msg", "Hello Easemob!");
        txtmsg.put("type","txt");
        ObjectNode sendTxtMessageusernode = sendMessages(targetTypeus, targetusers, txtmsg, from, ext);
        if (null != sendTxtMessageusernode) {
            LOGGER.info("给用户发一条文本消息: " + sendTxtMessageusernode.toString());
            System.out.println("给用户发一条文本消息: " + sendTxtMessageusernode.toString());
        }
        // 给一个群组发文本消息
        String targetTypegr = "chatgroups";
        ArrayNode  chatgroupidsNode = (ArrayNode) EasemobChatGroups.getAllChatgroupids().path("data");
        ArrayNode targetgroup = factory.arrayNode();
        targetgroup.add(chatgroupidsNode.get(0).path("groupid").asText());
        ObjectNode sendTxtMessagegroupnode = sendMessages(targetTypegr, targetgroup, txtmsg, from, ext);
        if (null != sendTxtMessagegroupnode) {
            LOGGER.info("给一个群组发文本消息: " + sendTxtMessagegroupnode.toString());
        }

        // 给用户发一条图片消息
       /* File uploadImgFile = new File("/home/lynch/Pictures/24849.jpg");
        ObjectNode imgDataNode = EasemobFiles.mediaUpload(uploadImgFile);
        String imgFileUUID = imgDataNode.path("entities").get(0).path("uuid").asText();
        String shareSecret = imgDataNode.path("entities").get(0).path("share-secret").asText();
        if (null != imgDataNode) {
            LOGGER.info("上传图片文件: " + imgDataNode.toString());
        }
        ObjectNode imgmsg = factory.objectNode();
        imgmsg.put("type","img");
        imgmsg.put("url", HTTPClientUtils.getURL(Constants.APPKEY.replace("#", "/") + "/chatfiles/" + imgFileUUID).toString());
        imgmsg.put("filename", "24849.jpg");
        imgmsg.put("length", 10);
        imgmsg.put("secret", shareSecret);
        ObjectNode sendimgMessageusernode = sendMessages(targetTypeus, targetusers, imgmsg, from, ext);
        if (null != sendimgMessageusernode) {
            LOGGER.info("给一个群组发文本消息: " + sendimgMessageusernode.toString());
        }
        // 给一个群组发图片消息
        ObjectNode sendimgMessagegroupnode = sendMessages(targetTypegr, targetgroup, imgmsg, from, ext);
        if (null != sendimgMessagegroupnode) {
            LOGGER.info("给一个群组发文本消息: " + sendimgMessagegroupnode.toString());
        }
        // 给用户发一条语音消息
        File uploadAudioFile = new File("/home/lynch/Music/music.MP3");
        ObjectNode audioDataNode = EasemobFiles.mediaUpload(uploadAudioFile);
        String audioFileUUID = audioDataNode.path("entities").get(0).path("uuid").asText();
        String audioFileShareSecret = audioDataNode.path("entities").get(0).path("share-secret").asText();
        if (null != audioDataNode) {
            LOGGER.info("上传语音文件: " + audioDataNode.toString());
        }
        ObjectNode audiomsg = factory.objectNode();
        audiomsg.put("type","audio");
        audiomsg.put("url", HTTPClientUtils.getURL(Constants.APPKEY.replace("#", "/") + "/chatfiles/" + audioFileUUID).toString());
        audiomsg.put("filename", "music.MP3");
        audiomsg.put("length", 10);
        audiomsg.put("secret", audioFileShareSecret);
        ObjectNode sendaudioMessageusernode = sendMessages(targetTypeus, targetusers, audiomsg, from, ext);
        if (null != sendaudioMessageusernode) {
            LOGGER.info("给用户发一条语音消息: " + sendaudioMessageusernode.toString());
        }*/
//
//        // 给一个群组发语音消息
//        ObjectNode sendaudioMessagegroupnode = sendMessages(targetTypegr, targetgroup, audiomsg, from, ext);
//        if (null != sendaudioMessagegroupnode) {
//            LOGGER.info("给一个群组发语音消息: " + sendaudioMessagegroupnode.toString());
//        }
//
//        // 给用户发一条透传消息
//        ObjectNode cmdmsg = factory.objectNode();
//        cmdmsg.put("action", "gogogo");
//        cmdmsg.put("type","cmd");
//        ObjectNode sendcmdMessageusernode = sendMessages(targetTypeus, targetusers, cmdmsg, from, ext);
//        if (null != sendcmdMessageusernode) {
//            LOGGER.info("给用户发一条透传消息: " + sendcmdMessageusernode.toString());
//            System.out.println(sendcmdMessageusernode.toString());
//        }
        //Baihua 编写
    /*    ObjectNode sendcmdMessageusernodeTest = sendPushMessages("dx000018", "推送消息", "collectionShop");//sendDaixinMessages("kenshinnuser100", "消息ss");
        if (null != sendcmdMessageusernodeTest) {
          LOGGER.info("给用户发一条透传消息: " + sendcmdMessageusernodeTest.toString());
          System.out.println(sendcmdMessageusernodeTest.toString());
      }*/
        
        ObjectNode ob =  getUserStatus("gsf002");
        System.out.println(ob);
    }

	/**
	 * 检测用户是否在线
	 * 
	 * @param username
     *
	 * @return
	 */
	public static ObjectNode getUserStatus(String username) {
		ObjectNode objectNode = factory.objectNode();

		// check appKey format
		if (!HTTPClientUtils.match("^(?!-)[0-9a-zA-Z\\-]+#[0-9a-zA-Z]+", APPKEY)) {
			LOGGER.error("Bad format of Appkey: " + APPKEY);
			objectNode.put("message", "Bad format of Appkey");
			objectNode.put("statusCode", "400");
			return objectNode;
		}

		// check properties that must be provided
		if (StringUtils.isEmpty(username)) {
			LOGGER.error("You must provided a targetUserPrimaryKey .");
			objectNode.put("message", "You must provided a targetUserPrimaryKey .");
			objectNode.put("statusCode", "400");
			return objectNode;
		}

		try {
			URL userStatusUrl = HTTPClientUtils.getURL(Constants.APPKEY.replace("#", "/") + "/users/"
					+ username + "/status");
			objectNode = HTTPClientUtils.sendHTTPRequest(userStatusUrl, credential, null, HTTPMethod.METHOD_GET);
			String userStatus = objectNode.get("data").path(username).asText();
			if ("online".equals(userStatus)) {
				LOGGER.error(String.format("The status of user[%s] is : [%s] .", username, userStatus));
			} else if ("offline".equals(userStatus)) {
				LOGGER.error(String.format("The status of user[%s] is : [%s] .", username, userStatus));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return objectNode;
	}

	/**
	 * 发送消息
	 * 
	 * @param targetType
	 *            消息投递者类型：users 用户, chatgroups 群组
	 * @param target
	 *            接收者ID 必须是数组,数组元素为用户ID或者群组ID
	 * @param msg
	 *            消息内容
	 * @param from
	 *            发送者
	 * @param ext
	 *            扩展字段
	 * 
	 * @return 请求响应
	 */
	public static ObjectNode sendMessages(String targetType, ArrayNode target, ObjectNode msg, String from,
			ObjectNode ext) {

		ObjectNode objectNode = factory.objectNode();

		ObjectNode dataNode = factory.objectNode();

		// check appKey format
		if (!HTTPClientUtils.match("^(?!-)[0-9a-zA-Z\\-]+#[0-9a-zA-Z]+", APPKEY)) {
			LOGGER.error("Bad format of Appkey: " + APPKEY);

			objectNode.put("message", "Bad format of Appkey");

			return objectNode;
		}

		// check properties that must be provided
		if (!("users".equals(targetType) || "chatgroups".equals(targetType))) {
			LOGGER.error("TargetType must be users or chatgroups .");

			objectNode.put("message", "TargetType must be users or chatgroups .");

			return objectNode;
		}

		try {
			// 构造消息体
			dataNode.put("target_type", targetType);
			dataNode.put("target", target);
			dataNode.put("msg", msg);
			dataNode.put("from", from);
			dataNode.put("ext", ext);

			objectNode = HTTPClientUtils.sendHTTPRequest(EndPoints.MESSAGES_URL, credential, dataNode,
					HTTPMethod.METHOD_POST);

			objectNode = (ObjectNode) objectNode.get("data");
			for (int i = 0; i < target.size(); i++) {
				String resultStr = objectNode.path(target.path(i).asText()).asText();
				if ("success".equals(resultStr)) {
					LOGGER.error(String.format("Message has been send to user[%s] successfully .", target.path(i)
							.asText()));
				} else if (!"success".equals(resultStr)) {
					LOGGER.error(String.format("Message has been send to user[%s] failed .", target.path(i).asText()));
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return objectNode;
	}

	
	/**
	 * 给用户发一条透传消息
	 * @param target
	 * 		接收者ID 用户ID或者群组ID
	 * @param msg
	 * 		消息内容
	 * @param action
	 * 		推送类型
	 * @return
	 */
	public static ObjectNode sendPushMessages( ArrayNode targetusers, String msg, String action) {
        // 给用户发一条文本消息
        String from = Constants.APP_ADMIN_NAME1;
        String targetTypeus = "users";
        ObjectNode cmdmsg = factory.objectNode();
        cmdmsg.put("action", action);
        cmdmsg.put("type","cmd");
        ObjectNode ext = factory.objectNode();
        ext.put("msg", msg);
        ObjectNode sendcmdMessageusernode = sendMessages(targetTypeus, targetusers, cmdmsg, from, ext);
        if (null != sendcmdMessageusernode) {
            LOGGER.info("给用户发一条透传消息: " + sendcmdMessageusernode.toString());
        }
        return sendcmdMessageusernode;
	}
	
	/**
	 * 批量发送透传消息
	 * @param target
	 * 		接收者ID 用户ID或者群组ID
	 * @param msg
	 * 		消息内容
	 * @param action
	 * 		推送类型
	 * @return
	 */
	public static ObjectNode sendPushsMessages(List<String> target, String msg, String action) {
        // 给用户发一条文本消息
        String from = Constants.APP_ADMIN_NAME1;
        String targetTypeus = "users";
        ArrayNode targetusers = factory.arrayNode();
        for (int i = 0; i < target.size(); i++) {
            targetusers.add(target.get(i));
		}
        ObjectNode cmdmsg = factory.objectNode();
        cmdmsg.put("action", action);
        cmdmsg.put("type","cmd");
        ObjectNode ext = factory.objectNode();
        ext.put("msg", msg);
        ObjectNode sendcmdMessageusernode = sendMessages(targetTypeus, targetusers, cmdmsg, from, ext);
        if (null != sendcmdMessageusernode) {
            LOGGER.info("批量发送了透传消息: " + sendcmdMessageusernode.toString());
        }
        return sendcmdMessageusernode;
	}
	
	/**
	 * 给用户发一条文本消息（透传消息）
	 * @param target
	 * 		接收者ID 必须是数组,数组元素为用户ID或者群组ID
	 * @param msg
	 * 		消息内容
	 * @return
	 */
	public static ObjectNode sendDaixinMessages(String target, String msg) {
        // 给用户发一条文本消息
        String from = Constants.ORG_ADMIN_USERNAME;
        String targetTypeus = "users";
        ArrayNode targetusers = factory.arrayNode();
        targetusers.add(target);
        ObjectNode txtmsg = factory.objectNode();
        txtmsg.put("msg", msg);
        txtmsg.put("type","txt");
        ObjectNode ext = factory.objectNode();
        ObjectNode sendcmdMessageusernode = sendMessages(targetTypeus, targetusers, txtmsg, from, ext);
        if (null != sendcmdMessageusernode) {
            LOGGER.info("给用户发一条透传消息: " + sendcmdMessageusernode.toString());
        }
        return sendcmdMessageusernode;
	}
}
