package scw.app.user.service;

import scw.app.common.annotation.LoginUse;
import scw.http.HttpMethod;
import scw.mvc.annotation.Controller;
import scw.mvc.annotation.ResultFactory;
import scw.result.Result;
import scw.security.login.UserToken;
import scw.tencent.wx.Scope;
import scw.tencent.wx.Userinfo;

@Controller(value = "weixin", methods = { HttpMethod.GET, HttpMethod.POST })
@ResultFactory
public interface WeiXinService {
	@Controller(value = "login")
	UserToken<Long> login(String code, Scope scope);

	@LoginUse
	@Controller(value = "userinfo")
	Userinfo getUserInfo(long uid);

	@LoginUse
	@Controller(value = "bind")
	Result bind(long uid, String code, Scope scope);
}