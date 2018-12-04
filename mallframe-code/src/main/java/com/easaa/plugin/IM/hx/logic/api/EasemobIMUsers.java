package com.easaa.plugin.IM.hx.logic.api;

import java.net.URL;

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
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * REST API Demo :用户体系集成 REST API HttpClient4.3实现
 * 
 * Doc URL: http://www.easemob.com/docs/rest/userapi
 * 
 * @author liujunbo 2014-09-15
 * 
 */
public class EasemobIMUsers {

	private static Logger LOGGER = LoggerFactory.getLogger(EasemobIMUsers.class);
	private static JsonNodeFactory factory = new JsonNodeFactory(false);

    // 通过app的client_id和client_secret来获取app管理员token
    private static Credential credential = new ClientSecretCredential(Constants.APP_CLIENT_ID,
            Constants.APP_CLIENT_SECRET, Roles.USER_ROLE_APPADMIN);

    public static void main(String[] args) {
        /**
         * 注册IM用户[单个]
         */
        ObjectNode datanode = JsonNodeFactory.instance.objectNode();
        datanode.put("username","gsf002");
        datanode.put("password", "002");
      /*  ObjectNode createNewIMUserSingleNode = createNewIMUserSingle(datanode);
        System.out.println(createNewIMUserSingleNode);*/
    /*    if (null != createNewIMUserSingleNode) {
            LOGGER.info("注册IM用户[单个]: " + createNewIMUserSingleNode.toString());
            System.out.println("注册IM用户[单个]: " + createNewIMUserSingleNode.toString());
            JSONObject objectNode = JSON.parseObject(createNewIMUserSingleNode.toString());
            if("200".equals(objectNode.get("statusCode").toString())) {
            	System.out.println(objectNode.getJSONArray("entities").getJSONObject(0).get("uuid"));
            	System.out.println();
            } else {
            	System.out.println("false");
            }
            System.out.println("注册IM用户[单个]: " + createNewIMUserSingleNode.toString());
        }*/

        /**
         * IM用户登录
         */
       ObjectNode imUserLoginNode = imUserLogin(datanode.get("username").asText(), datanode.get("password").asText());
       System.out.println(imUserLoginNode);
        if (null != imUserLoginNode) {
            LOGGER.info("IM用户登录: " + imUserLoginNode.toString());
        }
         /*
        *//**
         * 注册IM用户[批量生成用户然后注册]
         *//*
        String usernamePrefix = "miyi";
        Long perNumber = 10l;
        Long totalNumber = 100l;
        ObjectNode createNewIMUserBatchGenNode = createNewIMUserBatchGen(usernamePrefix, perNumber, totalNumber);
        if (null != createNewIMUserBatchGenNode) {
            LOGGER.info("注册IM用户[批量]: " + createNewIMUserBatchGenNode.toString());
        }

        *//**
         * 获取IM用户[主键查询]
         *//*
        String userPrimaryKey = "miyi";
        ObjectNode getIMUsersByPrimaryKeyNode = getIMUsersByPrimaryKey(userPrimaryKey);
        if (null != getIMUsersByPrimaryKeyNode) {
            LOGGER.info("获取IM用户[主键查询]: " + getIMUsersByPrimaryKeyNode.toString());
        }

        *//**
         * 重置IM用户密码 提供管理员token
         *//*
		String username = "miyi";
        ObjectNode json2 = JsonNodeFactory.instance.objectNode();
        json2.put("newpassword", Constants.DEFAULT_PASSWORD);
        ObjectNode modifyIMUserPasswordWithAdminTokenNode = modifyIMUserPasswordWithAdminToken(username, json2);
        if (null != modifyIMUserPasswordWithAdminTokenNode) {
            LOGGER.info("重置IM用户密码 提供管理员token: " + modifyIMUserPasswordWithAdminTokenNode.toString());
        }
        ObjectNode imUserLoginNode2 = imUserLogin(username, json2.get("newpassword").asText());
        if (null != imUserLoginNode2) {
            LOGGER.info("重置IM用户密码后,IM用户登录: " + imUserLoginNode2.toString());
        }
		*//**
		 * 重置IM用户昵称 提供管理员token
		 *//*
	     username = "dx_18665478854";
	     json2 = JsonNodeFactory.instance.objectNode();
	     json2.put("nickname", "修改昵称");
	     ObjectNode updIMUserNicknameWithAdminToken = updIMUserNicknameWithAdminToken(username, json2);
	     if (null != updIMUserNicknameWithAdminToken) {
	         LOGGER.info("修改用户昵称 提供管理员token: " + updIMUserNicknameWithAdminToken.toString());
	         System.out.println(updIMUserNicknameWithAdminToken.toString());
	     }
	
	    *//**
	     * 添加好友[单个]
	     *//*
	    String ownerUserPrimaryKey = "daixin001";
	    String friendUserPrimaryKey = "dx_15982677350";
	    ObjectNode addFriendSingleNode = addFriendSingle(ownerUserPrimaryKey, friendUserPrimaryKey);
	    if (null != addFriendSingleNode) {
	        LOGGER.info("添加好友[单个]: " + addFriendSingleNode.toString());
	        System.out.println(addFriendSingleNode.toString());
	    }
	
	    *//**
	     * 查看好友
	     *//*
	    ObjectNode getFriendsNode = getFriends(ownerUserPrimaryKey);
	    if (null != getFriendsNode) {
	        LOGGER.info("查看好友: " + getFriendsNode.toString());
	    }
	
	    *//**
	     * 解除好友关系
	     **//*
	    ObjectNode deleteFriendSingleNode = deleteFriendSingle(ownerUserPrimaryKey, friendUserPrimaryKey);
	    if (null != deleteFriendSingleNode) {
	        LOGGER.info("解除好友关系: " + deleteFriendSingleNode.toString());
	    }
	
	    *//**
	     * 删除IM用户[单个]
	     *//*
	    ObjectNode deleteIMUserByUserPrimaryKeyNode = deleteIMUserByUserPrimaryKey("daixin002");
	    if (null != deleteIMUserByUserPrimaryKeyNode) {
	        LOGGER.info("删除IM用户[单个]: " + deleteIMUserByUserPrimaryKeyNode.toString());
	    }
	
	    *//**
	     * 删除IM用户[批量]
	     *//*
	    Long limit = 2l;
	    ObjectNode deleteIMUserByUsernameBatchNode = deleteIMUserByUsernameBatch(limit, null);
	    if (null != deleteIMUserByUsernameBatchNode) {
	        LOGGER.info("删除IM用户[批量]: " + deleteIMUserByUsernameBatchNode.toString());
	    }*/
    }

