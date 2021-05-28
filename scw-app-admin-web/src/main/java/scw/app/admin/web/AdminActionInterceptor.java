package scw.app.admin.web;

import scw.beans.annotation.Autowired;
import scw.context.annotation.Provider;
import scw.context.result.ResultFactory;
import scw.env.Sys;
import scw.event.Observable;
import scw.mvc.HttpChannel;
import scw.mvc.action.Action;
import scw.mvc.action.ActionInterceptor;
import scw.mvc.action.ActionInterceptorChain;
import scw.mvc.action.ActionParameters;
import scw.web.model.Page;

@Provider
public class AdminActionInterceptor implements ActionInterceptor {
	public static final Observable<String> ADMIN_WEBSITE_NAME = Sys.env
			.getObservableValue("admin.website.name", String.class, "后台管理系统");

	@Autowired
	private ResultFactory resultFactory;

	public Object intercept(HttpChannel httpChannel, Action action,
			ActionParameters parameters, ActionInterceptorChain chain)
			throws Throwable {
		Object value = chain.intercept(httpChannel, action, parameters);
		if (value instanceof Page) {
			Page page = (Page) value;
			page.put("adminWebsiteName", ADMIN_WEBSITE_NAME.get());
			return httpChannel.getRequest().getHeaders().isAjax() ? resultFactory
					.success(page) : page;
		}
		return value;
	}

}
