package scw.app.user.service;

import scw.mvc.annotation.Controller;
import scw.security.login.UserToken;

@Controller(value = "weixin")
public interface WeiXinLoginService {
	@Controller(value = "login")
	UserToken<Long> login(String code);
}