    /**
	 * 注册IM用户[单个]
	 * 
	 * 给指定Constants.APPKEY创建一个新的用户
	 * 
	 * @param dataNode
	 * @return
	 */
	public static ObjectNode createNewIMUserSingle(ObjectNode dataNode) {

		ObjectNode objectNode = factory.objectNode();

		// check Constants.APPKEY format
		if (!HTTPClientUtils.match("^(?!-)[0-9a-zA-Z\\-]+#[0-9a-zA-Z]+", Constants.APPKEY)) {
			LOGGER.error("Bad format of Constants.APPKEY: " + Constants.APPKEY);

			objectNode.put("message", "appkey格式不正确");
			objectNode.put("statusCode", "400");

			return objectNode;
		}

		objectNode.removeAll();

		// check properties that must be provided
		if (null != dataNode && !dataNode.has("username")) {
			LOGGER.error("参数错误用户名必填参数");

			objectNode.put("message", "用户名必须被提供");
			objectNode.put("statusCode", "400");

			return objectNode;
		}
		if (null != dataNode && !dataNode.has("password")) {
			LOGGER.error("密码必须被提供");

			objectNode.put("message", "密码必须被提供");
			objectNode.put("statusCode", "400");

			return objectNode;
		}
		
		//昵称非必须
	/*	if (null != dataNode && !dataNode.has("nickname")) {
			LOGGER.error("Property that named nickname must be provided .");

			objectNode.put("message", "Property that named nickname must be provided .");
			objectNode.put("statusCode", "400");

			return objectNode;
		}*/

		try {

		    objectNode = HTTPClientUtils.sendHTTPRequest(EndPoints.USERS_URL, credential, dataNode,
					HTTPMethod.METHOD_POST);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return objectNode;
	}

	/**
	 * 注册IM用户[批量]
	 * 
	 * 给指定Constants.APPKEY创建一批用户
	 * 
	 * @param dataArrayNode
	 * @return
	 */
	public static ObjectNode createNewIMUserBatch(ArrayNode dataArrayNode) {

		ObjectNode objectNode = factory.objectNode();

		// check Constants.APPKEY format
		if (!HTTPClientUtils.match("^(?!-)[0-9a-zA-Z\\-]+#[0-9a-zA-Z]+", Constants.APPKEY)) {
			LOGGER.error("Bad format of Constants.APPKEY: " + Constants.APPKEY);

			objectNode.put("message", "Bad format of Constants.APPKEY");
			objectNode.put("statusCode", "400");

			return objectNode;
		}

		// check properties that must be provided
		if (dataArrayNode.isArray()) {
			for (JsonNode jsonNode : dataArrayNode) {
				if (null != jsonNode && !jsonNode.has("username")) {
					LOGGER.error("Property that named username must be provided .");

					objectNode.put("message", "Property that named username must be provided .");
					objectNode.put("statusCode", "400");

					return objectNode;
				}
				if (null != jsonNode && !jsonNode.has("password")) {
					LOGGER.error("Property that named password must be provided .");

					objectNode.put("message", "Property that named password must be provided .");
					objectNode.put("statusCode", "400");

					return objectNode;
				}
				if (null != jsonNode && !jsonNode.has("nickname")) {
					LOGGER.error("Property that named password must be provided .");

					objectNode.put("message", "Property that named password must be provided .");
					objectNode.put("statusCode", "400");

					return objectNode;
				}
			}
		}

		try {

			objectNode = HTTPClientUtils.sendHTTPRequest(EndPoints.USERS_URL, credential, dataArrayNode,
					HTTPMethod.METHOD_POST);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return objectNode;
	}

	/**
	 * 注册IM用户[批量生成用户然后注册]
	 * 
	 * 给指定Constants.APPKEY创建一批用户
	 * 
	 * @param usernamePrefix
	 *            生成用户名的前缀
	 * @param perNumber
	 *            批量注册时一次注册的数量
	 * @param totalNumber
	 *            生成用户注册的用户总数
	 * @return
	 */
	public static ObjectNode createNewIMUserBatchGen(String usernamePrefix, Long perNumber, Long totalNumber) {
		ObjectNode objectNode = factory.objectNode();

		if (totalNumber == 0 || perNumber == 0) {
			return objectNode;
		}

		System.out.println("你即将注册" + totalNumber + "个用户，如果大于" + perNumber + "了,会分批注册,每次注册" + perNumber + "个");

		ArrayNode genericArrayNode = EasemobIMUsers.genericArrayNode(usernamePrefix, totalNumber);
		if (totalNumber <= perNumber) {
			objectNode = EasemobIMUsers.createNewIMUserBatch(genericArrayNode);
		} else {

			for (int i = 0; i < genericArrayNode.size(); i++) {
				ArrayNode tmpArrayNode = factory.arrayNode();
				tmpArrayNode.add(genericArrayNode.get(i));
				// 300 records on one migration
				if ((i + 1) % perNumber == 0) {
					objectNode = EasemobIMUsers.createNewIMUserBatch(genericArrayNode);
					tmpArrayNode.removeAll();
					continue;
				}

				// the rest records that less than the times of 300
				if (i > (genericArrayNode.size() / perNumber * perNumber - 1)) {
					objectNode = EasemobIMUsers.createNewIMUserBatch(genericArrayNode);
					tmpArrayNode.removeAll();
				}
			}
		}

		return objectNode;
	}

	/**
	 * 获取IM用户
	 * 
	 * @param userPrimaryKey
	 *            用户主键：username或者uuid
	 * @return
	 */
	public static ObjectNode getIMUsersByPrimaryKey(String userPrimaryKey) {
		ObjectNode objectNode = factory.objectNode();

		// check Constants.APPKEY format
		if (!HTTPClientUtils.match("^(?!-)[0-9a-zA-Z\\-]+#[0-9a-zA-Z]+", Constants.APPKEY)) {
			LOGGER.error("Bad format of Constants.APPKEY: " + Constants.APPKEY);

			objectNode.put("message", "Bad format of Constants.APPKEY");
			objectNode.put("statusCode", "400");

			return objectNode;
		}

		// check properties that must be provided
		if (StringUtils.isEmpty(userPrimaryKey)) {
			LOGGER.error("The primaryKey that will be useed to query must be provided .");

			objectNode.put("message", "The primaryKey that will be useed to query must be provided .");
			objectNode.put("statusCode", "400");

			return objectNode;
		}

		try {

			URL userPrimaryUrl = HTTPClientUtils
					.getURL(Constants.APPKEY.replace("#", "/") + "/users/" + userPrimaryKey);
			objectNode = HTTPClientUtils.sendHTTPRequest(userPrimaryUrl, credential, null, HTTPMethod.METHOD_GET);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return objectNode;
	}

	/**
	 * 删除IM用户[单个]
	 * 
	 * 删除指定Constants.APPKEY下IM单个用户
	 *
	 * 
	 * @param userPrimaryKey
	 * @return
	 */
	public static ObjectNode deleteIMUserByUserPrimaryKey(String userPrimaryKey) {
		ObjectNode objectNode = factory.objectNode();

		// check Constants.APPKEY format
		if (!HTTPClientUtils.match("^(?!-)[0-9a-zA-Z\\-]+#[0-9a-zA-Z]+", Constants.APPKEY)) {
			LOGGER.error("Bad format of Constants.APPKEY: " + Constants.APPKEY);

			objectNode.put("message", "Bad format of Constants.APPKEY");
			objectNode.put("statusCode", "400");

			return objectNode;
		}

		try {

			URL deleteUserPrimaryUrl = HTTPClientUtils.getURL(Constants.APPKEY.replace("#", "/") + "/users/"
					+ userPrimaryKey);
			objectNode = HTTPClientUtils.sendHTTPRequest(deleteUserPrimaryUrl, credential, null,
					HTTPMethod.METHOD_DELETE);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return objectNode;
	}

	/**
	 * 删除IM用户[批量]
	 * 
	 * 批量指定Constants.APPKEY下删除IM用户
	 * 
	 * @param limit
	 * @param queryStr
	 * @return
	 */
	public static ObjectNode deleteIMUserByUsernameBatch(Long limit, String queryStr) {

		ObjectNode objectNode = factory.objectNode();

		// check Constants.APPKEY format
		if (!HTTPClientUtils.match("^(?!-)[0-9a-zA-Z\\-]+#[0-9a-zA-Z]+", Constants.APPKEY)) {
			LOGGER.error("Bad format of Constants.APPKEY: " + Constants.APPKEY);

			objectNode.put("message", "Bad format of Constants.APPKEY");
			objectNode.put("statusCode", "400");

			return objectNode;
		}
		if (StringUtils.isEmpty(queryStr)) {
			LOGGER.error("queryStr must be provided .");

			objectNode.put("message", "queryStr must be provided .");
			objectNode.put("statusCode", "400");

			return objectNode;
		}

		try {

			URL deleteIMUserByUsernameBatchUrl = HTTPClientUtils.getURL(Constants.APPKEY.replace("#", "/") + "/users"
					+ "?ql=" + queryStr + "&limit=" + limit);
			objectNode = HTTPClientUtils.sendHTTPRequest(deleteIMUserByUsernameBatchUrl, credential, null,
					HTTPMethod.METHOD_DELETE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return objectNode;
	}

	/**
	 * 重置IM用户密码 提供管理员token
	 * @param userPrimaryKey
	 * @param dataObjectNode
	 * @return
	 */
	public static ObjectNode modifyIMUserPasswordWithAdminToken(String userPrimaryKey, ObjectNode dataObjectNode) {
		ObjectNode objectNode = factory.objectNode();

		// check Constants.APPKEY format
		if (!HTTPClientUtils.match("^(?!-)[0-9a-zA-Z\\-]+#[0-9a-zA-Z]+", Constants.APPKEY)) {
			LOGGER.error("Bad format of Constants.APPKEY: " + Constants.APPKEY);

			objectNode.put("message", "Bad format of Constants.APPKEY");
			objectNode.put("statusCode", "400");

			return objectNode;
		}

		if (StringUtils.isEmpty(userPrimaryKey)) {
			LOGGER.error("Property that named userPrimaryKey must be provided，the value is username or uuid of imuser.");

			objectNode.put("message",
					"Property that named userPrimaryKey must be provided，the value is username or uuid of imuser.");
			objectNode.put("statusCode", "400");
			return objectNode;
		}

		if (null != dataObjectNode && !dataObjectNode.has("newpassword")) {
			LOGGER.error("Property that named newpassword must be provided .");

			objectNode.put("message", "Property that named newpassword must be provided .");
			objectNode.put("statusCode", "400");

			return objectNode;
		}

		try {
			URL modifyIMUserPasswordWithAdminTokenUrl = HTTPClientUtils.getURL(Constants.APPKEY.replace("#", "/")
					+ "/users/" + userPrimaryKey + "/password");
			objectNode = HTTPClientUtils.sendHTTPRequest(modifyIMUserPasswordWithAdminTokenUrl, credential,
					dataObjectNode, HTTPMethod.METHOD_PUT);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return objectNode;
	}
	
	/**
	 * 修改用户昵称 提供管理员token
	 * @param userPrimaryKey
	 * @param dataObjectNode
	 * @return
	 */
	public static ObjectNode updIMUserNicknameWithAdminToken(String userPrimaryKey, ObjectNode dataObjectNode) {
		ObjectNode objectNode = factory.objectNode();

		// check Constants.APPKEY format
		if (!HTTPClientUtils.match("^(?!-)[0-9a-zA-Z\\-]+#[0-9a-zA-Z]+", Constants.APPKEY)) {
			LOGGER.error("Bad format of Constants.APPKEY: " + Constants.APPKEY);

			objectNode.put("message", "Bad format of Constants.APPKEY");
			objectNode.put("statusCode", "400");

			return objectNode;
		}

		if (StringUtils.isEmpty(userPrimaryKey)) {
			LOGGER.error("Property that named userPrimaryKey must be provided，the value is username or uuid of imuser.");

			objectNode.put("message",
					"Property that named userPrimaryKey must be provided，the value is username or uuid of imuser.");
			objectNode.put("statusCode", "400");
			return objectNode;
		}

		if (null != dataObjectNode && !dataObjectNode.has("nickname")) {
			LOGGER.error("Property that named nickname must be provided .");

			objectNode.put("message", "Property that named nickname must be provided .");
			objectNode.put("statusCode", "400");

			return objectNode;
		}

		try {
			URL modifyIMUserPasswordWithAdminTokenUrl = HTTPClientUtils.getURL(Constants.APPKEY.replace("#", "/")
					+ "/users/" + userPrimaryKey);
			objectNode = HTTPClientUtils.sendHTTPRequest(modifyIMUserPasswordWithAdminTokenUrl, credential,
					dataObjectNode, HTTPMethod.METHOD_PUT);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return objectNode;
	}
	
	/**
	 * 添加好友[单个]
	 * 
	 * @param ownerUserPrimaryKey
	 * @param friendUserPrimaryKey
	 * 
	 * @return
	 */
	public static ObjectNode addFriendSingle(String ownerUserPrimaryKey, String friendUserPrimaryKey) {
		ObjectNode objectNode = factory.objectNode();

		// check Constants.APPKEY format
		if (!HTTPClientUtils.match("^(?!-)[0-9a-zA-Z\\-]+#[0-9a-zA-Z]+", Constants.APPKEY)) {
			LOGGER.error("Bad format of Constants.APPKEY: " + Constants.APPKEY);

			objectNode.put("message", "Bad format of Constants.APPKEY");
			objectNode.put("statusCode", "400");

			return objectNode;
		}

		if (StringUtils.isEmpty(ownerUserPrimaryKey)) {
			LOGGER.error("Your userPrimaryKey must be provided，the value is username or uuid of imuser.");

			objectNode.put("message", "Your userPrimaryKey must be provided，the value is username or uuid of imuser.");
			objectNode.put("statusCode", "400");
			return objectNode;
		}

		if (StringUtils.isEmpty(friendUserPrimaryKey)) {
			LOGGER.error("The userPrimaryKey of friend must be provided，the value is username or uuid of imuser.");

			objectNode.put("message",
					"The userPrimaryKey of friend must be provided，the value is username or uuid of imuser.");
			objectNode.put("statusCode", "400");
			return objectNode;
		}

		try {

			URL addFriendSingleUrl = HTTPClientUtils.getURL(Constants.APPKEY.replace("#", "/") + "/users/"
					+ ownerUserPrimaryKey + "/contacts/users/" + friendUserPrimaryKey);

			ObjectNode body = factory.objectNode();
			objectNode = HTTPClientUtils.sendHTTPRequest(addFriendSingleUrl, credential, body, HTTPMethod.METHOD_POST);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return objectNode;
	}
	
	/**
	 * 删除好友
	 * 
	 * @param ownerUserPrimaryKey
	 * @param friendUserPrimaryKey
	 * 
	 * @return
	 */
	public static ObjectNode deleteFriendSingle(String ownerUserPrimaryKey, String friendUserPrimaryKey) {
		ObjectNode objectNode = factory.objectNode();

		// check Constants.APPKEY format
		if (!HTTPClientUtils.match("^(?!-)[0-9a-zA-Z\\-]+#[0-9a-zA-Z]+", Constants.APPKEY)) {
			LOGGER.error("Bad format of Constants.APPKEY: " + Constants.APPKEY);

			objectNode.put("message", "Bad format of Constants.APPKEY");
			objectNode.put("statusCode", "400");

			return objectNode;
		}

		if (StringUtils.isEmpty(ownerUserPrimaryKey)) {
			LOGGER.error("Your userPrimaryKey must be provided，the value is username or uuid of imuser.");

			objectNode.put("message", "Your userPrimaryKey must be provided，the value is username or uuid of imuser.");
			objectNode.put("statusCode", "400");
			return objectNode;
		}

		if (StringUtils.isEmpty(friendUserPrimaryKey)) {
			LOGGER.error("The userPrimaryKey of friend must be provided，the value is username or uuid of imuser.");

			objectNode.put("message",
					"The userPrimaryKey of friend must be provided，the value is username or uuid of imuser.");
			objectNode.put("statusCode", "400");
			return objectNode;
		}

		try {
			URL addFriendSingleUrl = HTTPClientUtils.getURL(Constants.APPKEY.replace("#", "/") + "/users/"
					+ ownerUserPrimaryKey + "/contacts/users/" + friendUserPrimaryKey);

			ObjectNode body = factory.objectNode();
			objectNode = HTTPClientUtils.sendHTTPRequest(addFriendSingleUrl, credential, body, HTTPMethod.METHOD_DELETE);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return objectNode;
	}
	
	/**
	 * 获取用户所有好友
	 * 
	 * @param ownerUserPrimaryKey
	 * 
	 * @return
	 */
	public static ObjectNode getFriends(String ownerUserPrimaryKey) {
		ObjectNode objectNode = factory.objectNode();

		// check Constants.APPKEY format
		if (!HTTPClientUtils.match("^(?!-)[0-9a-zA-Z\\-]+#[0-9a-zA-Z]+", Constants.APPKEY)) {
			LOGGER.error("Bad format of Constants.APPKEY: " + Constants.APPKEY);

			objectNode.put("message", "Bad format of Constants.APPKEY");
			objectNode.put("statusCode", "400");
			return objectNode;
		}

		if (StringUtils.isEmpty(ownerUserPrimaryKey)) {
			LOGGER.error("Your userPrimaryKey must be provided，the value is username or uuid of imuser.");

			objectNode.put("message", "Your userPrimaryKey must be provided，the value is username or uuid of imuser.");
			objectNode.put("statusCode", "400");
			return objectNode;
		}

		try {
			
			URL addFriendSingleUrl = HTTPClientUtils.getURL(Constants.APPKEY.replace("#", "/") + "/users/"
					+ ownerUserPrimaryKey + "/contacts/users");

			ObjectNode body = factory.objectNode();
			objectNode = HTTPClientUtils.sendHTTPRequest(addFriendSingleUrl, credential, body, HTTPMethod.METHOD_GET);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return objectNode;
	}

	/**
	 * IM用户登录
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	public static ObjectNode imUserLogin(String username, String password) {

		ObjectNode objectNode = factory.objectNode();

		// check appKey format
		if (!HTTPClientUtils.match("^(?!-)[0-9a-zA-Z\\-]+#[0-9a-zA-Z]+", Constants.APPKEY)) {
			LOGGER.error("Bad format of Appkey: " + Constants.APPKEY);

			objectNode.put("message", "Bad format of Appkey");
			objectNode.put("statusCode", "400");
			return objectNode;
		}
		if (StringUtils.isEmpty(username)) {
			LOGGER.error("Your username must be provided，the value is username or uuid of imuser.");

			objectNode.put("message", "Your username must be provided，the value is username or uuid of imuser.");
			objectNode.put("statusCode", "400");
			return objectNode;
		}
		if (StringUtils.isEmpty(password)) {
			LOGGER.error("Your password must be provided，the value is username or uuid of imuser.");

			objectNode.put("message", "Your password must be provided，the value is username or uuid of imuser.");
			objectNode.put("statusCode", "400");
			return objectNode;
		}

		try {
			ObjectNode dataNode = factory.objectNode();
			dataNode.put("grant_type", "password");
			dataNode.put("username", username);
			dataNode.put("password", password);

			objectNode = HTTPClientUtils.sendHTTPRequest(EndPoints.TOKEN_APP_URL, null, dataNode,
					HTTPMethod.METHOD_POST);

		} catch (Exception e) {
			throw new RuntimeException("Some errors ocuured while fetching a token by usename and passowrd .");
		}

		return objectNode;
	}

	/*************************************************************************************************************************/
	/**
	 * 指定前缀和数量生成用户基本数据
	 * 
	 * @param usernamePrefix
	 * @param number
	 * @return
	 */
	public static ArrayNode genericArrayNode(String usernamePrefix, Long number) {
		ArrayNode arrayNode = factory.arrayNode();
		for (int i = 0; i < number; i++) {
			ObjectNode userNode = factory.objectNode();
			userNode.put("username", usernamePrefix + "_" + i);
			userNode.put("password", Constants.DEFAULT_PASSWORD);

			arrayNode.add(userNode);
		}

		return arrayNode;
	}

}
