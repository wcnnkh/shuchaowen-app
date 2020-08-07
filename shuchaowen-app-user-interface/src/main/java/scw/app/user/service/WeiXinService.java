package scw.app.user.service;

import scw.app.user.security.LoginRequired;
import scw.http.HttpMethod;
import scw.mvc.annotation.Controller;
import scw.mvc.annotation.ResultFactory;
import scw.result.Result;
import scw.security.login.UserToken;
import scw.tencent.wx.Scope;

@Controller(value = "weixin", methods = { HttpMethod.GET, HttpMethod.POST })
@ResultFactory
public interface WeiXinService {
	@Controller(value = "login")
	UserToken<Long> login(String code, Scope scope);

	@LoginRequired
	@Controller(value = "bind")
	Result bind(long uid, String code, Scope scope);
}