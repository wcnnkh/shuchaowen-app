package scw.app.admin.web;

import scw.beans.annotation.Autowired;
import scw.core.GlobalPropertyFactory;
import scw.core.instance.annotation.Configuration;
import scw.event.support.DynamicValue;
import scw.mvc.HttpChannel;
import scw.mvc.action.Action;
import scw.mvc.action.ActionInterceptor;
import scw.mvc.action.ActionInterceptorChain;
import scw.mvc.action.ActionParameters;
import scw.mvc.page.Page;
import scw.result.ResultFactory;

@Configuration(order = Integer.MIN_VALUE)
public class AdminActionInterceptor implements ActionInterceptor {
	public static final DynamicValue<String> ADMIN_WEBSITE_NAME = GlobalPropertyFactory.getInstance()
			.getDynamicValue("admin.website.name", String.class, "后台管理系统");

	@Autowired
	private ResultFactory resultFactory;

	public Object intercept(HttpChannel httpChannel, Action action, ActionParameters parameters,
			ActionInterceptorChain chain) throws Throwable {
		Object value = chain.intercept(httpChannel, action, parameters);
		if (value instanceof Page) {
			Page page = (Page) value;
			page.put("adminWebsiteName", ADMIN_WEBSITE_NAME.getValue());
			return httpChannel.getRequest().getHeaders().isAjax() ? resultFactory.success(page) : page;
		}
		return value;
	}

}
