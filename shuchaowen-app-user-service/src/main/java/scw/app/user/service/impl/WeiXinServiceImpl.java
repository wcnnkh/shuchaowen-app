package scw.app.user.service.impl;

import scw.app.enums.SexType;
import scw.app.user.enums.OpenidType;
import scw.app.user.model.UserAttributeModel;
import scw.app.user.pojo.User;
import scw.app.user.service.UserService;
import scw.app.user.service.WeiXinService;
import scw.app.util.BaseServiceImpl;
import scw.core.instance.annotation.Configuration;
import scw.core.utils.StringUtils;
import scw.db.DB;
import scw.result.DataResult;
import scw.result.Result;
import scw.result.ResultFactory;
import scw.security.login.LoginService;
import scw.security.login.UserToken;
import scw.tencent.wx.Scope;
import scw.tencent.wx.UserAccessToken;
import scw.tencent.wx.UserGrantClient;
import scw.tencent.wx.Userinfo;
import scw.tencent.wx.WeiXinUtils;

@Configuration(order = Integer.MIN_VALUE)
public class WeiXinServiceImpl extends BaseServiceImpl implements WeiXinService {
	protected final UserGrantClient userGrantClient;
	private UserService userService;
	private LoginService<Long> loginService;

	public WeiXinServiceImpl(DB db, ResultFactory resultFactory, UserGrantClient userGrantClient,
			UserService userService, LoginService<Long> loginService) {
		super(db, resultFactory);
		this.userGrantClient = userGrantClient;
		this.loginService = loginService;
		this.userService = userService;
	}

	public UserToken<Long> login(String code, Scope scope) {
		if (StringUtils.isEmpty(code)) {
			new RuntimeException("参数错误");
		}

		UserAccessToken userAccessToken = userGrantClient.getAccessToken(code, null);
		User user = userService.getUserByOpenid(OpenidType.WX, userAccessToken.getOpenid());
		if (user == null) {
			UserAttributeModel userAttributeModel = new UserAttributeModel();
			if (scope == Scope.USERINFO) {
				Userinfo userinfo = WeiXinUtils.getUserinfo(userAccessToken.getOpenid(),
						userAccessToken.getAccessToken().getToken());
				userAttributeModel.setSex(SexType.forValue(userinfo.getSex()));
				userAttributeModel.setNickname(userinfo.getNickname());
				if(StringUtils.isNotEmpty(userinfo.getHeadimgurl())){
					userAttributeModel.setHeadImg(StringUtils.split(userinfo.getHeadimgurl(), ",")[0]);
				}
			}

			DataResult<User> result = userService.registerByOpenid(OpenidType.WX, userAccessToken.getOpenid(),
					userAttributeModel);
			if (result.isError()) {
				throw new RuntimeException("register error: " + result);
			}
			user = result.getData();
		}
		return loginService.login(user.getUid());
	}

	public Result bind(long uid, String code, Scope scope) {
		UserAccessToken userAccessToken = userGrantClient.getAccessToken(code, null);
		User user = userService.getUserByOpenid(OpenidType.WX, userAccessToken.getOpenid());
		if (user == null) {
			return resultFactory.error("未注册，无法绑定");
		}

		UserAttributeModel userAttributeModel = new UserAttributeModel();
		if (scope == Scope.USERINFO) {
			Userinfo userinfo = WeiXinUtils.getUserinfo(userAccessToken.getOpenid(),
					userAccessToken.getAccessToken().getToken());
			userAttributeModel.setSex(SexType.forValue(userinfo.getSex()));
			userAttributeModel.setNickname(userinfo.getNickname());
			userAttributeModel.setHeadImg(StringUtils.split(userinfo.getHeadimgurl(), ",")[0]);
		}
		return userService.bindOpenid(uid, OpenidType.WX, userAccessToken.getOpenid(), userAttributeModel).result();
	}
}
