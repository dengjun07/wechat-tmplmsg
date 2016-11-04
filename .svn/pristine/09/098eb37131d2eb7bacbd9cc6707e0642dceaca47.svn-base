package cn.org.j2ee.wechat.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.org.j2ee.wechat.context.WeChatContext;
import cn.org.j2ee.wechat.model.AjaxJsonView;
import cn.org.j2ee.wechat.model.TemplateData;
import cn.org.j2ee.wechat.model.WarnInfoModel;
import cn.org.j2ee.wechat.model.WeChatTemplate;
import cn.org.j2ee.wechat.utils.SecurityUtil;
import cn.org.j2ee.wechat.utils.WeChatUtil;

/**
 * 微信服务端
 * @Author wuwz
 * @TypeName WeChatController
 */
@Controller
@RequestMapping("/wx")
public class WeChatController {
	
	//微信配置信息上下文
	private WeChatContext context = WeChatContext.getInstance();
	
	/**
	 * 验证服务器地址的有效性
	 * 加密/校验流程如下：
	 * 1. 将token、timestamp、nonce三个参数进行字典序排序
	 * 2. 将三个参数字符串拼接成一个字符串进行sha1加密
	 * 3. 开发者获得加密后的字符串可与signature对比，标识该请求来源于微信
	 * 4. 如果验证有效，则原样返回echostr
	 */
	@RequestMapping(value="/validate",method=RequestMethod.GET)
	public void validate(String signature,String timestamp,
			String nonce,String echostr,HttpServletResponse response) throws IOException {
		WeChatContext context = WeChatContext.getInstance();
		
		String[] attrs = {context.getToken(), timestamp, nonce};
		Arrays.sort(attrs);
		
		StringBuffer sb = new StringBuffer();
		for (String attr : attrs) {
			sb.append(attr);
		}
		
		String sha1 = SecurityUtil.getSha1(sb.toString());
		
		if(sha1.equals(signature)) {
			System.out.println("验证成功。。");
			
			//不知是什么原因，使用@ResponseBody返回此字符串竟然无效，只有使用原生的servlet api了
			response.getWriter().println(echostr);
		}
	}
	
	/**
	 * 发送模板消息,测试
	 * @return
	 */
	@RequestMapping("/sendTmplMsg")
	public @ResponseBody AjaxJsonView sendTmplMsg(WarnInfoModel model,String url,String color) {
		AjaxJsonView jsonView = new AjaxJsonView();
		//封装模板消息
		WeChatTemplate tmpl = new WeChatTemplate();
		tmpl.setTopcolor(color);
		tmpl.setUrl(url); //点击后跳转的网址,留空会存在问题-- 苹果机：点击后跳转到空白页面  安卓：点击没反应
		tmpl.setTemplate_id(context.getTemplateId());//模板ID,需先建立模板
		
		//模板数据
		Map<String, TemplateData> data = new HashMap<String, TemplateData>();
		data.put("first", new TemplateData(model.getFirst(), color));
		data.put("alarm_unit", new TemplateData(model.getAlarm_unit(), color));
		data.put("alarm_type", new TemplateData(model.getAlarm_type(), color)); //事件类型
		data.put("alarm_level", new TemplateData(model.getAlarm_level(), color)); //事件类型
		data.put("alarm_time", new TemplateData(new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒").format(new Date()), color));
		data.put("remark", new TemplateData(model.getRemark(), color));
		
		
		List<JSONObject> resultMsgs = new ArrayList<JSONObject>();
		
		//获取关注用户openid
		JSONArray openIdArray = WeChatUtil.getOpenIds();
		//每个用户循环发送
		for (Object openId : openIdArray) {
			tmpl.setTouser(String.valueOf(openId)); //关注用户的openid
			tmpl.setData(data);
			
			//发送模板数据并记录回执
			JSONObject object = WeChatUtil.sendTmplMsg(tmpl);
			resultMsgs.add(object);
		}
		
		jsonView.setObject(JSON.toJSONString(resultMsgs));
		return jsonView;
	}
}
