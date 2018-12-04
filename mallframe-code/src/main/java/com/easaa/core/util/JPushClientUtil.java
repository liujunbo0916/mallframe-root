/*package com.easaa.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.jpush.api.JPushClient;
import cn.jpush.api.common.resp.APIConnectionException;
import cn.jpush.api.common.resp.APIRequestException;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.Notification;
import cn.jpush.api.report.ReceivedsResult;

*//**
 * 说明：极光推送工具类 创建人：wanjb 修改时间：2016年6月25日
 * 
 * @version
 *//*
public class JPushClientUtil {

	private String masterSecret_IOS_teacher = "2e9a25d3cb8e1a318089d439";
	private String appKey_IOS_teacher = "177ef48924e50b53cad2ac54";
	
	private String masterSecret_IOS_custodian = "2406ce4ca5b1f6cda70338f2";
	private String appKey_IOS_custodian = "db105e1acf2719bcd7278515";
	
	private String masterSecret_android_teacher = "4174a3188f51104bc233ac1f";
	private String appKey_android_teacher = "d1ef9474ce9abc9cce56cb77";
	
	private String masterSecret_android_custodian = "9c86153dad796061ae8e0eae";
	private String appKey_android_custodian = "3d49dad6d5035e23cfe3f52c";
	private Logger LOG = LoggerFactory.getLogger("JPushController");
	private String ALERT = "收到了么？";

	public JPushClientUtil(String ALERT) {
		this.ALERT = ALERT;
	}

	public PushPayload buildPushObject_all_all_alert() {
		return PushPayload.alertAll(ALERT);
	}

	public PushPayload buildPushObject_all_tags_alert(String[] tags) {
		return PushPayload.newBuilder().setPlatform(Platform.all()).setAudience(Audience.alias(tags))
				.setNotification(Notification.alert(ALERT)).build();
	}

	public PushPayload buildPushObject_all_alias_alert(String[] alias) {
		return PushPayload.newBuilder().setPlatform(Platform.all()).setAudience(Audience.alias(alias))
				.setNotification(Notification.alert(ALERT)).build();
	}

	public void getTotal() {
		JPushClient[] jpushClient = { new JPushClient(masterSecret_IOS_teacher, appKey_IOS_teacher),
				new JPushClient(masterSecret_IOS_custodian, appKey_IOS_custodian),
				new JPushClient(masterSecret_android_teacher, appKey_android_teacher),
				new JPushClient(masterSecret_android_custodian, appKey_android_custodian) };
		try {
			for (JPushClient jPush : jpushClient) {
				ReceivedsResult result = jPush.getReportReceiveds("1942377665");
				LOG.debug("Got result - " + result);
			}
		} catch (APIConnectionException e) {
			// Connection error, should retry later
			LOG.error("Connection error, should retry later", e);

		} catch (APIRequestException e) {
			// Should review the error, and fix the request
			LOG.error("Should review the error, and fix the request", e);
			LOG.info("HTTP Status: " + e.getStatus());
			LOG.info("Error Code: " + e.getErrorCode());
			LOG.info("Error Message: " + e.getErrorMessage());
		}
	}

	*//**
	 * 推送给老师
	 *//*
	public void pushToTeacher() {
		JPushClient[] jpushClient = { new JPushClient(masterSecret_IOS_teacher, appKey_IOS_teacher),
				new JPushClient(masterSecret_android_teacher, appKey_android_teacher) };

		// For push, all you need do is to build PushPayload object.
		PushPayload payload = buildPushObject_all_all_alert();

		try {
			for (JPushClient jPush : jpushClient) {
				PushResult result = jPush.sendPush(payload);
				LOG.info("Got result - " + result);
			}

		} catch (APIConnectionException e) {
			// Connection error, should retry later
			LOG.error("Connection error, should retry later", e);

		} catch (APIRequestException e) {
			// Should review the error, and fix the request
			LOG.error("Should review the error, and fix the request", e);
			LOG.info("HTTP Status: " + e.getStatus());
			LOG.info("Error Code: " + e.getErrorCode());
			LOG.info("Error Message: " + e.getErrorMessage());
		}
	}

	*//**
	 * 推送给家长
	 *//*
	public void pushToCustodian() {
		JPushClient[] jpushClient = { new JPushClient(masterSecret_IOS_custodian, appKey_IOS_custodian),
				new JPushClient(masterSecret_android_custodian, appKey_android_custodian) };

		// For push, all you need do is to build PushPayload object.
		PushPayload payload = buildPushObject_all_all_alert();

		try {
			for (JPushClient jPush : jpushClient) {
				PushResult result = jPush.sendPush(payload);
				LOG.info("Got result - " + result);
			}

		} catch (APIConnectionException e) {
			// Connection error, should retry later
			LOG.error("Connection error, should retry later", e);

		} catch (APIRequestException e) {
			// Should review the error, and fix the request
			LOG.error("Should review the error, and fix the request", e);
			LOG.info("HTTP Status: " + e.getStatus());
			LOG.info("Error Code: " + e.getErrorCode());
			LOG.info("Error Message: " + e.getErrorMessage());
		}
	}

	*//**
	 * 推送给所有人
	 *//*
	public void pushAll() {
		JPushClient[] jpushClient = { new JPushClient(masterSecret_IOS_teacher, appKey_IOS_teacher),
				new JPushClient(masterSecret_IOS_custodian, appKey_IOS_custodian),
				new JPushClient(masterSecret_android_teacher, appKey_android_teacher),
				new JPushClient(masterSecret_android_custodian, appKey_android_custodian) };

		// For push, all you need do is to build PushPayload object.
		PushPayload payload = buildPushObject_all_all_alert();

		try {
			for (JPushClient jPush : jpushClient) {
				PushResult result = jPush.sendPush(payload);
				LOG.info("Got result - " + result);
			}

		} catch (APIConnectionException e) {
			// Connection error, should retry later
			LOG.error("Connection error, should retry later", e);

		} catch (APIRequestException e) {
			// Should review the error, and fix the request
			LOG.error("Should review the error, and fix the request", e);
			LOG.info("HTTP Status: " + e.getStatus());
			LOG.info("Error Code: " + e.getErrorCode());
			LOG.info("Error Message: " + e.getErrorMessage());
		}
	}

	*//**
	 * 给某/多个班级的家长推送
	 *//*
	public void pushClass_custodian(String[] class_id) {
		JPushClient[] jpushClient = { new JPushClient(masterSecret_IOS_custodian, appKey_IOS_custodian),
				new JPushClient(masterSecret_android_custodian, appKey_android_custodian) };
		PushPayload payload = buildPushObject_all_tags_alert(class_id);

		try {
			for (JPushClient jPush : jpushClient) {
				PushResult result = jPush.sendPush(payload);
				LOG.info("Got result - " + result);
			}

		} catch (APIConnectionException e) {
			// Connection error, should retry later
			LOG.error("Connection error, should retry later", e);

		} catch (APIRequestException e) {
			// Should review the error, and fix the request
			LOG.error("Should review the error, and fix the request", e);
			LOG.info("HTTP Status: " + e.getStatus());
			LOG.info("Error Code: " + e.getErrorCode());
			LOG.info("Error Message: " + e.getErrorMessage());
		}
	}

	*//**
	 * 给某/多个学校的家长推送
	 *//*
	public void pushSchool_custodian(String[] school_id) {
		JPushClient[] jpushClient = { new JPushClient(masterSecret_IOS_custodian, appKey_IOS_custodian),
				new JPushClient(masterSecret_android_custodian, appKey_android_custodian) };
		PushPayload payload = buildPushObject_all_tags_alert(school_id);

		try {
			for (JPushClient jPush : jpushClient) {
				PushResult result = jPush.sendPush(payload);
				LOG.info("Got result - " + result);
			}

		} catch (APIConnectionException e) {
			// Connection error, should retry later
			LOG.error("Connection error, should retry later", e);

		} catch (APIRequestException e) {
			// Should review the error, and fix the request
			LOG.error("Should review the error, and fix the request", e);
			LOG.info("HTTP Status: " + e.getStatus());
			LOG.info("Error Code: " + e.getErrorCode());
			LOG.info("Error Message: " + e.getErrorMessage());
		}
	}

	*//**
	 * 给某/多个班级的老师推送
	 *//*
	public void pushClass_teacher(String[] class_id) {
		JPushClient[] jpushClient = { new JPushClient(masterSecret_IOS_teacher, appKey_IOS_teacher),
				new JPushClient(masterSecret_android_teacher, appKey_android_teacher) };
		PushPayload payload = buildPushObject_all_tags_alert(class_id);

		try {
			for (JPushClient jPush : jpushClient) {
				PushResult result = jPush.sendPush(payload);
				LOG.info("Got result - " + result);
			}

		} catch (APIConnectionException e) {
			// Connection error, should retry later
			LOG.error("Connection error, should retry later", e);

		} catch (APIRequestException e) {
			// Should review the error, and fix the request
			LOG.error("Should review the error, and fix the request", e);
			LOG.info("HTTP Status: " + e.getStatus());
			LOG.info("Error Code: " + e.getErrorCode());
			LOG.info("Error Message: " + e.getErrorMessage());
		}
	}

	*//**
	 * 给某/多个学校的老师推送
	 *//*
	public void pushSchool_teacher(String[] school_id) {
		JPushClient[] jpushClient = { new JPushClient(masterSecret_IOS_teacher, appKey_IOS_teacher),
				new JPushClient(masterSecret_android_teacher, appKey_android_teacher) };
		PushPayload payload = buildPushObject_all_tags_alert(school_id);

		try {
			for (JPushClient jPush : jpushClient) {
				PushResult result = jPush.sendPush(payload);
				LOG.info("Got result - " + result);
			}

		} catch (APIConnectionException e) {
			// Connection error, should retry later
			LOG.error("Connection error, should retry later", e);

		} catch (APIRequestException e) {
			// Should review the error, and fix the request
			LOG.error("Should review the error, and fix the request", e);
			LOG.info("HTTP Status: " + e.getStatus());
			LOG.info("Error Code: " + e.getErrorCode());
			LOG.info("Error Message: " + e.getErrorMessage());
		}
	}

	*//**
	 * type 0老师 1家长
	 * 指定单人/多人推送
	 *//*
	public void pushOne(String[] ids,int type) {
		JPushClient[] jpushClient = null;
		if(type == 0){
			jpushClient = new JPushClient[]{ new JPushClient(masterSecret_IOS_teacher, appKey_IOS_teacher),
					new JPushClient(masterSecret_android_teacher, appKey_android_teacher), };
		}else{
			jpushClient = new JPushClient[]{ new JPushClient(masterSecret_IOS_custodian, appKey_IOS_custodian),
					new JPushClient(masterSecret_android_custodian, appKey_android_custodian) };
		}
		PushPayload payload = buildPushObject_all_alias_alert(ids);

		try {
			for (JPushClient jPush : jpushClient) {
				PushResult result = jPush.sendPush(payload);
				LOG.info("Got result - " + result);
			}

		} catch (APIConnectionException e) {
			// Connection error, should retry later
			LOG.error("Connection error, should retry later", e);

		} catch (APIRequestException e) {
			// Should review the error, and fix the request
			LOG.error("Should review the error, and fix the request", e);
			LOG.info("HTTP Status: " + e.getStatus());
			LOG.info("Error Code: " + e.getErrorCode());
			LOG.info("Error Message: " + e.getErrorMessage());
		}
	}

	
	public static void main(String[] args) {
		JPushClientUtil push = new JPushClientUtil("消息25");
//		push.pushToCustodian();
		push.pushOne(new String[]{"7c96222101f6492490aa61c7eb8df895"},0);
	}
}
*/