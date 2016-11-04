package cn.org.j2ee.wechat.utils;

import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.org.j2ee.wechat.context.WeChatContext;
import cn.org.j2ee.wechat.model.WeChatTemplate;

/**
 * 微信工具
 * @Author wuwz
 * @TypeName WeChatUtil
 */
@SuppressWarnings("unchecked")
public abstract class WeChatUtil {
	// access_token有效毫秒记录数
	public static long ACCESS_TOKEN_TIME = 0;
	
	// jsapi_ticket有效毫秒记录数
	public static long JSAPI_TICKET_TIME = 0;
	
	// jsapi_ticket有效毫秒记录数
	public static Map<String,String> tempData = new HashMap<String, String>();
	
	// 微信配置上下文
	public static WeChatContext context = WeChatContext.getInstance();
	

	/**
	 * 获取 getAccessToken
	 * @param code
	 * @return
	 * @throws Exception
	 */
	public static Map<String,Object> getAccessToken(String code)throws Exception{
		//请求接口地址
		String requestUrl = "https://api.weixin.qq.com/sns/oauth2/access_token";
		
		//请求参数
		String parameters = MessageFormat.format(
			"appid={0}&secret={1}&code={2}&grant_type=authorization_code",
			context.getAppId(), context.getAppSecrct(), code
		); 
		
		String result = HttpUtil.httpRequest(requestUrl, "GET", parameters);
		return (Map<String, Object>) JSON.parse(result);
	}
	
	/**
	 * 获取接口acessToken
	 * @return
	 */
	public static Map<String,Object> getAccessToken()throws Exception{
		Map<String,Object> resultMap = new HashMap<String, Object>();
		Long nowTime = new Date().getTime();
		//判断accessToken是否缓存 且是否过期
		if(ACCESS_TOKEN_TIME < nowTime){
			//请求接口地址
			String requestUrl = "https://api.weixin.qq.com/cgi-bin/token";

			
			//请求参数
			String parameters = MessageFormat.format(
				"grant_type=client_credential&appid={0}&secret={1}",
				context.getAppId(), context.getAppSecrct()
			); 
			
			String result = HttpUtil.httpRequest(requestUrl, "GET", parameters);
			
			resultMap = (Map<String, Object>) JSON.parse(result);
			
			//获取新的有效时间 单位秒
			Long newExpiresTime = Long.valueOf(resultMap.get("expires_in").toString()) ;
			//将access_token的有效时间更新（有效时间默认减少5分钟，避免意外）
			ACCESS_TOKEN_TIME = newExpiresTime*1000+nowTime-30000;
			//将access_token更新
			tempData.put("access_token", resultMap.get("access_token").toString());
		}else{
			resultMap.put("access_token", tempData.get("access_token"));
		}
		return resultMap;
	}
	
	// 获取关注用户
	public static JSONObject getUsersJSON() {
		
		try {
			//获取accessTokenMap
			Map<String, Object> map = getAccessToken();
			
			//请求接口地址
			String requestUrl = "https://api.weixin.qq.com/cgi-bin/user/get";
			
			//请求参数
			String parameters = MessageFormat.format(
				"access_token={0}",
				String.valueOf(map.get("access_token"))
			);
			
			//发起请求
			String result = HttpUtil.httpRequest(requestUrl, "GET", parameters);
			
			if(result != null && !"".equals(result)) {
				return JSON.parseObject(result);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}
	
	// 获取关注用户的OpenIds
	public static JSONArray getOpenIds() {
		try {
			JSONObject usersJsonObj = getUsersJSON();
			
			return (usersJsonObj.getJSONObject("data")).getJSONArray("openid");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	// 发送模板消息
	public static JSONObject sendTmplMsg(WeChatTemplate tmpl) {
		
		try {
			//获取accessTokenMap
			Map<String, Object> map = getAccessToken();
			
			//将模板对象序列化成JSON字符串
			String jsonMsg = JSON.toJSONString(tmpl);
			
			//请求接口地址
			String requestUrl = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" 
						+ map.get("access_token");
			
			//发送请求
			String result = HttpUtil.httpRequest(requestUrl, "POST", jsonMsg);
			
			return JSON.parseObject(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
