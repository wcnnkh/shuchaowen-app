package scw.app.web;

import java.util.Map;

import scw.app.enums.SexType;
import scw.app.user.enums.UnionIdType;
import scw.app.user.model.UserAttributeModel;
import scw.app.user.pojo.User;
import scw.app.user.security.LoginRequired;
import scw.app.user.security.UserLoginService;
import scw.app.user.service.UserService;
import scw.beans.annotation.Autowired;
import scw.context.result.DataResult;
import scw.context.result.Result;
import scw.context.result.ResultFactory;
import scw.core.utils.StringUtils;
import scw.http.HttpMethod;
import scw.integration.tencent.qq.connect.QQ;
import scw.integration.tencent.qq.connect.QQRequest;
import scw.integration.tencent.qq.connect.UserInfoResponse;
import scw.mvc.HttpChannel;
import scw.mvc.annotation.Controller;
import scw.oauth2.AccessToken;

@Controller(value = "qq", methods = { HttpMethod.GET, HttpMethod.POST })
@scw.mvc.annotation.FactoryResult
public class QQController {
	private QQ qq;
	private UserService userService;
	@Autowired
	private ResultFactory resultFactory;
	@Autowired
	private UserLoginService userControllerService;

	public QQController(UserService userService, QQ qq) {
		this.userService = userService;
		this.qq = qq;
	}

	@Controller(value = "login")
	public Result login(String openid, String accessToken, HttpChannel httpChannel) {
		if (StringUtils.isEmpty(openid, accessToken)) {
			return resultFactory.parameterError();
		}
		
		User user = userService.getUserByUnionId(openid, UnionIdType.QQ_OPENID);
		if (user == null) {
			UserInfoResponse userinfo = qq.getUserinfo(new QQRequest(accessToken, openid));
			UserAttributeModel userAttributeModel = new UserAttributeModel();
			userAttributeModel.setSex(SexType.forDescribe(userinfo.getGender()));
			userAttributeModel.setHeadImg(userinfo.getfigureUrlQQ1());
			userAttributeModel.setNickname(userinfo.getNickname());
			DataResult<User> dataResult = userService.registerUnionId(UnionIdType.QQ_OPENID, openid, null, userAttributeModel);
			if (dataResult.isError()) {
				return dataResult;
			}

			user = dataResult.getData();
		}
		Map<String, Object> infoMap = userControllerService.login(user, httpChannel);
		return resultFactory.success(infoMap);
	}

	@Controller(value = "web_login")
	public Result webLogin(String code, String redirect_uri, HttpChannel httpChannel) {
		if (StringUtils.isEmpty(code, redirect_uri)) {
			return resultFactory.parameterError();
		}

		AccessToken accessToken = qq.getAccessToken(redirect_uri, code);
		String openid = qq.getOpenid(accessToken.getToken().getToken());
		return login(openid, accessToken.getToken().getToken(), httpChannel);
	}

	@LoginRequired
	@Controller(value = "bind")
	public Result bind(long uid, String openid, String accessToken) {
		if (StringUtils.isEmpty(openid, accessToken)) {
			return resultFactory.parameterError();
		}

		User user = userService.getUserByUnionId(openid, UnionIdType.QQ_OPENID);
		if (user == null) {
			return resultFactory.error("用户不存在");
		}
		
		Result result = userService.bindUnionId(uid, UnionIdType.QQ_OPENID, openid);
		if(result.isError()){
			return result;
		}

		UserInfoResponse userinfo = qq.getUserinfo(new QQRequest(accessToken, openid));
		UserAttributeModel userAttributeModel = new UserAttributeModel();
		userAttributeModel.setSex(SexType.forDescribe(userinfo.getGender()));
		userAttributeModel.setHeadImg(userinfo.getfigureUrlQQ1());
		userAttributeModel.setNickname(userinfo.getNickname());
		return userService.updateUserAttribute(uid, userAttributeModel);
	}

	@Controller(value = "web_bind")
	@LoginRequired
	public Result webBind(long uid, String code, String redirect_uri) {
		if (StringUtils.isEmpty(code, redirect_uri)) {
			return resultFactory.parameterError();
		}

		AccessToken accessToken = qq.getAccessToken(redirect_uri, code);
		String openid = qq.getOpenid(accessToken.getToken().getToken());
		return bind(uid, openid, accessToken.getToken().getToken());
	}
}
