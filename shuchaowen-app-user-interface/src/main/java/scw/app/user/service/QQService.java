package scw.app.user.service;

import scw.app.common.security.LoginRequired;
import scw.http.HttpMethod;
import scw.mvc.annotation.Controller;
import scw.mvc.annotation.ResultFactory;
import scw.result.Result;
import scw.security.login.UserToken;
import scw.tencent.qq.Userinfo;

@Controller(value = "qq", methods = { HttpMethod.GET, HttpMethod.POST })
@ResultFactory
public interface QQService {
	@Controller(value = "login")
	UserToken<Long> login(String openid, String accessToken);

	@Controller(value = "web_login")
	UserToken<Long> webLogin(String code, String redirect_uri);

	@LoginRequired
	@Controller(value = "bind")
	Result bind(long uid, String openid, String accessToken);

	@Controller(value = "web_bind")
	@LoginRequired
	Result webBind(long uid, String code, String redirect_uri);

	@LoginRequired
	@Controller(value = "userinfo")
	Userinfo getUserInfo(long uid);
}
