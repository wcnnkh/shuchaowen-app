package scw.app.user.controller;

import scw.app.enums.SexType;
import scw.app.user.enums.OpenidType;
import scw.app.user.model.UserAttributeModel;
import scw.app.user.pojo.User;
import scw.app.user.security.LoginManager;
import scw.app.user.security.LoginRequired;
import scw.app.user.service.UserService;
import scw.beans.annotation.Autowired;
import scw.core.utils.StringUtils;
import scw.http.HttpMethod;
import scw.mvc.annotation.Controller;
import scw.oauth2.AccessToken;
import scw.result.DataResult;
import scw.result.Result;
import scw.result.ResultFactory;
import scw.security.login.UserToken;
import scw.tencent.qq.Configuration;
import scw.tencent.qq.QQUtils;
import scw.tencent.qq.Userinfo;

@Controller(value = "qq", methods = { HttpMethod.GET, HttpMethod.POST })
@scw.mvc.annotation.ResultFactory
public class QQController {
	private final Configuration configuration;
	@Autowired
	private UserService userService;
	@Autowired
	private LoginManager loginManager;
	@Autowired
	private ResultFactory resultFactory;

	public QQController(Configuration configuration) {
		this.configuration = configuration;
	}

	@Controller(value = "login")
	public UserToken<Long> login(String openid, String accessToken) {
		if (StringUtils.isEmpty(openid, accessToken)) {
			throw new RuntimeException("参数错误");
		}

		User user = userService.getUserByOpenid(OpenidType.QQ, openid);
		if (user == null) {
			Userinfo userinfo = QQUtils.getUserinfo(configuration.getAppId(),
					accessToken, openid);
			UserAttributeModel userAttributeModel = new UserAttributeModel();
			userAttributeModel
					.setSex(SexType.forDescribe(userinfo.getGender()));
			userAttributeModel.setHeadImg(userinfo.getFigureurl_qq_1());
			userAttributeModel.setNickname(userinfo.getNickname());
			DataResult<User> dataResult = userService.registerByOpenid(
					OpenidType.QQ, openid, userAttributeModel);
			if (dataResult.isError()) {
				throw new RuntimeException("register error:" + dataResult);
			}

			user = dataResult.getData();
		}
		return loginManager.login(user.getUid());
	}

	@Controller(value = "web_login")
	public UserToken<Long> webLogin(String code, String redirect_uri) {
		if (StringUtils.isEmpty(code, redirect_uri)) {
			throw new RuntimeException("参数错误");
		}

		AccessToken accessToken = QQUtils.getAccessToken(
				configuration.getAppId(), configuration.getAppKey(),
				redirect_uri, code);
		String openid = QQUtils.getOpenId(accessToken.getAccessToken()
				.getToken());
		return login(openid, accessToken.getAccessToken().getToken());
	}

	@LoginRequired
	@Controller(value = "bind")
	public Result bind(long uid, String openid, String accessToken) {
		if (StringUtils.isEmpty(openid, accessToken)) {
			return resultFactory.parameterError();
		}

		User user = userService.getUserByOpenid(OpenidType.QQ, openid);
		if (user == null) {
			return resultFactory.error("用户不存在");
		}

		Userinfo userinfo = QQUtils.getUserinfo(configuration.getAppId(),
				accessToken, openid);
		UserAttributeModel userAttributeModel = new UserAttributeModel();
		userAttributeModel.setSex(SexType.forDescribe(userinfo.getGender()));
		userAttributeModel.setHeadImg(userinfo.getFigureurl_qq_1());
		userAttributeModel.setNickname(userinfo.getNickname());
		return userService.bindOpenid(uid, OpenidType.QQ, openid,
				userAttributeModel);
	}

	@Controller(value = "web_bind")
	@LoginRequired
	public Result webBind(long uid, String code, String redirect_uri) {
		if (StringUtils.isEmpty(code, redirect_uri)) {
			return resultFactory.parameterError();
		}

		AccessToken accessToken = QQUtils.getAccessToken(
				configuration.getAppId(), configuration.getAppKey(),
				redirect_uri, code);
		String openid = QQUtils.getOpenId(accessToken.getAccessToken()
				.getToken());
		return bind(uid, openid, accessToken.getAccessToken().getToken());
	}
}
