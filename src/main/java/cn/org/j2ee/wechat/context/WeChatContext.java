package cn.org.j2ee.wechat.context;

/**
 * 微信基础配置上下文
 * @Author wuwz
 * @TypeName WeChatContext
 */
public class WeChatContext {
	private WeChatContext() {}
	private static WeChatContext context;
	private String appId;
	private String appSecrct;
	private String validateUrl;
	private String token;
	private String templateId;
	
	public static WeChatContext getInstance() {
		if(context == null) {
			context = new WeChatContext();
		}
		return context;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getAppSecrct() {
		return appSecrct;
	}

	public void setAppSecrct(String appSecrct) {
		this.appSecrct = appSecrct;
	}

	public String getValidateUrl() {
		return validateUrl;
	}

	public void setValidateUrl(String validateUrl) {
		this.validateUrl = validateUrl;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	public String getTemplateId() {
		return templateId;
	}
	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}
}
