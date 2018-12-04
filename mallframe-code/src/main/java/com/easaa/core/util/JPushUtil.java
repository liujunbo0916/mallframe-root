/*package com.easaa.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.jpush.api.JPushClient;
import cn.jpush.api.common.resp.APIConnectionException;
import cn.jpush.api.common.resp.APIRequestException;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.audience.AudienceTarget;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;

*//**
 * 说明：极光推送工具类 创建人：chenlit 修改时间：2016年9月5日
 * 
 * @version
 *//*
public class JPushUtil {

	private static String masterSecret = "006a6bf39904be18c8243195";
	private static String appKey = "60d836d5eba6f81a464ac354";
	private static Logger LOG = LoggerFactory.getLogger(JPushUtil.class);
	private static String ALERT = "区你的大爷";

	private static String TITLE = "开麦开麦啦啦啦";
	private static String MSG_CONTENT = "小伙子不错哟";

	public JPushUtil(String ALERT) {
		JPushUtil.ALERT = ALERT;
	}

	*//**
	 * 所有平台，所有设备，内容为 ALERT 的通知
	 * 
	 * @return
	 *//*
	public static PushPayload buildPushObject_all_all_alert(String content) {
		return PushPayload.alertAll(content);
	}
	

	*//**
	 * 所有平台，推送目标是别名为 "alias1"，通知内容为 ALERT
	 * 
	 * @param tags
	 * @return
	 *//*
	public static PushPayload buildPushObject_all_alias_alert(String alias, String content) {
		return PushPayload.newBuilder().setPlatform(Platform.all()).setAudience(Audience.alias(alias))
				.setNotification(Notification.alert(content)).build();
	}

	*//**
	 * 平台是 Android，目标是 tag 为 "tag1" 的设备，内容是 Android 通知 ALERT，并且标题为 TITLE。
	 * 
	 * @return
	 *//*
	public static PushPayload buildPushObject_android_tag_alertWithTitle() {
		return PushPayload.newBuilder().setPlatform(Platform.android()).setAudience(Audience.tag("tag1"))
				.setNotification(Notification.android(ALERT, TITLE, null)).build();
	}

	*//**
	 * 平台是 iOS，推送目标是 "tag1", "tag_all" 的交集，推送内容同时包括通知与消息 - 通知信息是 ALERT，角标数字为 5，
	 * 通知声音为 "happy"，并且附加字段 from = "JPush"；消息内容是 MSG_CONTENT。通知是 APNs 推送通道的， 消息是
	 * JPush 应用内消息通道的。APNs 的推送环境是“生产”（如果不显式设置的话，Library 会默认指定为开发）
	 * 
	 * @return
	 *//*
	public static PushPayload buildPushObject_ios_tagAnd_alertWithExtrasAndMessage() {
		return PushPayload.newBuilder().setPlatform(Platform.ios()).setAudience(Audience.tag_and("tag1", "tag_all"))
				.setNotification(Notification.newBuilder()
						.addPlatformNotification(IosNotification.newBuilder().setAlert(ALERT).setBadge(5)
								.setSound("happy.caf").addExtra("from", "JPush").build())
						.build())
				.setMessage(Message.content(MSG_CONTENT))
				.setOptions(Options.newBuilder().setApnsProduction(true).build()).build();
	}

	*//**
	 * 构建推送对象：平台是 Andorid 与 iOS，推送目标是 （"tag1" 与 "tag2" 的并集）且（"alias1" 与 "alias2"
	 * 的并集）， 推送内容是 - 内容为 MSG_CONTENT 的消息，并且附加字段 from = JPush。
	 * 
	 * @return
	 *//*
	public static PushPayload buildPushObject_ios_audienceMore_messageWithExtras() {
		return PushPayload.newBuilder().setPlatform(Platform.android_ios())
				.setAudience(Audience.newBuilder().addAudienceTarget(AudienceTarget.tag("tag1", "tag2"))
						.addAudienceTarget(AudienceTarget.alias("alias1", "alias2")).build())
				.setMessage(Message.newBuilder().setMsgContent(MSG_CONTENT).addExtra("from", "JPush").build()).build();
	}

	*//**
	 * 指定单人推送(注册)
	 * @param alias  别名
	 * @param userGrade  注册会员等级
	 * @param user_name  注册会员名称
	 *//*
	public static void pushOneRegister(String alias,String userGrade, String user_name) {
        String content = "你的"+userGrade+"会员："+user_name+"，已经注册成功";
		
		JPushClient jpushClient = new JPushClient(masterSecret, appKey);
        
		PushPayload payload = buildPushObject_all_alias_alert(alias, content);

		try {

			PushResult result = jpushClient.sendPush(payload);

		} catch (APIConnectionException e) {

			LOG.error("Connection error, should retry later", e);

		} catch (APIRequestException e) {

			LOG.error("Should review the error, and fix the request", e);
			LOG.info("HTTP Status: " + e.getStatus());
			LOG.info("Error Code: " + e.getErrorCode());
			LOG.info("Error Message: " + e.getErrorMessage());
		}
	}
	
	*//**
	 * 指定单人推送(订单)
	 * @param alias 别名
	 * @param orderNO  订单号
	 * @param orderStatus  订单状态
	 *//*
	public static void pushOneOrder(String alias, String orderNO, String orderStatus) {
        String content = "您的订单："+orderNO+""+orderStatus+" ";
		
		JPushClient jpushClient = new JPushClient(masterSecret, appKey);
        
		PushPayload payload = buildPushObject_all_alias_alert(alias, content);

		try {

			PushResult result = jpushClient.sendPush(payload);

		} catch (APIConnectionException e) {

			LOG.error("Connection error, should retry later", e);

		} catch (APIRequestException e) {

			LOG.error("Should review the error, and fix the request", e);
			LOG.info("HTTP Status: " + e.getStatus());
			LOG.info("Error Code: " + e.getErrorCode());
			LOG.info("Error Message: " + e.getErrorMessage());
		}
	}
	
	*//**
	 * 指定单人推送(佣金)
	 * @param alias 别名
	 * @param brokerage  佣金
	 *//*
	public static void pushOneRebate(String alias, String brokerage) {
        String content = "您已成功返佣："+brokerage+" ";
		
		JPushClient jpushClient = new JPushClient(masterSecret, appKey);
        
		PushPayload payload = buildPushObject_all_alias_alert(alias, content);

		try {

			PushResult result = jpushClient.sendPush(payload);

		} catch (APIConnectionException e) {

			LOG.error("Connection error, should retry later", e);

		} catch (APIRequestException e) {

			LOG.error("Should review the error, and fix the request", e);
			LOG.info("HTTP Status: " + e.getStatus());
			LOG.info("Error Code: " + e.getErrorCode());
			LOG.info("Error Message: " + e.getErrorMessage());
		}
	}
	
	*//**
	 * 指定单人推送(佣金取消)
	 * @param alias 别名
	 *//*
	public static void pushOneRebateCancel(String alias) {
        String content = "您的返佣已经取消";
		
		JPushClient jpushClient = new JPushClient(masterSecret, appKey);
        
		PushPayload payload = buildPushObject_all_alias_alert(alias, content);

		try {

			PushResult result = jpushClient.sendPush(payload);

		} catch (APIConnectionException e) {

			LOG.error("Connection error, should retry later", e);

		} catch (APIRequestException e) {

			LOG.error("Should review the error, and fix the request", e);
			LOG.info("HTTP Status: " + e.getStatus());
			LOG.info("Error Code: " + e.getErrorCode());
			LOG.info("Error Message: " + e.getErrorMessage());
		}
	}
	

	*//**
	 * 推送所有人通知
	 * @param content 推送的信息	
	 *//*
	public static void pushAll(String content) {
		JPushClient jpushClient = new JPushClient(masterSecret, appKey);
		PushPayload payload = buildPushObject_all_all_alert(content);
		try {
			PushResult result = jpushClient.sendPush(payload);
		} catch (APIConnectionException e) {
			LOG.error("Connection error, should retry later", e);
		} catch (APIRequestException e) {
			LOG.error("Should review the error, and fix the request", e);
			LOG.info("HTTP Status: " + e.getStatus());
			LOG.info("Error Code: " + e.getErrorCode());
			LOG.info("Error Message: " + e.getErrorMessage());
		}
	}

}
*/