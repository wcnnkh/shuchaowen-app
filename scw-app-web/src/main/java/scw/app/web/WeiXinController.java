package scw.app.web;

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
import scw.result.DataResult;
import scw.result.Result;
import scw.result.ResultFactory;
import scw.security.login.UserToken;
import scw.tencent.wx.Scope;
import scw.tencent.wx.UserAccessToken;
import scw.tencent.wx.UserGrantClient;
import scw.tencent.wx.Userinfo;
import scw.tencent.wx.WeiXinUtils;

@Controller(value = "weixin", methods = { HttpMethod.GET, HttpMethod.POST })
@scw.mvc.annotation.ResultFactory
public class WeiXinController {
	protected final UserGrantClient userGrantClient;
	private UserService userService;
	@Autowired
	private LoginManager loginManager;
	@Autowired
	private ResultFactory resultFactory;

	public WeiXinController(UserService userService, UserGrantClient userGrantClient) {
		this.userService = userService;
		this.userGrantClient = userGrantClient;
	}

	@Controller(value = "login")
	public UserToken<Long> login(String code, Scope scope) {
		if (StringUtils.isEmpty(code)) {
			new RuntimeException("参数错误");
		}

		UserAccessToken userAccessToken = userGrantClient.getAccessToken(code,
				null);
		User user = userService.getUserByOpenid(OpenidType.WX,
				userAccessToken.getOpenid());
		if (user == null) {
			UserAttributeModel userAttributeModel = new UserAttributeModel();
			if (scope == Scope.USERINFO) {
				Userinfo userinfo = WeiXinUtils.getUserinfo(userAccessToken
						.getOpenid(), userAccessToken.getAccessToken()
						.getToken());
				userAttributeModel.setSex(SexType.forValue(userinfo.getSex()));
				userAttributeModel.setNickname(userinfo.getNickname());
				if (StringUtils.isNotEmpty(userinfo.getHeadimgurl())) {
					userAttributeModel.setHeadImg(StringUtils.split(
							userinfo.getHeadimgurl(), ",")[0]);
				}
			}

			DataResult<User> result = userService.registerByOpenid(
					OpenidType.WX, userAccessToken.getOpenid(),
					userAttributeModel);
			if (result.isError()) {
				throw new RuntimeException("register error: " + result);
			}
			user = result.getData();
		}
		return loginManager.login(user.getUid());
	}

	@LoginRequired
	@Controller(value = "bind")
	public Result bind(long uid, String code, Scope scope) {
		UserAccessToken userAccessToken = userGrantClient.getAccessToken(code,
				null);
		User user = userService.getUserByOpenid(OpenidType.WX,
				userAccessToken.getOpenid());
		if (user == null) {
			return resultFactory.error("未注册，无法绑定");
		}

		UserAttributeModel userAttributeModel = new UserAttributeModel();
		if (scope == Scope.USERINFO) {
			Userinfo userinfo = WeiXinUtils.getUserinfo(userAccessToken
					.getOpenid(), userAccessToken.getAccessToken().getToken());
			userAttributeModel.setSex(SexType.forValue(userinfo.getSex()));
			userAttributeModel.setNickname(userinfo.getNickname());
			userAttributeModel.setHeadImg(StringUtils.split(
					userinfo.getHeadimgurl(), ",")[0]);
		}
		return userService.bindOpenid(uid, OpenidType.WX,
				userAccessToken.getOpenid(), userAttributeModel).result();
	}
}
