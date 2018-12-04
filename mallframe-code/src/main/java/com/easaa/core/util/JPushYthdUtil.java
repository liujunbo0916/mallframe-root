package com.easaa.core.util;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;

/**
 * 国士风 消息同送工具类
 * 
 * tags 普通用户：gsf_user 老师 gsf_tea 送琴员 gsf_send
 * 
 * @author Administrator
 */

public class JPushYthdUtil {

	private static final String MASTERSECRET_HXT = "2eb8223da0fb224de0979ac1";
	private static final String APPKEY_HXT = "d4992e3a9aecb901f3526c16";

	private String ALERT = "收到了么？";

	public JPushYthdUtil(String ALERT) {
		this.ALERT = ALERT;
	}

	/**
	 * 发送给所有人不带参数
	 * 
	 * @return
	 */
	public PushPayload buildPushObject_all_all_alert() {
		return PushPayload.alertAll(ALERT);
	}

	/**
	 * 发送给所有人 带参数
	 * 
	 * @param param
	 * @return
	 */
	public PushPayload buildPushObject_all_all_alert(Map<String, String> param) {
		return PushPayload.newBuilder().setPlatform(Platform.all()).setAudience(Audience.all())
				.setNotification(Notification.newBuilder().setAlert(ALERT)
						.addPlatformNotification(AndroidNotification.newBuilder().setTitle(ALERT).build())
						.addPlatformNotification(IosNotification.newBuilder().addExtras(param).build()).build())
				.build();
	}

	/**
	 * 推送给指定标签的人
	 * 
	 * @param tags
	 * @return
	 */

	public PushPayload buildPushObject_all_tags_alert(String[] tags) {
		return PushPayload.newBuilder().setPlatform(Platform.all()).setAudience(Audience.alias(tags))
				.setNotification(Notification.alert(ALERT)).build();
	}

	/**
	 * 推送给指定标签的人带参数
	 */
	public PushPayload buildPushObject_all_tags_alert(String[] tags, Map<String, String> param) {
		return PushPayload.newBuilder().setPlatform(Platform.android_ios()).setAudience(Audience.alias(tags))
				.setNotification(Notification.alert(ALERT))
				.setMessage(Message.newBuilder().setMsgContent(ALERT).addExtras(param).build()).build();
	}

	/**
	 * 发送给指定别名的人
	 * 
	 * @param alias
	 * @return
	 */
	public PushPayload buildPushObject_all_alias_alert(String[] alias) {
		return PushPayload.newBuilder().setPlatform(Platform.all()).setOptions(Options.newBuilder().setApnsProduction(true).build()).setAudience(Audience.alias(alias))
				.setNotification(Notification.alert(ALERT)).build();
	}

	/**
	 * 发送给指定别名且带参数
	 * 
	 */
	public PushPayload buildPushObject_all_alias_alert(String[] alias, Map<String, String> param) {
		return PushPayload.newBuilder().setPlatform(Platform.all()).setAudience(Audience.alias(alias))
				.setNotification(Notification.alert(ALERT))
				.setNotification(Notification.newBuilder().setAlert(ALERT)
						.addPlatformNotification(AndroidNotification.newBuilder().setTitle(ALERT).build())
						.addPlatformNotification(IosNotification.newBuilder().addExtras(param).build()).build())
				.build();
	}

	/**
	 * 推送给所有人
	 */
	public void pushAll() {
		JPushClient jpushClient = new JPushClient(MASTERSECRET_HXT, APPKEY_HXT);
		PushPayload payload = buildPushObject_all_all_alert();
		try {
			PushResult result = jpushClient.sendPush(payload);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 推送给所有人 带参数
	 */
	public void pushAll(Map<String, String> param) {
		JPushClient jpushClient = new JPushClient(MASTERSECRET_HXT, APPKEY_HXT);
		PushPayload payload = buildPushObject_all_all_alert(param);
			try {
				PushResult result = jpushClient.sendPush(payload);
			} catch (Exception e) {
			}
	}

	/**
	 * 指定某标签的人
	 */
	public void pushOneTags(String[] tags) {
		JPushClient jpushClient = new JPushClient(MASTERSECRET_HXT, APPKEY_HXT);
		PushPayload payload = buildPushObject_all_tags_alert(tags);
			try {
				PushResult result = jpushClient.sendPush(payload);
			} catch (Exception e) {
				e.printStackTrace();
			}
	}

	/**
	 * 指定某标签的人 带参数
	 */
	public void pushOneTags(String[] tags, Map<String, String> param) {
		JPushClient jpushClient = new JPushClient(MASTERSECRET_HXT, APPKEY_HXT);
		PushPayload payload = buildPushObject_all_tags_alert(tags, param);
			try {
				PushResult result = jpushClient.sendPush(payload);
			} catch (Exception e) {
				e.printStackTrace();
			}
	}

	/**
	 * 指定单人/多人
	 */
	public void pushOne(String[] ids) {
		JPushClient jpushClient = new JPushClient(MASTERSECRET_HXT, APPKEY_HXT);
		PushPayload payload = buildPushObject_all_alias_alert(ids);
			try {
				PushResult result = jpushClient.sendPush(payload);
			} catch (Exception e) {
				e.printStackTrace();
			}
	}

	/**
	 * 指定单人/多人
	 * 带参数
	 */
	public void pushOne(String[] ids,Map<String, String> param) {
		JPushClient jpushClient = new JPushClient(MASTERSECRET_HXT, APPKEY_HXT);
		PushPayload payload = buildPushObject_all_alias_alert(ids,param);
			try {
				PushResult result = jpushClient.sendPush(payload);
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
	
	/**
	 * 给指定用户推送消息
	 * @param aString 推送消息
	 * @param id 推送别名ID
	 */
	public static void userBuyPush(String aString,String id) {
		JPushYthdUtil push= new JPushYthdUtil(aString);
		String[] tags= new String[1];
		tags[0]=MD5.md5(id);
		push.pushOne(tags);
	}

}
